package src;

class FingerTable {

    
    public int rows;
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

}
