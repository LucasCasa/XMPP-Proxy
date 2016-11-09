package ar.itba.edu.ar.pdc.xmlparser;


import com.sun.xml.internal.fastinfoset.util.CharArray;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import static java.lang.System.out;

public class XMLParser {
	
	
	public XMLParser(){

	}

    public static boolean startWith(String s, ByteBuffer bf){
        Charset utf18 = Charset.forName("UTF-8");
        bf.flip();
        CharBuffer buff = utf18.decode(bf); //Tengo el buffer en chars
       // out.println("ESTOY POR ENTRAR EN STARTWITH PRIVATE STATIC");
        return startWith(s,buff);
    }

    private static boolean startWith(String s, CharBuffer cb){
        /*out.println("ENTRE EN STARTWITH PRIVATE STATIC Y ESTOY POR ENTRAR AL FOR");
        out.println("EL STRING VALE: " + s);
        out.println("EL CHARBUFFER VALE: " + cb);*/

        for(int i = 0; i< s.length();i++){
            if(cb.get(i) != s.charAt(i)){
                return false;
            }
        }
        return true;
    }

    public static boolean contains(String s, ByteBuffer bf){
        Charset utf18 = Charset.forName("UTF-8");
        bf.flip();
        CharBuffer buff = utf18.decode(bf); //Tengo el buffer en chars
        return contains(buff,s);
    }

    private static boolean contains(CharBuffer aux, String str){

        int buffLength = aux.length();
        int strLenth = str.length();
        int i=aux.position();
        int j=0;

        while(i < buffLength){
            /*if((aux.charAt(i) != str.charAt(j)) && j< strLenth){
                i++;
            }else{
                i++;
                j++;
            }*/
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

                isBeginTagBody = contains(aux, beginTagBody);
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

        //aux.append('J');
        //aux.append('A');
        //out.println("ANTES DE ENTRAR AL WHILE, aux vale " + aux.flip());
        /*

        out.println("ESTOY AFUERA DEL WHILEEEEEEEE");
        out.println("CURRENT POSITION VALE: " + currentPosition);
        out.println("LENGTH  VALE: " + length);*/
        for(int i=0, j=0; i < length ;){
            if(buff.charAt(i) == '<'){
                //out.println("ENTRE ACCAAAAAA");
                while(buff.charAt(i+j) != '>'){
                    //out.print(buff.charAt(i+j));
                    j++;
                }
                isBeginAuthTag = contains(buff, authBegin);
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

            //out.println("ANSWER VALE: " + answer);

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
        //out.println("AUX AHORA VALE: " + aux.flip());
        return sb.toString();
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
