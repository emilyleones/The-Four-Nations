package model.tasks;

import model.map.Map;
import model.units.Unit;

/**
 * Performed when the unit needs to drink
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class DrinkTask extends Task {

	private static final long serialVersionUID = -3242173019984231381L;

	public DrinkTask(int work, int locTask, Map map, Unit unit) {
		super(work, locTask, map);
	}

	/**
	 * Restores the unit's thirst level to it's max level
	 */
	@Override public void performAction() {
		int maxThirstLevel = unit.getMAX_THIRST_LEVEL();
		
		unit.setThirstLevel( maxThirstLevel);
		this.isDone = true;
	}

}
