/*
 * The BrokerListener listens for nodes to connect to the network and handles adding them to memory...
 */


package pubsub;


import java.util.concurrent.*;
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
            this.serverSocket = new ServerSocket(this.globals.BROKER_PORT);

        } catch( UnknownHostException e ){
            throw e;
        }
        catch( IOException e ){
            throw e;
        } System.out.println("Server socket opened"); 
    }



    /*
    handlesocket
    create clientHandler thread to handle new connection

    @param: socket 
    */
    private void handleSocket( Socket socket ){
        ClientHandler handler = new ClientHandler( socket, this );
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
    }

    /*
    isTopicInList
    checks if topic is in list

    @param: topicName
    @return bool on whether in list
    */
    protected boolean isTopicInList( String topicName ){
        try{
            this.broker.topicsMutex.acquire();

            for( Topic topic : this.broker.topics ){
               if( topic.topic == topicName ){
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


    /*
    * for every new accept, create a new clientHandler thread
    */
    public void run(){

        // Continually listens for new client connections and adds them
        while(true){

            try{
                Socket socket;
                socket = this.serverSocket.accept();

                handleSocket( socket );
                                
            }catch( Exception e ){
                e.printStackTrace();
            }
        }
    }
}

