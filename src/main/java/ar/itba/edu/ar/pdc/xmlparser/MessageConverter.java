package ar.itba.edu.ar.pdc.xmlparser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team Muffin on 26/10/16.
 * Esta clase se encarga de convertir el texto que recibe
 */
public class MessageConverter {

    public MessageConverter(){

    }

    private static ByteBuffer convertToL33t(ByteBuffer buffer){

        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer);

        /*Lo que necesito para manejarme con el buffer*/
        int length = buff.length();
        int currentPosition = buffer.position(); //Posicion actual del ByteBuffer. Es importante porque el buffer es circular.
        /**/

        int i=currentPosition;
        boolean inBody = false;
        for(; i < length && !inBody ;i++){

                if(buff.charAt(i) == '<') {
                    if(buff.charAt(i+1) == 'b'){
                        if(buff.charAt(i+2) == 'o'){
                            if(buff.charAt(i+3) == 'd'){
                                if(buff.charAt(i+4) == 'y'){
                                    if(buff.charAt(i+5) == '>'){
                                        i+=5;
                                        inBody = true;
                                    }
                                }
                            }
                        }
                    }

                }

            }

            while(i != '<'){
                switch (buff.charAt(i)){
                    case 'a':
                        buff.put(buff.charAt(i), '4'); //= '4';
                        break;
                    case 'e':
                        buff.put(buff.charAt(i), '3');
                        break;
                    case 'i':
                        buff.put(buff.charAt(i), '1');
                        break;
                    case 'o':
                        buff.put(buff.charAt(i), '0');
                        break;
                    case 'c':
                        buff.put(buff.charAt(i), '&');
                        buff.put(buff.charAt(i), 'l');
                        buff.put(buff.charAt(i), 't');
                        buff.put(buff.charAt(i), ';');
                        break;
                    default:
                        break;
                }
            }

        return Charset.forName("UTF-8").encode(buff);

    }



/*
    public static ByteBuffer transform(Bytebuffer buffer){

        Charset utf18 = Charset.forName("UTF-8");
        CharBuffer buff = utf18.decode(buffer);

        if(!message.isEmpty()){


            StringBuilder messageToBeTransformed = new StringBuilder(sb.substring(start, end));
            messageToBeTransformed = convertToL33t(messageToBeTransformed);

            return sb.replace(start,end,messageToBeTransformed.toString()).toString();
        }

        return m;
    }
*/

}
