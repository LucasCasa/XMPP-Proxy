package ar.edu.itba.pdc.Connection;

import ar.edu.itba.pdc.logger.XMPPLogger;
import ar.edu.itba.pdc.xmlparser.MessageConverter;
import ar.edu.itba.pdc.xmlparser.State;
import ar.edu.itba.pdc.xmlparser.XMLParser;
import ar.edu.itba.pdc.Metrics;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Team Muffin on 25/10/16.
 * The information of the connection
 */
public class ProxyConnection implements Connection{
    private SelectionKey clientKey;
    private SelectionKey serverKey;

    private List<String> clientMessages = new LinkedList<>();
    private List<String> serverMessages = new LinkedList<>();

    private ByteBuffer clientBuffer;
    private ByteBuffer serverBuffer;

    private Status status = Status.STARTING;

    private String JIDName = null;
    private String serverName = null;
    private String JID = null;

    private boolean incomplete = false;
    private boolean silencedIncomplete = false;
    private boolean clear = true;
    private boolean waiting;


    public ProxyConnection(SelectionKey ck, SelectionKey sk){
        clientKey = ck;
        serverKey = sk;
        clientBuffer = ByteBuffer.allocate(ConnectionHandler.BUFF_SIZE);
        serverBuffer = ByteBuffer.allocate(ConnectionHandler.BUFF_SIZE);
        waiting = false;
    }
    public ByteBuffer getClientBuffer(){
        return fillBuffer(clientMessages);
    }
    public ByteBuffer getServerBuffer(){
        return fillBuffer(serverMessages);
    }

    private ByteBuffer fillBuffer(List<String> msg){
        int length = 0;
        for(String s : msg){
            length += s.getBytes().length;
        }
        ByteBuffer bf = ByteBuffer.allocate(length);
        for(String s : msg){
            bf.put(s.getBytes());
        }
        bf.flip();
        msg.clear();
        return bf;
    }
    public void handleWrite(SelectionKey key){
        try {
            int byteWrite = 0;
            if(status == Status.STARTING){
                clientBuffer.flip();
                byteWrite = ((SocketChannel) key.channel()).write(clientBuffer);
                status = Status.NEGOTIATING;
                clientKey.interestOps(SelectionKey.OP_READ);
            } else if (status == Status.WAITING_SERVER) {
                    XMPPLogger.getInstance().debug("STATUS: WAITING SERVER");
                    serverBuffer.clear();
                    if (ConnectionHandler.isMultiplex(JID)) {
                        serverBuffer.put((ConnectionHandler.INITIAL_STREAM[0] + ConnectionHandler.multiplex(JID).split("@")[1] + ConnectionHandler.INITIAL_STREAM[1]).getBytes("UTF-8"));
                    } else {
                        serverBuffer.put((ConnectionHandler.INITIAL_STREAM[0] + serverName + ConnectionHandler.INITIAL_STREAM[1]).getBytes("UTF-8"));
                    }
                    serverBuffer.flip();
                    byteWrite = ((SocketChannel) key.channel()).write(serverBuffer);
                    key.interestOps(SelectionKey.OP_READ);
                checkConnection(byteWrite);
            }else if(status == Status.SENDING_AUTH){
                clientBuffer.flip();
                byteWrite = ((SocketChannel) key.channel()).write(clientBuffer);
                serverKey.interestOps(SelectionKey.OP_READ);
            }else if(status == Status.CONNECTED){
                if(key.equals(clientKey)){
                    serverBuffer.flip();

                    byteWrite = ((SocketChannel)clientKey.channel()).write(serverBuffer);
                    XMPPLogger.getInstance().debug("SERVER IS SENDING TO " + JID + " " + byteWrite + " bytes");
                    clientKey.interestOps(SelectionKey.OP_READ);
                    serverKey.interestOps(SelectionKey.OP_READ);
                    serverBuffer.clear();
                }else{
                    clientBuffer.flip();
                    byteWrite = ((SocketChannel)serverKey.channel()).write(clientBuffer);
                    XMPPLogger.getInstance().debug(JID + " IS SENDING TO SERVER" + " " + byteWrite + " bytes");
                    serverKey.interestOps(SelectionKey.OP_READ);
                    clientKey.interestOps(SelectionKey.OP_READ);
                    clientBuffer.clear();
                }
            }
            checkConnection(byteWrite);
        }catch (Exception e){
            XMPPLogger.getInstance().error("HANDLE WRITE ERROR");
            e.printStackTrace();
        }
    }

