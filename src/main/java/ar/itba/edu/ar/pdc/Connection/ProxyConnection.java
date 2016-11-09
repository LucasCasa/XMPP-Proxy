package ar.itba.edu.ar.pdc.Connection;

import ar.itba.edu.ar.pdc.xmlparser.MessageConverter;
import ar.itba.edu.ar.pdc.xmlparser.XMLParser;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Team Muffin on 25/10/16.
 * The information of the connection
 */
public class ProxyConnection {
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

    private boolean waiting;


    public ProxyConnection(SelectionKey ck, SelectionKey sk){
        clientKey = ck;
        serverKey = sk;
        clientBuffer = ByteBuffer.allocate(4096);
        serverBuffer = ByteBuffer.allocate(4096);
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
            out.println(s);
        }
        bf.flip();
        msg.clear();
        return bf;
    }
    public void handleWrite(SelectionKey key){

        try {
            int byteWrite = 0;
            if (status == Status.WAITING_SERVER) {
                serverBuffer.clear();
                serverBuffer.put((ConnectionHandler.INITIAL_STREAM[0] + serverName + ConnectionHandler.INITIAL_STREAM[1]).getBytes("UTF-8"));
                out.println(new String(serverBuffer.array()));
                serverBuffer.flip();
                byteWrite = ((SocketChannel)key.channel()).write(serverBuffer);
                key.interestOps(SelectionKey.OP_READ);
            }else if(status == Status.CONNECTED){
                if(key.equals(clientKey)){
                    serverBuffer.flip();
                    System.out.println("MANDO CLI: " + new String(serverBuffer.array()));
                    byteWrite = ((SocketChannel)clientKey.channel()).write(serverBuffer);
                    clientKey.interestOps(SelectionKey.OP_READ);
                    serverBuffer.clear();
                }else{
                    clientBuffer.flip();
                    System.out.println("MANDO SRV: " + new String(clientBuffer.array()));
                    byteWrite = ((SocketChannel)serverKey.channel()).write(clientBuffer);
                    serverKey.interestOps(SelectionKey.OP_READ);
                    clientBuffer.clear();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleRead(SelectionKey key){
        try{
            int bytesRead = 0;
            if(status == Status.STARTING){
                bytesRead = ((SocketChannel) key.channel()).read(clientBuffer);
                //if lo que leo tiene stream y esta bien formado -> mando los 2 cosos, sino me quedo esperando el stream
                out.println(new String(clientBuffer.array()));
                if(XMLParser.startWith("<?xml",clientBuffer)){

                }
                if(XMLParser.contains("<stream:stream",clientBuffer)){
                    serverName = XMLParser.getTo(clientBuffer);
                    clientBuffer.clear();
                    clientBuffer.put(ConnectionHandler.INITIAL_SERVER_STREAM);
                    clientBuffer.put(ConnectionHandler.NEGOTIATION);
                    clientBuffer.flip();
                    ((SocketChannel) key.channel()).write(clientBuffer);
                    status = Status.NEGOTIATING;
                    key.interestOps(SelectionKey.OP_READ);
                }
                clientBuffer.clear();
            }else if( status == Status.NEGOTIATING){
                //if lo que leo tiene auth -> leo el usuario y me guardo el stream, sino me quedo esperando / envio auth al server
                bytesRead = ((SocketChannel) key.channel()).read(clientBuffer);
                if(XMLParser.startWith("<auth",clientBuffer)) {
                    byte[] d = Base64.decodeBase64(XMLParser.getAuth(clientBuffer).getBytes("UTF-8"));
                    String stringData = new String(d);
                    JIDName = stringData.substring(1, stringData.indexOf(0, 1));
                    out.println(new String(clientBuffer.array()));
                    serverKey.interestOps(SelectionKey.OP_WRITE);
                    status = Status.WAITING_SERVER;
                }
            }else if(status == Status.WAITING_SERVER){
                //if server respondio -> reenvio lo que recibi al usuario. si es success ya esta, sino vuelvo al estado anterior.
                serverBuffer.clear();
                bytesRead = ((SocketChannel) key.channel()).read(serverBuffer);
                out.print(new String(serverBuffer.array()));
                if(XMLParser.contains("mechanism",serverBuffer)) {
                    clientBuffer.flip();
                    ((SocketChannel) key.channel()).write(clientBuffer);
                    serverKey.interestOps(SelectionKey.OP_READ);
                }else if(XMLParser.startWith("<success",serverBuffer)){
                        status = Status.CONNECTED;
                        clientKey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                }else if(XMLParser.startWith("<failure",serverBuffer)){
                        status = Status.NEGOTIATING;
                    clientKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }

                    //System.out.println(new String(clientBuffer.array()));

            }else if(status == Status.CONNECTED){
                //Ya estoy conectado, aca toda la logica del proxy
                if(key.equals(clientKey)){
                    clientBuffer.clear();
                    bytesRead = ((SocketChannel)clientKey.channel()).read(clientBuffer);
                    if(XMLParser.startWith("<message",clientBuffer)){
                            if(XMLParser.contains("<body",clientBuffer)){
                                clientBuffer = MessageConverter.convertToL33t(clientBuffer);
                                clientBuffer.position(clientBuffer.limit());
                            }
                    }
                    clientKey.interestOps(SelectionKey.OP_READ);
                    serverKey.interestOps(SelectionKey.OP_WRITE);
                }else{
                    serverBuffer.clear();
                    bytesRead = ((SocketChannel)serverKey.channel()).read(serverBuffer);
                    serverKey.interestOps(SelectionKey.OP_READ);
                    clientKey.interestOps(SelectionKey.OP_WRITE);

                }
            }
            if (bytesRead == -1) { // Did the other end close?
                clientKey.channel().close();
                serverKey.channel().close();
                clientKey.cancel();
                serverKey.cancel();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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
    public void setJID(String JID){

        String[] JIDSplit = JID.split("/");
        if(JIDSplit.length >0){
            this.JIDName = JIDSplit[0];
        }else {
            this.JIDName = JID;
        }
        this.JID = JID;
        out.println("TENGO ID: " + JIDName);
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
