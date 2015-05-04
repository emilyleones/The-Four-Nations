package model.tasks;
import java.util.Set;

import model.Civilization;
import model.exceptions.DisallowedTaskException;
import model.map.Map;
import model.map.ResourceType;
import model.map.Terrain;
import model.structures.AbstractStructure;

/**
 * Builds a structure at a specified location
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class BuildStructureTask extends Task {

	private static final long serialVersionUID = 2155606028566751830L;
	private AbstractStructure structureToBuild;

	/**
	 * Creates a task that results in a structure being added to the map
	 * @param work The amount of work required to complete this task (number of game ticks)
	 * @param locWorker The location the worker working on this task will be
	 * @param locTask The location that the task will be located at
	 * @param map The map that this task takes place on
	 * @param structure The structure being built during this task
	 */
	public BuildStructureTask(int work, Map map, AbstractStructure structure ) {
		super(work, structure.getLocation(), map);
		this.structureToBuild = structure;
		int location = structure.getLocation();

		if( ! isValidStructureLocation() ) {
			Set<Terrain> types = structure.getValidTerrainTypes();
			String validTypes = "";
			for( Terrain t : types ) validTypes += t.name() + ",";
			if( validTypes.endsWith("," ) ) validTypes = validTypes.substring(0, validTypes.lastIndexOf("," ) );
			String message = "The location " + location + " is not valid. " + structure.getName() + " requires " +
					validTypes + " but the cell is " + civ.getMap().getCell(location).getTerrain().name();
			throw new DisallowedTaskException( this, message );
		}

		Civilization civ = Civilization.getInstance();
		int resourceAmount = structureToBuild.getAmountOfResourceUsed();
		ResourceType typeUsed = structureToBuild.getResourceTypeUsed();

		if( civ.getResourceAmount( typeUsed ) < resourceAmount ) {
			String message = "This building requires " + resourceAmount + " " + typeUsed.name() + " but you only have " +
					civ.getResourceAmount(typeUsed) + ".";
			throw new DisallowedTaskException( this, message );
		}

		if(!map.getCell(location).isAccessible()) {
			String message = "The cell at that location has not yet been cleared!";
			throw new DisallowedTaskException( this, message );
		}
	}

	private boolean isValidStructureLocation() {
		return structureToBuild.getValidTerrainTypes().contains( civ.getMap().getCell(locationOfTask).getTerrain() );
	}

	/**
	 * {@inheritDoc}
	 * <p>Adds the structure to the map and to the civilization</p>
	 */
	@Override public void performAction() {
		Civilization civ = Civilization.getInstance();
		int resourceAmount = structureToBuild.getAmountOfResourceUsed();
		ResourceType typeUsed = structureToBuild.getResourceTypeUsed();

		//Check that the user has enough resources to build the structure
		if( civ.getResourceAmount( typeUsed ) >= resourceAmount) {
			map.getCell(super.locationOfTask).addStructure(this.structureToBuild);
			map.makeInaccesible(super.locationOfTask);
			civ.addStructure( this.structureToBuild );
			civ.pollResource( typeUsed, resourceAmount );
			this.isDone = true;
		}

		//Throw an exception if not
		else {
			String message = "This building requires " + resourceAmount + " " + typeUsed + " but you only have " +
					civ.getResourceAmount(typeUsed) + ".";
			throw new DisallowedTaskException( this, message );
		}
	}
}
