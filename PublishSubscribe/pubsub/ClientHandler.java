package pubsub;


import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;

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
                response = (Packet) new ExceptionPacket("id not in brokers uuid list and not globals.initDeviceId");
                out.writeObject(response);
                return;
            }
            client.updateClientWithNewSocket(socket);                         // update client data

            if( packet.getPacketType().equals(this.globals.CONNECT) ){        // connect
                ConnectPacket connectPacket = (ConnectPacket) packet;
                // Get your connack after processing the connect packet
                response = (Packet) invokeConnect((ConnectPacket) packet, client);
                out.writeObject( response );    
                
            } else if( packet.getPacketType().equals(this.globals.SUBSCRIBE) ){   // subscribe
                invokeSubscribe( (SubscribePacket) packet, client );
            } else if( packet.getPacketType().equals(this.globals.UNSUBSCRIBE) ){  // unsubscribe
                invokeUnsubscribe( (UnsubscribePacket) packet, client);
            } else if( packet.getPacketType().equals(this.globals.PUBLISH) ){  // publish 
                invokePublish( (PublishPacket) packet, client );
            }
            else if( packet.getPacketType().equals(this.globals.ADVERTISE) ){  // advertise
                invokeAdvertise((AdvertisePacket) packet);
            }
            
            System.out.println(this.listener.broker.topics);
            //here we notify of new events and advertisements
            if (client.nonEmptyOutStream()) {
                sendNotify(client);
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
 




    }




    private NotifyPacket sendNotify(ClientData client) {
        ArrayList<Event> missedEvents = new ArrayList<Event>();
        for(int i = 0; i < client.missedEvents.size(); i++) {
            Event event = client.missedEvents.get(i);
            Topic topic = event.topic;
            if(client.isSubscribed(topic)) {
                missedEvents.add(event); 
            }
        }
        NotifyPacket packet = new NotifyPacket(missedEvents, client.missedAds);
        return packet;
    }



    /*
    * getOrMakeClient

    * checks if client of id exists. 
    * If id is init, make new one. 
    * If not init and doesn't exist, it errors
    @param id 
    @return clientData
    */
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

    private void invokeSubscribe( SubscribePacket subscribePacket, ClientData client ) {
        String topicName = subscribePacket.getTopic().getTopic();
        // Check with listener if this is a valid topic
        if( listener.isTopicInList( topicName ) ) {     //TODO: switch to topicExists
            Topic topic = listener.getTopicByName(topicName);
            client.addSubscription(topic);
            listener.broker.subscribe(topic, client);
        }

        // Right now there is no failure suback
        // TODO differentiate between a successful suback and a failure
        //SubackPacket subackPacket = new SubackPacket();
        //return subackPacket;
    }


    private void invokeUnsubscribe( UnsubscribePacket unsubscribePacket, ClientData client ) {
        String topicName = unsubscribePacket.getTopic().getTopic();
        // Check with listener if this is a valid topic
        if( listener.isTopicInList( topicName ) ) {   //TODO: switch to topicExists 
            Topic topic = listener.getTopicByName(topicName);
            client.removeSubscription(topic);
            listener.broker.unsubscribe(topic, client);
        }
    }


    private Packet invokePublish( PublishPacket publishPacket, ClientData clientData ) {
        return null;

    }



    /*
    * advertise
    */ 
    private void invokeAdvertise ( AdvertisePacket packet ) {
        Topic topic = packet.topic;
        this.listener.broker.addTopic(topic);  
    }



    /*
     * connect
     */
    private Packet invokeConnect( ConnectPacket connectPacket, ClientData client) { 
        return new ConnackPacket( connectPacket, client.id);
    } 

}

