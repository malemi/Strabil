package org.strabil.market;

import org.neo4j.graphdb.RelationshipType;

public enum MktRelationship implements RelationshipType {

	OWNS, //An agent owns only one product 
	SOLD, //An agent used to own, now has the money in the budget from this product
	EVALUATES, //An agent will evaluate if buy and pass to OWNS or kill the connection....
	IS_CUSTOMER, //An agent bought from the other Ðat anytime
	AGENT
}
