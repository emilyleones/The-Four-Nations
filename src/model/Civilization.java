package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JOptionPane;

import model.map.Cell;
import model.map.Map;
import model.map.Resource;
import model.map.ResourceType;
import model.map.Terrain;
import model.structures.AbstractStructure;
import model.tasks.Task;
import model.units.BasicUnit;
import model.units.Unit;



/**
 * Coordinator for the management of settlers. This class is a singleton.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class Civilization {

	private Queue<Task> taskQueue;
	private ArrayList<Unit> units, unitsToKill, unitsToBeBorn;
	private ArrayList<AbstractStructure> structures;
	private HashMap<ResourceType, Integer> globalResourcePool;
	private static Civilization instance = new Civilization( 2 );
	private Map map;
	private int cols = 70; //70 by default
	private String gameName;
	private Tribe tribe;

	/**
	 * Creates a civilization with a specified number of starting units
	 * @param numberOfStartingUnits
	 */
	private Civilization( int numberOfStartingUnits ) {
		this.taskQueue = new LinkedList<Task>();
		this.units = new ArrayList<Unit>();
		this.unitsToKill = new ArrayList<>();
		this.unitsToBeBorn = new ArrayList<>();
		for(int i = 0; i < numberOfStartingUnits; i++)
		{
			units.add(new BasicUnit("U", 500, 500, cols));
		}	
		this.structures = new ArrayList<AbstractStructure>();
		this.globalResourcePool = new HashMap<ResourceType, Integer>();
		for( ResourceType rt : ResourceType.values() ) {
			this.globalResourcePool.put(rt, 0 );
		}
		
		this.globalResourcePool.put( ResourceType.wood, 3 );
		this.globalResourcePool.put( ResourceType.food, 500 );
		this.globalResourcePool.put( ResourceType.gold, 10 );
	}

	/**
	 * Loads the given civilization state as the current game state. 
	 * @param state The persistant CivilizationState to load from
	 */
	public void parseCivilizationState( CivilizationState state ) {
		this.globalResourcePool = state.getGlobalResourcePool();
		this.taskQueue = state.getTaskQueue();
		this.structures = state.getStructures();
		this.units = state.getUnits();
		this.unitsToKill = state.getUnitsToKill();
		this.unitsToBeBorn = new ArrayList<Unit>();
		this.map = state.getMap();
		this.tribe = state.getTribe();
		this.gameName = state.getGameName();
	}
	
	public void reinitCivilization( Map map, Tribe tribe, String name ) {
		instance = new Civilization(2);
		instance.setMap( map );
		instance.setTribe( tribe );
		instance.setGameName( name );
	}

	/**
	 * Updates all ingame entities and handles unit death
	 */
	public void update()
	{
		if(units.size() == 0){
			JOptionPane.showMessageDialog( null, "Your units have died!" );
			System.out.println("YOU HAVE BEEN DEFEATED!"); 
			System.exit(0);
		}	
		if(units.size() >= 10){
			JOptionPane.showMessageDialog( null, "Your units have reached the goal population!" +
					"\n\nYou may continue playing if you so desire." );
		}
		
		for(Unit person: units)
		{
			Cell currentCell = map.getCell(person.getLocation());
			person.update();
			currentCell.setUnit(false);
			map.getCell(person.getLocation()).setUnit(true);
		}

		for( Unit unit : unitsToKill ) {
			unit.die();
		}
		
		for( Unit unit : unitsToBeBorn ) {
			units.add(unit);
		}

		unitsToBeBorn.clear();
		unitsToKill.clear();
	}

	/**
	 * Sets the map and places units on the map
	 * @param m
	 */
	public void setMap(Map m)
	{
		this.map = m;
		this.cols = map.getCols();
		int mapSize = map.getMapSize();
		int rows = mapSize/cols;
		for(Unit u: units)
		{
			u.setCols(cols);
			boolean placed = false;
			while(!placed)
			{
				int randRow = (int)(Math.random()*(rows/3))+rows/3;
				int randCol = (int)(Math.random()*(cols/3))+cols/3;
				int randLoc = randRow*cols+randCol;
				Cell randCell = map.getCell(randLoc);
				if(!(randCell.hasResource()||randCell.hasUnit()||randCell.getTerrain().equals(Terrain.water)))
				{
					placed = true;

					u.setLocation(randLoc);
					map.getCell(randLoc).setUnit(true);
				}
			}
		}
	}

	/**
	 * Attempts to remove a certain amount of a resource from the global store. If
	 * there is enough, the desired amount will be returned. Otherwise, the
	 * amount currently in the stores will be returned and the global store will
	 * be set to 0.
	 * @param desiredResourceAmount The amount of a resource you would like to remove from
	 * 								the global store.
	 * @param resourceType The resource you would like to retrieve from the global store
	 * @return The desired amount if it is available, or all available resource if the desired
	 * 			amount could not be met.
	 */
	public int pollResource( ResourceType resourceType, int desiredResourceAmount ) {
		int currentResourceCount = this.globalResourcePool.get( resourceType );

		if( currentResourceCount >= desiredResourceAmount ) {
			this.globalResourcePool.put( resourceType, currentResourceCount - desiredResourceAmount );
			return desiredResourceAmount;
		}
		else {
			int amountTaken = currentResourceCount;
			this.globalResourcePool.put(resourceType, 0);
			return amountTaken;
		}
	}
	
	/**
	 * Stores the given resource in the resource stores
	 */
	public void storeResource(Resource resource) {
		if( resource == null ) return;
		this.globalResourcePool.put(resource.getResourceType(), 
				this.globalResourcePool.get(resource.getResourceType() ) + resource.getResourceValue() );
	}
	
	/**
	 * Places the specified unit on the list of units to kill during the next tick
	 */
	public void sentenceToDeath( Unit unit ) {
		this.unitsToKill.add( unit );
		System.out.println( unit.getName() + " has died :\'(" );
	}

	/**
	 * Returns the singleton instance of this class
	 * @return The Civilization instance
	 */
	public static Civilization getInstance() {
		return instance;
	}

	/**
	 * Adds a task to the job queue. Jobs will be periodically
	 * pulled off the job queue by units in the civilization
	 * and the units will perform the jobs as long as they are able.
	 * 
	 * @param task The task to the add to the job queue
	 */
	public void addTaskToQueue( Task task ) {
		this.taskQueue.add( task );
	}

	/**
	 * Determines if there is an available task on the job queue
	 * @return True if there is a task on the job queue, false otherwise.
	 */
	public boolean isAvailableTask() {
		return ! this.taskQueue.isEmpty();
	}

	/**
	 * Pulls a job from the job queue and returns it.
	 * @return The next job at the top of the job queue. If no job is available, null is returned.
	 */
	public Task getNextTask() {
		return this.taskQueue.poll();
	}

	/**
	 * Adds a unit to the list of maintined units.
	 * @param unit The unit to add to the list
	 */
	public void addUnit( Unit unit ) {
		this.unitsToBeBorn.add( unit );
	}

	/**
	 * Removes the specified unit from the list of units. Occurs in the instance of unit death.
	 * @param unit The unit to remove
	 * @return True if the unit was removed from the list.
	 */
	public boolean removeUnit( Unit unit ) {
		return this.units.remove(unit);
	}

	/**
	 * Adds a structure to the list of structures in this civilization.
	 * @param structure The structure to add
	 */
	public void addStructure( AbstractStructure structure ) {
		this.structures.add( structure );
	}



	public int getResourceAmount( ResourceType rt ) {
		return this.globalResourcePool.get(rt);
	}

	public void setResourceAmount( ResourceType resourceType, int amount ) {
		this.globalResourcePool.put( resourceType, amount );
	}

	/**
	 * Returns the map
	 * @return The map
	 */
	public Map getMap() {
		return this.map;
	}


	public Queue<Task> getTaskQueue() {
		return this.taskQueue;
	}

	public ArrayList<Unit> getUnitsToKill() {
		return this.unitsToKill;
	}

	public HashMap<ResourceType, Integer> getGlobalResourcePool() {
		return this.globalResourcePool;
	}

	/**
	 * @return the units
	 */
	public ArrayList<Unit> getUnits() {
		return units;
	}

	public ArrayList<AbstractStructure> getStructures() {
		return structures;
	}

	public Tribe getTribe() {
		return this.tribe;
	}

	public void setTribe( Tribe tribe ) {
		this.tribe = tribe;
	}
	
	public String getGameName() {
		return this.gameName;
	}
	
	public void setGameName( String gameName ) {
		this.gameName = gameName;
	}

}
