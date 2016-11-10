package ar.itba.edu.ar.pdc;

/**
 * Created by lucas on 10/11/16.
 */
public class Metrics {
    private static long bytesTransfered = 0;
    private static long access = 0;
    private static long blocked = 0;
    private static long l33ted = 0;

    private Metrics(){

    }
    public static void addBytes(int bytes){
        if(bytes > 0) {
            bytesTransfered += bytes;
        }
    }
    public static void incrementAccess(){
        access++;
    }
    public static void incrementBlocked(){
        blocked++;
    }
    public static void incrementL33ted(){
        l33ted++;
    }
}
