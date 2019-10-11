/*
 * One of the basic classes
 * he had it as an interface
 * I don't know what to do with it
 * I would make it just a client
 */

package pubsub;


public interface Publisher {
	/*
	 * publish an event of a specific topic with title and content
	 */
	public void publish();
	
	/*
	 * advertise new topic
	 */
	public void advertise();
}
