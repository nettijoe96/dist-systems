package src;


import packet.*;
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

            if( input.equals( this.globals.PRINT_TABLE ) ){
                this.node.printTable();
            }else if( input.equals( this.globals.Message ) ){
                // System.out.println( "What node do you want to send to?" );
                node.callManager( new Message( this.node.id, 4, "Hello World" ) );
            }else{
                System.out.println( "TODO implement CLI" );
            }
        }
    }

}
