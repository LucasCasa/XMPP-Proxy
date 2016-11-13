package ar.itba.edu.ar.pdc.xmlparser;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class XMLParser {


    private XMLParser(){

    }

    public static boolean startWith(String s, CharBuffer bf){
        return startWithInner(s,bf);
    }

    private static boolean startWithInner(String s, CharBuffer cb){
        for(int i = 0; i< s.length() && i < cb.length() ;i++){
            if(cb.get(i) != s.charAt(i)){
                return false;
            }
        }
        return true;
    }

    public static boolean contains(String s, CharBuffer bf){
        return containsInner(bf,s);
    }

    private static boolean containsInner(CharBuffer aux, String str){

        int buffLength = aux.length();
        int strLenth = str.length();
        int i=aux.position();
        int j=0;

        while(i < buffLength){
            while( i < buffLength && aux.charAt(i) != str.charAt(j)){
                i++;
            }
            while(i < buffLength && aux.charAt(i) == str.charAt(j)){
                i++;
                j++;
                if(j == strLenth){
                    return true;
                }
            }
            j=0;
        }
        return false;
    }


    public static String getBody(ByteBuffer buffer){
        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer); //Tengo el buffer en chars
        StringBuilder answer = new StringBuilder();

        /*Lo que necesito para manejarme con el buffer*/
        int length = buff.length();
        int currentPosition = buffer.position(); //Posicion actual del ByteBuffer. Es importante porque el buffer es circular.
        /**/

        /*Las variables boolean que necesito*/
        boolean isBeginTagBody = false;
        boolean isEndTagBody = false;
        /**/

        /*Las variables auxiliares que necesito*/
        CharBuffer aux = null;
        String beginTagBody = "body";
        String endTagBody = "/body";
        /**/


        for(int i=currentPosition,j=1; i < length ;){
            if(buff.charAt(i) == '<'){
                while(buff.charAt(i + j) !='>'){
                    aux.append(buff.charAt(i + j));
                }

                isBeginTagBody = contains(beginTagBody, aux);
                //isEndTagBody = compare(aux, endTagBody);
                i=j;
                j=1;
                if(isBeginTagBody) {
                    while (buff.charAt(i + j) != '<') {
                        answer.append(buff.charAt(i + j));
                        j++;
                    }
                }
                i=length;
            }

        }

        return answer.toString();

    }


    public static String getAuth(ByteBuffer buffer){
        Charset utf18 = Charset.forName("UTF-8");
        buffer.flip();
        CharBuffer buff = utf18.decode(buffer); //Tengo el buffer en chars
        StringBuilder answer = new StringBuilder();
        /*Lo que necesito para manejarme con el buffer*/
        int length = buff.length();
        int currentPosition = buffer.position(); //Posicion actual del ByteBuffer. Es importante porque el buffer es circular.
        /**/

        /*Las variables boolean que necesito*/
        boolean isBeginAuthTag = false;
        boolean isEndAuthTag = false;
        /**/

        /*Las variables auxiliares que necesito*/
        String authBegin = "auth ";
        String authEnd = "/auth";
        /**/


        for(int i=0, j=0; i < length ;){
            if(buff.charAt(i) == '<'){
                ;
                while(buff.charAt(i+j) != '>'){

                    j++;
                }
                isBeginAuthTag = contains(authBegin, buff);
                i=j;
                j=1;
                if(isBeginAuthTag){
                    while(buff.charAt(i + j)!= '<'){
                        answer.append(buff.charAt(i + j));
                        j++;
                    }


                }
                i=length;
            }


        }
        return answer.toString();

    }

    public static String getTo(ByteBuffer buffer){
        Charset utf18 = Charset.forName("UTF-8");
        buffer.flip();
        CharBuffer buff = utf18.decode(buffer);
        StringBuilder sb = new StringBuilder();
        int length = buff.length();

        for(int i=0,j=0; i< length;i++){
            if(buff.charAt(i) == 't'){

                if(buff.charAt(i+1) == 'o'){

                    if(buff.charAt(i+2) == '='){

                        if (buff.charAt(i + 3) == '\'') {

                            j=i+4;
                            while(buff.charAt(j)!= '\''){
                                sb.append(buff.charAt(j));
                                j++;
                            }
                            i = length;
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String getFrom(ByteBuffer buffer){

        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer);
        StringBuilder sb = new StringBuilder();
        int length = buff.length();

        for(int i=0,j=0; i< length;i++){
            if(buff.charAt(i) == 'f'){

                if(buff.charAt(i+1) == 'r'){

                    if(buff.charAt(i+2) == 'o'){

                        if (buff.charAt(i + 3) == 'm') {

                            if(buff.charAt(i+4) == '='){

                                if(buff.charAt(i+5) == '\''){
                                    j=i+6;
                                    while(buff.charAt(j)!= '\''){
                                        sb.append(buff.charAt(j));
                                        j++;
                                    }
                                    i = length;
                                }
                            }

                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    public static ByteBuffer setFrom(ByteBuffer buffer, String from){
        Charset utf18 = Charset.forName("UTF-8");
        buffer.flip();
        CharBuffer buff = utf18.decode(buffer);
        CharBuffer answer = CharBuffer.allocate(buff.capacity()*4);
        answer.clear();

        int length = buff.length();
        char c;
        for(int i=0,j=0; i< length;i++){
            c = buff.charAt(i);
            answer.append(c);
            if(buff.charAt(i) == 'f'){
                if(buff.charAt(i+1) == 'r'){

                    if(buff.charAt(i+2) == 'o'){

                        if (buff.charAt(i + 3) == 'm') {

                            if(buff.charAt(i + 4) == '='){

                                if(buff.charAt(i+5) == '\''){
                                    c =buff.charAt(i+1);
                                    answer.append(c);
                                    c =buff.charAt(i+2);
                                    answer.append(c);
                                    c =buff.charAt(i+3);
                                    answer.append(c);
                                    c =buff.charAt(i+4);
                                    answer.append(c);
                                    c= buff.charAt(i+5);
                                    answer.append(c);
                                    i=i+6;

                                    while(buff.charAt(i)!='\''){
                                        i++;
                                    }
                                    while(j < from.length()){
                                        answer.append(from.charAt(j));
                                        j++;
                                    }

                                    answer.append('\'');
                                    answer.append('>');
                                    i++;
                                }
                            }


                        }
                    }
                }
            }


        }

        answer.flip();
        buffer.clear();
        buffer.put(Charset.forName("UTF-8").encode(answer));

        return buffer;
    }

    public static ByteBuffer setTo(ByteBuffer buffer, String to){
        buffer.flip();
        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer);
        CharBuffer answer = CharBuffer.allocate(buff.capacity()*4);
        answer.clear();

        boolean isTo = false;
        int length = buff.length();
        char c;
        int i=0;
        for(int j=0; i< length;i++){
            c = buff.charAt(i);
            answer.append(c);
            if(buff.charAt(i) == 't' && !isTo){

                if(buff.charAt(i+1) == 'o'){

                    if(buff.charAt(i+2) == '='){

                        if (buff.charAt(i + 3) == '\'') {
                            answer.append(buff.charAt(i+1));
                            answer.append(buff.charAt(i+2));
                            c= buff.charAt(i+3);
                            answer.append(c);
                            i=i+4;

                            while(buff.charAt(i)!='\''){
                                i++;
                            }

                            while(j < to.length()){
                                answer.append(to.charAt(j));
                                j++;
                            }
                            answer.append('\'');
                            answer.append(' ');
                            i++;
                            isTo = true;
                        }
                    }
                }
            }


        }

        answer.flip();
        buffer.clear();
        buffer.put(Charset.forName("UTF-8").encode(answer));

        return buffer;
    }

    public static void main(String[] args) {
        String str = "<hola ><chau ></chau>";
        CharBuffer aux = CharBuffer.wrap(str.toCharArray());
        System.out.println("EN EL MAIN EL CHARBUFFER TIENE: " + aux.toString());

        //Charset utf8 = Charset.forName("UTF-8");
        //ByteBuffer buff = utf8.encode(aux);

           /* if(tagFinished(ByteBuffer.wrap(str.getBytes()))){
                System.out.println("EL TAG ESTA BIEN FORMADO");
            }else{
                System.out.println("EL TAG NO ESTA BIEN FORMADO");
            }*/

        if(checkMessage(aux)){
            System.out.println("LEI TODO EL TAG Y ESTA BIEN FORMADO");
        }else{
            System.out.println("NO ESTA BIEN FORMADO, PERO TENGO QUE ESPERAR AL OTRO PEDAZO DE XML PARA DETERMINARLO");
        }

        // System.out.println("LO QUE TIENE EL TAG TO ES: " + getTo(ByteBuffer.wrap(str.getBytes())).toString());
        // System.out.println("LO QUE TIENE EL TAG FROM ES: " + getFrom(ByteBuffer.wrap(str.getBytes())).toString());
        //ByteBuffer bufercito = ByteBuffer.allocate(1024);
        //System.out.println("LO QUE ME DEVUELVE SET tO ES: " + new String(setTo(bufercito.put(str.getBytes()), "nico@example.com").array()));
        //bufercito.clear();
        //System.out.println("LO QUE ME DEVUELVE SET from ES: " + new String(setFrom(bufercito.put(str.getBytes()), "ncastano@example.com").array()));



    }

    public static boolean checkMessage(CharBuffer buffer){
        int bufferLength = buffer.length();
        char[] tagArray = new char[bufferLength/2];
        char c;
        int j;
        int k=0;
        int fullTags = 0;
        boolean closedTag;

        for(int h=0; h< tagArray.length ; h++){
            tagArray[h] = '0';
        }
        for( int i=0; i < bufferLength; i++ ){
            c = buffer.get(i);
            if(c == '<'){
                c = buffer.get(i+1);
                if(c == '/'){
                    j=i+2;
                    k=0;
                    closedTag = false;
                    while( (k < tagArray.length) && ((c=buffer.get(j)) != '>') && !closedTag  ){

                        if( tagArray[k] == c ){

                            if( tagArray[k+1] == '0' ){
                                closedTag = true;
                                k=0;
                                fullTags--;
                            }else{
                                k++;
                                j++;
                            }

                        }else{
                            k++;
                        }

                    }

                    if(k >= tagArray.length && !closedTag){
                        return false;
                    }

                    i = j;

                }
                else{
                    j=i+1;

                    while((c = buffer.get(j)) != ' '){
                        tagArray[k] = c;
                        k++;
                        j++;
                    }
                    tagArray[k]= '0';
                    k++;
                    i=j;

                    for(int h=0; h<k;){
                        System.out.print(tagArray[h]);
                        h++;
                    }
                    fullTags ++;
                }
            }
        }
        return fullTags == 0;
    }

    public static boolean tagFinished(ByteBuffer buffer){

        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer);
        StringBuilder build = new StringBuilder();

        int length = buff.length();
        int i=0;
        int j=0;
        int k=0;
        char c;
        boolean tagOpen = false;
        boolean contain = true;

        for(;i < length;){

            if((c= buff.charAt(i)) == '<'){
                i=i+1;
                if(!tagOpen){
                    while((c= buff.charAt(i)) != '>'){
                        build.append(c);
                        i++;
                        j++;

                    }

                    j=0;
                    tagOpen = true;
                }else{
                    c= buff.charAt(i);
                    if(c=='/'){
                        contain = true;
                        k=i+1;
                        while((c=buff.charAt(k)) != '>' && contain){
                            if(j == build.length() || build.charAt(j) != c ){
                                contain = false;
                            }
                            j++;
                            k++;
                        }

                        if(j != build.length()){
                            contain = false;
                        }

                        j=0;

                    }
                }
            }
            i++;

        }
        return contain && i==length;
    }


    /*
	public static boolean isMessage(ByteBuffer buffer){


        buffer.position();
        Charset utf18 = Charset.forName("UTF-8");
		CharBuffer buff =  utf18.decode(buffer);  //buffer.asCharBuffer();
		int largo = buff.length();
        boolean message;
        boolean to = false;
        boolean from = false;
        boolean jid = false;
        boolean body = false;


        message = isBeginTagMessage(buff) && isEndTagMessage(buff,largo);

        if(!message){
            return false;
        }

        for(int i=9; i< largo;i++){
            if(buff.charAt(i) == 't' && !to){
                if(buff.charAt(i+1)!='o'){
                    return false;
                }else if(buff.charAt(i+2)!= '='){
                    return false;
                }else{
                    to = true;
                    i+=2;
                }
            }

            if(buff.charAt(i) == 'f' && !from){
                if(buff.charAt(i+1)!='r'){
                    return false;
                }else if(buff.charAt(i+2) != 'o'){
                    return false;
                }else if(buff.charAt(i+3)!= 'm'){
                    return false;
                }else if(buff.charAt(i+4)!='='){
                    return false;
                }else{
                    from = true;
                    i+=4;
                }
            }



            if(buff.charAt(i) == '<' &&  !body){
                if(buff.charAt(i+1) == 'b'){
                    if(buff.charAt(i+2)!='o'){
                        return false;
                    }else if(buff.charAt(i+3) != 'd'){
                        return false;
                    }else if(buff.charAt(i + 4)!='y'){
                        return false;
                    }else if(buff.charAt(i+5)!='>'){
                        return false;
                    }else{
                        body= true;
                        i+=6;
                    }
                }
            }

            i++;


        }

        if((from == true && to == false) || (from == false && to == false) || !jid || !body){
            return false;
        }

        return true;
	}

	private static boolean isBeginTagMessage(CharBuffer buff){
	    if(buff.charAt(0) != '<' || buff.charAt(8) !='>'){
	        return false;
        }else if(buff.charAt(1) != 'm'){
            return false;
        }else if(buff.charAt(2) != 'e' || buff.charAt(7)!= 'e'){
            return false;
        }else if(buff.charAt(3) != 's' || buff.charAt(4) != 's'){
            return false;
        }else if(buff.charAt(5)!= 'a'){
            return false;
        }else if(buff.charAt(6)!='g'){
            return false;
        }

        return true;

    }

    private static boolean isEndTagMessage(CharBuffer buff, int largo){
        if(buff.charAt(largo) != '>' || buff.charAt(largo-9) != '>'){
            return false;
        }else if(buff.charAt(largo-8) != '/'){
            return false;
        }else if(buff.charAt(largo -1 ) != 'e' || buff.charAt(largo - 6) != 'e'){
            return false;
        }else if(buff.charAt(largo - 2) != 'g'){
            return false;
        }else if(buff.charAt(largo - 3) != 'a'){
            return false;
        }else if(buff.charAt(largo -4) != 's' || buff.charAt(largo -5 )!='s'){
            return false;
        }

        return true;
    }

    public static boolean isJID(ByteBuffer buffer){

        CharBuffer buff = buffer.asCharBuffer();

        boolean beginJID = false;
        boolean endJID = false;

        for(int i=0; i< buff.length();i++){
            if(buff.charAt(i) == '<' && !beginJID){
                if(buff.charAt(i+1)!= 'j' ){
                    return false;
                }else if(buff.charAt(i+2)!='i'){
                    return false;
                }else if(buff.charAt(i+3)!='d'){
                    return false;
                }else if(buff.charAt(i+4)!='>'){
                    return false;
                }else{
                    beginJID = true;
                    i+=4;
                }
            }

            if(buff.charAt(i) == '<' && !endJID){
                if(buff.charAt(i+1)!='/'){
                    return false;
                }else if(buff.charAt(i+1)!= 'j' ){
                    return false;
                }else if(buff.charAt(i+2)!='i'){
                    return false;
                }else if(buff.charAt(i+3)!='d'){
                    return false;
                }else if(buff.charAt(i+4)!='>'){
                    return false;
                }else{
                    endJID = true;
                    i+=5;
                }
            }

        }


        return beginJID && endJID;
    }
*/

    /*
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
	}*/
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
