package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import model.map.Map;
import model.map.ResourceType;
import model.structures.AbstractStructure;
import model.tasks.Task;
import model.units.Unit;

//TODO: Create and Serialize game runner
/**
 * A persistant state for the Civilization singleton object. This allows
 * for the game state to be saved to a file and reovered at a later date
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class CivilizationState implements Serializable {

	private static final long serialVersionUID = 50960125111940890L;
	private final Queue<Task> taskQueue;
	private final ArrayList<Unit> units, unitsToKill;
	private final ArrayList<AbstractStructure> structures;
	private final HashMap<ResourceType, Integer> globalResourcePool;
	private final Map map;
	private final Tribe tribe;
	private final String gameName;

	/**
	 * Generates the civilization state based upon the provided civilization
	 */
	public CivilizationState( Civilization civ ) {
		this( civ.getTaskQueue(), civ.getUnits(), civ.getUnitsToKill(), civ.getStructures(), 
				civ.getGlobalResourcePool(), civ.getMap(), civ.getTribe(), civ.getGameName() );
	}

	@SuppressWarnings("unchecked")
	/**
	 * Generates the civilization state based upon the data that will be saved
	 * @param taskQueue The tasks currently waiting to be performed
	 * @param units The units currently located on the map
	 * @param unitsToKill The units that are supposed to die next tick
	 * @param structures The structures that exist on the map
	 * @param globalResourcePool The collection of resources in the game
	 * @param map The map that the game is being played on
	 * @param tribe The tribe that the user is playing as
	 */
	public CivilizationState( Queue<Task> taskQueue, ArrayList<Unit> units, ArrayList<Unit> unitsToKill, 
			ArrayList<AbstractStructure> structures, HashMap<ResourceType, Integer> globalResourcePool, Map map,
			Tribe tribe, String gameName ) {
		this.taskQueue = new LinkedList<>( taskQueue );
		this.units = (ArrayList<Unit>) units.clone();
		this.unitsToKill = (ArrayList<Unit>) unitsToKill.clone();
		this.structures = (ArrayList<AbstractStructure>) structures.clone();
		this.globalResourcePool = (HashMap<ResourceType, Integer>) globalResourcePool.clone();
		this.map = map;
		this.tribe = tribe;
		this.gameName = gameName;
	}

	public Queue<Task> getTaskQueue() {
		return taskQueue;
	}

	public ArrayList<Unit> getUnits() {
		return units;
	}

	public ArrayList<Unit> getUnitsToKill() {
		return unitsToKill;
	}

	public ArrayList<AbstractStructure> getStructures() {
		return structures;
	}

	public HashMap<ResourceType, Integer> getGlobalResourcePool() {
		return globalResourcePool;
	}

	public Map getMap() {
		return map;
	}

	public Tribe getTribe() {
		return tribe;
	}
	
	public String getGameName() {
		return this.gameName;
	}
}
