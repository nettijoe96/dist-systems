package pubsub;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Calendar;
import java.text.DateFormat;
import java.io.*;

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


    final int getDeviceId(){
        return this.deviceId;
    }

    final String getPacketId(){
        return this.packetId;
    }

    final String getPacketType(){
        return this.packetType;
    }
}
