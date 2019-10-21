/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */
package pubsub;

import java.util.Scanner;
import java.util.ArrayList;

public class BrokerCLI implements Runnable{

    private Broker broker;  
    private Globals globals;

    public BrokerCLI(Broker broker) {
        this.globals = new Globals();
        this.broker = broker; 
    }

    public void run() {
        Scanner scanner = new Scanner( System.in );
        scanning: while(true){
            System.out.print("> ");
            
            String userInput = scanner.nextLine();

            String[] tokens = userInput.split(" ", 2);
            
            String command = tokens[0].toUpperCase();

            // switch statement will not work here
            if( command.equals( globals.EXIT_COMMAND) ){
                this.broker.exitService();
                break scanning;
            }else if( command.equals( globals.LIST_TOPICS ) ){
                try{
                    this.broker.topicsMutex.acquire();
                    System.out.println("The following topics are available:");
                    for( Topic topic : this.broker.topics ){
                        System.out.println( topic );
                    }
                    this.broker.topicsMutex.release();
                }catch( InterruptedException e ){
                    System.out.println("Topics mutex operation interrupted while listing topics");
                    e.printStackTrace();
                }
            }
            //SUBSCRIPTION topic
            else if( command.equals( globals.SUBSCRIPTION ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[1];
                    Topic topic = this.broker.getTopicByName(topicName);
                    if(topic == null) {
                        System.out.println("no such topic exists");  
                    }
                    else if(this.broker.topics.contains(topic)) {
                        System.out.println("56 brokercli\n");
                        ArrayList<Event> events = this.broker.topicEvents.get(topic.topic);
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
                    String topicName = subtokens[1];
                    String name = subtokens[2];
                    Topic topic = this.broker.getTopicByName(topicName);
                    if(topic == null) {
                        System.out.println("no such topic exists");  
                    }
                    else if(this.broker.topics.contains(topic)) {
                        ArrayList<Event> events = this.broker.topicEvents.get(topic.topic);
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
            }else if( command.equals(globals.HELP_COMMAND) ) {
                System.out.println(globals.BROKER_HELP);
            }else{
                System.out.println("Command not understood. Enter " + globals.HELP_COMMAND +" for help.");
            }
        }
    }
    private void printMalformed(){
        System.out.println("Malformed command. Type " + globals.HELP_COMMAND + " for help");
    }
    private void printTopic(Topic t) {
        System.out.println(t.topic + ": " + t.description);
    }
    private void printEvent(Event e) {
        System.out.println(e.name + ": " + e.payload);
    }
}
