package model.units;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JOptionPane;

import model.Civilization;
import model.map.ResourceType;
import model.structures.AbstractStructure;
import model.tasks.DrinkTask;
import model.tasks.EatTask;
import model.tasks.SleepTask;
import model.tasks.Task;



/**
 * The unit is the basic entity of the game. It it the superclass to all other autonomous units within the game.
 * The unit performs tasks by pulling them from a global job queue and works on them until other needs otherwise present
 * themselves.
 * 
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public abstract class Unit implements Serializable {

	private static final long serialVersionUID = 5975310412672383975L;
	private String name;
	private HashMap<UnitSkill, Integer> skills;
	protected int energyLevel, hungerLevel, thirstLevel;
	private final int MAX_HUNGER_LEVEL, MAX_ENERGY_LEVEL, MAX_THIRST_LEVEL;
	private Task currentTask;
	private Queue<Integer> movementQueue;
	private boolean currentlyWorking = false;
	private int location;
	private int cols;
	private int spriteType;
	private Random rand = new Random();


	/**
	 * Creates a unit that has a specific set of skills
	 * @param name The name of the unit
	 * @param skills A map of skills
	 * @param maxEnergyLevel The maximum amount of energy that this unit can have. They start the game at 75% of their max energy level.
	 * 							They will need to sleep at 15% of their max energy level.
	 * @param maxHungerLevel The maximum amount of food energy that this unit can have. They start the game at 75% of their max hunger
	 * 							level. They will need to eat at 15% of their max energy level.
	 */
	public Unit( String name, HashMap<UnitSkill, Integer> skills, int maxEnergyLevel, int maxHungerLevel, int location, int cols ) {
		this.name = name;
		this.energyLevel = (int)(maxEnergyLevel * 0.75);
		this.hungerLevel = (int)(maxHungerLevel * 0.75);
		this.thirstLevel = (int)(maxHungerLevel * 0.75);
		this.MAX_ENERGY_LEVEL = maxEnergyLevel;
		this.MAX_HUNGER_LEVEL = maxHungerLevel;
		this.MAX_THIRST_LEVEL = maxHungerLevel; //TODO: Determine assignment
		this.skills = skills;
		this.movementQueue = new LinkedList<Integer>();
		this.location = location;
		this.cols = cols;

		this.spriteType = rand.nextInt(3);
	}

	/**
	 * Starts with the default skillset
	 * @param name The name of the unit
	 * @param maxEnergyLevel The maximum amount of energy that this unit can have. They start the game at 75% of their max energy level.
	 * 							They will need to sleep at 15% of their max energy level.
	 * @param maxHungerLevel The maximum amount of food energy that this unit can have. They start the game at 75% of their max hunger
	 * 							level. They will need to eat at 15% of their max energy level.
	 */
	public Unit( String name, int maxEnergyLevel, int maxHungerLevel, int cols) {
		this.name = name;
		this.spriteType = rand.nextInt(3);
		this.energyLevel = (int)(maxEnergyLevel * 0.75);
		this.hungerLevel = (int)(maxHungerLevel * 0.75);
		this.thirstLevel = (int)(maxHungerLevel * 0.75);
		this.MAX_ENERGY_LEVEL = maxEnergyLevel;
		this.MAX_HUNGER_LEVEL = maxHungerLevel;
		this.MAX_THIRST_LEVEL = maxHungerLevel; //TODO:
		this.cols = cols;
		this.movementQueue = new LinkedList<Integer>();


		//Initialize all skills at one
		this.skills = new HashMap<UnitSkill, Integer>();
		for( UnitSkill us : UnitSkill.values() )
			this.skills.put(us, 1);
	}


	/**
	 * Gives the unit a new task to work on
	 * @param task
	 */
	public void setCurrentTask( Task task ) {
		this.currentTask = task;

		generatePath(this.currentTask.getTaskLocation() );
	}

	public boolean workingOnNeed() {
		return this.currentTask instanceof EatTask || this.currentTask instanceof SleepTask || this.currentTask instanceof DrinkTask;
	}

	/**
	 * @return True if the current task that the unit is working on is finished or not
	 */
	public boolean hasFinishedCurrentTask() {
		return this.currentTask.getRemainingWorkRequirement() <= 0;
	}

	/**
	 * If the unit does not need sleep, does not need food, and currently has no task; then provide then they can be assigned a task
	 * @return True if the unit is not otherwise occupied
	 */
	public boolean needsNewTask() {
		return !needsToEat() && !needsToSleep() && this.currentTask == null;
	}

	/**
	 * If this unit's hunger level is below a certain point, then they need to eat
	 * @return True if the unit needs to sleep, false otherwise.
	 */
	public boolean needsToEat() {
		return this.hungerLevel <= (int) Math.floor( this.MAX_HUNGER_LEVEL * 0.15 );
	}

	/**
	 * If this unit's energy level is below a certain point, then they need to sleep
	 * @return True if the unit needs to sleep, false otherwise
	 */
	public boolean needsToSleep() {
		return this.energyLevel <= (int) Math.floor( this.MAX_ENERGY_LEVEL * 0.15 );
	}

	public boolean needsToDrink () {
		return this.thirstLevel <= (int) Math.floor( this.MAX_THIRST_LEVEL * 0.15 );
	}

	/**
	 * Generates a queue of movements for the unit to take to move to a desired location
	 * @param destination The location on the map that the unit will move to
	 */
	public boolean generatePath( int destination ) {
		this.movementQueue.clear();
		ArrayList<Integer> moves = Civilization.getInstance().getMap().getMoves(location, destination);
		if(moves == null)
			return false;
		for(int move: moves)
			movementQueue.add(move);
		return true;
	}

	/**
	 * Moves the unit to the given location.
	 */
	public void move()
	{
		location = movementQueue.remove();
		if(movementQueue.size()==0) {
			this.setCurrentlyWorking(true);
			return;
		}
	}

	private void idle() {
		if( Math.random() < 0.5 ) {
			int loc = this.getLocation();
			//Left or right
			if( Math.random() < 0.5 )
				loc = Math.random() < 0.5 ? loc + 1 : loc - 1;
			else
				loc = Math.random() < 0.5 ? loc + cols : loc - cols;

			if( Civilization.getInstance().getMap().getCell(loc).isAccessible() ) {
				this.location = loc;
//				System.out.println( "Idle movement" );
			}
		}
	}

	/**
	 * The method that is called every tick by Civilization
	 */
	public void update()
	{
		Civilization civ = Civilization.getInstance();
		updateUnitCounters();

		//make babies
		if(Math.random() < .001)
		{	
			this.giveBirth();
			System.out.println("A new birth!");
		}	

		if(!workingOnNeed())
		{
			if(this.needsToEat() && civ.getResourceAmount(ResourceType.food) > 0)
			{
				//Place task back on queue
				if( this.currentTask != null ) {
					civ.addTaskToQueue( this.currentTask );
					this.currentTask.setUnit(null);
					this.currentTask = null;
				}

				ArrayList<AbstractStructure> structures = civ.getStructures();
				ArrayList<AbstractStructure> validStructures = new ArrayList<AbstractStructure>();

				for( AbstractStructure as : structures ) {
					if( as.providesFood() )
						validStructures.add(as);
				}

				//Create task to eat
				if( ! validStructures.isEmpty() )
				{	
					this.currentTask = new EatTask( 1, validStructures.get(0).getLocation(), civ.getMap(), this );
					if(!generatePath(currentTask.getTaskLocation())) {
						currentTask = null;
						currentlyWorking = false;
					}
				}
			}
			if(this.needsToSleep() && !this.workingOnNeed())
			{
				//Place task back on queue
				if( this.currentTask != null ) {
					civ.addTaskToQueue( this.currentTask );
					this.currentTask.setUnit(null);
					this.currentTask = null;
				}

				ArrayList<AbstractStructure> structures = civ.getStructures();
				ArrayList<AbstractStructure> validStructures = new ArrayList<AbstractStructure>();

				for( AbstractStructure as : structures ) {
					if( as.providesBed() )
						validStructures.add(as);
				}

				//Create task to sleep
				if( ! validStructures.isEmpty() )
				{	
					this.currentTask = new SleepTask( 1, validStructures.get(0).getLocation(), civ.getMap(), this );
					if(!generatePath(currentTask.getTaskLocation())) {
						currentTask = null;
						currentlyWorking = false;
					}
				}	
			}
			if(this.needsToDrink() && !this.workingOnNeed())
			{
				//Place task back on queue
				if( this.currentTask != null ) {
					civ.addTaskToQueue( this.currentTask );
					this.currentTask.setUnit(null);
					this.currentTask = null;
				}	

				ArrayList<AbstractStructure> structures = civ.getStructures();
				ArrayList<AbstractStructure> validStructures = new ArrayList<AbstractStructure>();

				for( AbstractStructure as : structures ) {
					if( as.providesDrink() )
						validStructures.add(as);
				}

				//Create task to drink
				if( ! validStructures.isEmpty() )
				{	
					this.currentTask = new DrinkTask( 1, validStructures.get(0).getLocation(), civ.getMap(), this );
					if(!generatePath(currentTask.getTaskLocation())) {
						currentTask = null;
						currentlyWorking = false;
					}
				}	
			}
		}
		if(!movementQueue.isEmpty())
		{
			move();
//			System.out.println("moving");
		}
		else if(this.currentlyWorking && this.currentTask != null )
		{
//			System.out.println("working");
			if( this.currentTask.getUnit() == null ) this.currentTask.setUnit( this );
			currentTask.decrement(1);
			if(currentTask.isDone())
			{	
				currentlyWorking = false;
				currentTask = null;
			}
		}	
		else
		{
			if(civ.isAvailableTask())
			{	
				currentTask = civ.getNextTask();
//				System.out.println("generating: " + currentTask.getClass());
				if(!generatePath(currentTask.getTaskLocation()))
				{	
					currentTask.setUnit(null);
					currentTask = null;
//					System.out.println("No available path");

				}	

			}
			else
			{	
				this.idle();
//				System.out.println("idle");
			}	
		}	
	}



	/**
	 * Updates the unit's hunger and energy levels. Called every tick.
	 */
	protected abstract void updateUnitCounters();

	/**
	 * Generates another unit nearby
	 */
	protected abstract void giveBirth();

	public void die() {
		Civilization civ = Civilization.getInstance();
		if( this.currentTask != null )
			civ.addTaskToQueue(this.currentTask);
		civ.removeUnit(this);
		civ.getMap().getCell(this.location).setUnit(false);
		JOptionPane.showMessageDialog( null, "A unit has died!" );
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the skills
	 */
	public HashMap<UnitSkill, Integer> getSkills() {
		return skills;
	}

	/**
	 * @return the energyLevel
	 */
	public int getEnergyLevel() {
		return energyLevel;
	}

	/**
	 * @return the hungerLevel
	 */
	public int getHungerLevel() {
		return hungerLevel;
	}

	/**
	 * @return the currentTask
	 */
	public Task getCurrentTask() {
		return currentTask;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int loc) {
		location = loc;
	}

	/**
	 * @return the currentlyWorking
	 */
	public boolean isCurrentlyWorking() {
		return currentlyWorking;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(HashMap<UnitSkill, Integer> skills) {
		this.skills = skills;
	}

	/**
	 * @param energyLevel the energyLevel to set
	 */
	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
	}

	/**
	 * @param hungerLevel the hungerLevel to set
	 */
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	/**
	 * @param currentlyWorking the currentlyWorking to set
	 */
	public void setCurrentlyWorking(boolean currentlyWorking) {
		this.currentlyWorking = currentlyWorking;
	}

	/**
	 * @return the mAX_HUNGER_LEVEL
	 */
	public int getMAX_HUNGER_LEVEL() {
		return MAX_HUNGER_LEVEL;
	}

	/**
	 * @return the mAX_ENERGY_LEVEL
	 */
	public int getMAX_ENERGY_LEVEL() {
		return MAX_ENERGY_LEVEL;
	}

	public int getMAX_THIRST_LEVEL() {
		return MAX_THIRST_LEVEL;
	}

	public void setCols(int cols2) {
		this.cols = cols2;

	}
	
	public int getSpriteType() {
		return this.spriteType;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String lineSep = System.lineSeparator();
		
		sb.append( "Hunger Level: ").append( this.hungerLevel).append("/").append( this.MAX_HUNGER_LEVEL ).append(lineSep);
		sb.append( "Energy Level: ").append( this.energyLevel).append("/").append( this.MAX_ENERGY_LEVEL ).append(lineSep);
		sb.append( "Thirst Level: ").append( this.thirstLevel).append("/").append( this.MAX_THIRST_LEVEL ).append(lineSep);
		sb.append( "Location: " ).append( "(" ).append( this.location % cols ).append(",").append( this.location/cols).append(")").append( lineSep );
		
		return sb.toString();
	}

	public void setThirstLevel(int maxThirstLevel) {
		this.thirstLevel = maxThirstLevel;
	}
}
