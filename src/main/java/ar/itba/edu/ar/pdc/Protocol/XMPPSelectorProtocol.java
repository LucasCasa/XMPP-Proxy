package ar.itba.edu.ar.pdc.Protocol;

import ar.itba.edu.ar.pdc.Connection.ConnectionHandler;
import ar.itba.edu.ar.pdc.Connection.ProxyConnection;
import ar.itba.edu.ar.pdc.xmlparser.MessageConverter;
import ar.itba.edu.ar.pdc.xmlparser.XMLParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Team Muffin on 24/10/16.
 * Manage the connections
 **/
public class XMPPSelectorProtocol implements TCPProtocol {
    private int bufSize; // Size of I/O buffer

    public XMPPSelectorProtocol(int bufSize) {
        this.bufSize = bufSize;
    }

    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();
        clntChan.configureBlocking(false); // Must be nonblocking to register
        // Register the selector with new channel for read and attach byte buffer
        ProxyConnection pc = new ProxyConnection(null,null);
        pc.setClientKey(ConnectionHandler.getInstance().addConnection(clntChan,pc));
        SocketChannel serverChannel = SocketChannel.open();
        serverChannel.connect(new InetSocketAddress("localhost", 5222));
        serverChannel.configureBlocking(false);
        pc.setServerKey(ConnectionHandler.getInstance().addConnection(serverChannel,pc));

    }

    public void handleRead(SelectionKey key) throws IOException {
        // Client socket channel has pending data

        if(((ProxyConnection) key.attachment()).getClientKey().equals(key)) {
            SocketChannel clntChan = (SocketChannel) key.channel();
            ProxyConnection pc = (ProxyConnection) key.attachment();
            ByteBuffer aux = ByteBuffer.allocate(bufSize);
            long bytesRead = clntChan.read(aux);
            System.out.println("LEO DEL CLIENTE: " + bytesRead);
            String auxS = new String(aux.array()).substring(0,aux.position());
            //XMLParser.isMessage(aux);
            if(XMLParser.isMessage(aux)){
                auxS = MessageConverter.transform(auxS);
            }
            pc.addClientMessage(auxS);
            if (bytesRead == -1) { // Did the other end close?
                clntChan.close();
                pc.getServerKey().channel().close();
                key.cancel();
                pc.getServerKey().cancel();
            } else if (bytesRead > 0) {
                // Indicate via key that reading/writing are both of interest now.
                key.interestOps(SelectionKey.OP_READ);
                pc.getServerKey().interestOps(SelectionKey.OP_WRITE);
                pc.setWaiting(true);
            }

        }else if(((ProxyConnection) key.attachment()).getServerKey().equals(key)){
            SocketChannel srvChan = (SocketChannel)key.channel();
            ProxyConnection pc = (ProxyConnection) key.attachment();
            ByteBuffer aux = ByteBuffer.allocate(bufSize);
            long bytesRead = srvChan.read(aux);
            System.out.println("LEO DEL SERVIDOR: " + bytesRead);
            String auxS = new String(aux.array()).substring(0,aux.position());
            if(XMLParser.isMessage(aux)){
                auxS = MessageConverter.transform(auxS);
            }else if(XMLParser.isJID(aux)){
                pc.setJID(XMLParser.isJID(aux));
            }
            pc.addServerMessage(auxS);
            if (bytesRead == -1) { // Did the other end close?
                srvChan.close();
                pc.getClientKey().channel().close();
                key.cancel();
                pc.getClientKey().cancel();
            } else if (bytesRead > 0) {
                // Indicate via key that reading/writing are both of interest now.
                pc.getClientKey().interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                pc.getServerKey().interestOps(SelectionKey.OP_READ);
            }
        }



    }
    public void handleWrite(SelectionKey key) throws IOException {
        /*
         * Channel is available for writing, and key is valid (i.e., client
         * channel not closed).
         */
        // Retrieve data read earlier
        if(((ProxyConnection) key.attachment()).getClientKey().equals(key)) {
            ProxyConnection pc = (ProxyConnection) key.attachment();
            SocketChannel cliChan = (SocketChannel) pc.getClientKey().channel();
            System.out.println("MANDO AL CLIENTE");
            ByteBuffer bf = pc.getServerBuffer();
            cliChan.write(bf);
            if (!bf.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.getClientKey().interestOps(SelectionKey.OP_READ);
                pc.getServerKey().interestOps(SelectionKey.OP_READ);
                pc.setWaiting(false);
            }
        }else if(((ProxyConnection) key.attachment()).getServerKey().equals(key)) {
            ProxyConnection pc = (ProxyConnection)key.attachment();

            SocketChannel srvChan = (SocketChannel) pc.getServerKey().channel();
            System.out.println("MANDO AL SERVIDOR");
            ByteBuffer bf =pc.getClientBuffer();
            srvChan.write(bf);
            if (!bf.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.getClientKey().interestOps(SelectionKey.OP_READ);
                pc.getServerKey().interestOps(SelectionKey.OP_READ);
                pc.setWaiting(false);
            }

        }

    }
}
