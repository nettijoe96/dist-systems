package pubsub;

public interface Subscriber {
	/*
	 * subscribe to a topic
	 */
	public void subscribe();
	
	/*
	 * subscribe to a topic with matching keywords
	 */
	public void subscribe();
	
	/*
	 * unsubscribe from a topic 
	 */
	public void unsubscribe();
	
	/*
	 * unsubscribe to all subscribed topics
	 */
	public void unsubscribe();
	
	/*
	 * show the list of topics current subscribed to 
	 */
	public void listSubscribedTopics();
	
}
