package ar.itba.edu.ar.pdc.Connection;

import java.nio.channels.SelectionKey;

/**
 * Created by lucas on 10/11/16.
 */
public interface Connection {

    public void handleWrite(SelectionKey key);

    public void handleRead(SelectionKey key);
}