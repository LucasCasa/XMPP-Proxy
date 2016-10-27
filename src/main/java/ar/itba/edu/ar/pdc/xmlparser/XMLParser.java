package ar.itba.edu.ar.pdc.xmlparser;


public class XMLParser {
	
	
	public XMLParser(){

	}

	public static boolean isMessage(String str){
		StringBuilder sb = new StringBuilder(str);
		//int j = sb.indexOf("from='");
		int i = sb.indexOf("to='");
		int k = sb.indexOf("body");
		return sb.indexOf("message") != -1 && i != -1 && k != -1;
	}
	public static boolean isJID(String message){
		return message.contains("<jid>") && message.contains("</jid>");
	}

	public static String getJID(String message){
		return message.substring(message.indexOf("<jid>") + 5,message.indexOf("</jid>"));
	}
	/*
	private static StringBuilder parseTo(StringBuilder sb) {
		int k = sb.indexOf("'>");
		int length = sb.length();
		if(k == -1){
			return sb.replace(0,length,"-1");
		}


		return sb;
	}
	private static StringBuilder parseFrom(StringBuilder s) {
		int k = s.indexOf("'xmlns");
		int length = s.length();

		if(k == -1){
			return s.replace(0,length,"-1");
		}

		//String from = s.substring(j+6,k );

		return s;
	}

	private static StringBuilder parseBody(StringBuilder sb){
		int length = sb.length();

		int start = sb.indexOf("<body>");
		int end = sb.indexOf("</body>");
		if(start == -1 || end == -1){
			return sb.replace(0,length,"-1");
		}

		return sb;
	}

	*/

}
