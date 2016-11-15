package ar.edu.itba.pdc.Connection;

import java.nio.channels.SelectionKey;

/**
 * Created by Team Muffin on 10/11/16.
 */
public interface Connection {

    public void handleWrite(SelectionKey key);

    public void handleRead(SelectionKey key);
}
