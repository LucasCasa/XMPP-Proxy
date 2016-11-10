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
        CharBuffer answer = CharBuffer.allocate(buff.capacity()*4);

        /*Lo que necesito para manejarme con el buffer*/
        int length = buff.length();
        int currentPosition = buffer.position(); //Posicion actual del ByteBuffer. Es importante porque el buffer es circular.
        /**/


        int i=0;
        int k=0;
        int j;
        boolean inBody = false;

        boolean lower = false;
        boolean b = false;
        boolean o = false;
        boolean d = false;
        boolean y = false;

        char c;




        for(;i< length; i++){
            c= buff.charAt(i);
            if(c != '<' && c != 'b' && c != 'o' && c != 'd' && c != 'y' && c != '>'){
                lower = false;
                b = false;
                o = false;
                d = false;
                y = false;
                answer.put(k,c);
                k++;
            }else{
                if(c == '<'){
                    lower = true;
                    answer.put(k,c);
                    k++;
                }

                if(c == 'b'){
                    b= true;
                    answer.put(k,c);
                    k++;
                }

                if(c== 'o' ){
                    o = true;
                    //out.println("entre aca, tengo que meter la o");
                    answer.put(k,c);
                    k++;
                }

                if(c=='d' ){
                    d = true;
                    //out.println("entre aca, tengo que meter la d");
                    answer.put(k,c);
                    k++;
                }

                if(c=='y' ){
                    y=true;
                   // out.println("entre aca, tengo que meter la y");
                    answer.put(k,c);
                    k++;
                }



                if(c=='>'){
                   // out.println("CIERRE TAG");
                    answer.put(k,c);
                    k++;

                    if(lower && b && o && d && y){
                        j=i + 1;
                        k = j;
                        while((c=buff.charAt(j)) != '<'){
                            //out.println("ENTREEEEEE");
                            switch (c) {
                                case 'a':
                                    //out.println("MODIFICO EL CARACTER?");
                                    answer.put(k,'4');
                                    //buff.put(j, '4'); //= '4';
                                    j++;
                                    k++;
                                    break;
                                case 'e':
                                    answer.put(k,'3');
                                    //buff.put(j, '3');
                                    j++;
                                    k++;
                                    break;
                                case 'i':
                                    answer.put(k, '1');
                                    //buff.put(j, '1');
                                    j++;
                                    k++;
                                    break;
                                case 'o':
                                    answer.put(k, '0');
                                    j++;
                                    k++;
                                    break;
                                case 'c':
                                    answer.put(j, '&');
                                    answer.put(j+1, 'l');
                                    answer.put(j+2, 't');
                                    answer.put(j+3, ';');
                                    k+=4;
                                    j++;
                                    break;
                                default:
                                    answer.put(k,c);
                                    k++;
                                    j++;
                                    break;
                            }

                        }
                        //out.println("SALI DEL WHILE");
                        //out.println("EL CARACTER ES <?" + c);
                        answer.put(k, c);
                        k++;
                        //out.println("I VALE ESTO: " + i);
                        //out.println("LA LONGITUD DEL BUFFER ORIGINAL ES: " + length);
                        i = j;

                    }

                }
            }


        }
        //out.println("SALI DEL FOR");
        //out.println("LA RESPUESTA ES: " + answer);
        buffer.clear();
        int w = 0;
        while(answer.get(w) != 0 && w < answer.length()){
            buffer.put(w,(byte)answer.get(w));
            w++;
        }
        buffer.limit(w);
        return buffer;

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
