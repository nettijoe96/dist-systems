/*
 * All information relevant to the Broker about a subscriber
 */

package pubsub;

import java.util.*;

public class SubMetadata{
    // Need to track if we sent a message
    private ArrayList<String> eventUUIDList = new ArrayList<>();
    private String UUID;
    private ArrayList<Topic> topicsSubscribed = new ArrayList<>();
}

