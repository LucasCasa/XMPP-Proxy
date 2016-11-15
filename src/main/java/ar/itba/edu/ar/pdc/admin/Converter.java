package ar.itba.edu.ar.pdc.admin;

import java.util.Map;
import java.util.Set;

public interface Converter {
	public String resultOk(String message);
	public String resultOk(long value);
	public String resultError(String message);
	public String resultSee(Set<String> set);
	public String resultSee(Map<String, String> multiplex);
	
	
}
