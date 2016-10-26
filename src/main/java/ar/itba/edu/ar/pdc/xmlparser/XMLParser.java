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
		int k = sb.indexOf("'>");
		if(k == -1){
			return;
		}
		int start = sb.indexOf("<body>");
		int end = sb.indexOf("</body>");
		if(start == -1 || end == -1){
			return;
		}

		//Falta chequear si al from , el to lo tiene en bloqueados
		String message = sb.substring(start, end);
		if(!message.isEmpty()){

			/*aca hago la modificacion del mensaje*/
			StringBuilder messageToBeTransformed = new StringBuilder(sb.substring(start+6, end));
			messageToBeTransformed = convertMessage(messageToBeTransformed);

		}
		
		
	}


	private static void parseFrom(StringBuilder s, int j) {
		int k = s.indexOf("'xmlns");
		if(k == -1)
			return;
		String from = s.substring(j+6,k );
	}


	private static StringBuilder convertMessage(StringBuilder message){

		String aux = message.toString();
		char[] vector = aux.toCharArray();
		int strBlen = message.length();
		int longitud= aux.length();

		for(int i=0; i<longitud;i++){
			switch (vector[i]){
				case 'a':
					vector[i]= '4';
					break;
				case 'e':
					vector[i]= '3';
					break;
				case 'i':
					vector[i]= '1';
					break;
				case 'o':
					vector[i]= '0';
					break;
				case 'c':
					vector[i]= '<';
					break;
			}
		}

		aux = String.copyValueOf(vector);
		message = message.replace(0, strBlen, aux);

		return message;

	}



	
}
