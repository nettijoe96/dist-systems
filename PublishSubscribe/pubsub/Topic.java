/*
 * One of the original classes
 * Topics are just strings though?
 * Should they have a list of events?
 */
package pubsub;

public class Topic{
    private String topic;
    private String description;

    public Topic( String topic ){
        this.topic = topic;
    }

    @Override
    public String toString(){
        return this.topic +"\n\t"+ this.description;
    }

    @Override
    public boolean equals( Object o ) {
    
        if( o == this ){
            return true;
        }

        if( !( o instanceof Topic ) ){
            return false;
        }

        Topic t = (Topic) o;

        return this.topic.equals( t.topic );
    }

    public String getTopic(){
        return this.topic;
    }

    public String getDescription(){
        return this.description;
    }
}

