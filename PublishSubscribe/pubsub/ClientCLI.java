/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */
package pubsub;


import java.util.Scanner;

public class ClientCLI implements Runnable{

    private Client client;  
    private Globals globals;

    public ClientCLI(Client client) {
        this.globals = new Globals();
        this.client = client;
    }

    private void printMalformed(){
        System.out.println("Malformed command. Type " + globals.HELP_COMMAND + " for help");
    }

    public void run() {
        System.out.println("Ready to accept commands");
        Scanner scanner = new Scanner( System.in );
        scanning: while(scanner.hasNextLine()){
            System.out.print("> ");
            
            String userInput = scanner.nextLine();

            String[] tokens = userInput.split(" ", 2);
            
            String command = tokens[0].toUpperCase();

            // switch statement will not work here
            // EXIT
            if( command.equals( globals.EXIT_COMMAND) ){
                this.client.exitService();
                break scanning;
            //ADVERTISE
            }else if( command.equals( globals.ADVERTISE ) ){
                if( tokens.length == 2 ){
                    String[] subTokens = tokens[1].split(" ", 2);
                    if( subTokens.length == 2 ){
                        String topicName = subTokens[0];
                        String topicDescription = subTokens[1];
                        Topic topic = new Topic( topicName, topicDescription );
                        this.client.callManager( globals.ADVERTISE, topic );
                    }
                }else{
                    printMalformed();
                    continue;
                }
            // HELP
            }else if( command.equals( globals.HELP_COMMAND ) ) {
                System.out.println( globals.CLIENT_HELP );
            // PUBLISH
            }else if( command.equals( globals.PUBLISH ) ){
                if( tokens.length == 2 ){
                    String[] subTokens = tokens[1].split( " ", 2 );
                    if( subTokens.length == 2 ){
                        String topicName = subTokens[0];
                        String payload = subTokens[1];
                        // Topics are equal if they have the same topic name
                        Topic topic = new Topic( topicName, "" );
                        Event event = new Event( topic, payload );
                        this.client.callManager( globals.ADVERTISE, event );
                    }
                }else{
                    printMalformed();
                    continue;
                }
            // SUBSCRIBE
            }else if( command.equals( globals.SUBSCRIBE ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[0];
                    Topic topic = new Topic( topicName, "" );
                    this.client.callManager( globals.SUBSCRIBE, topic );
                }else{
                    printMalformed();
                    continue;
                }
            // UNSUBSCRIBE
            }else if( command.equals( globals.UNSUBSCRIBE ) ){
                if( tokens.length == 2 ){
                    String topicName = tokens[0];
                    Topic topic = new Topic( topicName, "" );
                    this.client.callManager( globals.UNSUBSCRIBE, topic );
                }else{
                    printMalformed();
                    continue;
                }
            }else{
                System.out.println("Command not understood. Enter " + globals.HELP_COMMAND +" for help.");
            }
        }
    }
}
