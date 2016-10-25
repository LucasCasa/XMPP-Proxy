package ar.itba.edu.ar.pdc;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by lucas on 25/10/16.
 */
public class ConnectionHandler{
    Selector s;
    private static ConnectionHandler ch = new ConnectionHandler();

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
}
