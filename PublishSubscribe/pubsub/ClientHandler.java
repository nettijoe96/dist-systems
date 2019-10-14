/*
 * Keeps a client connection open and listens for any messages
 * Messages are then invoked using the broker listener class
 */
package pubsub;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BrokerListener listener;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler( Socket socket, BrokerListener listener, ConnackPacket connackPacket ) throws IOException {
        try{
            this.socket = socket;
            this.listener = listener;
            this.out = new ObjectOutputStream( socket.getOutputStream() );
            this.in = new ObjectInputStream( socket.getInputStream() );
            this.out.writeObject( connackPacket );
        }
        catch( IOException e ){
            throw e;
        }
    }

    /*
     * Called when a packet is gotten
     */
    private Packet invokeBroker( Packet packet ){
        return this.listener.invoke( packet );
    }

    public void run(){
        while(true){
            try{
                // Get the packet
                Packet packet = (Packet) this.in.readObject();
                // Process the packet and get some ACK
                packet = invokeBroker( packet );
                // Send the ACK packet back
                this.out.writeObject( packet );    
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}


