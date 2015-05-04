package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import model.Civilization;
import model.CivilizationState;
import model.SaveLoadManager;
import model.Tribe;
import model.map.Map;
import model.map.ResourceType;
import model.map.Terrain;
import model.structures.Bed;
import model.structures.Well;
import model.tasks.BuildStructureTask;

import org.junit.Test;

/**
 * Tests the Serialization of the Civilization State
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class SerializationTest {

	/**
	 * Tests the differentiability of states
	 */
	@Test public void testSerialization() {
		//Get civ
		Civilization civ = Civilization.getInstance();
		civ.setMap( new Map() );
		
		//Create serialization object
		CivilizationState civStateInit = new CivilizationState( civ );

		//Add some stuff to the civilization
		Bed b = new Bed( 0, "BED", ResourceType.wood );
		civ.addStructure( b );
		civ.setResourceAmount( ResourceType.wood, 5000 );
		civ.setMap( new Map() );
		civ.getMap().getCell(300).setTerrain(Terrain.plains); //Prevent DisallowedTaskException
		civ.setResourceAmount( ResourceType.stone, 1 );
		civ.addTaskToQueue( new BuildStructureTask(5, civ.getMap(), new Well( 300, "WELL", ResourceType.stone ) ) );

		//Assertions
		assertEquals( 5000, civ.getResourceAmount( ResourceType.wood ) );
		assertTrue( civ.getStructures().contains(b) );
		assertFalse( civ.getTaskQueue().isEmpty() );

		//Parse serialization
		civ.parseCivilizationState(civStateInit);

		//Assertions
		assertFalse( 5000 == civ.getResourceAmount(ResourceType.wood) );
		assertFalse( civ.getStructures().contains(b) );
		assertTrue( civ.getTaskQueue().isEmpty() );
		
	}
	
	/**
	 * Tests saving the states to files and then recovering the state from those files
	 */
	@Test public void saveSomeStates() {
		//Get civilization and save it
		Civilization civ = Civilization.getInstance();
		for( ResourceType rt : ResourceType.values() ) civ.setResourceAmount(rt, 0);
		civ.setTribe( Tribe.WATER );
		CivilizationState water = new CivilizationState(civ);
		SaveLoadManager.saveGame( "Water", water );
		
		//Change some stuff
		civ.setResourceAmount( ResourceType.wood, 500 );
		civ.setResourceAmount( ResourceType.stone, 300 );
		
		//Save another persistant state
		CivilizationState fire = new CivilizationState(civ);
		SaveLoadManager.saveGame( "Fire", fire );
		
		//Get the saves located in the file tree
		System.out.print( "Existing saves: " );
		ArrayList<String> firstTwoSaves = SaveLoadManager.getSavedGames();
		System.out.println( firstTwoSaves ); 
		assertEquals( 2, firstTwoSaves.size() );
		System.out.println();

		
		//Load and test the first state
		System.out.println( "Water" );
		System.out.println("------------------------------");
		CivilizationState cs1Loaded = SaveLoadManager.loadGame("Water");
		System.out.println( "Structures:\t" + cs1Loaded.getStructures().size() );
		System.out.println( "Units to Kill:\t" + cs1Loaded.getUnitsToKill().size() );
		System.out.println( "Resource amounts:" );
		for( ResourceType rt : ResourceType.values() ) {
			System.out.println("\t" + rt + ":\t" + water.getGlobalResourcePool().get(rt) );
		}
		System.out.println( "Task Queue:\t" + cs1Loaded.getTaskQueue().size() );
		assertEquals( 0, cs1Loaded.getStructures().size() );
		assertEquals( 0, cs1Loaded.getUnitsToKill().size() );
		for( ResourceType rt : ResourceType.values() ) assertEquals( 0, water.getGlobalResourcePool().get(rt).intValue());
		assertEquals( 0, cs1Loaded.getTaskQueue().size() );
		System.out.println();
		
		//Load and test the second state
		System.out.println( "Fire" );
		System.out.println("------------------------------");
		CivilizationState cs2Loaded = SaveLoadManager.loadGame("Fire");
		System.out.println( "Structures:\t" + cs2Loaded.getStructures().size() );
		System.out.println( "Units to Kill:\t" + cs2Loaded.getUnitsToKill().size() );
		System.out.println( "Resource amounts:\t" );
		for( ResourceType rt : ResourceType.values() ) {
			System.out.println("\t" + rt + ":\t" + fire.getGlobalResourcePool().get(rt) );
		}
		System.out.println( "Task Queue:\t" + cs2Loaded.getTaskQueue().size() );
		assertEquals( 0, cs2Loaded.getStructures().size() );
		for( ResourceType rt : ResourceType.values() ) assertEquals( 0, water.getGlobalResourcePool().get(rt).intValue());
		assertEquals( 0, cs2Loaded.getUnitsToKill().size() );
		assertEquals( 0, cs2Loaded.getTaskQueue().size() );
		System.out.println();
	}
}
