package model.units;

import model.Civilization;
import model.map.Map;

/**
 * A standard unit with no special abilities or advancements. Concrete implementation
 * of it's abstract parent.
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class BasicUnit extends Unit {

	private static final long serialVersionUID = -1305684877971681010L;

	public BasicUnit(String name, int maxEnergyLevel, int maxHungerLevel,
			int cols) {
		super(name, maxEnergyLevel, maxHungerLevel, cols);
		
	}

	/**
	 * Lose 1 hunger, energy, thirst per update
	 */
	@Override protected void updateUnitCounters() {
		this.hungerLevel--;
		this.energyLevel--;
		this.thirstLevel--;
		
		if( hungerLevel <= 0 || energyLevel <= 0 || thirstLevel <= 0) 
			Civilization.getInstance().sentenceToDeath( this );
	}

	
	@Override protected void giveBirth() {
		Civilization civ = Civilization.getInstance();
		Map m = civ.getMap();
		int location = super.getLocation();
		int cols = m.getCols();
		int[] locs = {location + cols, location - cols, location + 1, location - 1};
		for(int loc: locs)
		{
			if(loc < m.getMapSize() && loc > 0)
			{
				if(m.getCell(loc).isAccessible())
				{
					Unit baby = new BasicUnit("U", 500, 500, cols);
					baby.setLocation(loc);
					baby.setHungerLevel( baby.getHungerLevel() / 2 );
					m.getCell(loc).setUnit(true);
					civ.addUnit(baby);
					return;
				}
			}	
		}	
	}

}
