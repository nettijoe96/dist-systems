package src;


import java.util.Arrays;
import java.net.Socket;
import java.util.HashMap;
import utility.Globals;


/*
* Fingertable class. 
* each row in the fingertable is a fingerTableEntry object
*/
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

    /*
    * create leafTable from nodeTable
    * @param nodeTable
    */
    public void processNodeTable(HashMap<Integer, String> nodeTable) {
        for( int i = 0; i < this.rows; i++){
            fingerTableEntries[i] = new FingerTableEntry( this.myUUID, i, nodeTable );
        }
    }


    /*
    * when there is a new client we potentially have to adjust our leaf table
    * @param id
    * @param ip
    */
    public void newClient(int id, String ip) {
        for(FingerTableEntry e : fingerTableEntries) {
            if(inBetween(e.nodeNumber, e.successorNumber, id) || e.nodeNumber == id) {
                e.successorNumber = id; 
                e.nodeIp = ip; 
            }
        }
    }


    /*
    * when there is a client that closes we potentially have to update our leaf table
    * @param oldId
    * @param successor
    * @param successor's ip
    */
    public void closeClient(int oldId, int successor, String successorIp) {
        for(FingerTableEntry e: fingerTableEntries) {
            if (e.successorNumber.equals(oldId)) {
                e.successorNumber = successor;
                e.nodeIp = successorIp;
            }
        } 
    }

    /*
    * utility function for checking if between start and end
    * @param start
    * @param end
    * @param a node that we are checking if in between start and end
    */
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
    }


    /*
    * get successor of ip of a row specified by j+2^i
    * @param: ideal id (j+2^i)
    */
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
