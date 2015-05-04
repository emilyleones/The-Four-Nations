package model.units;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedList;

/*+----------------------------------------------------------------------
 ||  Class Graph 
 ||
 ||         Author:  James Fagan
 ||
 ||  Inherits From:  None.
 ||
 ||     Interfaces:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||      Constants:  None.
 ||
 |+-----------------------------------------------------------------------
 ||
 ||   Constructors:  public Graph(int numVertices)
 ||						initializes the adjacency matrix and sets 
 ||						this.numVertices to the given value
 ||
 ||  Class Methods:  None.
 ||
 ||  Inst. Methods:  public List<Integer> getUnusedVertices()
 ||					 public boolean isEdge(int source, int destination)
 ||					 public void addEdge(int source, int destination)
 ||					 public void removeEdge(int source, int destination)
 ||					 public boolean isInGraph(int vertex)
 ||					 public List<Integer> getAdjacent(int vertex)
 ||					 public int degree (int vertex)
 ||					 public boolean connected()
 ||
 ++-----------------------------------------------------------------------*/
public class Graph implements Serializable 
{
	private static final long serialVersionUID = 2502465201038032759L;
	int[][] adjacencyMatrix; /*a matrix representation of the graph
	 		adjacencyMatrix[r][c] == 1 means there is an edge from r to c
	 		adjacencyMatrix[r][c] == 0 means there is no edge from r to c
			adjacencyMatrix[r][c] == -1 means either r or c is inactive*/
	int numVertices; //the number of vertices in the graph
	private int cols;
	
	public Graph(int numVertices, int cols)
	{
		adjacencyMatrix = new int[numVertices][numVertices];
		this.numVertices = numVertices;
		this.cols = cols;
	}
	
