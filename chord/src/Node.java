package src;


import java.util.*;
import java.net.*;
import java.io.*;
import packet.*;

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
    protected FingerTable fingerTable = null;
    public Integer id;
    protected ServerSocket serverSocket;
    public Globals globals = new Globals();
    // some sort of structure to store the file?

    public Node( Integer id ){
        this.id = id;
        try{
            this.serverSocket = new ServerSocket( this.globals.PORT );
        } catch( IOException e ){
            System.out.println( "Error creating Node. ServerSocket could not be created." );
            e.printStackTrace();
        }
    } 

    public void forwardMessage( Message message ){
        int dest = message.destinationId;
        FingerTableEntry[] fingerTableEntries = this.fingerTable.fingerTableEntries;
       // Check my fingertable
        // Try to find the largest node in my table that is smaller than the destination
        FingerTableEntry forwardTo = null;
        for( FingerTableEntry ent : fingerTableEntries ){
            if( ent.nodeNumber <= dest ){
                if( forwardTo == null ){
                    forwardTo = ent;
                }else{
                    if( forwardTo.nodeNumber < ent.nodeNumber ){
                        forwardTo = ent;
                    }
                }
            }
        }
        // If we never found anything lower than our destination node, we need the highest node
        for( FingerTableEntry ent : fingerTableEntries ){
            if( forwardTo == null){
                forwardTo = ent;
            }else if( forwardTo.nodeNumber < ent.nodeNumber ){
                forwardTo = ent;
            }
        }

        try{
            // Now we definitely have a node that we are forwarding to,
            // so time to forward it
            Socket socket = new Socket( forwardTo.nodeIp, this.globals.PORT );
            ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );

            out.writeObject( message );

            Packet response = (Packet) in.readObject();
            System.out.println( response );

            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    abstract void printTable();

    public void callManager( Packet packet ){
        //Logic to decide where to pass it?
        //Need a structure for calls?

        String type = packet.getPacketType();
        System.out.println( "packet type:\t" + type );
        if( type.equals( this.globals.Connect ) ){
            System.out.println( "TODO: implement connect, maybe... I don't think we have to do this");
            // add to table and return an IP
        }else if( type.equals( this.globals.Message ) ){
            Message message = (Message) packet;

            // Need to deal with when we are holding the keys for the destination node
            if( message.destinationId == this.id ){
                System.out.println( "Recieved message:" );
                System.out.println( message.message );
            }else{
                forwardMessage( message );
            }
        }else{
            System.out.println( "Unimplemented command" );
        }
    }

    public void run(){
        while( true ){
            try{
                Socket socket = this.serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
                Packet packet = (Packet) in.readObject();

                callManager( packet );

                out.writeObject( new Packet( "ack", this.id ) );

                in.close();
                out.close();
                socket.close();
            }catch( IOException e ){
                System.out.println( "Error while client connecting." );
                e.printStackTrace();
            }catch( ClassNotFoundException e ){
                System.out.println( "Received an unknown object over the network" );
                e.printStackTrace();
            }
        }
    }
}
