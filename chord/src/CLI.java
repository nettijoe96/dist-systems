package src;


import java.util.*;

public class CLI implements Runnable{
    private Node node;

    public CLI( Node node ){
        this.node = node;
    }

    public void run(){
        Scanner in = new Scanner(System.in);
        while(true){
            String input = in.nextLine();

            this.node.testConnection();


            System.out.println( "TODO implement CLI" );
        }
    }

}
