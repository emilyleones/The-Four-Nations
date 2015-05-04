package model.structures;
import model.map.ResourceType;
import model.map.Terrain;

/**
 * A table is located in the kitchen and provides the unit with a location
 * to eat food.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung.
 *
 */
public class Table extends AbstractStructure{

	private static final long serialVersionUID = 844649945126996075L;

	public Table(int location, String name, ResourceType resourceUsed ) {
		//TODO: Determine how much wood is used to build a table
		super(location, 1, name, resourceUsed, Terrain.kitchen);
	}
	
	/**
	 * Since this structure provides food, this now returns true.
	 */
	@Override public boolean providesFood() {
		return true;
	}

}
