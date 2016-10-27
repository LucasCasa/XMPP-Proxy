package ar.itba.edu.ar.pdc.Connection;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Team Muffin on 25/10/16.
 * The information of the connection
 */
public class ProxyConnection {
    private SelectionKey clientKey;
    private SelectionKey serverKey;
    private List<String> clientMessages = new LinkedList<>();
    private List<String> serverMessages = new LinkedList<>();
    private String JIDName = null;
    private String JID = null;
    private boolean waiting;

    public ProxyConnection(SelectionKey ck, SelectionKey sk){
        clientKey = ck;
        serverKey = sk;
        waiting = false;
    }
    public ByteBuffer getClientBuffer(){
        return fillBuffer(clientMessages);
    }
    public ByteBuffer getServerBuffer(){
        return fillBuffer(serverMessages);
    }

    private ByteBuffer fillBuffer(List<String> msg){
        int length = 0;
        for(String s : msg){
            length += s.getBytes().length;
        }
        ByteBuffer bf = ByteBuffer.allocate(length);
        for(String s : msg){
            bf.put(s.getBytes());
            System.out.println(s);
        }
        bf.flip();
        msg.clear();
        return bf;
    }

    public void addClientMessage(String s){
        clientMessages.add(s);
    }
    public void addServerMessage(String s){
        serverMessages.add(s);
    }

    public SelectionKey getClientKey() {
        return clientKey;
    }
    public void setJID(String JID){

        String[] JIDSplit = JID.split("/");
        if(JIDSplit.length >0){
            this.JIDName = JIDSplit[0];
        }else {
            this.JIDName = JID;
        }
        this.JID = JID;
        System.out.println("TENGO ID: " + JIDName);
    }
    public boolean isWaiting() {
        return waiting;
    }

    public String getJID() {
        return JID;
    }

    public String getJIDName() {
        return JIDName;
    }

    public SelectionKey getServerKey() {
        return serverKey;
    }

    public void setClientKey(SelectionKey clientKey) {
        this.clientKey = clientKey;
    }

    public void setServerKey(SelectionKey serverKey) {
        this.serverKey = serverKey;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
}
