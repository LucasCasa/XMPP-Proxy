package ar.itba.edu.ar.pdc.admin;

import java.util.Map;
import java.util.Set;

public class ConverterImpl implements Converter {


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
			return Info.result  + Info.separator + Info.ok +  Info.separator + "NO VALUES" + Info.endOfMessage;
		}else if(set.isEmpty()){
			return Info.result  + Info.separator + Info.ok +  Info.separator + "NO VALUES" + Info.endOfMessage;
		}
		StringBuilder s = new StringBuilder(Info.result  + Info.separator + Info.ok);
		for(String r: set){
			s.append(Info.enter + r);
		}
		s.append(Info.endOfMessage);
		return s.toString();
	}

	@Override
	public String resultOk(long value) {
		return Info.result  + Info.separator  +  Info.ok + Info.separator + value + Info.endOfMessage;
	}

	@Override
	public String resultSee(Map<String, String> set) {
		if(set == null){
			return Info.result  + Info.separator + Info.ok +  Info.separator + "NO VALUES" + Info.endOfMessage;
		}else if(set.isEmpty()){
			return Info.result  + Info.separator + Info.ok +  Info.separator + "NO VALUES" + Info.endOfMessage;
		}
		StringBuilder s = new StringBuilder(Info.result  + Info.separator + Info.ok);
		for(String r: set.keySet()){
			s.append(Info.enter + r + Info.separator + set.get(r));
		}
		s.append(Info.endOfMessage);
		return s.toString();
	}
	
	



}
