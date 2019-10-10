package pubsub;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.util.Calendar;
import java.text.DateFormat;
import java.io.*;

public class Packet implements Serializable{
    private String deviceUUID;
    private String packetUUID;
    private String packetType;

    public Packet( String packetType ){
        // InetAddress ip = InetAddress.getLocalHost();
        // NetworkInterface net = NetworkInterface.getByInetAddress(ip);
        // this.deviceUUID = net.getHardwareAddress().toString();
        this.deviceUUID = "000";

        // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // Calendar calendar = Calendar.getInstance();
        // this.packetUUID = this.deviceUUID + dateFormat.format(calendar);
        this.packetUUID = "0001";

        this.packetType = packetType;
    }
    
    public Packet( String packetType, String packetUUID ){
        // InetAddress ip = InetAddress.getLocalHost();
        // NetworkInterface net = NetworkInterface.getByInetAddress(ip);
        // this.deviceUUID = (String) net.getHardwareAddress();   
        this.deviceUUID = "000";
        
        this.packetUUID = packetUUID;

        this.packetType = packetType;
    }

    final String getDeviceUUID(){
        return this.deviceUUID;
    }

    final String getPacketUUID(){
        return this.packetUUID;
    }

    final String getPacketType(){
        return this.packetType;
    }
}