package ar.itba.edu.ar.pdc.admin;

import java.util.List;

public class ConverterImpl implements Converter {
	private final String access = "ACCESS";
	private final String login = "LOGIN";
	private final String register = "REGISTER";
	private final String multiplex = "MULTIPLEX";
	private final String l33t = "L33T";
	private final String bytes = "BYES";
	private final String silence = "SILENCE";
	private final String unsilence = "UNSILENCE";
	private final String unl33t = "UNL33T";
	private final String see = "SEE";
	private final String exit = "EXIT";
	
	private final String result = "RESULT";
	private final String ok = "OK";
	private final String error = "ERROR";
	
	private final String separator = " ";
	private final String endOfMessage = "\n.\n";
	private final String enter = "\n";

	@Override
	public String login(final String user, final String pass) {
		if(user != null && pass != null){
			return login + separator + user.toUpperCase() + separator + pass.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String register(final String user, final String pass) {
		if(user != null && pass != null){
			return register + separator + user.toUpperCase() + separator + pass.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String multiplex(final String jid, final String server) {
		if(jid != null && server !=null){
			return multiplex + separator + jid.toUpperCase() + separator + server.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String multiplex(final String jid) {
		if(jid != null){
			return multiplex(jid,"DEFALUT");
		}
		return null;
	}

	@Override
	public String silence(final String jid) {
		if(jid != null){
			return silence + separator + jid.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String unSilence(final String jid) {
		if(jid != null){
			return unsilence + separator + jid.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String l33t(final String jid) {
		if(jid != null){
			return l33t + separator + jid.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String unL33t(final String jid) {
		if(jid != null){
			return unl33t + separator + jid.toUpperCase() + endOfMessage;
		}
		return null;
	}

	@Override
	public String access() {
		return access + endOfMessage;
	}

	@Override
	public String bytes() {
		return bytes + endOfMessage;
	}

	@Override
	public String see(final String command) {
		if(command == null){
			return null;
		}
		String com = command.toUpperCase();
		if(com.equals("L33T") || com.equals("SILENCE") || com.equals("MULTIPLEX")){
			return see + separator + com + endOfMessage;
		}
		return null;
	}

	@Override
	public String resultOk(String message) {
		if(message == null){
			return result + separator + ok + endOfMessage;
		}
		return result + separator + ok + separator + message.toUpperCase() + endOfMessage;
	}

	@Override
	public String resultError(String message) {
		if(message == null){
			return result + separator + error + endOfMessage;
		}
		return result + separator + error + separator + message.toUpperCase() + endOfMessage;
	}

	@Override
	public String resultSee(List<String> result) {
		if(result == null){
			
		}else if(result.isEmpty()){
			return result + separator + "EMPTY" + endOfMessage;
		}
		StringBuilder s = new StringBuilder(result + ok + enter);
		for(String r:result ){
			s.append(r + enter);
		}
		s.append(endOfMessage);
		return s.toString();
	}

	@Override
	public String exit() {
		return exit + endOfMessage;
	}
	
	



}
