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

package org.strabil.market;

import org.strabil.currencies.Money;


/**
 * An AgentSet record all the property of a set of Agent's. A AgentFactory would eventyally use AgentSet's 
 * for producing Agents: it would for instance instantiate NumberAgents Agents wit a  
 * random budget[0] btw MinBudget and MaxBudget and average AveBudget.
 * 
 * @author Mario Alemi
 *
 */
public interface AgentSet {
	public void setAveBudget(Money[] aveBudget);

	public void setAveBudget(int i, Money aveBudget);
	public Money getAveBudget(int i);

	
	public void setPeriod(long period);
	public long getPeriod() ;
	public void setMinBudget(int i, Money minBudget) ;
	public Money getMinBudget(int i) ;
	public void setMaxBudget(int i, Money maxBudget) ;
	public Money getMaxBudget(int i) ;

	public void setMinBudget(Money[] minBudget);
	public Money[] getMinBudget() ;
	public void setMaxBudget(Money[] maxBudget) ;
	public Money[] getMaxBudget() ;

	public void setNumberAgents(int numberAgents) ;
	public int getNumberAgents() ;

	/**
	 * The Program Name is going evetually  be a property of the relationshiop with a seller (eg, in case of Loyalty scheme, it could be Gold, Silver etc). 
	 * @param programName
	 */
	public void setProgramName(String programName);
	/**
	 * Eg Gold, Silver etc
	 */ 
	public String getProgramName() ;
	
	/**
	 * The identifier of its Agents (RESELLER, FINAL_CUSTOMER etc)
	 * @param identifier
	 */
	public void setIdentifier(MarketSectorType identifier);
	/**
	 * The identifier of its Agents (RESELLER, FINAL_CUSTOMER etc)
	 */
	public MarketSectorType getIdentifier();

}
