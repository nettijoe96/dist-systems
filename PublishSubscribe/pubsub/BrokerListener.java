package pubsub;


import java.net.*;
import java.io.*;


public class BrokerListener implements Runnable{

    private int port;
    protected SubList subList;
    protected PubList pubList;
    protected ArrayList<Topic> topics;
    protected ArrayList<Event> events;
    private ServerSocket serverSocket;


    public BrokerListener( int port ){
        this.subList = new SubList();
        this.pubList = new PubList();
        this.port = port;

        this.serverSocket = new ServerSocket( this.port );
        System.out.println("Server socket opened")

    }

    private void addClient(){
        
    }


    public void run(){
        Socket socket = new Socket

        while true{
            System.out.println("Waiting for client connection");

            socket = this.serverSocket.accept();

            System.out.println("New client connected!");

            addClient( socket );

        }
    }


}

