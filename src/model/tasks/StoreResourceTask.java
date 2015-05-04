package model.tasks;
import model.map.*;
import model.Civilization;

/**
 * Stores a resource in a stockpile
 * @author Christopher Chapline, James Fagan, Michelle Yung, Emily Leones
 *
 */
public class StoreResourceTask extends Task {

	private static final long serialVersionUID = -142993928361461443L;
	private Resource resource;
	
	public StoreResourceTask(int work, int locTask, Map map, Resource resource) {
		super(work, locTask, map);
		this.resource = resource;
	}

	/**
	 * Stores the resource in the Civilizations reserves
	 */
	@Override public void performAction() {
		Civilization.getInstance().storeResource( this.resource );
		this.isDone = true;
	}

}
