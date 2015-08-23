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

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.strabil.currencies.Money;
import org.strabil.utils.DoTest;

/**
 * Products have (at the moment) string nameidentifier,Price and Value. 
 * 
 * 
 * @author Mario Alemi
 * @version 0.1 Nov-09 created
 *
 */
public class Product  implements NodeWrapper {

@Deprecated
	public Product(){
		this.underlyingNode = null;
	}
	 
	 /**
	  * You may want to set id and price, values immediately after. If you create the products through an <@link Agent> 
	  * you can use CreateProduct/List, so you can also set the relationship of ownership.
	  * 
	  * 
	  * @param underlyingNode
	  */
	public Product(Node underlyingNode){

			
		this.underlyingNode = underlyingNode;
	}

	private final Node underlyingNode;
	private static final String KEY_NAME = "Name";
	private static final String KEY_UID = "UId"; //Manufacturer + Production counter
    private static final String KEY_CURRENCY = "Currency";
	private static final String KEY_PRICE = "Price";
	private static final String KEY_FCOST = "Fixed Cost";
	private static final String KEY_VCOST = "Variable Cost";
	private static final String KEY_MKT_VALUE = "Market Value";

	/**
	 * 
	 * 
	 * @param name
	 */
	public void setName( String name )    {
		underlyingNode.setProperty( KEY_NAME, name );
	}
	public String getName()    {
		return ( String ) underlyingNode.getProperty( KEY_NAME );
	}

	public void setCurrency(String cur){
		underlyingNode.setProperty(KEY_CURRENCY, cur);
	}
	
	public String getCurrency(){
		return (String) underlyingNode.getProperty(KEY_CURRENCY, null);
	}

	public void setUId( Long UId)    {
		underlyingNode.setProperty( KEY_UID, UId);
	}
	public Long getUId() {
		return ( Long ) underlyingNode.getProperty( KEY_UID);
	}

	public void setFixedCost( Money m)    {
		String cur = (String) this.getCurrency(); //Old value if already set, otherwise null
		if(cur==null) this.setCurrency(m.getCurrency() );

		else{ 
			DoTest.require(cur == m.getCurrency(), "Product.setPrice: Trying to set budget with the wrong currency.");//exception if previously set value differs
			this.setCurrency(cur);
		}
			underlyingNode.setProperty( KEY_FCOST, m.getValue() );
	}
	public Money getFixedCost()    {
		return new Money(this.getCurrency(), (Double) underlyingNode.getProperty( KEY_FCOST ));
	}

	public void setPrice( Money m)    {
		String cur = (String) this.getCurrency(); //Old value if already set, otherwise null
		if(cur==null) this.setCurrency(m.getCurrency() );

		else{ 
			DoTest.require(cur == m.getCurrency(), "Product.setPrice: Trying to set budget with the wrong currency.");//exception if previously set value differs
			this.setCurrency(cur);
		}
		underlyingNode.setProperty( KEY_PRICE, m.getValue() );
	}
	public Money getPrice()    {
		return new Money(this.getCurrency(), (Double) underlyingNode.getProperty( KEY_PRICE ));	
		}

	public void setVariableCost( Money m)    {
		String cur = (String) this.getCurrency(); //Old value if already set, otherwise null
		if(cur==null) this.setCurrency(m.getCurrency() );
		else{ 
			DoTest.require(cur == m.getCurrency(), "Product.setPrice: Trying to set budget with the wrong currency.");//exception if previously set value differs
			this.setCurrency(cur);
		}
		underlyingNode.setProperty( KEY_VCOST, m.getValue() );
	}
	public Money getVariableCost()    {
		return new Money(this.getCurrency(), (Double) underlyingNode.getProperty( KEY_VCOST ));	
	}

	public void setMarketValue( Money m)    {
		String cur = (String) this.getCurrency(); //Old value if already set, otherwise null
		if(cur==null) this.setCurrency(m.getCurrency() );
		else{ 
			DoTest.require(cur == m.getCurrency(), "Product.setPrice: Trying to set budget with the wrong currency.");//exception if previously set value differs
			this.setCurrency(cur);
		}
		underlyingNode.setProperty( KEY_MKT_VALUE, m.getValue() );
	}
	
	public Money getMarketValue(){
		return new Money(this.getCurrency(), (Double) underlyingNode.getProperty( KEY_MKT_VALUE ));	
	}
	public Node getUnderlyingNode()
	{
		return underlyingNode;
	}

	/**
	 * 
	 * @return Node of the owner
	 * @throws NotFoundException if more than one ownership relationship is found
	 */
	public Node getOwnerNode() {
		Relationship r = underlyingNode.getSingleRelationship(
				MktRelationship.OWNS, Direction.OUTGOING );

		DoTest.require((r != null),"Product "+this.getName()+" has no owner.");
		
		Node agentNode = r.getEndNode(); // throws Runtime exeption if more than one relationthip are found

		return agentNode;


	}
/**
 * For debugging
 */
	public void printInfo(){
		//SLogger.logger.debug("\nStart Info");
		System.out.println("START Product Info");
		Node n = this.getUnderlyingNode();
		for(String key: n.getPropertyKeys())
			System.out.println("Node property "+key+" = "+n.getProperty(key));
		for(Relationship rr: n.getRelationships()){
			System.out.println("Relationship: "+rr.getType());
			for(String key: rr.getPropertyKeys())
				System.out.println("Relationship property "+key+" = "+rr.getProperty(key));
		}
		Relationship r = n.getSingleRelationship(MktRelationship.OWNS, Direction.OUTGOING);
		if(r == null)
		System.out.println("No Relationship on this node! ");
		System.out.println("STOP Info \n");

		
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj instanceof Product )
		{
			return getUnderlyingNode().equals(
					( ( Product ) obj ).getUnderlyingNode() );
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return getUnderlyingNode().hashCode();
	}
}

