package ar.edu.itba.pdc;

import ar.edu.itba.pdc.Protocol.XMPPSelectorProtocol;
import ar.edu.itba.pdc.Connection.ConnectionHandler;
import ar.edu.itba.pdc.Protocol.TCPProtocol;
import ar.edu.itba.pdc.logger.XMPPLogger;

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
            adminChannel.configureBlocking(false);
            listnChannel.configureBlocking(false); // must be nonblocking to register
            listnChannel.socket().bind(new InetSocketAddress(42069));
            adminChannel.socket().bind(new InetSocketAddress(42070));
            // Register selector with channel. The returned key is ignored
            listnChannel.register(selector, SelectionKey.OP_ACCEPT,false);
            adminChannel.register(selector, SelectionKey.OP_ACCEPT,true);

            TCPProtocol protocol = new XMPPSelectorProtocol(4096);
            while (true) { // Run forever, processing available I/O operations
                // Wait for some channel to be ready (or timeout)
                if (selector.select(3000) == 0) { // returns # of ready chans
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
                    if(key.isValid() && key.isConnectable()){
                        protocol.handleConnect(key);
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