    /*---------------------------------------------------------------------
    |  Method getUnusedVertices
    |
    |  Purpose:  This method returns a list of inactive vertices.
    |			 Since the row and column corresponding to an 
    |			 inactive vertex are -1s, a -1 on the diagonal means 
    |			 that the vertex corresponding to it is inactive. So 
    |			 this method checks every value on the diagonal 
    |			 looking for -1s. When it does find a -1 it adds 
    |			 that vertex to the list. 
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: None.
    |
    |  Parameters: None.
    |
    |  Returns:  the list of inactive vertices
    *-------------------------------------------------------------------*/
	public List<Integer> getUnusedVertices()
	{
		ArrayList<Integer> unusedVertices = new ArrayList<Integer>();
		for(Integer i = new Integer(0); i < numVertices; i++)
			if(adjacencyMatrix[i][i] == -1)
				unusedVertices.add(i);
		return unusedVertices;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method isEdge
    |
    |  Purpose:  This method determines if there is an edge between the
    |			 given vertices by checking for a 1 in the adjacency matrix.
    |
    |  Pre-condition:  source and destination are less than numVertices
    |
    |  Post-condition: None.
    |
    |  Parameters:
    |      source -- this method checks for an edge from this vertex
    |	   destination -- this method checks for an edge to this vertex
    |
    |  Returns:  true if there is an edge from source to destination, 
    |				false otherwise
    *-------------------------------------------------------------------*/
	public boolean isEdge(int source, int destination)
	{
		return adjacencyMatrix[source][destination] == 1;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method addEdge
    |
    |  Purpose:  This method adds an edge in the graph from the given 
    |			 source vertex to the given destination vertex. It 
    |			 does so by setting the element in the adjacency 
    |			 matrix corresponding to those two vertices to 1. 
    |
    |  Pre-condition:  source and destination are less than numVertices
    |
    |  Post-condition: the adjacency matrix has been modified to
    |					reflect the newly added edge
    |
    |  Parameters:
    |      source -- this method adds an edge from this vertex
    |	   destination -- this method adds an edge to this vertex
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void addEdge(int source, int destination)
	{
		adjacencyMatrix[source][destination] = 1;
		adjacencyMatrix[source][source] = 0;
		adjacencyMatrix[destination][destination] = 0;
		adjacencyMatrix[destination][source] = 1;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method addEdge
    |
    |  Purpose:  This method removes an edge in the graph from the given 
    |			 source vertex to the given destination vertex. It 
    |			 does so by setting the element in the adjacency 
    |			 matrix corresponding to those two vertices to 0. 
    |
    |  Pre-condition:  source and destination are less than numVertices
    |
    |  Post-condition: the adjacency matrix has been modified to
    |					reflect the newly removed edge
    |
    |  Parameters:
    |      source -- this method removes an edge from this vertex
    |	   destination -- this method removes an edge to this vertex
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void removeEdge(int source, int destination)
	{
		adjacencyMatrix[source][destination] = 0;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method isInGraph
    |
    |  Purpose:  This method determines whether or not the given vertex
    |			 is active in this graph. It does so by checking if 
    |			 the value at the row and column of the vertex is -1. 
    |
    |  Pre-condition:  vertex < numVertices
    |
    |  Post-condition: None.
    |
    |  Parameters:
    |      vertex -- the vertex for which we are determining if it is
    |					active in the graph
    |
    |  Returns:  true if the vertex is active in the graph, false otherwise
    *-------------------------------------------------------------------*/
	public boolean isInGraph(int vertex)
	{
		if(vertex >= numVertices || vertex < 0)
			return false;
		return adjacencyMatrix[vertex][vertex] != -1;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method removeVertex
    |
    |  Purpose:  This method makes the given vertex inactive in the
    |			 graph. It does this by setting the every value in 
    |			 the row and column of the vertex to -1 in the 
    |			 adjaceny matrix. By doing this for the entire row 
    |			 and the entire column, we ensure that any existing 
    |			 edges related to this vertex will be removed. 
    |
    |  Pre-condition:  vertex < numVertices
    |
    |  Post-condition: The vertex has been removed from the graph.
    |
    |  Parameters:
    |      vertex -- the vertex to be removed
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void removeVertex(int vertex)
	{
		for(int i = 0; i < numVertices; i++)
		{	
			adjacencyMatrix[vertex][i] = 0;
			adjacencyMatrix[i][vertex] = 0;
		}	
		adjacencyMatrix[vertex][vertex] = -1;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method getAdjacent
    |
    |  Purpose:  This method returns a list of all the vertices adjacent
    |			 to the given one. It does so by checking every 
    |			 vertex in the adjacency matrix. For each of these 
    |			 vertices, if there is an edge from the given vertex 
    |			 to that vertex, that vertex is added to an 
    |			 ArrayList. After all vertices have been considered 
    |			 the list is returned.
    |
    |  Pre-condition:  vertex < numVertices
    |
    |  Post-condition: None.
    |
    |  Parameters:
    |      vertex -- the vertex whose adjacent vertices we are finding
    |
    |  Returns:  the list of adjacent vertices
    *-------------------------------------------------------------------*/
	public List<Integer> getAdjacent(int vertex)
	{
		ArrayList<Integer> adjacentVertices = new ArrayList<Integer>();
		for(Integer i = new Integer(0); i < numVertices; i++)
		{	
			if(isEdge(vertex, i))
				adjacentVertices.add(i);
		}
		return adjacentVertices;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method degree
    |
    |  Purpose:  This method returns the number of vertices
    |			 that are adjacent to the given vertex. It does so 
    |			 by getting a list of adjacent vertices and 
    |			 returning the size of that list. 
    |
    |  Pre-condition:  vertex < numVertices
    |
    |  Post-condition: None.
    |
    |  Parameters:
    |      vertex -- the vertex whose adjacent vertices we are counting
    |
    |  Returns:  returns the number of vertices adjacent to the given vertex
    *-------------------------------------------------------------------*/
	public int degree(int vertex)
	{
		return getAdjacent(vertex).size();
	}

	
	public ArrayList<Integer> hashingDijkstras(int source, int destination)
	{
		boolean hasPath = false;
		int locOfEndOfPath = 0;
		
		//set d(source, source) = 0 and d(source, x) = + infinity, for all other x
		int[] d = new int[numVertices];
		for(int i = 0; i<numVertices; i++)
		{
			if(i == source)
				d[i]=0;
			else
				d[i]=Integer.MAX_VALUE;
		}
		
		//initialize known to contain source
		HashMap<Integer, Integer> known = new HashMap<Integer,Integer>();
		known.put(source, null);
		
		//initialize fringe to contain the vertices adjacent to source
		HashMap<Integer, Integer> fringe = new HashMap<Integer,Integer>();
		for(int v: getAdjacent(source))
		{
			fringe.put(v, source);
		}
		
		
		//so long as fringe is not empty
		while(!fringe.isEmpty() && !hasPath)
		{
			//find the fringe vertex f that has the smallest d(source,f)
			for(int vertex: fringe.keySet())
			{
				for(int t: getAdjacent(vertex))
				{
					//where d(source, f) = d(source,t) + w(t,f) where t is known
					if(known.containsKey(t)&&d[vertex]>(d[t]+adjacencyMatrix[t][vertex]))
						d[vertex] = d[t]+adjacencyMatrix[t][vertex];
				}	
			}
			int f = (int) fringe.keySet().toArray()[0];
			for(int vertex: fringe.keySet())
			{
				if(distanceAsBirdFlies(vertex, destination) < distanceAsBirdFlies(f, destination) 
						|| (distanceAsBirdFlies(vertex, destination) == distanceAsBirdFlies(f, destination) && d[vertex] < d[f]))
					f = vertex;
			}
			
			//move f from fringe to known
			int fromVertex = fringe.get(f);
			fringe.remove(new Integer(f));
			known.put(new Integer(f), new Integer(fromVertex));
			if(distanceAsBirdFlies(f,destination) <= 1)
			{	
				hasPath = true;
				locOfEndOfPath = f;
			}
			
			//add unknown, unfringe vertices that are adjacent to f to the fringe
			for(int vertex: this.getAdjacent(f))
			{
				Integer v = new Integer(vertex);
				if(!(fringe.containsKey(v)||known.containsKey(v)))
				{
					fringe.put(v, f);
				}	
			}	
		}
		if(hasPath)
		{
			ArrayList<Integer> moves = new ArrayList<Integer>();
			moves.add(locOfEndOfPath);
			int currVertex = locOfEndOfPath;
			while(currVertex != source)
			{
				currVertex = known.get(currVertex);
				moves.add(0,currVertex);
			}
			return moves;
		}	
		return null;
	}
	
	
    /*---------------------------------------------------------------------
    |  Method connected
    |
    |  Purpose:  This method determines whether the graph is connected or not.
    |			 It does this by maintaining a set of vertices that 
    |			 are accounted for and after performing a breadth 
    |			 first search starting from any active vertex, and then
    |			 comparing the number of elements in that set to the 
    |			 number of vertices in the graph. The unused 
    |			 vertices are added to the graph at the beginning of 
    |			 the implementation, and if the graph is indeed 
    |			 connected, the breadth first search will add all 
    |			 the active vertices to the set of known vertices. 
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: None.
    |
    |  Parameters: None.
    |
    |  Returns:  true if the graph is connected, false otherwise
    *-------------------------------------------------------------------*/
	public boolean connected()
	{
		Set<Integer> accountedFor = new TreeSet<Integer>(); //set of vertices we know about
		List<Integer> unused = getUnusedVertices(); //list of unused vertices
		accountedFor.addAll(unused);  //since we know about the unused vertices
		Integer source = 0;   //source is the location we will start BFS with
		while(source < numVertices) //this loop will find the first active vertex and use it as the source
		{
			if(!unused.contains(source))
				break;
			source++;
		}		
		if(source == 40)     //In case somehow all the vertices were unused.
			return true;
		
		Queue<Integer> breadthFirstSearchQueue = new LinkedList<Integer>();
		breadthFirstSearchQueue.add(source);
		
		while(!breadthFirstSearchQueue.isEmpty())
		{
			List<Integer> neighbors = getAdjacent(breadthFirstSearchQueue.peek());
			for(Integer neighbor : neighbors)
			{
				if(!breadthFirstSearchQueue.contains(neighbor) && !accountedFor.contains(neighbor))
					breadthFirstSearchQueue.add(neighbor);
			}
			accountedFor.add(breadthFirstSearchQueue.remove());
		}	
		
		return accountedFor.size() == numVertices;
	}
	
	public int distanceAsBirdFlies(int source, int destination)
	{
		int rowDiff = Math.abs(source/cols - destination/cols);
		int colDiff = Math.abs(source%cols - destination%cols);
		return rowDiff + colDiff;
	}
	
	
	
	public void printMatrix()
	{
		for(int[] ints : adjacencyMatrix)
		{
			for(int i: ints)
				if(i<0)
					System.out.print(i+" ");
				else
					System.out.print(" " + i+" ");
			System.out.println();
		}
	}
}
