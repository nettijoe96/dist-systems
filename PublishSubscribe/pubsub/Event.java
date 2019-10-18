package pubsub;

import java.io.Serializable;


public class Event implements Serializable {
    public Topic topic;
    public String payload;
}

