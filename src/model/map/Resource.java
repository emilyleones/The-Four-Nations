package model.map;
/**
 * A resource is stored in a cell and can be collected and used
 * in tasks undertaken by the units
 * @author Emily Leones, Michelle Yung, Christopher Chapline, James Fagan
 *
 */
public enum Resource {

	tree( "#", ResourceType.wood, 1 ), stone( "^", ResourceType.stone, 1 ), goldMine( "g", ResourceType.gold, 10 ), garden( "f", ResourceType.food, 150 );

	private String symbol;
	private ResourceType type;
	private int resourceValue;

	Resource( String res, ResourceType type, int resourceValue ) {
		this.symbol = res;
		this.type = type;
		this.resourceValue = resourceValue;
	}

	public String toString() {
		return this.symbol;
	}
	
	public ResourceType getResourceType() {
		return type;
	}

	public int getResourceValue() {
		return this.resourceValue;
	}
}
