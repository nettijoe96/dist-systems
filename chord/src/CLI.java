package src;


import utility.*;
import java.util.Scanner;
import java.io.*;


/*
* CLI class. See globals for available commands.
*/
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
            String input = in.nextLine();
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
            }else if( command.equals( this.globals.FILES ) ){
                String dir = tokens[1];
                try{
                    File f = new File( dir );
                    String[] paths = f.list();
                    for( String path : paths ){
                        System.out.println( path );
                    }
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }else if(command.equals(this.globals.NEW_KEY_VALUE)) {
                String fName = tokens[1];
                // create dataString
                try{
                    File file = new File( fName );

                    BufferedReader br = new BufferedReader( new FileReader( file ) );
                    String dataString = "";
                    String tmp;

                    while( ( tmp = br.readLine() ) != null ){
                        dataString = dataString + tmp;
                    }

                    node.addData( fName, dataString );
                }catch( FileNotFoundException e ){
                    System.out.println( "File not found" );
                }catch( IOException e ){
                    System.out.println( "Error Reading File" );
                }
                    
                /*
                String[] subtokens = tokens[1].split(" ", 2);
                if(subtokens.length != 2) {
                    System.out.println("must provide key and value"); 
                } 
                else {
                    String key = subtokens[0];
                    String dataString = subtokens[1];
                    node.addData(key, dataString);
                }
                */
            }else if( command.equals( this.globals.REQUEST_DATA ) ){
                String key = tokens[1];
                node.requestData( key );
            }else if( command.equals( this.globals.CLOSE ) ){
                node.close();
            }else if( command.equals( this.globals.HASH_IDS ) ){
                node.showHashIds();
            }else{
                System.out.println( "Unrecognized command, please see README" );
            }
        }
    }

}
