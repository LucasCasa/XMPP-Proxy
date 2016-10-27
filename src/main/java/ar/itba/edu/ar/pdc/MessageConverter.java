package ar.itba.edu.ar.pdc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 26/10/16.
 */
public class MessageConverter {
    public MessageConverter(){

    }

    public static String transform(String m){
        StringBuilder sb = new StringBuilder(m);


        int start = sb.indexOf("<body>")+6;
        int end = sb.indexOf("</body>");

        //Falta chequear si al from , el to lo tiene en bloqueados

        String message = sb.substring(start, end);
        if(!message.isEmpty()){

			/*aca hago la modificacion del mensaje*/
            StringBuilder messageToBeTransformed = new StringBuilder(sb.substring(start, end));
            messageToBeTransformed = convertToL33t(messageToBeTransformed);

            return sb.replace(start,end,messageToBeTransformed.toString()).toString();
        }

        return m;
    }

    private static StringBuilder convertToL33t(StringBuilder message){

        String aux = message.toString();
        char[] vector = aux.toCharArray();
        int strBlen = message.length();
        int longitud= aux.length();
        List<Character> nc = new ArrayList<>();
        for(char c : aux.toCharArray()){
            switch (c){
                case 'a':
                    nc.add('4');
                    break;
                case 'e':
                    nc.add('3');
                    break;
                case 'i':
                    nc.add('1');
                    break;
                case 'o':
                    nc.add('0');
                    break;
                case 'c':
                    nc.add('&');
                    nc.add('l');
                    nc.add('t');
                    nc.add(';');
                    break;
                default:
                    nc.add(c);
            }
        }
        char[] c = new char[nc.size()];
        for(int i = 0; i<nc.size();i++){
            c[i] = nc.get(i);
        }
        return new StringBuilder(String.copyValueOf(c));

    }
}
