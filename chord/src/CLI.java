package src;


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
            }else{
                System.out.println( "TODO implement CLI" );
            }
        }
    }

}
