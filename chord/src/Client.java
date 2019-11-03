package src;
/* Just for the client node
 * Don't put anything in here that the anchor node will also have to do
 */

import java.net.*;
import java.io.*;

public class Client extends Node {

    public Client( Integer uuid ){
        super( uuid );
    }

    public static void main( String[] arg ) {
        Client node = new Client( Integer.parseInt( arg[0] ) );
        Thread nodeThread = new Thread( node );
        nodeThread.start();

        CLI cli = new CLI( node );
        Thread cliThread = new Thread( cli );
        cliThread.start();

        node.testConnection();
    }

    // Should only need a different behavior when asking to join the network
    // and when being asked to join (refuse it instead of dole out an address?)

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


}