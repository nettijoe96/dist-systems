package packet;

import java.io.*;

public class Packet implements Serializable{
    private String deviceUUID;
    private String packetUUID;
    private String packetType;

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
