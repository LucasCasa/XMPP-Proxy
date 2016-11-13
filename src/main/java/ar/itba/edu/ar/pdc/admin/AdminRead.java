package ar.itba.edu.ar.pdc.admin;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import ar.itba.edu.ar.pdc.Connection.ConnectionHandler;

public class AdminRead {

	private final static int [] params = {0,2,2,2,1,0,1,1,1,1,0,1}; 
	
	private final static String error = "Invalid Parameters";
	private static Converter conv = new ConverterImpl();

	private static int value = -1;

	public static void main(String[] args) {
		Selector selector;
		try{
			selector = ConnectionHandler.getInstance().getSelector();
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(new InetSocketAddress(42070));
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT,true);
			System.out.println("Empeza a escribir");
			ByteBuffer bd = readInput();
			((SocketChannel) serverChannel.keyFor(selector).channel()).write(bd);
			System.out.println(bd);
			
			
		}catch(Exception e){
			
		}
	}
	
	private static ByteBuffer readInput(){
		Charset utf8 = Charset.forName("UTF-8");
		String rta = null;
		Scanner s = new Scanner(System.in);
		while(s.hasNext()){
			String aux = s.nextLine();
			 rta = Validate(aux);
			if(rta == null){
				System.out.println("HUBO ERROR");
				rta = conv.resultError(error);
			}
			else{
				System.out.println(rta);
				//mandar al servidor
			}
		}
		s.close();
		return utf8.encode(rta);
	}

	private static String Validate(String s) {
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<s.length();i++){
			char c = s.charAt(i);
			if(c == ' '){
				value = isCorrect(sb);
				if(value == -1){
					return conv.resultError("Command does not exist");
				}
				int param = params[value];
				switch(param){
				case 1:
					return readOneParam(s,i+1);
				case 2:
					return readTwoParam(s,i+1);
				}
			}else{
				sb.append(c);
			}
		}
		return readNoParam(sb);
	}

	private static String readTwoParam(String s, int i) {
		StringBuilder s1 = new StringBuilder("");
		StringBuilder s2 = new StringBuilder("");
		String rta = conv.resultError(error);
		int cant = 0;
		for(int j=i;j<s.length();j++){
			char c = s.charAt(j);
			if(c == ' ' && cant == 0){
				cant ++;
			}else if(cant == 0){
				s1.append(c);
			}else{
				s2.append(c);
			}
		}
		if(cant == 0 && value == Info.multiplex){
			rta = conv.multiplex(s1.toString());
		}
		switch(value){
		case Info.login:
			rta = conv.login(s1.toString(), s2.toString());
			break;
		case Info.register:
			rta = conv.register(s1.toString(), s2.toString());
			break;
		case Info.multiplex:
			rta = conv.multiplex(s1.toString(), s2.toString());
			break;
		}
		return rta;

	}

	private static String readOneParam(String s,int i) {
		StringBuilder s1 = new StringBuilder("");
		String rta = conv.resultError(error);
		for(int j=i;j<s.length();j++){
			char c = s.charAt(j);
			s1.append(c);
		}
		switch(value){
		case Info.l33t:
			rta = conv.l33t(s1.toString());
			break;
		case Info.silence:
			rta = conv.silence(s1.toString());
			break;
		case Info.unsilence:
			rta = conv.unSilence(s1.toString());
			break;
		case Info.unl33t:
			rta = conv.unL33t(s1.toString());
			break;
		case Info.see:
			rta = conv.see(s1.toString());
			break;
		case Info.unmultiplex:
			rta = conv.unmultiplex(s1.toString());
			break;
		}
		return rta;

	}

	private static String readNoParam(StringBuilder s) {
		value = isCorrect(s);
		String rta = conv.resultError(error);
		switch(value){
		case Info.bytes:
			rta = conv.bytes();
			break;
		case Info.access:
			rta = conv.access();
			break;
		case Info.exit:
			rta = conv.exit();
			break;
		}
		return rta;
	}

	private static int isCorrect(StringBuilder sb) {
		String string = sb.toString().toUpperCase();
		if(string.equals("LOGIN")){
			return Info.login;
		}else if(string.equals("REGISTER")){
			return Info.register;

		}else if(string.equals("MULTIPLEX")){
			return Info.multiplex;

		}else if(string.equals("SEE")){
			return Info.see;

		}else if(string.equals("SILENCE")){
			return Info.silence;

		}else if(string.equals("UNSILENCE")){
			return Info.unsilence;

		}else if(string.equals("L33T")){
			return Info.l33t;

		}else if(string.equals("UNL33T")){
			return Info.unl33t;

		}else if(string.equals("ACCESS")){
			return Info.access;

		}else if(string.equals("BYTES")){
			return Info.bytes;

		}else if (string.equals("EXIT")){
			return Info.exit;
		}else if(string.equals("UNMULTIPLEX")){
			return Info.unmultiplex;
		}
		return -1;
	}
}
