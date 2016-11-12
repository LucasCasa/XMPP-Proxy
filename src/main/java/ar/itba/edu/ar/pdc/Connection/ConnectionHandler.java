package ar.itba.edu.ar.pdc.Connection;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Team Muffin on 25/10/16.
 * Manage the Selector
 */
public class ConnectionHandler{
    private Selector s;
    private static Set<String> leet;
    private static Set<String> silence = new HashSet<>();
    private static Map<String,String> multiplex = new HashMap<>();
    private static Map<String,String> users = new HashMap<>();
    private static boolean loggedIn = false;

    private static ConnectionHandler ch = new ConnectionHandler();

    public static final byte[] INITIAL_SERVER_STREAM = ("<?xml version='1.0' ?><stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>")
            .getBytes();
    public static final byte[] NEGOTIATION = ("<stream:features><mechanisms xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><mechanism>PLAIN</mechanism></mechanisms><auth xmlns=\"http://jabber.org/features/iq-auth\"/></stream:features>")
            .getBytes();

    protected static final String[] INITIAL_STREAM = {"<?xml version='1.0' ?><stream:stream to='","' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>"};



    private ConnectionHandler(){
        try {
            s = Selector.open();
            leet = new HashSet<>();
            leet.add("lucas@muffin.com");
            leet.add("nicolas@muffin.com");
            silence.add("áñá@muffin.com");
            multiplex.put("test@muffin2.com","test@muffin.com");
            users.put("MUFFIN", "MUFFIN");
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

    public static boolean isL33t(String user){
            return leet.contains(user);
    }
    public static void setL33t(String user){
        leet.add(user);
    }
    public static void unSetL33t(String user){
        leet.remove(user);
    }

    public static boolean isSilenced(String user){
        return silence.contains(user);
    }
    public static void setSilence(String user){
        silence.add(user);
    }
    public static void unSetSilence(String user){
        silence.remove(user);
    }

    public static boolean isMultiplex(String user){
        return multiplex.containsKey(user);
    }
    public static String multiplex(String user){
        if(multiplex.containsKey(user)){
            return multiplex.get(user);
        }else{
            return user;
        }
    }
    public static void setMultiplex(String user,String newServer){
        multiplex.put(user,newServer);
    }
    public static void unSetMultiplex(String user){
        multiplex.remove(user);
    }

	public static boolean isRegistered(String user) {
		return users.containsKey(user);
	}

	public static void setRegister(String user, String password) {
		users.put(user, password);

	}

	public static boolean exists(String user, String password) {
		return users.containsKey(user) && users.get(user).equals(password);
	}

	public static void setLogin(String user, String password) {
		loggedIn = true;

	}

	public static Set<String> getMultiplex(){
		return multiplex.keySet();
	}

	public static Set<String> getL33t(){
		return leet;
	}

	public static void exit() {
		loggedIn = false;
	}

	public static boolean isLogged(){
		return loggedIn;
	}

	public static Set<String> getSilenced() {
		return silence;
	}

}
