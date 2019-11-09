package src;

import java.net.*;
import java.io.*;
import utility.*;

/*
* Client extends Node. 
* We connect to anchor to bootstrap node
*/
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

    /*
    * connects to anchor when first starting to get information to build fingerTable
    */
    public void connectToAnchor() {
        try{
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.ANCHOR_PORT);
            ObjectOutputStream out = new ObjectOutputStream(  socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );

            AnchorConnect anchorConnect = new AnchorConnect(this.myId);
            out.writeObject(anchorConnect);

            AnchorResponse anchorResponse = (AnchorResponse) in.readObject();
            this.fingerTable.processNodeTable(anchorResponse.nodeTable); 
            initMyHashIds(anchorResponse.nodeTable);
            System.out.println( this.fingerTable );

            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public void printTable(){
        System.out.println("only the anchor node has a nodeTable");
    }
}
