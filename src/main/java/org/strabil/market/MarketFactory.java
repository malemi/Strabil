package org.strabil.market;

import java.util.Collection;

public interface MarketFactory {
 
	void addProductSet(ProductSet productSet);
	
	void resetAgentSet();
	
	void resetProductSet();
	
	/**
	 * Outputs the agents created following the distribution of AgentSet.	
	 * 
	 * @return Collection of customers of Agent seller (possibly linked to a private var Event)
	 */
	Collection<Agent> createAgents(Agent agent); 

	/**
	 * Outputs the agents created following the distribution of ProductSet.	
	 * 
	 * @return Collection of Products of Agent seller (possibly linked to a private var Event)
	 */
	Collection<Product> createProducts(Agent agent);
	
	/**
	 * @param ll
	 */
	void addAgentSet(AgentSet ll);

}
