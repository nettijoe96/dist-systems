package src;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.net.ServerSocket;
import java.net.Socket;
import utility.*;
import java.io.*;

/*
*  Anchor Node. Bootstraps new nodes with ip address to join the network
*/
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
        for(int i = 1; i < globals.ringSize; i++) {  //add all myHashIds
            myHashIds.add(i);
        }
    }

    /*
    create new anchor. create new client. 
    */
    public static void main( String[] arg ){
        
        Anchor anchor = new Anchor();
        Thread nodeThread = new Thread( anchor );
        nodeThread.start();

        CLI cli = new CLI( anchor );
        Thread cliThread = new Thread( cli );
        cliThread.start();

        anchor.anchorRequests();
    }
 

    /*
    anchor listens and creates new listeners whenever there is a connection
    */
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


    /*
    Listens for connection from nodes. 
    These connections are either newClient connections or close connections
    */
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
                
                if (type.equals(globals.AnchorConnect)) {   //for anchorConnect type
                    String ip = socket.getInetAddress().getHostName();
                    int newId = packet.getId();
                    nodesMutex.acquire();
                    nodeTable.put(newId, ip);
                    fingerTable.newClient(newId, ip);
                    nodesMutex.release();
                    NewClient newClient = new NewClient(newId, ip);
                    for(Integer nodeId : nodeTable.keySet()) { //notify everyone of a new client
                        if(nodeId != newId) {
                            AnchorSender sender = new AnchorSender(nodeId, nodeTable.get(nodeId), (Packet) newClient, false);
                            sender.start();
                        }
                    }
                    AnchorResponse response = new AnchorResponse(nodeTable);
                    out.writeObject(response);
                }
                else if (type.equals(globals.InitiateClose)) {  //for initiateClose type
                    nodesMutex.acquire();
                    int oldId = packet.getId();
                    int successor;
                    int oldi = 0;
                    Integer[] ids = nodeTable.keySet().toArray(new Integer[0]);
                    Arrays.sort(ids);
                    for(int i = 0; i < ids.length; i++) {      
                        if(ids[i].equals(oldId)) {
                            oldi = i;
                            break;
                        }
                    } 
                    //find new successor after closing node
                    if(oldi == ids.length-1) {                
                        successor = ids[0];
                    }
                    else {
                        successor = ids[oldi+1];
                    }
                    String successorIp = nodeTable.get(successor);
                    fingerTable.closeClient(oldId, successor, successorIp); 
                    nodeTable.remove(oldId); 
                    CloseRelay closeRelay = new CloseRelay(oldId, successor, successorIp);
                    ArrayList<AnchorSender> senders = new ArrayList<AnchorSender>();
                    //each node updates their fingertable
                    for(Integer nodeId : nodeTable.keySet()) {  
                        AnchorSender sender = new AnchorSender(nodeId, nodeTable.get(nodeId), (Packet) closeRelay, true);
                        senders.add(sender);
                        sender.start();
                    }
                    //we wait until all clients update send response back up the chain to the closing node
                    for(AnchorSender sender : senders) {  
                        sender.join();
                    }
                    out.writeObject( new CloseResponse(successor, successorIp) ); //final response to closing node. 

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
        Boolean ack;

        AnchorSender(int dest, String ip, Packet packet, Boolean ack) {
            this.ip = ip;
            this.packet = packet;
            this.dest = dest;
            this.ack = ack;   //if we need to wait for an ack before completing
        }

        /*
        wrap packet in wrapper and send to destination 
        */
        public void run() {
            Globals globals = new Globals();  
            try {
                Socket socket = new Socket(ip, globals.PORT);
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
                ObjectInputStream in = new ObjectInputStream( socket.getInputStream() ); 
                PacketWrapper wrapper = new PacketWrapper(packet, dest);
                out.writeObject(wrapper);
                if(ack) {
                    Packet packet = (Packet) in.readObject(); 
                    if(packet.getPacketType().equals(globals.ACK)) {
                        return;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }       

    }



    /*
    printing for cli "nodes" command.
    */
    public void printTable(){
        System.out.println("id\tip");
        for( Integer uuid : nodeTable.keySet() ){
            String key = uuid.toString();
            String value = nodeTable.get(uuid);
            System.out.println( key + "\t" + value );
        }
    }

}
