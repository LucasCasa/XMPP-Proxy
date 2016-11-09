package ar.itba.edu.ar.pdc.xmlparser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * Created by Team Muffin on 26/10/16.
 * Esta clase se encarga de convertir el texto que recibe
 */
public class MessageConverter {

    public MessageConverter(){

    }

    public static ByteBuffer convertToL33t(ByteBuffer buffer){

        Charset utf18 = Charset.forName("UTF-8");
        buffer.flip();
        CharBuffer buff = utf18.decode(buffer);

        /*Lo que necesito para manejarme con el buffer*/
        int length = buff.length();
        int currentPosition = buffer.position(); //Posicion actual del ByteBuffer. Es importante porque el buffer es circular.
        /**/


        out.println("BUFF AHORA TIENE: "+ buff);
        int i=0;
        out.println("i vale: "+ i);
        out.println("LENGTH VALE: "+ length);
        int j=0;
        boolean inBody = false;

        for(; i < length ;i++){
                out.println("ENTRE EN EL CICLO FOR DE MESSAGE CONVERTER");
                if(buff.charAt(i) == '<') {
                    out.println("ENTRE EN <");
                    if(buff.charAt(i+1) == 'b'){
                        out.println("ENTRE EN b");
                        if(buff.charAt(i+2) == 'o'){
                            out.println("ENTRE EN o");
                            if(buff.charAt(i+3) == 'd'){
                                out.println("ENTRE EN d");
                                if(buff.charAt(i+4) == 'y'){
                                    out.println("ENTRE EN y");
                                    if(buff.charAt(i+5) == '>'){
                                        out.println("ENTRE EN >");
                                        i+=5;
                                        //inBody = true;
                                        out.println("I AHORA VALE: " + i);
                                        j=i+1;
                                        out.println("J AHORA PASA A VALER: " + j);
                                        out.println("EL CARACTER CORRESPONDIENTE ANTES DE ENTRAR AL WHILE ES :" + buff.charAt(j));
                                        while(buff.charAt(j) != '<'){
                                            out.println("ENTREEEEEE");
                                            switch (buff.charAt(j)){
                                                case 'a':
                                                    out.println("MODIFICO EL CARACTER?");

                                                    buff.put(j, '4'); //= '4';
                                                    j++;
                                                    break;
                                                case 'e':
                                                    buff.put(j, '3');
                                                    j++;
                                                    break;
                                                case 'i':
                                                    buff.put(j, '1');
                                                    j++;
                                                    break;
                                                case 'o':
                                                    buff.put(j, '0');
                                                    j++;
                                                    break;
                                                case 'c':
                                                    buff.put(buff.charAt(j), '&');
                                                    buff.put(buff.charAt(j+1), 'l');
                                                    buff.put(buff.charAt(j+2), 't');
                                                    buff.put(buff.charAt(j+3), ';');
                                                    j++;
                                                    break;
                                                default:
                                                    j++;
                                                    break;
                                            }
                                            out.println("DESPUES DE LA TRANSFORMACION : " + buff.toString());
                                            i=length;
                                    }
                                }
                            }
                        }
                    }

                }

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
