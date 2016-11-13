package ar.itba.edu.ar.pdc.admin;

import ar.itba.edu.ar.pdc.Metrics;
import ar.itba.edu.ar.pdc.Connection.ConnectionHandler;

import java.nio.CharBuffer;

public class Reader {
	private final static Converter conv = new ConverterImpl();
	private final static String wrongParams = "INVALID PARAMETERS";
	private final static String unknownError = "UNKNOWN ERROR";
	private final static int access = 1;
	private final static int login = 2;
	private final static int register = 3;
	private final static int multiplex = 4;
	private final static int l33t = 5;
	private final static int bytes = 6;
	private final static int silence = 7;
	private final static int unsilence = 8;
	private final static int unl33t = 9;
	private final static int see = 10;
	private final static int unmultiplex = 11;
	private final static int exit = 12;
	private final static char separator = ' ';

	public String Read(CharBuffer cb){
		StringBuilder sb = new StringBuilder("");
		//cb.flip();
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator){
				int value = validate(sb);
				if(value != 0 ){
					return checkParameters(cb,value);
				}else{
					return conv.resultError(wrongParams);
				}
			}else{
				sb = sb.append(c);
			}
		}
		String aux = sb.toString();
		if(!ConnectionHandler.isLogged()){
			return conv.resultError("MUST LOGIN");
		}
		if(aux.equals("BYTES\n.\n")){
			return conv.resultOk(Metrics.getBytes());
		}else if(aux.equals("ACCESS\n.\n")){
			return conv.resultOk(Metrics.getAccess());
		}else if(aux.equals("EXIT\n.\n")){
			ConnectionHandler.exit();
		}else{
			return conv.resultError(unknownError);
		}
		return conv.resultError(unknownError);
	}


	private static String checkParameters(CharBuffer cb, int value) {
		switch(value){
		case login:
			return login(cb);
		case register:
			return register(cb);
		case see:
			return see(cb);
		case silence:
			return silence(cb);
		case unsilence:
			return unsilence(cb);
		case multiplex:
			return multiplex(cb);
		case unmultiplex:
			return unmultiplex(cb);
		case l33t:
			return l33t(cb);
		case unl33t:
			return unl33t(cb);
		}
		return conv.resultError(wrongParams);
	}


	private static String unmultiplex(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(ConnectionHandler.isMultiplex(sb.toString())){
					ConnectionHandler.unSetMultiplex(user);
				}
				else{
					return conv.resultError("Cannot unmultiplex someone who is not multiplexed");
				}
				return conv.resultOk(user + " is unmultiplexed");
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String unl33t(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(ConnectionHandler.isL33t(user)){
					ConnectionHandler.unSetL33t(sb.toString());
					return conv.resultOk(user + " is unl33t");
				}else{
					return conv.resultError("Cannot unl33t someone who is not l33t");
				}
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String l33t(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(!ConnectionHandler.isL33t(user)){
					ConnectionHandler.setL33t(user);
					return conv.resultOk(user + " is l33t");
				}else{
					return conv.resultError(user + " already l33t");
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String multiplex(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder sb = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(!ConnectionHandler.isMultiplex(user)){
					ConnectionHandler.setMultiplex(name.toString(),sb.toString());
					return conv.resultOk(user + " multiplexed");
				}
				else{
					return conv.resultError(user + " is already multiplexed");
					
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String unsilence(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(ConnectionHandler.isSilenced(user)){
					ConnectionHandler.unSetSilence(user);
					return conv.resultOk(user + " silenced");
				}else{
					return conv.resultError(user + " already silenced");
				}
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String silence(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(!ConnectionHandler.isSilenced(user)){
					ConnectionHandler.setSilence(user);
					return conv.resultOk(user + " silenced");
				}else{
					return conv.resultError(user + " already silenced");
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static String see(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String param = sb.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(param.equals("L33T")){
					return conv.resultSee(ConnectionHandler.getL33t());
				}else if(param.equals("MULTIPLEX")){
					return conv.resultSee(ConnectionHandler.getMultiplex());
				}else if(param.equals("SILENCE")){
					return conv.resultSee(ConnectionHandler.getSilenced());
				}else{
					return conv.resultError(wrongParams);
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static String register(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder pass = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				pass = pass.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = name.toString();
				String password = pass.toString();
				if(!ConnectionHandler.isLogged()){
					return conv.resultError("MUST LOGIN");
				}
				if(!ConnectionHandler.isRegistered(user)){
					ConnectionHandler.setRegister(user,password);
					return conv.resultOk(user + " registered");
				}else{
					return conv.resultError(user + " already registered");
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static String login(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder pass = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				pass = pass.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = name.toString();
				String password = pass.toString();
				if(!ConnectionHandler.exists(user,password)){
					return conv.resultError("Username or password is not correct");
				}else{
					ConnectionHandler.setLogin(user,password);
					return conv.resultOk("Logged in");
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static int validate(StringBuilder sb) {
		String string = sb.toString();
		if(string.equals("LOGIN")){
			return login;
		}else if(string.equals("REGISTER")){
			return register;
		}else if(string.equals("MULTIPLEX")){
			return multiplex;
		}else if(string.equals("UNMULTIPLEX")){
			return unmultiplex;
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
		}else if(string.equals("EXIT")){
			return exit;
		}
		return 0;
	}


}
