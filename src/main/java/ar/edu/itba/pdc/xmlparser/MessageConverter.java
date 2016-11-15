package ar.edu.itba.pdc.xmlparser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

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
        answer.clear();
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
                answer.put(c);
                k++;
            }else{
                if(c == '<'){
                    lower = true;
                    answer.put(c);
                    k++;
                }

                if(c == 'b'){
                    b= true;
                    answer.put(c);
                    k++;
                }

                if(c== 'o' ){
                    o = true;
                    //out.println("entre aca, tengo que meter la o");
                    answer.put(c);
                    k++;
                }

                if(c=='d' ){
                    d = true;
                    //out.println("entre aca, tengo que meter la d");
                    answer.put(c);
                    k++;
                }

                if(c=='y' ){
                    y=true;
                   // out.println("entre aca, tengo que meter la y");
                    answer.put(c);
                    k++;
                }



                if(c=='>'){
                   // out.println("CIERRE TAG");
                    answer.put(c);
                    k++;

                    if(lower && b && o && d && y){
                        j=i + 1;
                        k = j;
                        while(j < buff.length() && (c=buff.charAt(j)) != '<'){
                            //out.println("ENTREEEEEE");
                            switch (c) {
                                case 'a':
                                    //out.println("MODIFICO EL CARACTER?");
                                    answer.put('4');
                                    //buff.put(j, '4'); //= '4';
                                    j++;
                                    k++;
                                    break;
                                case 'e':
                                    answer.put('3');
                                    //buff.put(j, '3');
                                    j++;
                                    k++;
                                    break;
                                case 'i':
                                    answer.put('1');
                                    //buff.put(j, '1');
                                    j++;
                                    k++;
                                    break;
                                case 'o':
                                    answer.put('0');
                                    j++;
                                    k++;
                                    break;
                                case 'c':
                                    answer.put('&');
                                    answer.put('l');
                                    answer.put('t');
                                    answer.put(';');
                                    k+=4;
                                    j++;
                                    break;
                                case '&':
                                    while((c = buff.charAt(j)) != ';'){
                                        j++;
                                        answer.put(c);
                                    }
                                    break;
                                default:
                                    answer.put(c);
                                    k++;
                                    j++;
                                    break;
                            }

                        }
                        //out.println("SALI DEL WHILE");
                        //out.println("EL CARACTER ES <?" + c);
                        if(c == '<') {
                            answer.put(c);
                            k++;
                        }
                        //out.println("I VALE ESTO: " + i);
                        //out.println("LA LONGITUD DEL BUFFER ORIGINAL ES: " + length);
                        i = j;

                    }

                }
            }


        }
        //out.println("SALI DEL FOR");
        //out.println("LA RESPUESTA ES: " + answer);
        answer.flip();
        buffer.clear();
        if(answer.length() > buffer.limit()) {
            answer.limit(buffer.limit());
        }
        buffer.put(Charset.forName("UTF-8").encode(answer));
        //buffer.position(k);
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
