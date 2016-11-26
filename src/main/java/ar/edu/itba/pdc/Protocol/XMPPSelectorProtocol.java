package ar.edu.itba.pdc.Protocol;

import ar.edu.itba.pdc.Connection.AdminConnection;
import ar.edu.itba.pdc.Connection.Connection;
import ar.edu.itba.pdc.Connection.ConnectionHandler;
import ar.edu.itba.pdc.Connection.ProxyConnection;
import ar.edu.itba.pdc.Metrics;
import ar.edu.itba.pdc.logger.XMPPLogger;

import java.io.IOException;
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
        if((Boolean)key.attachment()){ //ES ADMIN
            SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
            channel.configureBlocking(false);
            AdminConnection ac = new AdminConnection();
            ConnectionHandler.getInstance().addConnection(channel, ac);
            XMPPLogger.getInstance().info("ADMIN CONNECTED");
        }else {
            SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();
            clntChan.configureBlocking(false); // Must be nonblocking to register
            // Register the selector with new channel for read and attach byte buffer
            ProxyConnection pc = new ProxyConnection(null, null);
            pc.setClientKey(ConnectionHandler.getInstance().addConnection(clntChan, pc));
            XMPPLogger.getInstance().info("NEW USER CONNECTING");
        }
        Metrics.incrementAccess();
    }

    public void handleRead(SelectionKey key) throws IOException {
        ((Connection) key.attachment()).handleRead(key);

    }
    public void handleWrite(SelectionKey key) throws IOException {
        ((Connection) key.attachment()).handleWrite(key);
    }

    @Override
    public void handleConnect(SelectionKey key) throws IOException {
        if(((SocketChannel) key.channel()).finishConnect()){
            System.out.println("FINISHED");
            key.interestOps(SelectionKey.OP_WRITE);
            ((ProxyConnection)key.attachment()).setServerKey(key);
        }
    }
}
