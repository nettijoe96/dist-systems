package pubsub;

import pubsub.packet.*;

public class SubMetadata{
    private ArrayList<String> eventUUIDList = new ArrayList<>();
    private String UUID;
    private ArrayList<Topic> topicsSubscribed = new ArrayList<>();
}

