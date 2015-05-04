package model.map;

import java.io.Serializable;
import java.util.ArrayList;

import model.Civilization;
import model.units.Graph;

/**
 * The game map that maintains game location
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class Map implements Serializable {

	private static final long serialVersionUID = -6973886976003208870L;

	// TODO: Remove - used for testing purposes
	public static void main(String[] args) {
		Map board = new Map();
		board.buildRoom(27, 27, 25, 25, Terrain.kitchen);
		System.out.print(board.toString());
	}

	// TODO: Modularize
//	private int rows = 90;
//	private int cols = 90;
//	private int waterBorder = rows / 10;
	private int rows = 70;
	private int cols = 70;
	private int waterBorder = rows / 10;

	private Cell[][] map;
	private Graph graph;

	public Map() {
		this.map = MapGenerator.generateMap(rows, cols, waterBorder);
		
		//init graph
		int[][] acc = this.getAccessibilityArray();
		this.graph=new Graph(rows*cols , cols);
		
		for(int i = 0; i < rows*cols; i++)
		{
			int[] neighbors = {i+1, i-1, i+cols, i-cols};
			for(int neighbor: neighbors)
			{
				if(graph.isInGraph(neighbor) && (neighbor/cols == i/cols || neighbor%cols == i%cols))
				{
					graph.addEdge(i, neighbor);
				}	
			}
		}	
		for(int i = 0; i < map.length*map[0].length; i++)
		{
			if(acc[i/cols][i%cols] == 1)
				graph.removeVertex(i);
		}	
		
	}

	/**
	 * Fills the tiles between specified points with room tiles.
	 * 
	 * <p>
	 * Example: If it where called as such
	 * <code>buildRoom( 2, 2, 4, 4, Terrain.KITCHEN)</code> on the map below
	 * where the period character (".") represents valid tiles:
	 * </p>
	 * <tt>
	 * +---------------------+<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * +---------------------+<br></tt>
	 * 
	 * <p>
	 * Would become:
	 * </p>
	 * <tt>
	 * +---------------------+<br>
	 * |.....................|<br>
	 * |.....................|<br>
	 * |..KKK................|<br>
	 * |..KKK................|<br>
	 * |..KKK................|<br>
	 * |.....................|<br>
	 * +---------------------+<br></tt>
	 * 
	 * @param firstClickX
	 *            The x-coordinate of the first location user clicks
	 * @param firstClickY
	 *            The y-coordinate of the first location user clicks
	 * @param secondClickX
	 *            The x-coordinate of the second location user clicks
	 * @param secondClickY
	 *            The y-coordinate of the second location user clicks
	 * @param roomType
	 *            The terrain to fill the given area with
	 * 
	 * returns 0 if room was built, 1 if user does not have enough gold, or 2 if there are resources in the way
	 */
	public int buildRoom(int firstClickX, int firstClickY,
			int secondClickX, int secondClickY, Terrain roomType) {

		Civilization civ = Civilization.getInstance();
		int startRow, startCol, endRow, endCol;

		// Check bounds
		if (firstClickX <= secondClickX && firstClickY <= secondClickY) {
			startRow = firstClickX;
			startCol = firstClickY;
			endRow = secondClickX;
			endCol = secondClickY;
		} else {
			startRow = secondClickX;
			startCol = secondClickY;
			endRow = firstClickX;
			endCol = firstClickY;
		}
		
		int area = (endCol - startCol + 1) * (endRow - startRow + 1);
		if (area > civ.getResourceAmount(ResourceType.gold)) {
			return 1;
		}

		// Check for water and resources within the given area
		for (int i = startRow; i <= endRow; i++) {
			for (int j = startCol; j <= endCol; j++) {
				if (this.map[i][j].getTerrain().equals(Terrain.water)|| this.map[i][j].hasStructure()) {
					return 2;
				}
				else if (this.map[i][j].hasResource()) {
					return 3;
				} 
			}
		}

		// Change the tiles in the given area to the specified terrain type
		
		for (int i = startRow; i <= endRow; i++) {
			for (int j = startCol; j <= endCol; j++) {
				this.map[i][j].setTerrain(roomType);
			}
		}
		civ.pollResource(ResourceType.gold, area);
		return 0;
	}

	/**
	 * Creates a 2D array of integers that indicate the accessibiility
	 * of the map tiles. This is used in the implementation of Dijkstra's
	 * shortest-path algorithm that handles unit movement.
	 * 
	 * 0 indicates that the terrain is accessible, 1 indicates that it is not.
	 */
	public int[][] getAccessibilityArray() {
		int[][] accessible = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ( ! map[i][j].isAccessible() )
					accessible[i][j] = 1;
				else
					accessible[i][j] = 0;
			}
		}
		return accessible;
	}

	/**
	 * Returns the cell at the specified location. The are numbered as such:
	 * 
	 * <tt><p><br>0 1 2 3 4 5 6 7<br>
	 * +- - - - - - - -+<br>
	 * |. . . . . . . .|0<br>
	 * |. . . . . . . .|1<br>
	 * |. . . . . . . .|2<br>
	 * |. . . . . . . .|3<br>
	 * |. . . . . . . .|4<br>
	 * |. . . . . . . .|5<br>
	 *  +- - - - - - - -+<br></tt></p>
	 * 
	 * <p>
	 * And the location of a specific cell is given by the product of it's row
	 * and column. Moving backwards from a specified cell, a row is given by the
	 * <code>location / columns</code> and the column is given by
	 * <code>location % columns</code>.
	 * 
	 * @param location
	 *            The cell's location within the map
	 * @return The cell at the given location
	 */
	public Cell getCell(int location) {
		return map[location / cols][location % cols];
	}
	
	public ArrayList<Integer> getMoves(int start, int end)
	{
		return this.graph.hashingDijkstras(start, end);
	}
	
	public void makeAccessible(int location)
	{
		int[] neighbors = {location+1, location-1, location+cols, location-cols};
		for(int neighbor: neighbors)
		{
			if(graph.isInGraph(neighbor) && (neighbor/cols == location/cols || neighbor%cols == location%cols))
			{
				graph.addEdge(location, neighbor);
			}	
		}
	}
	
	public void makeInaccesible(int location)
	{
		graph.removeVertex(location);
	}

	/**
	 * Generates a textual representation of the map
	 */
	public String toString() {
		StringBuilder toString = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell c = map[i][j];

				if (c.hasUnit())
					toString.append("U");
				else if (c.hasStructure())
					toString.append("S");
				else if (c.hasResource()) {
					toString.append(c.getResource());
				} else {
					toString.append(c.getTerrain());
				}
			}
			toString.append(lineSeparator);
		}
		return toString.toString();
	}

	/**
	 * Returns the number of rows in the map array
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Returns the number of columns in the map array
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Returns the number of tiles in the map
	 */
	public int getMapSize() {
		return this.cols * this.rows;
	}

	public Cell getCell(int row, int col) {
		return map[row][col];
	}
}
