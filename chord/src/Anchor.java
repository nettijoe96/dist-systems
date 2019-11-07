package src;

import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import packet.*;
import java.io.*;

public class Anchor extends Node {

    private HashMap<Integer, String> nodeTable;
    private static Globals globals = new Globals();

    public Anchor(){
        super( 0 );
        nodeTable = new HashMap<Integer, String>();
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

    private class AnchorListener extends Thread {

        Socket socket; 

        AnchorListener(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());
                AnchorConnect conn = (AnchorConnect) in.readObject();
                String ip = socket.getInetAddress().getHostName();
                nodeTable.put(conn.getId(), ip);
                AnchorResponse response = new AnchorResponse(nodeTable);
                out.writeObject(response);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
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
