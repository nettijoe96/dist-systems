package src;


import java.util.*;
import java.net.*;
import java.io.*;

/*
 * Abstract class Node
 *
 * Contains all of the functions that a node aught to have
 * Is extended by AnchorNode and ClientNode
 * Those classes are different because the AnchorNode should deal with
 * giving out arbitrary IP addresses to new nodes that join the network
 * while a ClientNode is restricted to addresses in it's fingertable
 */

abstract class Node implements Runnable{
    //protected FingerTable fingerTable;
    protected HashMap<Integer, String> ipTable;
    protected Integer uuid;
    protected ServerSocket serverSocket;
    public Globals globals = new Globals();
    // some sort of structure to store the file?

    public Node( Integer uuid ){
        //this.fingerTable = new FingerTable();
        this.ipTable = new HashMap<>();
        this.uuid = uuid;
        try{
            this.serverSocket = new ServerSocket( this.globals.PORT );
        } catch( IOException e ){
            System.out.println( "Error creating Node. ServerSocket could not be created." );
            e.printStackTrace();
        }
    } 

    public void forwardMessage( String message, Integer uuid ){
    

    }

    public void callManager( String call ){
        //Logic to decide where to pass it?
        //Need a structure for calls?

    }

    public void testConnection(){
        try{
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.PORT);
            ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
            out.writeObject( "Hello World" );
            System.out.println( (String) in.readObject() );
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }


    public void run(){
        while( true ){
            try{
                Socket socket = this.serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );

                System.out.println( (String) in.readObject() );
                out.writeObject( "ACK" );

                socket.close();
            }catch( IOException e ){
                System.out.println( "Error while client connecting." );
                e.printStackTrace();
            }catch( ClassNotFoundException e ){
                System.out.println( "Malformed message from client." );
                e.printStackTrace();
            }
        }
    }
}