    public void handleRead(SelectionKey key){
        Charset utf8 = Charset.forName("UTF-8");
        CharBuffer buff;
        try{
            int bytesRead = 0;
            if(status == Status.STARTING){
                bytesRead = ((SocketChannel) key.channel()).read(clientBuffer);
                if(checkConnection(bytesRead)){
                    return;
                }
                //if lo que leo tiene stream y esta bien formado -> mando los 2 cosos, sino me quedo esperando el stream
                clientBuffer.flip();
                buff = utf8.decode(clientBuffer);
                State s = XMLParser.checkMessage(buff);
                if(s == State.ERROR){
                    clientBuffer.clear();
                }else if(s == State.INCOMPLETE){
                    if(XMLParser.contains("<stream:stream",buff) && (XMLParser.endsWith(">",buff) || XMLParser.endsWith(">\n",buff))){
                        XMPPLogger.getInstance().debug("CONTAINS <stream:stream>");
                        serverName = XMLParser.getTo(buff);
                        clientBuffer.clear();
                        clientBuffer.put(ConnectionHandler.INITIAL_SERVER_STREAM);
                        clientBuffer.put(ConnectionHandler.NEGOTIATION);
                        clientKey.interestOps(SelectionKey.OP_WRITE);
                    }else{
                        clientBuffer.position(clientBuffer.limit());
                        clientBuffer.limit(ConnectionHandler.BUFF_SIZE);

                    }
                }
            }else if( status == Status.NEGOTIATING){
                clientBuffer.clear();
                bytesRead = ((SocketChannel) key.channel()).read(clientBuffer);
                if(checkConnection(bytesRead)){
                    return;
                }
                clientBuffer.flip();
                buff = utf8.decode(clientBuffer);
                if(XMLParser.startWith("<auth",buff)) {
                    byte[] d = Base64.decodeBase64(XMLParser.getAuth(clientBuffer).getBytes("UTF-8"));
                    String stringData = new String(d,"UTF-8");
                    setJID(stringData.substring(1, stringData.indexOf(0, 1)));
                    String servMultiplexed = ConnectionHandler.multiplex(JID).split("@")[1];
                    SocketChannel serverChannel = SocketChannel.open();
                    serverChannel.configureBlocking(false);
                    boolean connected = serverChannel.connect(new InetSocketAddress(ConnectionHandler.getAddr(servMultiplexed), 5222));
                    if(connected){
                        setServerKey(ConnectionHandler.getInstance().addConnection(serverChannel, this));
                        serverKey.interestOps(SelectionKey.OP_WRITE);
                    }else {
                        ConnectionHandler.getInstance().addConnect(serverChannel, this);
                    }

                    status = Status.WAITING_SERVER;
                }
            }else if(status == Status.WAITING_SERVER) {
                //if server respondio -> reenvio lo que recibi al usuario. si es success ya esta, sino vuelvo al estado anterior.
                serverBuffer.clear();
                bytesRead = ((SocketChannel) key.channel()).read(serverBuffer);
                if(checkConnection(bytesRead)){
                    return;
                }
                serverBuffer.flip();
                buff = utf8.decode(serverBuffer);
                if (XMLParser.contains("mechanism", buff)) {
                    serverKey.interestOps(SelectionKey.OP_WRITE);
                    status = Status.SENDING_AUTH;
                }
            }else if(status == Status.SENDING_AUTH){
                serverBuffer.clear();
                bytesRead = ((SocketChannel) key.channel()).read(serverBuffer);
                serverBuffer.flip();
                buff = utf8.decode(serverBuffer);
                if (XMLParser.startWith("<success", buff)) {
                    XMPPLogger.getInstance().info("USER " + JID + " CONECTED TO SERVER");
                    status = Status.CONNECTED;
                    clientKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (XMLParser.startWith("<failure", buff)) {
                    XMPPLogger.getInstance().error("FAILURE, CLOSING CONNECTION WITH " + JID);
                    clientKey.channel().close();
                    serverKey.channel().close();
                    clientKey.cancel();
                    serverKey.cancel();
                    XMPPLogger.getInstance().warn("USER " + JID + " DISCONECTED");
                }
                checkConnection(bytesRead);
            }else if(status == Status.CONNECTED){
                //Ya estoy conectado, aca toda la logica del proxy
                if(key.equals(clientKey)){
                    if(clear) {
                        clientBuffer.clear();
                    }
                    clientKey.interestOps(SelectionKey.OP_READ);
                    serverKey.interestOps(SelectionKey.OP_WRITE);
                    bytesRead = ((SocketChannel)clientKey.channel()).read(clientBuffer);
                    if(checkConnection(bytesRead)){
                        return;
                    }
                    clientBuffer.flip();
                    //clientBuffer.position(0);
                    buff = utf8.decode(clientBuffer);
                    State s = XMLParser.checkMessage(buff);

                    if(s == State.INCOMPLETE){
                        if(clientBuffer.position() >= ConnectionHandler.BUFF_SIZE){
                            XMPPLogger.getInstance().debug("STATUS: INCOMPLETE BIG STANZA ON " + JID);
                        }else{
                            XMPPLogger.getInstance().debug("STATUS: INCOMPLETE STANZA ON " + JID);
                        }

                        //System.out.println("INCOMPLETO ------------------------------");
                        //System.out.println(new String(buff.array()));
                        //System.out.println("INCOMPLETO ------------------------------");
                    }
                    if(!incomplete && s == State.INCOMPLETE && clientBuffer.position() < ConnectionHandler.BUFF_SIZE){
                        clientBuffer.limit(ConnectionHandler.BUFF_SIZE);
                        clear = false;
                        serverKey.interestOps(SelectionKey.OP_WRITE);
                        return;
                    }
                    clear = true;
                    if (s == State.INCOMPLETE && clientBuffer.position() >= ConnectionHandler.BUFF_SIZE) {
                        if(silencedIncomplete){
                            clientBuffer.clear();
                            serverKey.interestOps(SelectionKey.OP_READ);
                        }
                        if(!incomplete){
                            performOperations(buff,true);
                            key.interestOps(0);
                        }
                        incomplete = true;
                        if(silencedIncomplete){
                            key.interestOps(SelectionKey.OP_WRITE);
                        }else {
                            key.interestOps(0);
                        }
                        return;
                    }
                    if (incomplete && clientBuffer.position() < ConnectionHandler.BUFF_SIZE) {
                        incomplete = false;
                        if(silencedIncomplete){
                            clientBuffer.clear();
                            serverKey.interestOps(SelectionKey.OP_READ);
                            silencedIncomplete = false;
                        }

                        return;
                    }
                    performOperations(buff,false);
                }else {
                    serverBuffer.clear();
                    bytesRead = ((SocketChannel) serverKey.channel()).read(serverBuffer);
                    if(checkConnection(bytesRead)){
                        return;
                    }
                    serverBuffer.flip();
                    buff = utf8.decode(serverBuffer);
                    if(silencedIncomplete && serverBuffer.position() >= ConnectionHandler.BUFF_SIZE){
                        serverBuffer.clear();
                        return;
                    }else if(silencedIncomplete){
                        silencedIncomplete = false;
                        serverBuffer.clear();
                        return;
                    }
                        if (XMLParser.startWith("<message", buff)) {
                            if (ConnectionHandler.isSilenced(XMLParser.getTo(buff))) {
                                if(serverBuffer.position() >= ConnectionHandler.BUFF_SIZE){
                                    silencedIncomplete = true;
                                }
                                serverBuffer.clear();
                                Metrics.incrementBlocked();
                                if (XMLParser.contains("<body>", buff)) {
                                    clientBuffer.put(ConnectionHandler.silenced2(XMLParser.getFrom(buff)));
                                    serverKey.interestOps(SelectionKey.OP_WRITE);
                                }

                                return;
                            }
                    }
                    if(serverBuffer.position() >= 2000){
                        clientKey.interestOps(SelectionKey.OP_WRITE);
                        serverKey.interestOps(0);
                    }else {
                        serverKey.interestOps(SelectionKey.OP_READ);
                        clientKey.interestOps(SelectionKey.OP_WRITE);
                    }
                }
            }
        }catch(Exception e){
            try {
                XMPPLogger.getInstance().error("HANDLE READ ERROR");
                e.printStackTrace();
                if(clientKey != null) {
                    clientKey.channel().close();
                    clientKey.cancel();
                }
                if(serverKey != null){
                    serverKey.channel().close();
                    serverKey.cancel();
                }
                XMPPLogger.getInstance().warn("USER " + JID + " DISCONECTED");
            }catch (Exception e2){
                XMPPLogger.getInstance().error("HANDLE READ FATAL ERROR");
                e2.printStackTrace();
            }
        }
    }
    public boolean checkConnection(int bytes) throws IOException{
        if (bytes== -1) {
            if(clientKey != null) {
                clientKey.channel().close();
                clientKey.cancel();
            }
            if(serverKey != null){
                serverKey.channel().close();
                serverKey.cancel();
            }

            XMPPLogger.getInstance().warn("USER " + JID + " DISCONECTED");
            return true;
        }
        return false;
    }
    public void addClientMessage(String s){
        clientMessages.add(s);
    }
    public void addServerMessage(String s){
        serverMessages.add(s);
    }

    public SelectionKey getClientKey() {
        return clientKey;
    }

    public void performOperations(CharBuffer buff,boolean incomplete){
        if (XMLParser.contains("<stream:stream", buff)) {
            XMPPLogger.getInstance().debug("STATUS: COMPLETE AND CONTAINS <stream:stream>");
            if (ConnectionHandler.isMultiplex(JID)) {
                XMPPLogger.getInstance().debug("STATUS: COMPLETE AND CONTAINS <stream:stream> AND IS MULTIPLEXED");
                clientBuffer = XMLParser.setTo(clientBuffer, ConnectionHandler.multiplex(JID).split("@")[1]);
            }
        }else if (XMLParser.startWith("<message", buff)) {
            if (ConnectionHandler.isSilenced(JID)) {
                silencedIncomplete = incomplete;
                clientBuffer.clear();
                Metrics.incrementBlocked();
                if (XMLParser.contains("<body>", buff)) {
                    serverBuffer.put(ConnectionHandler.silenced(JID, XMLParser.getTo(buff)));
                }
                clientKey.interestOps(SelectionKey.OP_WRITE);
                serverKey.interestOps(SelectionKey.OP_READ);
            } else {
                if (XMLParser.contains("<body", buff)) {

                    if (ConnectionHandler.isL33t(JID)) {
                        clientBuffer = MessageConverter.convertToL33t(clientBuffer);
                        Metrics.incrementL33ted();
                    }
                    if (ConnectionHandler.isMultiplex(JID)) {
                        clientBuffer = XMLParser.setFrom(clientBuffer, ConnectionHandler.multiplex(JID).split("@")[1]);
                    }
                }
            }
        }
    }
    public void setJID(String JID){
        JIDName = JID;
        this.JID = JIDName + "@" + serverName;
    }
    public boolean isWaiting() {
        return waiting;
    }

    public String getJID() {
        return JID;
    }

    public String getJIDName() {
        return JIDName;
    }

    public SelectionKey getServerKey() {
        return serverKey;
    }

    public void setClientKey(SelectionKey clientKey) {
        this.clientKey = clientKey;
    }

    public void setServerKey(SelectionKey serverKey) {
        this.serverKey = serverKey;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
}
