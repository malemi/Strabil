/**
 * 
 */
package org.strabil.manager;

/**
 * @author Mario Alemi
 * @version 0.1
 * RunAction is used by RunManager to run two functions (BeginRunAction 
 * and EndRunAction) before and after any run.
 * The developer has to write its own BeginRunAction and EndRunAction in a child class.
 *
 */
public interface RunAction {

	
	/**
	 * Any special action to be performed before the Run. If none, just return true.
	 * @return false if not overloaded by a child class
	 */
	public boolean BeginRunAction();
	
	/**
	 * Executed after any run
	 * Once implemented by the user , this method should return true
	 * @return false if not overloaded by a child class
	 */
	public boolean EndRunAction();
}
