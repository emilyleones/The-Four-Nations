package model.map;
/**
 * The different types of terrain that can exist on the map,
 * along with a symbol that can be used in their toString
 * @author Emily Leones, Michelle Yung, Christopher Chapline, James Fagan
 *
 */
public enum Terrain {

	//Enumeration declarations
	plains(true, " "), water(false, "~"), stockpile( true, "C"), kitchen(true, "K"), barracks( true, "B"), coast(true, "."), rockyPlains( false, "R");

	//Instance variables
	private boolean accessible;
	private String symbol;

	/**
	 * Creates a terrain type that contains data about a specific location
	 * @param accessible True if the the terrain is accessible to a unit (can a unit walk on it, etc).
	 * @param symbol The symbol shown in the toString for this item of terrain
	 */
	Terrain(boolean accessible, String symbol) {
		this.accessible = accessible;
		this.symbol = symbol;
	}

	/**
	 * Returns whether or not this terrain is accessible. This determines if
	 * the terrain can be walked on, built on, etc.
	 * @return True if the terrain can be walked on, built on, etc; false otherwise.
	 */
	public boolean isAccessible() {
		return accessible;
	}

	/**
	 * The ASCII representation of this terrain type
	 * @return The String that represents this terrain type
	 */
	public String toString() {
		return this.symbol;
	}
	

}
