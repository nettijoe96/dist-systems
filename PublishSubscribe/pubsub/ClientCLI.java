/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */
package pubsub;


import java.util.Scanner;
import java.util.ArrayList;

public class ClientCLI implements Runnable{

    private Client client;  
    private Globals globals;

    public ClientCLI(Client client) {
        this.globals = new Globals();
        this.client = client;
    }

    /*
    notify user that their cmd was malformed
    */ 
    private void printMalformed(){
        System.out.println("Malformed command. Type " + globals.HELP_COMMAND + " for help");
    }

    /*
    the loop for checking and processing client cmdline commands
    */ 
    public void run() {
        System.out.println("Ready to accept commands");
        Scanner scanner = new Scanner( System.in );
        scanning: while(true){
            System.out.print("> ");

            String userInput = scanner.nextLine();

            String[] tokens = userInput.split(" ", 2);
            
            String command = tokens[0].toUpperCase();

            this.client.lockClient();
            //ADVERTISE
            if( command.equals( globals.ADVERTISE ) ){
                String[] subTokens = tokens[1].split(" ", 2);
                if( subTokens.length == 2 ){
                    String topicName = subTokens[0];
                    String topicDescription = subTokens[1];
                    Topic topic = new Topic( topicName, topicDescription );
                    this.client.callManager( globals.ADVERTISE, topic );
                }
                else{
                    printMalformed();
                    continue;
                }
            // HELP
            }else if( command.equals( globals.HELP_COMMAND ) ) {
                System.out.println( globals.CLIENT_HELP );
            // PUBLISH
            }else if( command.equals( globals.PUBLISH ) ){
                if( tokens.length == 2 ){
                    String[] subTokens = tokens[1].split( " ", 3 );
                    if( subTokens.length == 3 ){
                        String topicName = subTokens[0];
                        String name = subTokens[1];
                        String payload = subTokens[2];
                        // Topics are equal if they have the same topic name
                        Topic topic = new Topic( topicName, "" );
                        Event event = new Event( topic, name, payload );
                        this.client.callManager( globals.PUBLISH, event );
                    }
                    else{
                        printMalformed();
                        continue;
                    }
                }else{
                    printMalformed();
                    continue;
                }
            // SUBSCRIBE
            }else if( command.equals( globals.SUBSCRIBE ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[1];
                    Topic topic = new Topic( topicName, "" );
                    this.client.callManager( globals.SUBSCRIBE, topic );
                }else{
                    printMalformed();
                    continue;
                }
            // UNSUBSCRIBE
            }else if( command.equals( globals.UNSUBSCRIBE ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[1];
                    Topic topic = new Topic( topicName, "" );
                    this.client.callManager( globals.UNSUBSCRIBE, topic );
                }else{
                    printMalformed();
                    continue;
               
                }
            // SUBSCRIPTIONS
            }else if( command.equals( globals.LIST_SUBSCRIPTIONS ) ){
                if( tokens.length == 1 ){
                    for(String s : this.client.subscriptions) {
                        printTopic(this.client.nameTopic.get(s));
                    }
                }else{
                    printMalformed();
                    continue;
                }
            }
            //TOPICS
            else if( command.equals( globals.LIST_TOPICS ) ){
                if( tokens.length == 1 ){
                    for(String a : this.client.ads) {
                        printTopic(this.client.nameTopic.get(a));
                    }
                }else{
                    printMalformed();
                    continue;
                }
            }
            //SUBSCRIPTION topic
            else if( command.equals( globals.SUBSCRIPTION ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[1];
                    Topic topic = this.client.getTopicByName(topicName);
                    if(topic == null) {
                        System.out.println("no such topic exists");  
                    }
                    else if(this.client.subscriptions.contains(topic.topic)) {
                        ArrayList<Event> events = this.client.topicEvents.get(topic.topic);
                        for(Event e: events) {
                            printEvent(e);
                        }
                    }
                    else {
                        System.out.println("not subscribed");  
                    }
                }else{
                    printMalformed();
                    continue;
                }
            }
            //EVENT topic name
            else if( command.equals( globals.EVENT ) ){
                String[] subtokens = userInput.split(" ", 3);
                if( subtokens.length == 3 ){
                    String topicName = subtokens[0];
                    String name = subtokens[1];
                    Topic topic = this.client.getTopicByName(topicName);
                    if(topic == null) {
                        System.out.println("no such topic exists");  
                    }
                    else if(this.client.subscriptions.contains(topic)) {
                        ArrayList<Event> events = this.client.topicEvents.get(topic.topic);
                        for(Event e: events) {
                            if(e.name.equals(name)) {
                                printEvent(e);
                            }
                        }
                    }
                    else {
                        System.out.println("not subscribed to topic");  
                    }
                }else{
                    printMalformed();
                    continue;
                }
            }
            else{
                System.out.println("Command not understood. Enter " + globals.HELP_COMMAND +" for help.");
            }
           
            client.unlockClient();
        
        }
    }
    private void printTopic(Topic t) {
        System.out.println(t.topic + ": " + t.description);
    }
    private void printEvent(Event e) {
        System.out.println(e.name + ": " + e.payload);
    }
}
