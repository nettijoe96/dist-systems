package src;

import java.util.*;

public class Anchor extends Node {

    private HashMap<Integer, String> nodeTable;

    public Anchor(){
        super( 0 );
    }

    public static void main( String[] arg ){

        Anchor node = new Anchor();
        Thread nodeThread = new Thread( node );
        nodeThread.start();

        CLI cli = new CLI( node );
        Thread cliThread = new Thread( cli );
        cliThread.start();

    }
}
