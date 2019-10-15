/*
 * Used to manage all the subscribers
 * Needs to deal with keeping track of what messages have been sent already
 */


package pubsub;

import java.util.*;

public class SubList{
    ArrayList<SubMetadata> subMetaList = new ArrayList<>();

    /*
     * TODO Actually add them
     * TODO: make sure that if they already were connected once they get the messages they missed
     */
    public void addSub( ConnectPacket connectPacket ){
    }
}
