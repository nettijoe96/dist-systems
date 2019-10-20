package pubsub;

import java.io.Serializable;


public class Event implements Serializable {
    public Topic topic;
    public String payload;
    public String name;

    public Event( Topic topic, String name, String payload ){
        this.topic = topic;
        this.name = name;
        this.payload = payload;
    }

}

