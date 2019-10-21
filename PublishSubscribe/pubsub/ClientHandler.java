package pubsub;


import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.util.ArrayList;
import packet.*;



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
                response = (Packet) new ClosePacket("id not in brokers uuid list and not globals.initDeviceId");
                out.writeObject(response);
                return;
            }
      
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
                invokeAdvertise((AdvertisePacket) packet, client);
            }
            
            //here we notify of new events and advertisements
            if (client.nonEmptyOutStream()) {
                NotifyPacket n = sendNotify(client);
                out.writeObject(n); 
            }
            ClosePacket c = new ClosePacket();
            out.writeObject(c); 

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }




    private NotifyPacket sendNotify(ClientData client) {
        boolean access = client.waitTillAccess();
        ArrayList<Event> missedEvents = new ArrayList<Event>();
        for(int i = 0; i < client.missedEvents.size(); i++) {
            Event event = client.missedEvents.get(i);
            Topic topic = event.topic;
            if(client.isSubscribed(topic)) {
                missedEvents.add(event); 
            }
        }
        NotifyPacket packet = new NotifyPacket(missedEvents, client.missedAds);
        client.clearMissed();
        client.unlockClient();
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
            ClientData client = new ClientData(clientId);
            this.listener.broker.addClient(client);
            client.missedAds = new ArrayList<Topic>();
            for(Topic t : this.listener.broker.topics) {
                client.missedAds.add(t);
            }
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
        Topic topic = subscribePacket.getTopic();
        // Check with listener if this is a valid topic
        if( listener.broker.topicExists( topic ) ) {     //TODO: switch to topicExists
            client.addSubscription(topic);
            listener.broker.subscribe(topic, client);
        }

        // Right now there is no failure suback
        // TODO differentiate between a successful suback and a failure
        //SubackPacket subackPacket = new SubackPacket();
        //return subackPacket;
    }


    private void invokeUnsubscribe( UnsubscribePacket unsubscribePacket, ClientData client ) {
        Topic topic = unsubscribePacket.getTopic();
        // Check with listener if this is a valid topic
        if( listener.broker.topicExists( topic ) ) {     //TODO: switch to topicExists
            client.removeSubscription(topic);
            listener.broker.unsubscribe(topic, client);
        }
    }


    private void invokePublish( PublishPacket publishPacket, ClientData client ) {
        Event event = publishPacket.event;
        Topic topic = event.topic;
        if( listener.broker.topicExists(topic)) {
            listener.broker.addEvent(event, client);
        }
        
    }


    /*
    * advertise
    */ 
    private void invokeAdvertise ( AdvertisePacket packet, ClientData client) {
        Topic topic = packet.topic;
        this.listener.broker.addTopic(topic, client);  
    }



    /*
     * connect
     */
    private Packet invokeConnect( ConnectPacket connectPacket, ClientData client) { 
        return new ConnackPacket( connectPacket, client.id);
    } 

}

