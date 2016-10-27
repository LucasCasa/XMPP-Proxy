package ar.itba.edu.ar.pdc;

import ar.itba.edu.ar.pdc.xmlparser.XMLParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by lucas on 24/10/16.
 */
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
        pc.clientKey = ConnectionHandler.getInstance().addConnection(clntChan,pc);
        SocketChannel serverChannel = SocketChannel.open();
        serverChannel.connect(new InetSocketAddress("localhost", 5222));
        serverChannel.configureBlocking(false);
        pc.serverKey = ConnectionHandler.getInstance().addConnection(serverChannel,pc);

    }

    public void handleRead(SelectionKey key) throws IOException {
        // Client socket channel has pending data

        if(((ProxyConnection) key.attachment()).clientKey.equals(key)) {
            SocketChannel clntChan = (SocketChannel) key.channel();
            ProxyConnection pc = (ProxyConnection) key.attachment();
            ByteBuffer aux = ByteBuffer.allocate(4096);
            long bytesRead = clntChan.read(aux);
            System.out.println("LEO DEL CLIENTE: " + bytesRead);
            System.out.println(new String(aux.array()).substring(0,aux.position()));
            String auxS = new String(aux.array()).substring(0,aux.position());
            if(XMLParser.parse(auxS)){
                auxS = MessageConverter.transform(auxS);
            }
            pc.clientMessages.add(auxS);
            if (bytesRead == -1) { // Did the other end close?
                clntChan.close();
            } else if (bytesRead > 0) {
                // Indicate via key that reading/writing are both of interest now.
                key.interestOps(SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_WRITE);
                pc.waiting = true;
            }

        }else if(((ProxyConnection) key.attachment()).serverKey.equals(key)){
            SocketChannel srvChan = (SocketChannel)key.channel();
            ProxyConnection pc = (ProxyConnection) key.attachment();
            ByteBuffer aux = ByteBuffer.allocate(4096);
            long bytesRead = srvChan.read(aux);
            System.out.println("LEO DEL SERVIDOR: " + bytesRead);
            System.out.println(new String(aux.array()).substring(0,aux.position()));
            String auxS = new String(aux.array()).substring(0,aux.position());
            if(XMLParser.parse(auxS)){
                auxS = MessageConverter.transform(auxS);
            }
            pc.serverMessages.add(auxS);
            if (bytesRead == -1) { // Did the other end close?
                srvChan.close();
            } else if (bytesRead > 0) {
                // Indicate via key that reading/writing are both of interest now.
                pc.clientKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_READ);
            }
        }



    }
    public void handleWrite(SelectionKey key) throws IOException {
        /*
         * Channel is available for writing, and key is valid (i.e., client
         * channel not closed).
         */
        // Retrieve data read earlier
        if(((ProxyConnection) key.attachment()).clientKey.equals(key)) {
            ProxyConnection pc = (ProxyConnection) key.attachment();
            SocketChannel cliChan = (SocketChannel) pc.clientKey.channel();
            System.out.println("MANDO AL CLIENTE");
            ByteBuffer bf = ByteBuffer.allocate(4096);

            for(String s : pc.serverMessages){
                bf.put(s.getBytes());
                System.out.println(s);
            }
            bf.flip();
            pc.serverMessages.clear();
            cliChan.write(bf);
            if (!bf.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.clientKey.interestOps(SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_READ);
                pc.waiting = false;
            }
        }else if(((ProxyConnection) key.attachment()).serverKey.equals(key)) {
            ProxyConnection pc = (ProxyConnection)key.attachment();

            SocketChannel srvChan = (SocketChannel) pc.serverKey.channel();
            System.out.println("MANDO AL SERVIDOR");
            ByteBuffer bf = ByteBuffer.allocate(4096);

            for(String s : pc.clientMessages){
                bf.put(s.getBytes());
                System.out.println(new String(s.getBytes()));
            }
            bf.flip();
            pc.clientMessages.clear();
            srvChan.write(bf);
            if (!bf.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.clientKey.interestOps(SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_READ);
            }

        }

    }
}
