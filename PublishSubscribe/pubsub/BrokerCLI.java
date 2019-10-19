/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */
package pubsub;


import java.util.Scanner;

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
            }else if( command.equals( globals.LIST_TOPICS_COMMAND ) ){
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
            }else if( command.equals(globals.HELP_COMMAND) ) {
                System.out.println(globals.BROKER_HELP);
            }else{
                System.out.println("Command not understood. Enter " + globals.HELP_COMMAND +" for help.");
            }
        }
    }
}
