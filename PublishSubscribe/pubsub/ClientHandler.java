package pubsub;

import pubsub.packet.*;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BrokerListener listener;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler( Socket socket, BrokerListener listener, ConnackPacket connackPacket ){
        this.socket = socket;
        this.listener = listener;
        this.out = new ObjectOutputStream( socket.getOutputStream() );
        this.in = new ObjectInputStream( socket.getInputStream() );

        this.out.writeObject( connackPacket );
    }

    private Packet invokeBroker( Packet packet ){
        this.listener.invoke( packet );
    }

    public void run(){
        while true{
            Packet packet = (Packet) this.in.readObject();
            packet = invokeBroker( packet );
            this.out.writeObject( packet );    
        }
    }
}


