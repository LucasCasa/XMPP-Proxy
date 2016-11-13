package ar.itba.edu.ar.pdc;

import ar.itba.edu.ar.pdc.Connection.ConnectionHandler;
import ar.itba.edu.ar.pdc.Protocol.TCPProtocol;
import ar.itba.edu.ar.pdc.Protocol.XMPPSelectorProtocol;
import ar.itba.edu.ar.pdc.logger.XMPPLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by Team Muffin on 24/10/16.
 * Main Class. Has the Selector
 */
public class Server {

    public static void main(String[] args){
        Selector selector;
        try{
            selector = ConnectionHandler.getInstance().getSelector();

            ServerSocketChannel listnChannel = ServerSocketChannel.open();
            ServerSocketChannel adminChannel = ServerSocketChannel.open();
            SocketChannel originServer = SocketChannel.open();
            listnChannel.socket().bind(new InetSocketAddress(42069));
            adminChannel.socket().bind(new InetSocketAddress(42070));
            originServer.configureBlocking(false);
            adminChannel.configureBlocking(false);
            listnChannel.configureBlocking(false); // must be nonblocking to register
            // Register selector with channel. The returned key is ignored
            listnChannel.register(selector, SelectionKey.OP_ACCEPT,false);
            adminChannel.register(selector, SelectionKey.OP_ACCEPT,true);
            if (!originServer.connect(new InetSocketAddress("localhost", 5222))) {
                while (!originServer.finishConnect()) {
                    System.out.print(".a"); // Do something else
                }
            }

            TCPProtocol protocol = new XMPPSelectorProtocol(4096);
            while (true) { // Run forever, processing available I/O operations
                // Wait for some channel to be ready (or timeout)
                if (selector.select(3000) == 0) { // returns # of ready chans
                    System.out.print(".");
                    continue;
                }
                // Get iterator on set of keys with I/O to process
                Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
                while (keyIter.hasNext()) {
                    SelectionKey key = keyIter.next(); // Key is bit mask
                    // Server socket channel has pending connection requests?
                    if (key.isValid() && key.isAcceptable()) {
                        protocol.handleAccept(key);
                    }
                    // Client socket channel has pending data?
                    if (key.isValid() && key.isReadable()) {
                        protocol.handleRead(key);
                    }
                    // Client socket channel is available for writing and
                    // key is valid (i.e., channel not closed)?
                    if (key.isValid() && key.isWritable()) {
                        protocol.handleWrite(key);
                    }
                    keyIter.remove(); // remove from set of selected keys
                }
            }

        }catch (IOException e ) {
        	XMPPLogger.getInstance().error("Exception",e);
            System.out.println(e.toString());
        }


    }
}
