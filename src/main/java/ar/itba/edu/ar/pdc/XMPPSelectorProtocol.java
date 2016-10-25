package ar.itba.edu.ar.pdc;

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
            long bytesRead = clntChan.read(pc.clientBuffer);
            System.out.println("LEO DEL CLIENTE: " + bytesRead);
            System.out.println(new String(pc.clientBuffer.array()));
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
            long bytesRead = srvChan.read(pc.serverBuffer);
            System.out.println("LEO DEL SERVIDOR: " + bytesRead);
            System.out.println(new String(pc.serverBuffer.array()));
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
            pc.serverBuffer.flip(); // Prepare buffer for writing
            SocketChannel srvChan = (SocketChannel) pc.clientKey.channel();
            System.out.println("MANDO AL CLIENTE");
            srvChan.write(pc.serverBuffer);
            if (!pc.serverBuffer.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.clientKey.interestOps(SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_READ);
                pc.waiting = false;
            }
            pc.serverBuffer.compact(); // Make room for more data to be read in
        }else if(((ProxyConnection) key.attachment()).serverKey.equals(key)) {
            ProxyConnection pc = (ProxyConnection)key.attachment();
            pc.clientBuffer.flip(); // Prepare buffer for writing
            SocketChannel cliChan = (SocketChannel) pc.serverKey.channel();
            System.out.println("MANDO AL SERVIDOR");
            cliChan.write(pc.clientBuffer);
            if (!pc.clientBuffer.hasRemaining()) { // Buffer completely written?
                // Nothing left, so no longer interested in writes
                pc.clientKey.interestOps(SelectionKey.OP_READ);
                pc.serverKey.interestOps(SelectionKey.OP_READ);
            }
            pc.clientBuffer.compact(); // Make room for more data to be read in
        }

    }
}
