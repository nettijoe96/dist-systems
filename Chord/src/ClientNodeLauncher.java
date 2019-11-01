package src;

public class ClientNodeLauncher{
    public static void main( String[] arg ){
        ClientNode node = new ClientNode( Integer.parseInt( arg[1] ) );
        Thread nodeThread = new Thread( node );
        nodeThread.start();

        CLI cli = new CLI();
        Thread cliThread = new Thread( cli );
        cliThread.start();
    }
}
