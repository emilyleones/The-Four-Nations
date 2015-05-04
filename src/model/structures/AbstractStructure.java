package model.structures;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import model.map.ResourceType;
import model.map.Terrain;

/**
 * Superclass to all in-game structures. A structure is a building
 * that is located within a room on the map is provides some sort
 * of functionality to the civilization. This functionality could be
 * stat bonuses, resource storage, etc.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public abstract class AbstractStructure implements Serializable {

	private static final long serialVersionUID = -2955392819275955126L;
	private int location, amountOfResourceUsed;
	private ResourceType resourceUsed;
	private Set<Terrain> validTerrainTypes;
	private String name;
	
	
	/**
	 * Creates an abstract structure with the following attributes
	 * @param location The location of this structure on the game map
	 * @param amountOfResourceUsed The number of resources that this structure will require
	 * @param name The name of the structure
	 * @param resourceUsed The type of resource required to build this structure
	 * @param validTerrainTypes The types of terrain that this structure can built on
	 */
	public AbstractStructure(int location, int amountOfResourceUsed, String name, ResourceType resourceUsed, Terrain... validTerrainTypes) {
		this.location = location;
		this.amountOfResourceUsed = amountOfResourceUsed;
		this.name = name;
		this.resourceUsed = resourceUsed;
		this.validTerrainTypes = new HashSet<>( Arrays.asList( validTerrainTypes ) );
	}
	
	/**
	 * Determines whether or not this structure can provide in game
	 * agents with Food
	 */
	public boolean providesFood() {
		return false;
	}
	
	/**
	 * Determines whether or not this structure can provide in game agents
	 * with a means to sleep
	 */
	public boolean providesBed() {
		return false;
	}
	
	/**
	 * Determines whether or not this structure can provide in game agents
	 * with a means to drink
	 */
	public boolean providesDrink() {
		return false;
	}
	
	/**
	 * Determines whether or not this structure can provide in game agents
	 * with a means to storage
	 */
	public boolean isAContainer() {
		return false;
	}

	public int getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getResourceTypeUsed() {
		return resourceUsed;
	}
	
	public int getAmountOfResourceUsed() {
		return this.amountOfResourceUsed;
	}

	public void setResourceTypeUsed(ResourceType resourceUsed) {
		this.resourceUsed = resourceUsed;
	}
	
	public Set<Terrain> getValidTerrainTypes() {
		return this.validTerrainTypes;
	}
}
