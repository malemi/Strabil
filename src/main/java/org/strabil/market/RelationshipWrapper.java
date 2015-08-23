package org.strabil.market;

import org.neo4j.graphdb.Relationship;

public interface RelationshipWrapper {
	public Relationship getUnderlyingRelationship();
}
