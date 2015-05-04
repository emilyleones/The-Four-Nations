package model.structures;

import model.map.ResourceType;
import model.map.Terrain;

public class Container extends AbstractStructure
{

	private static final long serialVersionUID = -8557225426667302295L;

	public Container(int location) {
		
		super(location, 1, "Container", ResourceType.wood, Terrain.values());
		
	}
	
	@Override public boolean isAContainer()
	{
		return true;
	}

}
