/*
 * The BrokerListener listens for nodes to connect to the network and handles adding them to memory...
 */


package pubsub;


import java.util.concurrent.*;
import pubsub.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;

public class BrokerListener implements Runnable{


    // Data structures that must be kept track of
    private Globals globals;
    public Broker broker;
    private ServerSocket serverSocket;    

    public BrokerListener(Broker broker) throws UnknownHostException, IOException {
        this.globals = new Globals();
        this.broker = broker;

        try{
//            this.serverSocket = new ServerSocket(this.globals.BROKER_PORT, this.globals.serverConnectionBacklog, InetAddress.getByName(this.globals.BROKER_IP)); //TODO: this ip might not be needed once we have docker cont on that address
            this.serverSocket = new ServerSocket(this.globals.BROKER_PORT);

        } catch( UnknownHostException e ){
            throw e;
        }
        catch( IOException e ){
            throw e;
        } System.out.println("Server socket opened"); 
    }




    private void handleSocket( Socket socket ){
        ClientHandler handler = new ClientHandler( socket, this );
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
    }

    protected boolean isTopicInList( String topicName ){
        try{
            this.broker.topicsMutex.acquire();

            for( Topic topic : this.broker.topics ){
               if( topic.getTopic().equals( topicName ) ){
                   this.broker.topicsMutex.release();
                   return true;
               }
            }

            this.broker.topicsMutex.release();
        }catch( InterruptedException e ){
            System.out.println("Interupted Exception");
            e.printStackTrace();
        }
        
        return false;
    }

    protected Topic getTopicByName( String topicName ){
        try{
            this.broker.topicsMutex.acquire();

            for( Topic topic : this.broker.topics ){
                if( topic.getTopic().equals( topicName ) ){
                    this.broker.topicsMutex.release();
                    return topic;
                }
            }
            this.broker.topicsMutex.release();
        }catch( InterruptedException e ){
            System.out.println("Interupted Exception");
            e.printStackTrace();
        }

        return null;
    }


    public void run(){

        // Continually listens for new client connections and adds them
        while(true){
            System.out.println("Waiting for client connection");

            try{
                Socket socket;
                socket = this.serverSocket.accept();
                System.out.println("New client connected!");

                handleSocket( socket );
                System.out.println("after addClient");
                                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}

