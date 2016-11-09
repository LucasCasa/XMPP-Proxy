package ar.itba.edu.ar.pdc.Connection;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by Team Muffin on 25/10/16.
 * Manage the Selector
 */
public class ConnectionHandler{
    private Selector s;
    private static ConnectionHandler ch = new ConnectionHandler();

    public static final byte[] INITIAL_SERVER_STREAM = ("<?xml version='1.0' ?><stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>")
            .getBytes();
    public static final byte[] NEGOTIATION = ("<stream:features><mechanisms xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><mechanism>PLAIN</mechanism></mechanisms><auth xmlns=\"http://jabber.org/features/iq-auth\"/></stream:features>")
            .getBytes();

    protected static final String[] INITIAL_STREAM = {"<?xml version='1.0' ?><stream:stream to='","' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>"};



    private ConnectionHandler(){
        try {
            s = Selector.open();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ConnectionHandler getInstance(){
        return ch;
    }

    public SelectionKey addConnection(SocketChannel sc,Object attr) throws IOException{
        return sc.register(s,SelectionKey.OP_READ,attr);
    }

    public Selector getSelector() {
        return s;
    }
}
