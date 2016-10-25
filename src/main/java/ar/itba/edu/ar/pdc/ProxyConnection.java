package ar.itba.edu.ar.pdc;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * Created by Muffin on 25/10/16.
 */
public class ProxyConnection {
    SelectionKey clientKey;
    SelectionKey serverKey;
    ByteBuffer clientBuffer;
    ByteBuffer serverBuffer;
    boolean waiting;

    public ProxyConnection(SelectionKey ck, SelectionKey sk){
        clientKey = ck;
        serverKey = sk;
        clientBuffer = ByteBuffer.allocate(4096);
        serverBuffer = ByteBuffer.allocate(4096);
        waiting = false;
    }
}
