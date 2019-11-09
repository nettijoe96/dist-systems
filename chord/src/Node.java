package src;


import java.net.*;
import java.io.*;
import utility.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;

/*
 * Abstract class Node
 *
 * Contains all of the functions that a node aught to have
 * Is extended by AnchorNode and ClientNode
 * Those classes are different because the AnchorNode should deal with
 * giving out arbitrary IP addresses to new nodes that join the network
 * while a ClientNode is restricted to addresses in it's fingertable
 */

abstract class Node implements Runnable{
    protected FingerTable fingerTable;
    public Integer myId;
    protected ServerSocket serverSocket;
    public Globals globals = new Globals();
    private ArrayList<Data> dataArr;
    public ArrayList<Integer> myHashIds; //the ids that are offline before this node
    public Boolean running;

    public Node( Integer id ){
        this.myId = id;
        this.fingerTable = new FingerTable(id);
        this.dataArr = new ArrayList<Data>();
        this.myHashIds = new ArrayList<Integer>();
        this.myHashIds.add(myId);
        this.running = true;
        try{
            this.serverSocket = new ServerSocket( this.globals.PORT );
        } catch( IOException e ){
            System.out.println( "Error creating Node. ServerSocket could not be created." );
            e.printStackTrace();
        }
    } 


    public void initMyHashIds(HashMap<Integer, String> nodeTable) {
        
        System.out.println(nodeTable);
        Integer[] ids = nodeTable.keySet().toArray(new Integer[0]);
        Arrays.sort(ids);
        
        int self = 0;
        for(int i = 0; i < ids.length; i++) {
            if(ids[i].equals(myId)) {
                self = i;
                break;
            } 
        } 

        int prev;
        if(self == 0) {
            prev = ids[ids.length-1];
        }
        else {
            prev = ids[self-1];
        }
        System.out.println("prev: " + prev);
        System.out.println("self: " + self);

        if(prev > myId) {
            for(int i = prev+1; i < globals.ringSize; i++) {
                myHashIds.add(i);
            }
            for(int i = 0; i < myId; i++) {
                myHashIds.add(i);
            }
        }
        else {
            for(int i = prev+1; i < myId; i++) {
                myHashIds.add(i);
            }
        }

    }

