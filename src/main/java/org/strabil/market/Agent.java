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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.Traverser.Order;
import org.strabil.currencies.Money;
import org.strabil.manager.RunManager;
import org.strabil.reaction.ReactionToOffer;
import org.strabil.utils.DoTest;

/** 
 * 
 * @author Mario Alemi
 * @version 0.1 Nov-09 created
 * 
 */
public class Agent implements NodeWrapper {

	public static final int nBudgets = 9;

	private final Node underlyingNode;
	private final GraphDatabaseService graphDb;
	private static final String KEY_IDENTIFIER = "Identifier";
	private static final String KEY_UID = "UId";
	private static final String KEY_CURRENCY = "Currency";
	private static final String[] KEY_BDG = new String[nBudgets];


	//Number of products manufactured
	private static final String KEY_COUNTER = "Counter";

	public void setCurrency(String cur){
		String sc = this.getCurrency();
		if(sc==null) underlyingNode.setProperty(KEY_CURRENCY, cur);
		else DoTest.require(sc==cur, "Agent.setCurrency: trying to set wrong currency."); //Just check 
	}

	public String getCurrency(){
		return (String) underlyingNode.getProperty(KEY_CURRENCY, null);
	}

	/**
	 * Because Neo4j node.setProperty only accept Java native objects, we actually store
	 * m.value() and make a check that the currency has been previously set and has not been
	 * changed.
	 * 
	 * @param i
	 * @param m 
	 */
	public void setBudget(int i, Money m){

		String cur = (String) this.getCurrency(); //Old value if already set, otherwise null

		if(cur==null) this.setCurrency(m.getCurrency() );
		else {
			DoTest.require(cur == m.getCurrency(), "Agent.setBudget: Trying to set budget with the wrong currency. "+cur+" and "+m.getCurrency());//exception if previously set value differs		
			this.setCurrency(cur);
		}
		underlyingNode.setProperty(KEY_BDG[i], m.getValue());
	}

	/**
	 * Sets its i-th budget to a uniformly random value btw min and max
	 */
	public void setRandomBudget(int i, Money min, Money max){
		DoTest.require(min.getCurrency() == max.getCurrency(), "Agent.setRandomBudget: min and max have different currencies.");

		this.setBudget(i, new Money(min.getCurrency(), Math.random()*( max.getValue() - min.getValue())+min.getValue()));

	}


	public Money getBudget(int i){
		double value = (Double) underlyingNode.getProperty(KEY_BDG[i], 0.0);
		return new Money(this.getCurrency(),value);
	}

	public void setBudgets( Money[] m){
		for(int i=0;i<m.length;i++){
			if(m[i] != null) {
				this.setBudget(i, m[i]);
			}
		}
	}

	public Money[] getBudgets(){
		Money[] m = new Money[Agent.nBudgets];
		for(int i=0;i<Agent.nBudgets;i++){
			m[i] = this.getBudget(i);
		}
		return m;
	}

	public void addToBudget(int i, Money m){
		Money tm = this.getBudget(i);
		if(tm.getValue() == 0){
			this.setBudget(i, m);
		} else {
			tm = tm.add(m);
			this.setBudget(i, tm);
		}
	}

	public void subFromBudget(int i, Money m){
		Money tm = this.getBudget(i);
		tm = tm.sub(m);
		this.setBudget(i, tm);
	}

	public String getIdentifier()    {
		return ( String ) underlyingNode.getProperty( KEY_IDENTIFIER, "No Identifier" );
	}

	public void setIdentifier( String id)    {
		if(this.getIdentifier() !="No Identifier") DoTest.warn("Changing identifier of Agent "+this.getIdentifier());
		underlyingNode.setProperty( KEY_IDENTIFIER, id);
	}
	public Long getUId() {
		return ( Long ) underlyingNode.getProperty( KEY_UID );
	}


	public void setUId( Long UId)    {
		underlyingNode.setProperty( KEY_UID, UId);
	}

	/**
	 * You immediately should set Identifier and Currency (if you are using any budget).
	 * 
	 * @param underlyingNode
	 */

