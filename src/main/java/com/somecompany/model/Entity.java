package com.somecompany.model;

import java.util.List;

import lombok.Data;

/**
 * Model for an entity in credit limit calculations.
 * 
 * @author patrick
 */
@Data
public class Entity {

	private String name;

	private String parent;

	private Integer limit;

	private Integer utilization;

	// The list of all sub-entities (including non-direct ones) for this entity
	private List<String> listOfSubEntities;

	// The sum of utilization of this entity plus that of all sub-entities
	private Integer totalUtilization;

	// The sum of limit of all all sub-entities (not including limit of this entity)
	private Integer totalLimitOfSubEntities;
}
