package src;

public class AnchorNodeLauncher{

    public static void main( String[] arg ){

        AnchorNode node = new AnchorNode();
        Thread nodeThread = new Thread( node );
        nodeThread.start();

        CLI cli = new CLI();
        Thread cliThread = new Thread( cli );
        cliThread.start();

    }
}
