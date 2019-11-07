
public class FingerTableEntry{
    public Integer nodeNumber;
    public Integer successorNumber;
    public String nodeIp;

    public FingerTableEntry( int thisNode, int i, HashTable<Integer, String> nodeTable ){
        this.nodeNumber = thisNode + Math.pow(2, i);
        for( Integer uuid : nodeTable.keySet() ){
            if( uuid >= this.nodeNumber ){
                this.successorNumber = uuid;
                this.nodeIp = nodeTable.get(uuid);
                break;
            }
        }
        // No successor gotten from that
        if( successorNumber = null ){
            // So find the lowest node that must be it
            // this is a terrible way to do this but it works...
            for( Integer uuid : nodeTable.keySet() ){
                this.successorNumber = uuid;
                this.nodeIp = nodeTable.get( uuid );
                break;
            }
        }


    }
