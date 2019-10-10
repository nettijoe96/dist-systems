package pubsub;

import pubsub.packet.*;
import java.util.*;

public class SubMetadata{
    private ArrayList<String> eventUUIDList = new ArrayList<>();
    private String UUID;
    private ArrayList<Topic> topicsSubscribed = new ArrayList<>();
}

