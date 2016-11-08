package ar.itba.edu.ar.pdc.admin;

import java.nio.CharBuffer;

public class Reader {
	private final int access = 1;
	private final int login = 2;
	private final int register = 3;
	private final int multiplex = 4;
	private final int l33t = 5;
	private final int bytes = 6;
	private final int silence = 7;
	private final int unsilence = 8;
	private final int unl33t = 9;
	private final int see = 10;
	private final char separator = ' ';
	
	
	public void Read(CharBuffer cb){
		StringBuilder sb = new StringBuilder("");
		cb.flip();
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator){
				int value = validate(sb);
				if(value != 0 ){
					checkParameters(cb,value);
					return;
				}else{
					//mandar mensaje que no existe el comando
					return;
				}
			}else{
				sb = sb.append(c);
			}
		}
		if(sb.toString().equals("BYTES.\n.")){
			//llamo a bytes
		}else if(sb.toString().equals("ACCESS.\n.")){
			//llamo a acces
		}else{
			//mandar mensaje de unknown error
		}
		
	}


	private void checkParameters(CharBuffer cb, int value) {
		switch(value){
			case login:
				login(cb);
				break;
			case register:
				register(cb);
				break;
			case see:
				see(cb);
				break;
			case silence:
				silence(cb);
				break;
			case unsilence:
				unsilence(cb);
				break;
			case multiplex:
				multiplex(cb);
				break;
			case l33t:
				l33t(cb);
				break;
			case unl33t:
				unl33t(cb);
				break;
		}
		
	}


	private void unl33t(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de unl33t");
				//llamo a la funcion de unl33t
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void l33t(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de l33t");
				//llamo a la funcion de l33t
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void multiplex(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder sb = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de multiplex");
				//llamo a la funcion de multiplex
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void unsilence(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de unsilence");
				//llamo a la funcion de unsilence
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void silence(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de silence");
				//llamo a la funcion de silence
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}		
	}


	private void see(CharBuffer cb) {
		int aux = 0;
		StringBuilder sb = new StringBuilder("");
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0){
				sb = sb.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de see");
				//llamo a la funcion de see
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void register(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder pass = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				pass = pass.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de register");
				//llamo a la funcion de l33t
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private void login(CharBuffer cb) {
		StringBuilder name = new StringBuilder("");
		StringBuilder pass = new StringBuilder("");
		int aux = 0;
		int param = 0;
		while(cb.hasRemaining()){
			char c = cb.get();
			if(c == separator && param == 0){
				param ++;
			}else if(c == '\n' && aux == 0){
				aux ++;
				//llego a la primera separacion ahora tiene que ver el final
			}else if (aux == 0 && param == 0){
				name = name.append(c);
			}else if(aux == 0 && param == 1){
				pass = pass.append(c);
			}else if( c == '.' && aux == 1){
				aux ++;
			}else if( c == '\n' && aux == 2){
				System.out.println("llamo a la funcion de login");
				//llamo a la funcion de l33t
			}else{
				System.out.println("tirar error de que estan mal los parametros");
				//tirar error de que estan mal los parametros
			}
		}
		
	}


	private int validate(StringBuilder sb) {
		String string = sb.toString();
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
		return 0;
	}
	
	public static void main(String[] args) {
		Reader r = new Reader();
		String a  = "MULTIPLEX A B\n.\n";
		CharBuffer cb = CharBuffer.allocate(100);
		cb.put(a);
		r.Read(cb);
		
	}
	

}
