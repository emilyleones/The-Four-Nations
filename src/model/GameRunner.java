package model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import model.map.Map;
import model.map.Resource;
import model.map.ResourceType;
import model.map.Terrain;
import model.structures.Table;
import model.tasks.BuildStructureTask;
import model.tasks.CollectResourceTask;
import model.units.Unit;


public class GameRunner 
{
	private static Map board;
	
	public static void main(String[] args)
	{
		board = new Map();
		System.out.println(board.toString());
		Civilization civ = Civilization.getInstance();
		civ.setMap(board);
		civ.getMap().getCell(950).setResource(Resource.tree);
		civ.getMap().getCell(1420).setTerrain(Terrain.stockpile);
		civ.addTaskToQueue( new CollectResourceTask(5, 950, board) );
		for(int r = 12; r <= 14; r++) {
			for(int c = 27; c<=30; c++)
			{	
				board.getCell(r,c).removeResource();
				board.getCell(r,c).setTerrain(Terrain.plains);
			}
		}
		civ.getMap().buildRoom(12, 27, 14, 30, Terrain.kitchen);
		int loc = 13 * board.getCols() + 28;
		civ.addTaskToQueue( new BuildStructureTask( 10, board, new Table( loc, "S", ResourceType.wood ) ) );

		
		int delay = 200;
		/**
		 * Handles the game events
		 */
		ActionListener timeListener = new ActionListener(){
			private Civilization civ = Civilization.getInstance();
			private boolean enableNaturalDisaster = false; //Change to true to enable
			private double naturalDistanceChance = 0.05; //0.05 = 5% chance. Change accordingly.
			
			@Override
			public void actionPerformed(ActionEvent e) {
				civ.update();
				
				//Check for natural disaster
				if( enableNaturalDisaster && Math.random() < naturalDistanceChance ) {
					civ.setResourceAmount(ResourceType.food, (int) Math.floor( civ.getResourceAmount( ResourceType.food) * 0.75 ) );
				}
				
				//Print the board
				System.out.println(board);
				
				//Print resource count
				for( ResourceType rt : ResourceType.values() )
					System.out.println( rt.toString() + ": " + civ.getResourceAmount(rt) );
				
				//Print unit hunger
				for( Unit unit : civ.getUnits() ) {
					System.out.println( unit.getName() + ": " + unit.getHungerLevel() );
				}
			}
		};
		
		//Start timer
		new Timer(delay, timeListener).start();
		while(true){}
	}
}

