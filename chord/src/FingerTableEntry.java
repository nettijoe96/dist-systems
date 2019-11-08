package src;

import java.util.HashMap;
import java.util.Arrays;

class FingerTableEntry{
    // Possibly terrible names, don't know if we need successor number, more of a toubleshooting thing.
    public Integer nodeNumber; // The node this should point to if everyone was online
    public Integer successorNumber; // The actual node that this does point to
    public String nodeIp; // The IP of the node that is online

    public FingerTableEntry( int thisNode, int i, HashMap<Integer, String> nodeTable ){
        // Hard coded ring size here... need to change in a bit
        this.nodeNumber = (thisNode + (int) Math.pow(2, i)) % 16; 
        Integer[] keys = nodeTable.keySet().toArray(new Integer[0]);
        Arrays.sort(keys);
        for( Integer uuid : keys ){
            if( uuid >= this.nodeNumber ){
                this.successorNumber = uuid;
                this.nodeIp = nodeTable.get(uuid);
                break;
            }
        }
        // No successor gotten from that, then we take the smallest
        if( successorNumber == null ){
            Integer uuid = keys[0]; 
            this.successorNumber = uuid;
            this.nodeIp = nodeTable.get( uuid );
        }
    }


    @Override
    public String toString(){
        return this.nodeNumber.toString() + "\t" + this.successorNumber.toString() + "\t" + this.nodeIp;

    }
}
