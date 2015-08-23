/**
 * 
 */
package org.strabil.manager;


/**
* This is the abstract base class of the user's mandatory action class
* for primary agents generation. This class has only one 
* (virtual) method GeneratePrimaries() which is invoked from RunManager
* during the event loop.
*  Note that this class is NOT intended for generating agents by itself. This class should 
*  - have one or more concrete {@link MarketGenerator} classes
*  - set/change properties of generator(s)
*  - pass Event object so that the generator(s) can generate primaries.
*  
* @author Mario Alemi
* @version 0.1 Nov-09	
*/
public interface PrimaryGeneratorAction {
	
	
	public boolean GeneratePrimaries(Event theEvent);
	
}
