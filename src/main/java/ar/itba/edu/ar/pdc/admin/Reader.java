package ar.itba.edu.ar.pdc.admin;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class Reader {
	private final int access = 1;
	private final int login = 2;
	private final int register = 3;
	private final int multiplex = 4;
	private final int l33t = 5;
	private final int bytes = 6;
	private final int silence = 7;
	private final int unsilence = 8;
	private final int unl33t = 9;
	private final int see = 10;
	private final char separator = ' ';
	
	
	public void Read(CharBuffer cb){
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator){
				int value = validate(sb);
				if(validate(sb) != 0 ){
					checkParameters(cb,value);
					return;
				}else{
					//mandar mensaje que no existe el comando
					return;
				}
			}else{
				sb.append(c);
			}
		}
		//mandar mensaje de unknown error
	}


	private void checkParameters(CharBuffer cb, int value) {
		switch(value){
			case login:
				login(cb);
				break;
			case register:
				register(cb);
				break;
			case see:
				see(cb);
				break;
			case silence:
				silence(cb);
				break;
			case unsilence:
				unsilence(cb);
				break;
			case multiplex:
				multiplex(cb);
				break;
			case l33t:
				l33t(cb);
				break;
			case unl33t:
				unl33t(cb);
				break;
			case access:
				access(cb);
				break;
			case bytes:
				bytes(cb);
				break;
		}
		
	}


	private void bytes(CharBuffer cb) {
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			
		}
	}


	private void unl33t(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void access(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void l33t(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void multiplex(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void unsilence(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void silence(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void see(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void register(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private void login(CharBuffer cb) {
		// TODO Auto-generated method stub
		
	}


	private int validate(StringBuilder sb) {
		String string = sb.toString();
		if(string.equals("LOGIN")){
			return login;
			
		}else if(string.equals("REGISTER")){
			return register;
			
		}else if(string.equals("MULTIPLEX")){
			return multiplex;
			
		}else if(string.equals("SEE")){
			return see;
			
		}else if(string.equals("SILENCE")){
			return silence;
			
		}else if(string.equals("UNSILENCE")){
			return unsilence;
			
		}else if(string.equals("L33T")){
			return l33t;
			
		}else if(string.equals("UNL33T")){
			return unl33t;
			
		}else if(string.equals("ACCESS")){
			return access;
			
		}else if(string.equals("BYTES")){
			return bytes;
			
		}
		return 0;
	}

}
