package packet;

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
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface net = NetworkInterface.getByInetAddress(ip);
        this.deviceUUID = (String) net.getHardwareAddress();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        this.packetUUID = this.deviceUUID + dateFormat.format(calendar);

        this.packetType = packetType;
    }
    
    public Packet( String packetType, String packetUUID ){
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface net = NetworkInterface.getByInetAddress(ip);
        this.deviceUUID = (String) net.getHardwareAddress();   
        
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
