package src;


import java.util.Arrays;
import java.net.Socket;
import java.util.HashMap;

class FingerTable {

    
    private int rows;
    private int ringSize;
    private Globals globals;
    private int myUUID;
    public FingerTableEntry[] fingerTableEntries;

    FingerTable(int myUUID) {
        this.myUUID = myUUID;
        this.globals = new Globals();
        this.rows = this.globals.rows;
        this.ringSize = globals.ringSize;
        this.fingerTableEntries = new FingerTableEntry[this.rows];
    }

    public void processNodeTable(HashMap<Integer, String> nodeTable) {
        
        for( int i = 0; i < this.rows; i++){
            fingerTableEntries[i] = new FingerTableEntry( this.myUUID, i, nodeTable );
        }

        /*
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
        */
    }  

    @Override
    public String toString(){
        String builder = "";

        for( int i = 0; i < this.rows; i++ ){
            builder = builder + Integer.toString( i ) + "\t" + this.fingerTableEntries[i].toString();
            builder = builder + "\n";
        }

        return builder;
    }
}
