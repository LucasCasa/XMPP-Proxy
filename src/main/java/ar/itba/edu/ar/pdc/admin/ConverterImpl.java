package ar.itba.edu.ar.pdc.admin;

import java.util.Set;

public class ConverterImpl implements Converter {

	@Override
	public String login(final String user, final String pass) {
		if(user != null && pass != null){
			return Info.StrLogin + Info.separator + user + Info.separator + pass + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String register(final String user, final String pass) {
		if(user != null && pass != null){
			return Info.StrRegister + Info.separator + user + Info.separator + pass + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String multiplex(final String jid, final String server) {
		if(jid != null && server !=null){
			return Info.StrMultiplex + Info.separator + jid + Info.separator + server + Info.endOfMessage;
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
			return Info.StrSilence + Info.separator + jid + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String unSilence(final String jid) {
		if(jid != null){
			return Info.StrUnsilence + Info.separator + jid + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String l33t(final String jid) {
		if(jid != null){
			return Info.StrL33t + Info.separator + jid + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String unL33t(final String jid) {
		if(jid != null){
			return Info.StrUnl33t + Info.separator + jid + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String access() {
		return Info.StrAccess + Info.endOfMessage;
	}

	@Override
	public String bytes() {
		return Info.StrBytes + Info.endOfMessage;
	}

	@Override
	public String see(final String command) {
		if(command == null){
			return null;
		}
		String com = command.toUpperCase();
		if(com.equals("L33T") || com.equals("SILENCE") || com.equals("MULTIPLEX")){
			return Info.StrSee + Info.separator + com + Info.endOfMessage;
		}
		return null;
	}

	@Override
	public String resultOk(String message) {
		if(message == null){
			return Info.result + Info.separator + Info.ok + Info.endOfMessage;
		}
		return Info.result  + Info.separator + Info.ok + Info.separator + message + Info.endOfMessage;
	}

	@Override
	public String resultError(String message) {
		if(message == null){
			return Info.result  + Info.separator + Info.error + Info.endOfMessage;
		}
		return Info.result  + Info.separator + Info.error + Info.separator + message + Info.endOfMessage;
	}

	@Override
	public String resultSee(Set<String> set) {
		if(set == null){
			
		}else if(set.isEmpty()){
			return Info.result  + Info.separator + "EMPTY" + Info.endOfMessage;
		}
		StringBuilder s = new StringBuilder(Info.result  + Info.ok + Info.enter);
		for(String r:set ){
			s.append(r + Info.enter);
		}
		s.append(Info.endOfMessage);
		return s.toString();
	}

	@Override
	public String exit() {
		return Info.StrExit + Info.endOfMessage;
	}

	@Override
	public String unmultiplex(String message) {
		if (message == null){
			return Info.result  + Info.separator + Info.error + Info.endOfMessage;
		}
		return Info.StrUnmultiplex + Info.separator + message + Info.endOfMessage;
	}

	@Override
	public String resultOk(long value) {
		return Info.result  + Info.separator  +  Info.ok + Info.separator + value + Info.endOfMessage;
	}
	
	



}
