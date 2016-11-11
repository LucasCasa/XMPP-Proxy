package ar.itba.edu.ar.pdc.admin;

import java.util.Scanner;

public class AdminRead {

	private final static int access = 0;
	private final static int login = 1;
	private final static int register = 2;
	private final static int multiplex = 3;
	private final static int l33t = 4;
	private final static int bytes = 5;
	private final static int silence = 6;
	private final static int unsilence = 7;
	private final static int unl33t = 8;
	private final static int see = 9;
	private final static int [] params = {0,2,2,2,1,0,1,1,1,1}; 

	private static int value = -1;

	public static void main(String[] args) {
		System.out.println("Start Writing");
		Scanner s = new Scanner(System.in);
		while(s.hasNext()){
			String aux = s.nextLine();
			String rta = Validate(aux);
			if(rta == null){
				System.out.println("HUBO ERROR");
				//hubo error
			}
			else{
				System.out.println(rta);
				//mandar al servidor
			}
		}
		s.close();

	}

	private static String Validate(String s) {
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<s.length();i++){
			char c = s.charAt(i);
			if(c == ' '){
				value = isCorrect(sb);
				if(value == -1){
					//no se hace nada es un error
				}
				int param = params[value];
				switch(param){
				case 1:
					return readOneParam(s,i+1);
				case 2:
					return readTwoParam(s,i+1);
				}
			}else{
				sb.append(c);
			}
		}
		return readNoParam(sb);
	}

	private static String readTwoParam(String s, int i) {
		StringBuilder s1 = new StringBuilder("");
		StringBuilder s2 = new StringBuilder("");
		Converter conv = new ConverterImpl();
		String rta = null;
		int cant = 0;
		for(int j=i;j<s.length();j++){
			char c = s.charAt(j);
			if(c == ' ' && cant == 0){
				cant ++;
			}else if(cant == 0){
				s1.append(c);
			}else{
				s2.append(c);
			}
		}
		if(cant == 0 && value == multiplex){
			rta = conv.multiplex(s1.toString());
		}
		switch(value){
		case login:
			rta = conv.login(s1.toString(), s2.toString());
			break;
		case register:
			rta = conv.register(s1.toString(), s2.toString());
			break;
		case multiplex:
			rta = conv.multiplex(s1.toString(), s2.toString());
			break;
		}
		return rta;

	}

	private static String readOneParam(String s,int i) {
		String rta = null;
		StringBuilder s1 = new StringBuilder("");
		Converter conv = new ConverterImpl();
		for(int j=i;j<s.length();j++){
			char c = s.charAt(j);
			s1.append(c);
		}
		switch(value){
		case l33t:
			rta = conv.l33t(s1.toString());
			break;
		case silence:
			rta = conv.silence(s1.toString());
			break;
		case unsilence:
			rta = conv.unSilence(s1.toString());
			break;
		case unl33t:
			rta = conv.unL33t(s1.toString());
			break;
		case see:
			rta = conv.see(s1.toString());
			break;
		}
		return rta;

	}

	private static String readNoParam(StringBuilder s) {
		String rta = null;
		value = isCorrect(s);
		Converter conv = new ConverterImpl();
		switch(value){
		case bytes:
			rta = conv.bytes();
			break;
		case access:
			rta = conv.access();
			break;
		}
		return rta;
	}

	private static int isCorrect(StringBuilder sb) {
		String string = sb.toString().toUpperCase();
		if(string.equals("LOGIN")){
			return login;
		}else if(string.equals("REGISTER")){
			return register;

		}else if(string.equals("MULTIPLEX")){
			return multiplex;

		}else if(string.equals("SEE")){
			return see;

		}else if(string.equals("SILENCE")){
			return silence;

		}else if(string.equals("UNSILENCE")){
			return unsilence;

		}else if(string.equals("L33T")){
			return l33t;

		}else if(string.equals("UNL33T")){
			return unl33t;

		}else if(string.equals("ACCESS")){
			return access;

		}else if(string.equals("BYTES")){
			return bytes;

		}
		return -1;
	}
}
