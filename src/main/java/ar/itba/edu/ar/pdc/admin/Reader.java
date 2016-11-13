package ar.itba.edu.ar.pdc.admin;

import ar.itba.edu.ar.pdc.Metrics;
import ar.itba.edu.ar.pdc.Connection.ConnectionHandler;

import java.nio.CharBuffer;

public class Reader {
	private final static Converter conv = new ConverterImpl();
	private final static String wrongParams = "Invalid Parameters";
	private final static String unknownError = "Unkown Error";
	private final static String mustLogin = "Must Login";
	

	public String Read(CharBuffer cb){
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == Info.separator){
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
			return conv.resultError(mustLogin);
		}
		if(aux.equals(Info.StrBytes + Info.endOfMessage)){
			return conv.resultOk(Metrics.getBytes());
		}else if(aux.equals(Info.StrAccess + Info.endOfMessage)){
			return conv.resultOk(Metrics.getAccess());
		}else if(aux.equals(Info.StrExit + Info.endOfMessage)){
			ConnectionHandler.exit();
			return conv.resultOk("Logged out");
		}else{
			return conv.resultError(unknownError);
		}
	}


	private static String checkParameters(CharBuffer cb, int value) {
		switch(value){
		case Info.login:
			return login(cb);
		case Info.register:
			return register(cb);
		case Info.see:
			return see(cb);
		case Info.silence:
			return silence(cb);
		case Info.unsilence:
			return unsilence(cb);
		case Info.multiplex:
			return multiplex(cb);
		case Info.unmultiplex:
			return unmultiplex(cb);
		case Info.l33t:
			return l33t(cb);
		case Info.unl33t:
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
					return conv.resultError(mustLogin);
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
					return conv.resultError(mustLogin);
				}
				if(ConnectionHandler.isL33t(user)){
					ConnectionHandler.unSetL33t(sb.toString());
					return conv.resultOk(user + " is unl33t");
				}else{
					return conv.resultError("Cannot unl33t someone who is not l33t");
				}
			}else{
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
					return conv.resultError(mustLogin);
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
			if(c == Info.separator && param == 0){
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
					return conv.resultError(mustLogin);
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
					return conv.resultError(mustLogin);
				}
				if(ConnectionHandler.isSilenced(user)){
					ConnectionHandler.unSetSilence(user);
					return conv.resultOk(user + " unsilenced");
				}else{
					return conv.resultError(user + " is not silenced");
				}
			}else{
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
					return conv.resultError(mustLogin);
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
					return conv.resultError(mustLogin);
				}
				if(param.equals(Info.StrL33t)){
					return conv.resultSee(ConnectionHandler.getL33t());
				}else if(param.equals(Info.StrMultiplex)){
					return conv.resultSee(ConnectionHandler.getMultiplex());
				}else if(param.equals(Info.StrSilence)){
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
			if(c == Info.separator && param == 0){
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
					return conv.resultError(mustLogin);
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
			if(c == Info.separator && param == 0){
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
		if(string.equals(Info.StrLogin)){
			return Info.login;
		}else if(string.equals(Info.StrRegister)){
			return Info.register;
		}else if(string.equals(Info.StrMultiplex)){
			return Info.multiplex;
		}else if(string.equals(Info.StrUnmultiplex)){
			return Info.unmultiplex;
		}else if(string.equals(Info.StrSee)){
			return Info.see;
		}else if(string.equals(Info.StrSilence)){
			return Info.silence;
		}else if(string.equals(Info.StrUnsilence)){
			return Info.unsilence;
		}else if(string.equals(Info.StrL33t)){
			return Info.l33t;
		}else if(string.equals(Info.StrUnl33t)){
			return Info.unl33t;
		}else if(string.equals(Info.StrAccess)){
			return Info.access;
		}else if(string.equals(Info.StrBytes)){
			return Info.bytes;
		}else if(string.equals(Info.StrBytes)){
			return Info.exit;
		}
		return 0;
	}


}
