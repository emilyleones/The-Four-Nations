package model.tasks;

import model.map.Map;
import model.units.Unit;

/**
 * The task performed by agents when they sleep.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class SleepTask extends Task {
	private static final long serialVersionUID = 3337353166802863444L;

	public SleepTask(int work, int locTask, Map map, Unit unit) {
		super(work, locTask, map);
	}

	/**
	 * Sets the unit to their max energy level
	 */
	@Override public void performAction() {
		int maxEnergyLevel = unit.getMAX_ENERGY_LEVEL();
		
		unit.setEnergyLevel( maxEnergyLevel );
		this.isDone = true;
	}

}