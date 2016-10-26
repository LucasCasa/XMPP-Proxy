package ar.itba.edu.ar.pdc;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Muffin on 25/10/16.
 */
public class ProxyConnection {
    SelectionKey clientKey;
    SelectionKey serverKey;
    List<String> clientMessages = new LinkedList<>();
    List<String> serverMessages = new LinkedList<>();
    boolean waiting;

    public ProxyConnection(SelectionKey ck, SelectionKey sk){
        clientKey = ck;
        serverKey = sk;
        waiting = false;
    }
}
