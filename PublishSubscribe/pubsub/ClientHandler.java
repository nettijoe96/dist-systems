package pubsub;


import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class ClientHandler implements Runnable{


    private Socket socket;
    private BrokerListener listener;
    private Globals globals;    

    public ClientHandler( Socket socket, BrokerListener listener ){
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
            Packet response;
            ClientData client;
            try {
                client = getOrMakeClient(packet.getDeviceId());  //get client
            }
            catch(IllegalArgumentException e) {
                response = (Packet) new ExceptionPacket("uuid not in brokers uuid list and not initId");
                out.writeObject(response);
                return;
            }
            client.updateClientWithNewSocket(socket);                         //update client data

            if( packet.getPacketType().equals(this.globals.CONNECT) ){        //CONNECT
                ConnectPacket connectPacket = (ConnectPacket) packet;
                // Get your connack after processing the connect packet
                response = (Packet) invokeConnect((ConnectPacket) packet, client);
                out.writeObject( response );    
                
            }
            System.out.print("id: ");
            System.out.print(this.listener.broker.clientMap);
            //System.out.println(this.listener.broker.clientMap.get((Integer)1).id);
            //TODO: call invoke for subscribe, publish

            
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
 
    }


    private ClientData getOrMakeClient(int id) throws IllegalArgumentException {
        if( id == this.globals.initDeviceId ) {
            //here we have a brand new client. We need to give it a uuid.
            int clientId = this.listener.broker.getNewId();
            ClientData client = new ClientData(clientId, socket);
            this.listener.broker.addClient(client);
            return client;
        }
        else {
            try {
                ClientData client = listener.broker.getClient(id);
                return client;
            }
            catch(IllegalArgumentException e) {
                //if we cannot find a client for a uuid, there is likely a client-side error
                throw e;
            }

            
        }

    }

    /*
     * connect
     */
    private Packet invokeConnect( ConnectPacket connectPacket, ClientData client) { 
        return new ConnackPacket( connectPacket, client.id);
    } 

}