    public void forward( PacketWrapper wrapper ){
        int dest = wrapper.destination;
        FingerTableEntry[] fingerTableEntries = this.fingerTable.fingerTableEntries;
        // Check my fingertable
        // Try to find the largest node in my table that is smaller than the destination
        FingerTableEntry forwardTo = null;
        for( FingerTableEntry ent : fingerTableEntries ){
            if( ent.nodeNumber == dest) {
                wrapper.destination = ent.successorNumber;   //we need to send the data to the right place given that everyone does not know the whole network and the network may not be full
                forwardTo = ent;
                break;
            }
            else if( ent.nodeNumber < dest ){
                if( forwardTo == null ){
                    forwardTo = ent;
                }else{
                    if( ent.nodeNumber > forwardTo.nodeNumber ){ //getting as close as we can to destination
                        forwardTo = ent;
                    }
                }
            }
        }

        // If we never found anything less or equal to our destination node, we need the highest node
        if( forwardTo == null ){
            for( FingerTableEntry ent : fingerTableEntries ){
                if( forwardTo == null){
                    forwardTo = ent;
                }else if( forwardTo.nodeNumber < ent.nodeNumber ){
                    forwardTo = ent;
                }
            }
        }

        try{
            // Now we definitely have a node that we are forwarding to,
            // so time to forward it
            Socket socket = new Socket( forwardTo.nodeIp, this.globals.PORT );
            ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );

            out.writeObject( wrapper );
            System.out.println("wrapper sent to id: " );
            System.out.println(wrapper.destination);
            System.out.println(forwardTo.nodeIp);
            Packet response = (Packet) in.readObject();
            System.out.println( response );

            out.close();
            in.close();
            socket.close();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    abstract void printTable();

    public void callManager( Packet packet ){
        //Logic to decide where to pass it?
        //Need a structure for calls?

        String type = packet.getPacketType();
        System.out.println( "packet type:\t" + type );
        if( type.equals( this.globals.Connect ) ){
            System.out.println( "TODO: implement connect, maybe... I don't think we have to do this");
        }else if( type.equals( this.globals.NewClient ) ){
            NewClient newClient = (NewClient) packet;
            int newId = newClient.getId();
            String ip = newClient.ip;
            fingerTable.newClient(newId, ip);
            redistributeData(newId);
            System.out.println(fingerTable.toString());
        }else if( type.equals( this.globals.NewData ) ){
            NewData newData = (NewData) packet;         
            Data data = newData.data;
            newData(data); 
            System.out.println(myId);
            System.out.println(" received data " + data);
        }else if( type.equals( this.globals.RequestData ) ){
            System.out.println("Request for data received");
            RequestData request = (RequestData) packet;
            String key = request.key;
            int requester = request.requester;
            Data replyData = null;
            for( Data d : dataArr ){
                if( d.key.equals( key ) ){
                    replyData = d;
                }
            }

            ReplyData reply = new ReplyData( replyData );
            PacketWrapper replyPacket = new PacketWrapper( reply, requester );
            forward( replyPacket );
        }else if( type.equals( this.globals.ReplyData ) ){
            ReplyData reply = (ReplyData) packet;
            System.out.println( "Data Reply:" );
            System.out.println( reply.data );
        }else if( type.equals( this.globals.CloseRelay ) ){
            CloseRelay close = (CloseRelay) packet;
            int oldId = close.oldId;
            int successor = close.successor;
            String successorIp = close.successorIp;
            fingerTable.closeClient(oldId, successor, successorIp);
            System.out.println(fingerTable);
        }else{
            System.out.println( "Unimplemented command" );
        }
    }



    public void redistributeData(int newId) {
        ArrayList<Integer> oldIds = new ArrayList<Integer>();
        if(myHashIds.contains(newId)) {
            Collections.sort(myHashIds);
            int newi = myHashIds.indexOf(newId);
            //change the myHashIds
            int selfi = myHashIds.indexOf(myId); 
            

            if(myId < newId) {
                for(int i = newi; i > selfi; i--) {
                    oldIds.add(myHashIds.get(i));
                    myHashIds.remove(i);
                }
            }
            else if(myId > newId) {
                for(int i = myHashIds.size()-1; i > selfi; i--) {
                    oldIds.add(myHashIds.get(i));
                    myHashIds.remove(i);
                }
                for(int i = newi; i >= 0; i--) {
                    oldIds.add(myHashIds.get(i));
                    myHashIds.remove(i);
                }
            }          
            System.out.println("myHashIds: " + myHashIds);
            //search through all data and send the ids that we no longer care about
            for(Data d : dataArr) {
                if(oldIds.contains(d.dataHash)) {
                    NewData newData = new NewData(myId, d);
                    PacketWrapper wrapper = new PacketWrapper((Packet) newData, newId);
                    forward(wrapper);
                }
            }

        }
    }

    public void newData(Data data) {
        dataArr.add(data);    
    }

    public void requestData( String key ){
        int hash = key.hashCode() % globals.ringSize;

        if( myHashIds.contains(hash) ){
            System.out.println( "The hash is my value" );
            for( Data d : dataArr ){
                if( d.key.equals( key ) ){
                    System.out.println( d );
                }
            }
        }else{
            RequestData packet = new RequestData( key, myId);
            PacketWrapper wrapper = new PacketWrapper( (Packet) packet, hash );
            forward(wrapper);
        }

    }

    public void addData(String key, String dataString) {
        Data data = new Data(key, dataString);
        int hash = data.dataHash; 

        if(myHashIds.contains(hash)) {
            System.out.println("the hash is my value");
            newData(data);  
        }
        else {
            System.out.println("the hash is someone elses value");
            NewData newData = new NewData(myId, data);
            PacketWrapper wrapper = new PacketWrapper((Packet) newData, hash);
            forward(wrapper);
        }
    }

 
    public void close() {
        try {
            //send client close to anchor
            System.out.println("myId" + myId);
            InitiateClose initiateClose = new InitiateClose(myId);
            Socket socket = new Socket( this.globals.ANCHOR_IP, this.globals.ANCHOR_PORT);
            ObjectOutputStream out = new ObjectOutputStream(  socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
            out.writeObject(initiateClose);
            //wait until anchor responds with an ack, which means that everyone updated their fingertable
            Packet packet = (Packet) in.readObject();
            if(packet.getPacketType().equals(Globals.CloseResponse)) {
                CloseResponse closeResponse = (CloseResponse) packet;
                int successor = closeResponse.successor;
                String successorIp = closeResponse.successorIp;
                fingerTable.closeClient(myId, successor, successorIp);
                System.out.println("after receiving ack");
                System.out.println(fingerTable);
                //redistribute data (go through every data element and treat it as new data)
                for(Data d : dataArr) {
                    NewData newData = new NewData(myId, d);
                    PacketWrapper wrapper = new PacketWrapper((Packet) newData, d.dataHash);
                    forward(wrapper);
                } 
            }
            else {
                //TODO: error
            }
            System.out.println("before running = false");
            this.running = false;
            System.exit(0);
        }catch( ClassNotFoundException e ){
            e.printStackTrace();
        }catch( IOException e ){
                e.printStackTrace();
        }
    }


    public void processWrapper(PacketWrapper wrapper) {
        if(myHashIds.contains(wrapper.destination)) {
            Packet packet = (Packet) wrapper.packet; 
            callManager( packet );
        }
        else {
            forward(wrapper);    
        }
    }
    
    public void run(){
        while( running ){
            try{
                Socket socket = this.serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream( socket.getInputStream() );
                ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() );
                PacketWrapper wrapper = (PacketWrapper) in.readObject();
                processWrapper(wrapper);
                out.writeObject( new Packet(Globals.ACK, this.myId ) );
                in.close();
                out.close();
                socket.close();
            }catch( IOException e ){
                System.out.println( "Error while client connecting." );
                e.printStackTrace();
            }catch( ClassNotFoundException e ){
                System.out.println( "Received an unknown object over the network" );
                e.printStackTrace();
            }
        }
        System.out.print("exiting run");
    }
}
