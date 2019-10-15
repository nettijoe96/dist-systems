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

    public void run(){
        Scanner scanner = new Scanner( System.in );
        while(true){
            System.out.print("> ");
            
            String userInput = scanner.next();
            if( userInput.equals( globals.EXITLINE ) ){
                this.broker.exitService();
                break;
            }
        }
    }
}
