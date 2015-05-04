package model.units;

import java.util.ArrayList;

/**
 * An implementation of Dijkstra's shortest path algorithm
 * @author Christopher Chapline, James Fagan, Emily Leones, Michelle Yung
 *
 */
public class PathGenerator {

	private static final int BARRIER = 1;
	private int[][] map;
	private int start;
	private int cols;
	private int end;
	private Graph graph;
	private ArrayList<Integer> moves;
	
	//TODO: Example usage
	public static void main(String[] args)
	{
		int[][] GRID = { 
		        { 0, 0, 0, 0},
		        { 0, 0, 1, 0},
		        { 0, 0, 0, 0},
		        { 1, 1, 1, 0},
		        { 0, 0, 0, 0}
		    };
		new PathGenerator(GRID, 16, 9 );
	}
	
	public PathGenerator (int[][] map, int start, int end)
	{
		this.map= map;
		this.start = start;
		this.end = end;
		cols = map[0].length;
		
		this.graph=new Graph(map.length*map[0].length , cols);
		
		for(int i = 0; i < map.length*map[0].length; i++)
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
			if(map[i/cols][i%cols] == BARRIER)
				graph.removeVertex(i);
		}	
		
		
		moves = graph.hashingDijkstras(start, end);
		if(moves == null)
		{
			System.out.print("IMPOSSIBLE!!!");
			return;
		}
//		for(int m: moves)
//		{
//			System.out.print(m+" ");
//		}
	}
	
	public ArrayList<Integer> getMoves()
	{
		return moves;
	}
	
	public void printMap()
	{
		for(int[] ints : map)
		{
			for(int i: ints)
				System.out.print(i+" ");
			System.out.println();
		}
	}
}
