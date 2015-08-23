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
package org.strabil.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.strabil.currencies.Money;
import org.strabil.market.Agent;
import org.strabil.market.MktRelationship;
import org.strabil.market.NodeWrapper;
import org.strabil.market.Product;


/**
 * Event is LINKED up to one Run and down to many Agents. Agents can be created either one 
 * by one or with createLinkedAgentCollection. 
 * Only prop
 *  
 * @author mario alemi
 * @version 0.1
 */
public class Event  implements NodeWrapper {

    private final Node underlyingNode;
    private final GraphDatabaseService graphDb;
    private static final String KEY_ID = "Id";
    //Number of agent connected
	private static final String KEY_COUNTER = "Counter";

    public void setId(Long id){
    	underlyingNode.setProperty(KEY_ID, id);
    }
    
    public Long getId(){
    	return (Long) underlyingNode.getProperty(KEY_ID);
    }
	
	
	public Node getUnderlyingNode(){
	    return underlyingNode;
	}

	public Event(Node underlyingNode){
		
        this.underlyingNode = underlyingNode;
        RunManager rm= RunManager.getInstance();
		this.graphDb = rm.getGraphDb();
	}	
		
	/**
	 * If you want to create many agents and fix the budget use setBudget(n, $$$)
	 * @return Agent created
	 */
	public Agent createAgent(String id){
		
		Node an = graphDb.createNode();
		Agent agent = new Agent(an);
		agent.setIdentifier(id);
		an.createRelationshipTo(this.underlyingNode, SimRelationship.LINKED); 
	    agent.setUId(getNextId());
		return agent;
	}

	/**
	 * If you want to create many agents and fix the budget use setBudget(n, $$$)
	 * @return Agent created
	 */
	public Agent createAgent(){
		
		Node an = graphDb.createNode();
		Agent agent = new Agent(an);
		an.createRelationshipTo(this.underlyingNode, SimRelationship.LINKED); 
	    agent.setUId(getNextId());
		return agent;
	}

	
	
	/**
	 * It creates an array of Agents related to a NodeWrapper with a certain relationship.
	 * This should make easier to create pre-existing loyalty groups. Eg if an Agent has
	 * 1980 Agents in its Silver scheme, we can use
	 * <code>
	 *  createLinkedAgentCollection(1980, supplier, myRelationshipType.SILVER)
	 *  </code>
	 * When an Event creates an ArrayList, it should also take care of setting all
	 * Agent's properties.
	 * 
	 * @param i number of Agent's to be created
	 * @param wrapper_to NodeWrapper to which the new Agent(s) will be related (tipically its supplier)
	 * @param rel kind of relationship
	 * @return the linked ArrayList of Agent(s)
	 */
	public Collection<Agent> createLinkedAgentCollection(long i, String id,  NodeWrapper wrapper_to, RelationshipType rel){

		ArrayList<Agent> al = new ArrayList<Agent>();
		ArrayList<Relationship> arel = new ArrayList<Relationship>();

		for(long c=0;  c < i; c++){
			Agent agent = new Agent(graphDb.createNode());
			agent.setIdentifier(id);
			agent.getUnderlyingNode().createRelationshipTo(this.underlyingNode, SimRelationship.LINKED);
		    agent.setUId(getNextId());
			arel.add(agent.getUnderlyingNode().createRelationshipTo(wrapper_to.getUnderlyingNode(), rel));
			al.add(agent);
		}

		return al;
	}
	

	/**
	 * Create a {@link Product} with an INCOMING relationship  {@link MktRelationship}.OWNS to this.underlyingNode
	 * 
	 * @param identifier
	 * @param price
	 * @param marketValue
	 * @param fixedCost
	 * @param variableCost
	 * @return Product
	 */
	public Product createProduct(String identifier, Money  price, Money marketValue, 
			Money fixedCost, Money variableCost){

		Node pn = graphDb.createNode();
		Product prod = new Product(pn);
		prod.setName(identifier);
		prod.setPrice(price);
		prod.setMarketValue(marketValue);
		prod.setFixedCost(fixedCost);
		prod.setVariableCost(variableCost);
		pn.createRelationshipTo(this.underlyingNode, SimRelationship.LINKED);
		prod.setUId(getNextId() );
		return prod;
	}

	public ArrayList<Product> createProductCollection(long i, String identifier, Money price, Money marketValue, 
			Money fixedCost, Money variableCost){

		ArrayList<Product> ap = new ArrayList<Product>();

		for(long c=0;  c < i; c++){

			Product prod = new Product(graphDb.createNode());
			prod.setName(identifier);
			prod.setPrice(price);
			prod.setMarketValue(marketValue);
			prod.setFixedCost(fixedCost);
			prod.setVariableCost(variableCost);

			this.underlyingNode.createRelationshipTo(prod.getUnderlyingNode(), SimRelationship.LINKED);
			prod.setUId(getNextId() );
			ap.add(prod);
		}

		return ap;
	}

	
	
	
	public Iterable<Agent> getAgents(){
		final List<Agent> agents = new LinkedList<Agent>();
		for (Relationship rel : underlyingNode.getRelationships(SimRelationship.LINKED, Direction.OUTGOING)){
			agents.add(new Agent(rel.getEndNode()));
		}
		return agents;				
	}
	
	private synchronized Long getNextId()
	{
	    Long counter = null;
	    try
	    {
	        counter = ( Long ) underlyingNode.getProperty( KEY_COUNTER );
	    }
	    catch ( NotFoundException e )
	    {
	        // Create a new counter
	        counter = 1L;
	    }
	     
	    underlyingNode.setProperty( KEY_COUNTER, new Long( counter + 1 ) );
	    return counter;
	}

}
