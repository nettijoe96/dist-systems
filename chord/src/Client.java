package src;
/* Just for the client node
 * Don't put anything in here that the anchor node will also have to do
 */

import java.net.*;
import java.io.*;
import packet.*;

public class Client extends Node {

    private FingerTable fingerTable;

    public Client( Integer id ){
        super( id );
        this.fingerTable = new FingerTable(id);
    }

    public static void main( String[] arg ) {
        Client client = new Client( Integer.parseInt( arg[0] ) );
        Thread nodeThread = new Thread( client );
        nodeThread.start();

        CLI cli = new CLI( client );
        Thread cliThread = new Thread( cli );
        cliThread.start();

        client.connectToAnchor();
    }

    // Should only need a different behavior when asking to join the network
    // and when being asked to join (refuse it instead of dole out an address?)

    public void testConnection(){
        try{
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.PORT);
            PrintWriter out = new PrintWriter(  socket.getOutputStream(), true );
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            out.println( this.globals.CONNECT +
                   this.globals.DELIMITER +
                   Integer.toString( this.id ) );
            System.out.println( (String) in.readLine() );
            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }


    public void connectToAnchor() {
        try{
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.ANCHOR_PORT);
            ObjectOutputStream out = new ObjectOutputStream(  socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
            AnchorConnect anchorConnect = new AnchorConnect(this.id);
            out.writeObject(anchorConnect);
            AnchorResponse anchorResponse = (AnchorResponse) in.readObject();
            this.fingerTable.processNodeTable(anchorResponse.nodeTable); 
            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

}
