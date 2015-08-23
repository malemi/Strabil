/********************************************************************
* License and Disclaimer                                            *
*                                                                   *
* The  Strabil software  is  copyright of the Copyright Holders  of *
* the Strabil Collaboration.  It is provided  under  the terms  and *
* conditions of the Strabil Software License,  included in the file *
* LICENSE and available at  http://www.strabil.org/license .        *
*                                                                   *
* Neither the authors of this software system, nor their employing  *
* institutes,nor the agencies providing financial support for this  *
* work  make  any representation or  warranty, express or implied,  *
* regarding  this  software system or assume any liability for its  *
* use.  Please see the license in the file  LICENSE  and URL above  *
* for the full disclaimer and the limitation of liability.          *
*                                                                   *
* This  code  implementation is the result of  the  scientific and  *
* technical work of the STRABIL collaboration.                      *
* By using,  copying,  modifying or  distributing the software (or  *
* any work based  on the software)  you  agree  to acknowledge its  *
* use  in  resulting  scientific  publications,  and indicate your  *
* acceptance of all terms of the Strabil Software license.          *
********************************************************************/
package org.strabil.reaction;

import org.strabil.market.Agent;
import org.strabil.market.Product;


/**
 * SimpleReaction is a simple kind of consumer reaction when an offer is made to him.
 * 
 * 
 * @author Mario Alemi
 * @version 0.9
 *
 */

public class SimpleReaction implements ReactionToOffer {

	private double willingnessToBuy_;
	//private Agent buyer_;
	//private Agent seller_;
	private Product prod_;

	public SimpleReaction(){
		prod_ = null;
	}

	/**
	 *  Computes the willingness to buy (0-1) as (1/marketValue)*exp(-price/marketValue), 
	 *  then gives a random true (buy)/ false (doesnt buy).
	 */
	public boolean shoots(Agent buyer, Agent seller, Product prod) {
		prod_ = prod;		
		willingnessToBuy_ =(1/prod_.getMarketValue().getValue())* 
		Math.exp( prod_.getPrice().div(prod_.getMarketValue()).getValue()  );

		if(Math.random() < willingnessToBuy_)
			return true;
		return false;
	}
/**
 * As shoots, but returns an array.
 */
	public boolean[] shootsArray(Agent buyer, Agent seller, Product prod, int n) {
		prod_ = prod;		
		willingnessToBuy_ =(1/prod_.getMarketValue().getValue())* 
		Math.exp( prod_.getPrice().div(prod_.getMarketValue()).getValue()  );
		boolean[] out = new boolean[n];
		for(int i=0; i<n; i++){
		if(Math.random() < willingnessToBuy_)
			out[i]= true;
		else out[i]=false;
		}
		return out;
	}


}

