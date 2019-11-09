package utility;

import java.io.Serializable;

public class Packet implements Serializable{
    private int id;
    private String packetType;

    public Packet( String packetType, int id ){
        this.id = id; 
        this.packetType = packetType;
    }


    public final int getId(){
        return this.id;
    }


    public final String getPacketType(){
        return this.packetType;
    }
}