	public Agent(Node underlyingNode){

		this.underlyingNode = underlyingNode;
		RunManager rm= RunManager.getInstance();
		this.graphDb = rm.getGraphDb();	
		for(int i=0; i<KEY_BDG.length; i++){
			KEY_BDG[i]="Budget"+i;
		}

	}	

	public Agent(){
		System.out.println("PPPPIIPPPPPOOOO");
		RunManager rm= RunManager.getInstance();
		this.graphDb = rm.getGraphDb();	
		this.underlyingNode = this.graphDb.createNode();
		for(int i=0; i<KEY_BDG.length; i++){
			KEY_BDG[i]="Budget"+i;
		}

	}	

	public Node getUnderlyingNode(){
		return underlyingNode;
	}

	public Iterable<Product> getOwnedProducts(){
		//		SLogger.logger.debug("getOwnedProducts");
		final List<Product> products = new LinkedList<Product>();
		for (Relationship rel : underlyingNode.getRelationships(MktRelationship.OWNS, Direction.INCOMING)){
			products.add(new Product(rel.getStartNode()));
		}
		return products;				
	}

	/**
	 * For debugging
	 */
	public void printInfo(){
		//SLogger.logger.debug("\nStart Info");
		System.out.println("START Agent Info \n");
		Node n = this.getUnderlyingNode();
		for(String key: n.getPropertyKeys())
			System.out.println("Node property "+key+" = "+n.getProperty(key));
		for(Relationship rr: n.getRelationships()){
			System.out.println("Relationship: "+rr.getType());
			for(String key: rr.getPropertyKeys())
				System.out.println("Relationship property "+key+" = "+rr.getProperty(key));
		}

		System.out.println("STOP Info \n");


	}


	/**
	 * Delete old ownership (without asking the old owner!) and create new
	 * relationship of ownership
	 * 
	 * @param p
	 */
	public void buys(Product p) {

		Node pn = p.getUnderlyingNode();

		//Get the node of the to-be former owner
		Node formerOwnerNode = p.getOwnerNode();
		Agent formerOwner = new Agent(formerOwnerNode);
		//Create relationship supplier-customer if doesnt exist
		formerOwner.addCustomer(this);
		//this.printInfo();
		//Delete old ownership
		pn.getSingleRelationship(MktRelationship.OWNS, Direction.OUTGOING).delete(); 
		//Create new ownership
		pn.createRelationshipTo(underlyingNode, MktRelationship.OWNS);
	}

	/** 
	 * @checked
	 * Create relationship supplier-customer with weight 1 if doesnt exist, otherwise add +1 to weight
	 * @param customer
	 */

	public void addCustomer(Agent customer){

		boolean already = false;

		for(Relationship r: customer.getUnderlyingNode().getRelationships(MktRelationship.IS_CUSTOMER, Direction.OUTGOING)){
			//			System.out.println("Seller: "+r.getEndNode().getProperty("Identifier"));

			if(this.underlyingNode.getId() == r.getEndNode().getId()){
				WeightedRelationship curel = new WeightedRelationship(r);
				curel.setWeight( curel.getWeight()+1);
				already = true;
				//				System.out.println("WEIGHT "+curel.getWeight());
				break;
			}
		}

		//		System.out.println("ALREADY "+already);

		if(!already){
			//			System.out.println("CREATE REL CUSTOMER");
			Relationship rel = customer.underlyingNode.createRelationshipTo(this.underlyingNode, MktRelationship.IS_CUSTOMER );
			WeightedRelationship curel = new WeightedRelationship(rel);
			curel.setWeight(1L);
		}


	}

	public Iterable<Node> getCustomer(){

		return this.underlyingNode.traverse( Order.BREADTH_FIRST,
				StopEvaluator.DEPTH_ONE, ReturnableEvaluator.ALL_BUT_START_NODE,
				MktRelationship.IS_CUSTOMER, Direction.INCOMING );
	}

	/**
	 * Using a given reaction, this buyer decides if it will buy or not product from seller
	 * 
	 * @param product
	 * @param seller
	 * @param reaction
	 * @return boolean decision
	 */
	public boolean evaluates(Product product, Agent seller, ReactionToOffer reaction){
		return reaction.shoots(this, seller, product);
	}


}
