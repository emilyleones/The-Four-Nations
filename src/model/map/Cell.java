package model.map;
import java.io.Serializable;
import java.util.Random;

import model.structures.AbstractStructure;

/**
 * An individual location on the game map
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class Cell implements Serializable {

	private static final long serialVersionUID = -6950090612251717629L;
	private boolean hasUnit;
	private boolean isVisible;
	private boolean hasResource;
	private Terrain terrain;
	private Resource resource;
	private int average;
	private Random rand = new Random();
	private AbstractStructure item;
	
	/**
	 * Creates a cell that has no resources, a random average,
	 * no units, is not visible, and has terrain as it's default
	 * terrain.
	 */
	public Cell() {
		terrain= Terrain.plains;
		hasResource = false;
		hasUnit = false;
		isVisible = false;
		average = rand.nextInt(300);
		item = null;
	}

	public void setTerrain(Terrain t) {
		terrain = t;
	}
	
	public Terrain getTerrain(){
		return terrain;
	}
	
	public void setUnit(boolean unitHere) {
		hasUnit = unitHere;
	}
	
	public boolean hasUnit(){
		return hasUnit;
	}
	
	public void setVisible() {
		isVisible = true;
	}
	
	public boolean visibility() {
		return isVisible;
	}
	
	public void setAverage(int avg) {
		average = avg;
	}
	
	public int getAverage() {
		return average;
	}
	
	public void setResource(Resource res) {
		resource = res;
		hasResource = true;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public boolean hasResource() {
		return hasResource;
	}
	
	public void removeResource() {
		resource = null;
		hasResource = false;
	}
	
	public boolean isAccessible() {
		return !( this.hasResource || this.terrain == Terrain.water || this.hasStructure() || this.terrain == Terrain.rockyPlains );
	}
	
	public boolean hasStructure() {
		return item != null;
	}
	
	public AbstractStructure getStructure() {
		return item;
	}
	
	public void addStructure(AbstractStructure built)	{
		item = built;
	}
}
