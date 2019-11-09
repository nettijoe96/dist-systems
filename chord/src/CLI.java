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
        while(node.running){
            String input = in.nextLine().toLowerCase();
            String[] tokens = input.split(" ", 2);

            String command = tokens[0].toLowerCase();

            if( command.equals( this.globals.PRINT_NODE_TABLE ) ){
                this.node.printTable();
            }else if( command.equals( this.globals.PRINT_FINGER_TABLE ) ){
                System.out.println(this.node.fingerTable); 
            }else if( command.equals( this.globals.PRINT_DATA ) ){
                for(Data d : node.dataArr) {
                    System.out.println(d); 
                }
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
            }else if( command.equals( this.globals.REQUEST_DATA ) ){
                String key = tokens[1];
                node.requestData( key );
            }else if( command.equals( this.globals.CLOSE ) ){
                node.close();
            }else if( command.equals( this.globals.HASH_IDS ) ){
                node.showHashIds();
            }else{
                System.out.println( "TODO implement CLI" );
            }
        }
    }

}
