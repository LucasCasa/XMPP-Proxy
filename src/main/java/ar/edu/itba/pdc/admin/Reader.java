package ar.edu.itba.pdc.admin;

import ar.edu.itba.pdc.Metrics;
import ar.edu.itba.pdc.Connection.ConnectionHandler;

import java.nio.CharBuffer;

public class Reader {
	private final static Converter conv = new ConverterImpl();
	private final static String wrongParams = "Invalid Parameters";
	private final static String unknownError = "Unkown Error";
	private final static String mustLogin = "Must Login";
	

	public String Read(CharBuffer cb,boolean logged){
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == Info.separator){
				int value = validate(sb);
				if(value != 0 ){
					return checkParameters(cb,value,logged);
				}else{
					return conv.resultError("Command does not exist");
				}
			}else{
				sb = sb.append(c);
			}
		}
		String aux = sb.toString().toUpperCase();
		if(!logged){
			return conv.resultError(mustLogin);
		}
		if(aux.equals(Info.StrBytes + Info.endOfMessage)){
			return conv.resultOk(Metrics.getBytes());
		}else if(aux.equals(Info.StrAccess + Info.endOfMessage)){
			return conv.resultOk(Metrics.getAccess());
		}else if(aux.equals(Info.StrSilenced + Info.endOfMessage)){
			return conv.resultOk(Metrics.getBlocked());
		}else if(aux.equals(Info.StrL33ted + Info.endOfMessage)){
			return conv.resultOk(Metrics.getL33ted());
		}else if(aux.equals(Info.StrExit + Info.endOfMessage)){
			ConnectionHandler.exit();
			if(logged){
				return conv.resultOk("Logged out");
			}else{
				return conv.resultError("Not logged in");
			}
			
		}else{
			return conv.resultError(wrongParams);
		}
	}


	private static String checkParameters(CharBuffer cb, int value,boolean logged) {
		switch(value){
		case Info.login:
			return login(cb,logged);
		case Info.register:
			return register(cb,logged);
		case Info.see:
			return see(cb,logged);
		case Info.silence:
			return silence(cb,logged);
		case Info.unsilence:
			return unsilence(cb,logged);
		case Info.multiplex:
			return multiplex(cb,logged);
		case Info.unmultiplex:
			return unmultiplex(cb,logged);
		case Info.l33t:
			return l33t(cb,logged);
		case Info.unl33t:
			return unl33t(cb,logged);
		case Info.host:
			return host(cb,logged);
		}
		return conv.resultError(wrongParams);
	}


	private static String host(CharBuffer cb,boolean logged) {
		StringBuilder name = new StringBuilder("");
		StringBuilder source = new StringBuilder("");
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
				source = source.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				String user = name.toString();
				String ip = source.toString();
				if(!logged){
					return conv.resultError(mustLogin);
				}
				if(!ConnectionHandler.hasHost(user)){
					ConnectionHandler.addHost(user,ip);
					return conv.resultOk("Host " + user + " has been added");
				}else{
					ConnectionHandler.addHost(user,ip);
					return conv.resultError("Host " + user + "has been changed");
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static String unmultiplex(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private  static String unl33t(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private  static String l33t(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private  static String multiplex(CharBuffer cb,boolean logged) {
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
				String user = name.toString();
				String server = sb.toString();
				if(!logged){
					return conv.resultError(mustLogin);
				}
				if(!ConnectionHandler.isMultiplex(user)){
					ConnectionHandler.setMultiplex(user,server);
					return conv.resultOk(user + " multiplexed to " + server);
				}
				else{
					ConnectionHandler.setMultiplex(user,server);
					return conv.resultError(user + " has been changed to multiplex to " + server);
					
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private  static String unsilence(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private  static String silence(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private static String see(CharBuffer cb,boolean logged) {
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
				String param = sb.toString().toUpperCase();
				if(!logged){
					return conv.resultError(mustLogin);
				}
				if(param.equals(Info.StrL33t)){
					return conv.resultSee(ConnectionHandler.getL33t());
				}else if(param.equals(Info.StrMultiplex)){
					return conv.resultSee(ConnectionHandler.getMultiplex());
				}else if(param.equals(Info.StrSilence)){
					return conv.resultSee(ConnectionHandler.getSilenced());
				}else if(param.equals(Info.StrHost)){
					return conv.resultSee(ConnectionHandler.getHosts());
				}else{
					return conv.resultError(wrongParams);
				}
			}else{
				return conv.resultError(wrongParams);
			}
		}
		return conv.resultError(unknownError);
	}


	private static String register(CharBuffer cb,boolean logged) {
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
				if(!logged){
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


	private static String login(CharBuffer cb,boolean logged) {
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
				}else if(logged){
					return conv.resultError("Already logged in");
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
		String string = sb.toString().toUpperCase();
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
		}else if(string.equals(Info.StrHost)){
			return Info.host;
		}else if(string.equals(Info.StrL33ted)){
			return Info.l33ted;
		}else if(string.equals(Info.StrSilenced)){
			return Info.silenced;
		}
		return 0;
	}


}
