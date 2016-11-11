package ar.itba.edu.ar.pdc.admin;

import java.util.List;

public interface Converter {
	public String login(String user, String pass);
	public String register(String user, String pass);
	public String multiplex(String jid, String server);
	public String multiplex(String jid);
	public String silence(String jid);
	public String unSilence(String jid);
	public String l33t(String jid);
	public String unL33t (String jid);
	public String access();
	public String bytes();
	public String see(String command);
	
	public String resultOk(String message);
	public String resultError(String message);
	public String resultSee(List<String> result);
	public String exit();
	
}
