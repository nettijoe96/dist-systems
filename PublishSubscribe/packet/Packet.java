package packet;

import pubsub.Globals;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Calendar;
import java.text.DateFormat;
import java.io.Serializable;

public class Packet implements Serializable{
    private int deviceId;
    private String packetId;
    private String packetType;

    public Packet( String packetType){
        Globals globals = new Globals();
        this.deviceId = globals.initDeviceId;
        this.packetId = "0001";
        this.packetType = packetType;
    }
    
    public Packet( String packetType, int deviceId ){

        Globals globals = new Globals();
        this.deviceId = deviceId; 
        this.packetType = packetType;
    }


    public final int getDeviceId(){
        return this.deviceId;
    }


    public final String getPacketId(){
        return this.packetId;
    }

    public final String getPacketType(){
        return this.packetType;
    }
}
