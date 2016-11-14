package ar.itba.edu.ar.pdc.Connection;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ar.itba.edu.ar.pdc.logger.XMPPLogger;

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

    public static final String[] MESSAGE = {"<message type='chat' to='","' from='","'><active xmlns='http://jabber.org/protocol/chatstates'/><body>No estoy disponible</body></message>"};

    public static final String[] MESSAGE2 = {"<message type='chat' to='","' ><active xmlns='http://jabber.org/protocol/chatstates'/><body>No estoy disponible</body></message>"};

    protected static final String[] INITIAL_STREAM = {"<?xml version='1.0' ?><stream:stream to='","' xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams' version='1.0'>"};



    private ConnectionHandler(){
        try {
            s = Selector.open();
            leet = new HashSet<>();
            leet.add("lucas@muffin.com");
            leet.add("nicolas@muffin.com");
            leet.add("nico@example.com");
            leet.add("ncastano@example.com");
            silence.add("test@muffin.com");
            multiplex.put("test@muffin2.com","test@muffin.com");
            users.put("muffin", "muffin");
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
    	XMPPLogger.getInstance().info(user + " l33t");
        leet.add(user);
    }
    public static void unSetL33t(String user){
    	XMPPLogger.getInstance().info(user + " unl33t");
        leet.remove(user);
    }

    public static boolean isSilenced(String user){
        return silence.contains(user);
    }
    public static void setSilence(String user){
    	XMPPLogger.getInstance().info(user + " silenced");
        silence.add(user);
    }
    public static void unSetSilence(String user){
    	XMPPLogger.getInstance().info(user + " unsilenced");
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
    	XMPPLogger.getInstance().info(user + " multiplexed");
        multiplex.put(user,newServer);
    }
    public static void unSetMultiplex(String user){
    	XMPPLogger.getInstance().info(user + " unmultiplexed");
        multiplex.remove(user);
    }

	public static boolean isRegistered(String user) {
		return users.containsKey(user);
	}

	public static void setRegister(String user, String password) {
		XMPPLogger.getInstance().info(user + " registered");
		users.put(user, password);

	}

	public static boolean exists(String user, String password) {
		return users.containsKey(user) && users.get(user).equals(password);
	}

	public static void setLogin(String user, String password) {
		XMPPLogger.getInstance().info(user + " logged in");
		loggedIn = true;

	}

	public static Set<String> getMultiplex(){
		return multiplex.keySet();
	}

	public static Set<String> getL33t(){
		return leet;
	}

	public static void exit() {
		XMPPLogger.getInstance().info("logged out");
		loggedIn = false;
	}

	public static boolean isLogged(){
		return loggedIn;
	}

	public static Set<String> getSilenced() {
		return silence;
	}

    public static byte[] silenced(String jid, String from) {
        try {
            return (MESSAGE[0]+jid+MESSAGE[1]+from+MESSAGE[2]).getBytes("UTF-8");
        }catch (Exception e){
            return new byte[0];
        }

    }

    public static byte[] silenced2(String jid) {
        try {
            return (MESSAGE2[0]+jid+MESSAGE2[1]).getBytes("UTF-8");
        }catch (Exception e){
            return new byte[0];
        }

    }

}
