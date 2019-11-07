package src;

import java.util.HashMap;

class FingerTableEntry{
    // Possibly terrible names, don't know if we need successor number, more of a toubleshooting thing.
    public Integer nodeNumber; // The node this should point to if everyone was online
    public Integer successorNumber; // The actual node that this does point to
    public String nodeIp; // The IP of the node that is online

    public FingerTableEntry( int thisNode, int i, HashMap<Integer, String> nodeTable ){
        // Hard coded ring size here... need to change in a bit
        this.nodeNumber = (thisNode + (int) Math.pow(2, i)) % 16; 
        for( Integer uuid : nodeTable.keySet() ){
            if( uuid >= this.nodeNumber ){
                this.successorNumber = uuid;
                this.nodeIp = nodeTable.get(uuid);
                break;
            }
        }
        // No successor gotten from that
        if( successorNumber == null ){
            // So find the lowest node that must be it
            // this is a terrible way to do this but it works...
            for( Integer uuid : nodeTable.keySet() ){
                this.successorNumber = uuid;
                this.nodeIp = nodeTable.get( uuid );
                break;
            }
        }
    }


    @Override
    public String toString(){
        return this.nodeNumber.toString() + "\t" + this.successorNumber.toString() + "\t" + this.nodeIp;

    }
}
