package src;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.ServerSocket;
import java.net.Socket;
import utility.*;
import java.io.*;

public class Anchor extends Node {

    private HashMap<Integer, String> nodeTable;
    private static Globals globals = new Globals();
    private Semaphore nodesMutex;

    public Anchor(){
        super( 0 );
        nodesMutex = new Semaphore(1);
        nodeTable = new HashMap<Integer, String>();
        nodeTable.put( 0, this.globals.ANCHOR_IP );
        fingerTable.processNodeTable(nodeTable);
    }

    public static void main( String[] arg ){

        
        Anchor anchor = new Anchor();
        Thread nodeThread = new Thread( anchor );
        nodeThread.start();

        CLI cli = new CLI( anchor );
        Thread cliThread = new Thread( cli );
        cliThread.start();

        anchor.anchorRequests();
    }
 

    private void anchorRequests() {
        
        try {
            ServerSocket serverSocket = new ServerSocket(this.globals.ANCHOR_PORT);
            while (true) {
                Socket socket = serverSocket.accept(); 
                AnchorListener l = new AnchorListener(socket);
                l.start(); 
            } 
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class AnchorListener extends Thread {

        Socket socket; 

        AnchorListener(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
                Packet packet = (Packet) in.readObject();
                String type = packet.getPacketType();
                if (type.equals(globals.AnchorConnect)) {
                    String ip = socket.getInetAddress().getHostName();
                    int newId = packet.getId();
                    nodesMutex.acquire();
                    nodeTable.put(newId, ip);
                    fingerTable.processNodeTable(nodeTable);
                    nodesMutex.release();
                    NewClient newClient = new NewClient(newId, ip);
                    for(Integer nodeId : nodeTable.keySet()) {
                        if(nodeId != newId) {
                            AnchorSender sender = new AnchorSender(nodeId, nodeTable.get(nodeId), (Packet) newClient);
                            sender.start();
                        }
                    }
                    AnchorResponse response = new AnchorResponse(nodeTable);
                    out.writeObject(response);
                }
                else if (type.equals(globals.ClientClose)) {
                    nodesMutex.acquire();
                    nodeTable.remove(packet.getId()); 
                    fingerTable.processNodeTable(nodeTable);
                    nodesMutex.release();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class AnchorSender extends Thread {

        String ip; 
        Packet packet;
        int dest;

        AnchorSender(int dest, String ip, Packet packet) {
            this.ip = ip;
            this.packet = packet;
            this.dest = dest;
        }

        public void run() {
            Globals globals = new Globals();  
            try {
                Socket socket = new Socket(ip, globals.PORT);
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
                ObjectInputStream in = new ObjectInputStream( socket.getInputStream() ); 
                PacketWrapper wrapper = new PacketWrapper(packet, dest);
                out.writeObject(wrapper);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }       

    }

    public void printTable(){

        for( Integer uuid : nodeTable.keySet() ){
            String key = uuid.toString();
            String value = nodeTable.get(uuid);
            System.out.println( key + "\t" + value );
        }
    }
}
