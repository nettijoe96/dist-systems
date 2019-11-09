package src;
/* Just for the client node
 * Don't put anything in here that the anchor node will also have to do
 */

import java.net.*;
import java.io.*;
import utility.*;

public class Client extends Node {

    public Client( Integer id ){
        super( id );
        connectToAnchor();
    }

    public static void main( String[] arg ) {
        Client client = new Client( Integer.parseInt( arg[0] ) );
        Thread nodeThread = new Thread( client );
        nodeThread.start();

        CLI cli = new CLI( client );
        Thread cliThread = new Thread( cli );
        cliThread.start();

    }

    // Should only need a different behavior when asking to join the network
    // and when being asked to join (refuse it instead of dole out an address?)

    public void connectToAnchor() {
        try{
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.ANCHOR_PORT);
            ObjectOutputStream out = new ObjectOutputStream(  socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );

            AnchorConnect anchorConnect = new AnchorConnect(this.myId);
            out.writeObject(anchorConnect);

            AnchorResponse anchorResponse = (AnchorResponse) in.readObject();
            this.fingerTable.processNodeTable(anchorResponse.nodeTable); 

            //More for troubleshooting, but just printing the fingertable that we generated
            System.out.println( "Finger table;" );
            System.out.println( this.fingerTable );

            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public void printTable(){
        System.out.println( "this command is for the anchor node" );
    }
}
