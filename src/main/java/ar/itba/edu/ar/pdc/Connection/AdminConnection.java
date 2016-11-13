package ar.itba.edu.ar.pdc.Connection;

import ar.itba.edu.ar.pdc.admin.Reader;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by Team Muffin on 10/11/16.
 */
public class AdminConnection implements Connection{

    ByteBuffer buffer;
    Reader r;
    String response;
    public AdminConnection(){
        buffer = ByteBuffer.allocate(4096);
        r = new Reader();
        response = "";
    }

    public void handleRead(SelectionKey key){
        System.out.println("L");
        try {
            ((SocketChannel) key.channel()).read(buffer);
            buffer.flip();
            buffer.position(0);
            CharBuffer c = Charset.forName("UTF-8").decode(buffer);
            System.out.println(c.toString());

            if(c.toString().contains("\n.\n")) {
                buffer.clear();
                response = r.Read(c);
                handleWrite(key);
            }else if(c.toString().equals("\n")){
                buffer.clear();
            }else{
                buffer.limit(4096);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleWrite(SelectionKey key){
        try {
            //buffer.flip();
            buffer.put(response.getBytes("UTF-8"));
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
            buffer.put(s.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
