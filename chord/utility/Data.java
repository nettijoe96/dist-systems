package utility;


public class Data {

    public int dataHash; 
    public String dataString;
    public String key; 

    public Data(String key, String dataString) {
        this.key = key;
        this.dataString = dataString;
        this.dataHash = hash(key);
    } 

    private int hash(String key) {
        Globals globals = new Globals();
        return key.hashCode() % globals.ringSize;
    }

}
