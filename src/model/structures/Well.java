package model.structures;

import model.map.ResourceType;
import model.map.Terrain;

/**
 * A well is located ourdoors on a plains tile and provides units
 * with the ability to drink
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung.
 *
 */
public class Well extends AbstractStructure {

	private static final long serialVersionUID = -2357473624902642107L;

	public Well(int location, String name, ResourceType resourceUsed) {
		//TODO: Determine how much stone is used to build a well
		super(location, 1, name, resourceUsed, Terrain.plains);
	}

	/**
	 * Since a well provides a way to drink, this now returns true.
	 */
	@Override public boolean providesDrink() {
		return true;
	}

}
