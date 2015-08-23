package org.strabil.market;

import org.neo4j.graphdb.Relationship;

public class WeightedRelationship implements RelationshipWrapper {

	private Relationship rel;
	private static final String KEY_WEIGHT = "Weight";
	
	
	@Override
	public Relationship getUnderlyingRelationship() {
		return rel;
	}

	public void setWeight(Long weight){		
		rel.setProperty(KEY_WEIGHT, weight);
	}
	
	public Long getWeight(){
		return (Long) rel.getProperty(KEY_WEIGHT);
	}
	
	
	public WeightedRelationship(Relationship rel){
		this.rel = rel;
	}

}
