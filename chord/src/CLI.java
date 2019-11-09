package src;


import utility.*;
import java.util.Scanner;

public class CLI implements Runnable{
    private Node node;
    public Globals globals;

    public CLI( Node node ){
        this.node = node;
        this.globals = this.node.globals;
    }

    public void run(){
        System.out.println( "Starting Command Line Service" );
        Scanner in = new Scanner(System.in);
        while(true){
            String input = in.nextLine().toLowerCase();
            String[] tokens = input.split(" ", 2);

            String command = tokens[0].toLowerCase();

            if( command.equals( this.globals.PRINT_TABLE ) ){
                this.node.printTable();
            }else if( command.equals( this.globals.Message ) ){
                // System.out.println( "What node do you want to send to?" );
                node.callManager( new Message( this.node.myId, 4, "Hello World" ) );
            }else if(command.equals(this.globals.NEW_KEY_VALUE)) {
                String[] subtokens = tokens[1].split(" ", 2);
                if(subtokens.length != 2) {
                    System.out.println("must provide key and value"); 
                } 
                else {
                    String key = subtokens[0];
                    String dataString = subtokens[1];
                    node.addData(key, dataString);
                }
            }else{
                System.out.println( "TODO implement CLI" );
            }
        }
    }

}
