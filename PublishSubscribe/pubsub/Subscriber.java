package pubsub;

public interface Subscriber {
	/*
	 * subscribe to a topic with matching keywords
	 */
	public void subscribe( String keyword );
	
	/*
	 * unsubscribe from a topic 
	 */
	public void unsubscribe( String topic );
	
	/*
	 * unsubscribe to all subscribed topics
	 */
	public void unsubscribe();
	
	/*
	 * show the list of topics current subscribed to 
	 */
	public void listSubscribedTopics();
	
}
