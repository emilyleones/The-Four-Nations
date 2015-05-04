package model.tasks;

import model.Civilization;
import model.map.Map;
import model.map.ResourceType;
import model.units.Unit;

/**
 * Performed when the unit must eat
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class EatTask extends Task {

	private static final long serialVersionUID = -1116613252893400073L;

	public EatTask(int work, int locTask, Map map, Unit unit) {
		super(work, locTask, map);
	}

	/**
	 * Gives the unit as much food as possible (up to their max)
	 */
	@Override public void performAction() {
		int currentHungerLevel = unit.getHungerLevel();
		int maxHungerLevel = unit.getMAX_HUNGER_LEVEL();
		
		int amountRetrieved = Civilization.getInstance().pollResource(ResourceType.food, maxHungerLevel - currentHungerLevel );
		unit.setHungerLevel( unit.getHungerLevel() + amountRetrieved );
		this.isDone = true;
	}

}
