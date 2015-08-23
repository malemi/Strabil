/**
 * 
 */
package org.strabil.reaction;

import org.strabil.market.Agent;
import org.strabil.market.Product;

//import utils.Money;

/**
 * @author Mario Alemi
 * 
 *
 */

public interface ReactionToOffer  {
	
/**
 * 
 * @param buyer
 * @param seller
 * @param prod
 * @return it buys/it doesnt
 */
	public boolean shoots(Agent buyer, Agent seller, Product prod);
	
	/**
	 * 
	 * @param buyer
	 * @param seller
	 * @param prod
	 * @return it buys/it doesnt[]
	 */
	public boolean[] shootsArray(Agent buyer, Agent seller, Product prod, int n);
	
	
}
