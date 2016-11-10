package ar.itba.edu.ar.pdc.Connection;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by lucas on 10/11/16.
 */
public class AdminConnection implements Connection{

    ByteBuffer buffer;

    public AdminConnection(){
        buffer = ByteBuffer.allocate(4096);
    }

    public void handleRead(SelectionKey key){
        System.out.println("L");
        try {
            ((SocketChannel) key.channel()).read(buffer);
            buffer.flip();
            CharBuffer c = Charset.forName("ASCII").decode(buffer);
            System.out.println(c.toString());
            buffer.clear();
            addBuffer("Puto");
            handleWrite(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleWrite(SelectionKey key){
        try {
            buffer.flip();
            ((SocketChannel) key.channel()).write(buffer);
            buffer.clear();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void addBuffer(String s){
        try{
            buffer.clear();
            buffer.put(s.getBytes("ASCII"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
