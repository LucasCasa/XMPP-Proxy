package ar.itba.edu.ar.pdc;

/**
 * Created by root on 26/10/16.
 */
public class MessageConverter {
    public MessageConverter(){

    }

    public static String transform(String m){
        StringBuilder sb = new StringBuilder(m);


        int start = sb.indexOf("<body>");
        int end = sb.indexOf("</body>");

        //Falta chequear si al from , el to lo tiene en bloqueados

        String message = sb.substring(start, end);
        if(!message.isEmpty()){

			/*aca hago la modificacion del mensaje*/
            StringBuilder messageToBeTransformed = new StringBuilder(sb.substring(start+6, end));
            messageToBeTransformed = convertMessage(messageToBeTransformed);

            return messageToBeTransformed.toString();
        }

        return m;
    }

    private static StringBuilder convertMessage(StringBuilder message){

        String aux = message.toString();
        char[] vector = aux.toCharArray();
        int strBlen = message.length();
        int longitud= aux.length();

        for(int i=0; i<longitud;i++){
            switch (vector[i]){
                case 'a':
                    vector[i]= '4';
                    break;
                case 'e':
                    vector[i]= '3';
                    break;
                case 'i':
                    vector[i]= '1';
                    break;
                case 'o':
                    vector[i]= '0';
                    break;
                case 'c':
                    vector[i]= '<';
                    break;
            }
        }

        aux = String.copyValueOf(vector);
        message = message.replace(0, strBlen, aux);

        return message;

    }
}
