package model.structures;

import model.map.ResourceType;
import model.map.Terrain;

/**
 * A bed is located in the barracks and allows units to sleep
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung.
 *
 */
public class Bed extends AbstractStructure {

	private static final long serialVersionUID = 4304144076281330676L;

	public Bed(int location, String name, ResourceType resourceUsed ) {
		//TODO: Determine how much wood is used to build a bed
		super(location, 1, name, resourceUsed, Terrain.barracks );
	}

	/**
	 * Since a unit can sleep on the bed, this now returns true
	 */
	@Override public boolean providesBed() {
		return true;
	}
}
