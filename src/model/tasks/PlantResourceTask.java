package model.tasks;

import model.exceptions.DisallowedTaskException;
import model.map.Map;
import model.map.Resource;

/**
 * Plants a resource at a specified location
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class PlantResourceTask extends Task {
	private static final long serialVersionUID = 1752256657303846877L;
	private Resource resourceToPlant;
	
	/**
	 * Creates a task that results in a resource being added to the map
	 * @param location the location where the resource will go
	 * @param map The map that this task takes place on
	 * @param resource The resource being added
	 */
	public PlantResourceTask(int location, Map map, Resource resource) {
		super(10, location, map);
		this.resourceToPlant = resource;
		
		if(!map.getCell(location).isAccessible() ) {
			String message = "The location is not cleared, so a resource cannot be planted there.";
			throw new DisallowedTaskException( this, message );
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>Adds the resource to the map</p>
	 */
	@Override public void performAction() {
		map.getCell(super.locationOfTask).setResource(resourceToPlant);
		map.makeInaccesible(super.locationOfTask);
		this.isDone = true;
		
	}
}