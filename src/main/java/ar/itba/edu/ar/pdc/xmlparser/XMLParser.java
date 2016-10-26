package ar.itba.edu.ar.pdc.xmlparser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class XMLParser {
	
	
	public XMLParser(){

	}

	
	public static String parse(String str){
		StringBuilder sb = new StringBuilder(str);
		int j = sb.indexOf("from='");
		int i = sb.indexOf("to='");
		if(i != -1 && j != -1){
			parseTo(sb,i,j);
		}
		if(i !=-1){
			parseFrom(sb,i);
		}
		return sb.toString();
		
	}
	private static void parseTo(StringBuilder sb, int i, int j) {
		//String to = sb.substring(i+6,j);
		int k = sb.indexOf("'>");
		if(k == -1){
			return;
		}
		int start = sb.indexOf("<body>");
		int end = sb.indexOf("</body>");
		if(start == -1 || end == -1){
			return;
		}
		//String from = sb.substring(j+ 4, k);
		 //Hasta aca tengo from y to
		//Falta chequear si al from , el to lo tiene en bloqueados
		//Ahora quiero interceptar el mensaje

		String message = sb.substring(start, end);
		if(!message.isEmpty()){
			sb.replace(start+6, end, "maggie te re doy");
		}
		
		
	}


	private static void parseFrom(StringBuilder s, int j) {
		int k = s.indexOf("'xmlns");
		if(k == -1)
			return;
		String from = s.substring(j+6,k );		
	}
	
}
