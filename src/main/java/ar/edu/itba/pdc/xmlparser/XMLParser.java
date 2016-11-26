package ar.edu.itba.pdc.xmlparser;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;

public class XMLParser {

    private static Deque<StringBuilder> stack = new ArrayDeque<>();

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

        //aux.append('J');
        //aux.append('A');
        //out.println("ANTES DE ENTRAR AL WHILE, aux vale " + aux.flip());
        /*

        out.println("ESTOY AFUERA DEL WHILEEEEEEEE");
        out.println("CURRENT POSITION VALE: " + currentPosition);
        out.println("LENGTH  VALE: " + length);*/
        for(int i=0, j=0; i < length ;){
            if(buff.charAt(i) == '<'){
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

            //out.println("ANSWER VALE: " + answer);

        }
        return answer.toString();

    }

    public static String getTo(CharBuffer buff){
        StringBuilder sb = new StringBuilder();
        int length = buff.length();

        for(int i=0,j=0; i< length;i++){
            if(buff.charAt(i) == 't'){

                if(buff.charAt(i+1) == 'o'){

                    if(buff.charAt(i+2) == '='){

                        if (buff.charAt(i + 3) == '\'' || buff.charAt(i + 3) == '"') {
                            j=i+4;
                            while(buff.charAt(j)!= '\'' && buff.charAt(j) != '"' && buff.charAt(j) != '/'){
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

    public static String getFrom(CharBuffer buff){
        StringBuilder sb = new StringBuilder();
        int length = buff.length();

        for(int i=0,j=0; i< length;i++) {
            if (buff.charAt(i) == 'f') {
                if (buff.charAt(i + 1) == 'r') {
                    if (buff.charAt(i + 2) == 'o') {
                        if (buff.charAt(i + 3) == 'm') {
                            if (buff.charAt(i + 4) == '=') {
                                if (buff.charAt(i + 5) == '\'' || buff.charAt(i + 5) == '"') {
                                    j = i + 6;
                                    while (buff.charAt(j) != '\'' && buff.charAt(j) != '"') {
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
        //out.println("AUX AHORA VALE: " + aux.flip());
        return sb.toString();
    }

    public static ByteBuffer setFrom(ByteBuffer buffer, String from){
        Charset utf18 = Charset.forName("UTF-8");
        buffer.flip();
        CharBuffer buff = utf18.decode(buffer);
        CharBuffer answer = CharBuffer.allocate(buff.capacity()*4);
        char comilla = 0;
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

                                if(buff.charAt(i+5) == '\'' || buff.charAt(i+5) == '"'){
                                    comilla = buff.charAt(i+5);
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

                                    while(buff.charAt(i)!='\'' && buff.charAt(i) != '"'){
                                        i++;
                                    }
                                    while(j < from.length()){
                                        answer.append(from.charAt(j));
                                        j++;
                                    }

                                    answer.append(comilla);
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
        char comilla = 0;

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

                        if (buff.charAt(i + 3) == '\'' || buff.charAt(i + 3) == '"') {
                            comilla = buff.charAt(i + 3);
                            answer.append(buff.charAt(i+1));
                            answer.append(buff.charAt(i+2));
                            c= buff.charAt(i+3);
                            answer.append(c);
                            i=i+4;

                            while(buff.charAt(i)!='\'' && buff.charAt(i)!= '\"'){
                                i++;
                            }

                            while(j < to.length()){
                                answer.append(to.charAt(j));
                                j++;
                            }
                            answer.append(comilla);
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
        String str = "<iq type='set' id='purplead6649b3'><pubsub xmlns='http://jabber.org/protocol/pubsub'><publish node='http://jabber.org/protocol/tune'><item><tune xmlns='http://jabber.org/protocol/tune'/></item></publish></pubsub></iq>";
        CharBuffer aux = CharBuffer.wrap(str.toCharArray());
        State s = checkMessage(aux);
        System.out.println(endsWith("</iq>",aux));
        if(s== State.COMPLETE){
            System.out.println("LEI TODO EL TAG Y ESTA BIEN FORMADO");
        }else if(s == State.INCOMPLETE){
            System.out.println("INCOMPLETO");
        }else{
            System.out.println("ERROR");
        }


    }

    public static State checkMessage(CharBuffer buffer){
        int bufferLength = buffer.length();
        char c;
        boolean hasTags = false;
        for( int i=0; i < bufferLength-1; i++ ){
            c = buffer.get(i);
            if(c == '<'){
                c = buffer.get(i+1);
                if(c == '/'){
                    int l = checkTag(buffer,i+2);
                    if(l == -1){
                        return State.INCOMPLETE;
                    }else if(l == -2){
                        return State.ERROR;
                    }
                    i += l + 2;
                }else{
                    hasTags = true;
                    int l = loadTag(buffer,i+1);

                    if(l == -1){
                        return State.INCOMPLETE;
                    }
                    i+=l+1;
                }
            }else if(c == '/' && buffer.get(i+1) == '>'){
                stack.pop();
                i++;
            }
        }
        if(!hasTags){
            return State.INCOMPLETE;
        }
        if(stack.isEmpty()){
            return State.COMPLETE;
        }else{
            stack.clear();
            return State.INCOMPLETE;
        }
    }
    private static int checkTag(CharBuffer cb, int cbi){
        char c;
        if(stack.isEmpty()){
            return -1;
        }
        StringBuilder tag = stack.pop();
        int i = 0;
        while(((c=cb.get(cbi)) != '>') && cbi < cb.length()){
            if( tag.charAt(i) != c ) {
                return -2;
            }
            i++;
            cbi++;
        }
        if(cbi >= cb.length()){
            return -1;
        }
        if(tag.charAt(i) == '0'){
            return i;
        }else{
            return -2;
        }
    }
    private static int loadTag(CharBuffer cb,int cbi){
        char c;
        int length = 0;
        StringBuilder sb = new StringBuilder();
        while(cbi < cb.length() && (c = cb.get(cbi)) != ' ' && c != '>'){
            if(c == '/'){
                return cb.length() + 2;
            }
            sb.append(c);
            cbi++;
            length++;
        }
        if(cbi >= cb.length()){
            return -1;
        }
        sb.append('0');
        stack.push(sb);
        return length;
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

    public static boolean endsWith(String s, CharBuffer buff) {
        int p = s.length()-1;
        for(int i = buff.length()-1; i>= 0 && p >= 0 ;i--,p--){
            if(buff.get(i) != s.charAt(p)){
                return false;
            }
        }
        return p <= 0;
    }
}
