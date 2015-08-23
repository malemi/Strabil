/**
 * 
 */
package org.strabil.manager;

/**
 * 
 * EventAction is used by RunManager to run two functions (BeginEventAction 
 * and EndEventAction) before and after any run.
 * The developer has to write its own BeginEventAction and EndEventAction in a child class.
 *
 * @author Mario Alemi
 * @version 0.1 
 */
public interface EventAction {
	

	
	/**
	 * This method should be used to build the market: Agents and their Products with their relationship.
	 * At the moment that means that for every Event a new market is built). The databse stores all simulated 
	 * markets and then one can collects information. Another solution could be to build one market and 
	 * trace (how?) different evolutions for every event. This would probably be more efficient, but at the moment I 
	 * @return true/false success
	 */
	public boolean BeginEventAction(Event e);	
	
	/**
	 * The simulation is divided in many step, and each step has to do something. Contrary from a
	 * spacial detector, here the dimension is time, not space. Every actor has to pass through each 
	 * step of time. 
	 * 
	 * @param i
	 * @return true/false success
	 */
	public boolean StepAction(int i, Event e);
	
	/**
	 * Executed after any event
	 * Once implemented by the user , this method should return true
	 * @return false if not overloaded by a child class
	 */
	public boolean EndEventAction(Event e);
	
}
