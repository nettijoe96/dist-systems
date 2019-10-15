/*
 * The BrokerCLI listens to stdin and is used to monitor the broker
 */
package pubsub;


import java.util.Scanner;

public class BrokerCLI implements Runnable{

    Broker broker;  

    public BrokerCLI(Broker broker) {
        this.broker = broker; 
    }

    public void run(){
        Scanner scanner = new Scanner( System.in );
        while(true){
            System.out.print("> ");
            
            String userInput = scanner.next();
        }
    }
}
