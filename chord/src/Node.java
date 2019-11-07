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
    protected Integer id;
    protected ServerSocket serverSocket;
    public Globals globals = new Globals();
    // some sort of structure to store the file?

    public Node( Integer id ){
        //this.fingerTable = new FingerTable();
        this.ipTable = new HashMap<>();
        this.id = id;
        try{
            this.serverSocket = new ServerSocket( this.globals.PORT );
        } catch( IOException e ){
            System.out.println( "Error creating Node. ServerSocket could not be created." );
            e.printStackTrace();
        }
    } 

    public void forwardMessage( String message, Integer id ){
    

    }

    abstract void printTable();

    public void callManager( String call ){
        //Logic to decide where to pass it?
        //Need a structure for calls?

        String[] split = call.split(this.globals.DELIMITER); 

        String command = split[0];

        if( command.equals( this.globals.CONNECT ) ){
            System.out.println( "TODO: implement connect");
            // add to table and return an IP
        }else{
            System.out.println( "Unimplemented command" );
            System.out.println( command );
        }
    }

    public void run(){
        while( true ){
            try{
                Socket socket = this.serverSocket.accept();

                BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                PrintWriter out = new PrintWriter( socket.getOutputStream(), true );
                String message = in.readLine();

                callManager( message );
                out.println( "ACK" );

                in.close();
                out.close();
                socket.close();
            }catch( IOException e ){
                System.out.println( "Error while client connecting." );
                e.printStackTrace();
            }
        }
    }
}
