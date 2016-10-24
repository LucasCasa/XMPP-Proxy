package ar.itba.edu.ar.pdc;

import java.io.IOException;
import java.io.StringReader;
import java.nio.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

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
        clntChan.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
    }

    public void handleRead(SelectionKey key) throws IOException {
        // Client socket channel has pending data
        SocketChannel clntChan = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        long bytesRead = clntChan.read(buf);
        if (bytesRead == -1) { // Did the other end close?
            clntChan.close();
        } else if (bytesRead > 0) {
            // Indicate via key that reading/writing are both of interest now.
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    public void handleWrite(SelectionKey key,SocketChannel key2) throws IOException {
        /*
         * Channel is available for writing, and key is valid (i.e., client
         * channel not closed).
         */
        // Retrieve data read earlier
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip(); // Prepare buffer for writing
        SocketChannel clntChan = (SocketChannel) key.channel();
        System.out.println("MANDO");
        byte[] b = buf.array();
        String s = new String(b);
        System.out.println("LEO DEL CLIENTE: " + s);
        if(s.indexOf("<body>") != -1)
            s = s.replaceFirst("<body>.*</body>",s.substring(s.indexOf("<body>") +6,s.indexOf("</body>")).replace('a','4'));
        key2.write(buf);

        ByteBuffer bf = ByteBuffer.allocate(1024);
        while(key2.read(bf) == 0);
        bf.flip();
        System.out.println("LEO DEL SERVIDOR: " +new String(bf.array()));
        clntChan.write(bf);

        if (!buf.hasRemaining()) { // Buffer completely written?
            // Nothing left, so no longer interested in writes
            key.interestOps(SelectionKey.OP_READ);
        }
        buf.compact(); // Make room for more data to be read in
    }
}
