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



import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.strabil.market.NodeWrapper;


/**
 * Every Run, managed by {@link RunManager}, has an ID and is LINKed to its {@link Event}s.
 * 
 * @author Mario Alemi
 * @version 0.1 Nov-09 
 */
public class Run  implements NodeWrapper {

    private final Node underlyingNode;
    private final GraphDatabaseService graphDb;
    private static final String KEY_ID = "Id";
    //Number of event connected
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

	public Run(Node underlyingNode){
		
        this.underlyingNode = underlyingNode;
        RunManager rm= RunManager.getInstance();
		this.graphDb = rm.getGraphDb();
	}	
		
	/**
	 * 
	 * @return the Event
	 */

	public Event createEvent(){
		
		Node en = graphDb.createNode();
		Event evt = new Event(en);
		en.createRelationshipTo(this.underlyingNode, SimRelationship.LINKED);
	    evt.setId(getNextId());
		return evt;
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
