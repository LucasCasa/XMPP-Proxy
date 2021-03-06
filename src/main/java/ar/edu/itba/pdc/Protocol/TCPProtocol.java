package ar.edu.itba.pdc.Protocol;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by Team Muffin on 24/10/16.
 * TCP Protocol Interface
 */
public interface TCPProtocol {
    void handleAccept(SelectionKey key) throws IOException;
    void handleRead(SelectionKey key) throws IOException;
    void handleWrite(SelectionKey key) throws IOException;
    void handleConnect(SelectionKey key) throws IOException;
}
