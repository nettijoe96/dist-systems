package src;
/* Just for the client node
 * Don't put anything in here that the anchor node will also have to do
 */

import java.net.*;
import java.io.*;

public class ClientNode extends Node{

    public ClientNode( Integer uuid ){
        super( uuid );
    }
    // Should only need a different behavior when asking to join the network
    // and when being asked to join (refuse it instead of dole out an address?)
}
