package src;


import java.util.Arrays;
import java.net.Socket;
import java.util.HashMap;

class FingerTable {

    
    private int rows;
    private int ringSize;
    public int[][] fingerTable;  
    private Globals globals;
    private int myUUID;

    FingerTable(int myUUID) {
        this.myUUID = myUUID;
        this.globals = new Globals();
        this.rows = this.globals.rows;
        this.ringSize = globals.ringSize;
        this.fingerTable = new int[this.rows][2];
    }

    /*
    initiazes the 2^i + uuid column of the finger table. 
    */
    private void initFingerTable() {
        for(int i = 0; i < this.rows; i++) {
            this.fingerTable[i][0] = ((int)Math.pow(2, i) + this.myUUID) % this.ringSize;
        } 
    } 

    
    public void processNodeTable(HashMap<Integer, String> nodeTable) {
        
        Integer[] keys = nodeTable.keySet().toArray(new Integer[0]);
        Arrays.sort(keys);
 
        for(int i = 0; i < this.rows; i++) {
            int ideal = fingerTable[i][0];
            int successor = keys[0];
            for(int j = 0; j < keys.length; j++) {
                if(keys[j] > ideal) {
                    break;
                } 
                successor = keys[j];
            }
            fingerTable[i][1] = successor;
        }
    }  

    @Override
    public String toString(){
        String builder = "";

        for( int i = 0; i < this.rows; i++ ){
            builder = builder + Integer.toString( i ) + ":\t";
            for( int j = 0; j < 2; j++ ){
                builder = builder + Integer.toString( j ) + "\t";
            }
            builder = builder + "\n";
        }

        return builder;
    }
}
