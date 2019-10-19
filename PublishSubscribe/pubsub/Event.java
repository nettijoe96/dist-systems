package pubsub;

import java.io.Serializable;


public class Event implements Serializable {
    public Topic topic;
    public String payload;

    public Event( Topic topic, String payload ){
        this.topic = topic;
        this.payload = payload;
    }

    // Do we need toString?
    // equals?
}

