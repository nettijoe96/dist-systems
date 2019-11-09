package src;


import java.util.Arrays;
import java.net.Socket;
import java.util.HashMap;
import utility.Globals;

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
    }

    public void newClient(int id, String ip) {
        for(FingerTableEntry e : fingerTableEntries) {
            if(inBetween(e.nodeNumber, e.successorNumber, id) || e.nodeNumber == id) {
                e.successorNumber = id; 
                e.nodeIp = ip; 
            }
        }
    }


    public void closeClient(int oldId, int successor, String successorIp) {
        System.out.println("successor in closeClient" + successor);
        System.out.println("oldId in closeClient" + oldId);
        for(FingerTableEntry e: fingerTableEntries) {
            if (e.successorNumber.equals(oldId)) {
                e.successorNumber = successor;
                e.nodeIp = successorIp;
            }
        } 
    }

    private boolean inBetween(int startId, int endId, int thirdId) {
        if(thirdId > startId) {
            return endId > thirdId || endId < startId;
        }
        else if (thirdId < startId) {
            return endId < startId && endId > thirdId;
        }
        else {
            return false;
        }
        //return ((startId + thridId) % globals.ringSpace) < ((startId + endId) % globals.ringSpace)

    }


    public String getDestinationIp(int idealId) {
        for(FingerTableEntry e : fingerTableEntries) {
            if(e.successorNumber >= idealId) {
                return e.nodeIp; 
            }
        } 
        return fingerTableEntries[0].nodeIp; 
    }
  
    @Override
    public String toString(){
        String builder = "";

        System.out.println("row\tj+2^i\tsuccessor");
        for( int i = 0; i < this.rows; i++ ){
            builder = builder + Integer.toString( i ) + "\t" + this.fingerTableEntries[i].toString();
            builder = builder + "\n";
        }

        return builder;
    }
}
