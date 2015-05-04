package model.tasks;

import java.util.ArrayList;

import model.Civilization;
import model.exceptions.DisallowedTaskException;
import model.map.Map;
import model.map.Resource;
import model.structures.AbstractStructure;


/**
 * The task that a unit performs when they are collecting some resource
 * located on the map
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class CollectResourceTask extends Task{

	private static final long serialVersionUID = -5974294099911726904L;
	private ArrayList<Integer> storedCells = new ArrayList<Integer>();

	public CollectResourceTask(int work, int locTask, Map map) {
		super(work, locTask, map);

		// Find containers on the map
		for(AbstractStructure struct: Civilization.getInstance().getStructures())
		{
			if(struct.isAContainer())
				storedCells.add(struct.getLocation());
		}


		if( storedCells.size() == 0 ) {
			String message = "Collecting resources required a stockpile to store them in!";
			throw new DisallowedTaskException( this, message );
		}
	}

	/**
	 * Stores the resource in a stockpile on the map
	 */
	@Override public void performAction() {
		Map m = Civilization.getInstance().getMap();
		Resource res = m.getCell(locationOfTask).getResource();
		m.getCell(locationOfTask).removeResource();
		m.makeAccessible(locationOfTask);



		int loc = storedCells.get(0);
		StoreResourceTask srt = new StoreResourceTask(1, loc, m, res );
		this.unit.setCurrentTask(srt);
	}



}
