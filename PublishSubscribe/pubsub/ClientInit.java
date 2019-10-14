package pubsub;


import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class ClientInit implements Runnable{


    private Socket socket;
    private BrokerListener listener;
    private Globals globals;    

    public ClientInit( Socket socket, BrokerListener listener ){
        this.socket = socket;
        this.listener = listener;
        this.globals = new Globals();
    }

    public void run() {

        try{
            // Get the first packet (Should be a connect packet)
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
            ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
            Packet packet = (Packet) in.readObject();
            if( packet.getPacketType().equals(this.globals.CONNECT) ){ // Check that it's connect
                ConnectPacket connectPacket = (ConnectPacket) packet;
                // Get your connack after processing the connect packet
                ConnackPacket connackPacket = (ConnackPacket) listener.invoke( connectPacket );
                out.writeObject( connackPacket );    
                // Start the persistent client handler thread
/*
                ClientHandler clientHandler = new ClientHandler( this.socket, this.listener, connackPacket );
                Thread clientHandlerThread = new Thread( clientHandler );
                clientHandlerThread.start();
*/
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
 
    }
}

