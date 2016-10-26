package ar.itba.edu.ar.pdc.xmlparser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class XMLParser {
	
	
	public XMLParser(){

	}

	public static boolean parse(String str){
		StringBuilder sb = new StringBuilder(str);
		int j = sb.indexOf("from='");
		int i = sb.indexOf("to='");
		int k = sb.indexOf("body");

		if(i != -1 && j != -1){

			sb = parseTo(sb);

			if(sb.toString() == "-1"){
				return false;
			}
		}

		if(i !=-1){
			sb = parseFrom(sb);

			if(sb.toString() == "-1"){
				return false;
			}
		}

		if(k != -1){
			sb = parseBody(sb);

			if(sb.toString() == "-1"){
				return false;
			}
		}


		return true;
	}

	private static StringBuilder parseTo(StringBuilder sb) {
		int k = sb.indexOf("'>");
		int longitud = sb.length();
		if(k == -1){
			return sb.replace(0,longitud,"-1");
		}


		return sb;
	}

	private static StringBuilder parseFrom(StringBuilder s) {
		int k = s.indexOf("'xmlns");
		int longitud = s.length();

		if(k == -1){
			return s.replace(0,longitud,"-1");
		}

		//String from = s.substring(j+6,k );

		return s;
	}

	private static StringBuilder parseBody(StringBuilder sb){
		int longitud = sb.length();

		int start = sb.indexOf("<body>");
		int end = sb.indexOf("</body>");
		if(start == -1 || end == -1){
			return sb.replace(0,longitud,"-1");
		}

		return sb;
	}



}
