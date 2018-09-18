/*

  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: CSE2010
  Section: 23
  Description: Graph using a 2d array

 */
import java.util.LinkedList;
import java.util.Queue;

public class arrayGraphExtra2 {
	private int xValue;
	private int yValue;
	Vertex[][] graph;
	
	/**
	 * create graph
	 * @param x size
	 * @param y size
	 */
	arrayGraphExtra2(int x, int y) {
		xValue = x;
		yValue = y;
		graph = new Vertex[x][y];
	}
	
	/**
	 * Print graph with axis numbers
	 */
	void printG() {	
		System.out.print(" ");
		for(int x = 0; x < xValue; x++) {
			System.out.print(x);
		}
		System.out.println();
		for(int y = 0; y < yValue; y++) {
			System.out.print(y);
			for(int x = 0; x < xValue; x++) {
				System.out.print(graph[x][y].toString());	//uses boolean values in Vertex class
			}
		System.out.println();
		}
	}
	
	/**
	 * Get Vertex from Coordnates
	 * @param coord Coordnates
	 * @return Vertex
	 */
	Vertex get(Coordinates coord) {
		return graph[coord.getX()][coord.getY()];
	}
	
	/**
	 * Change value
	 * @param coordnate value to change
	 * @param update	new vertex to change to
	 */
	void change(Coordinates coord, Vertex update) {
		graph[coord.getX()][coord.getY()] = update;
	}
	
	/**
	 * BFS returns list to shortest path
	 * @param start	Coodnates
	 * @param end	Coordnates
	 * @return	List of Coordnates to the shortest path
	 */
    LinkedList<Coordinates> BFS(Coordinates start, Coordinates end, LinkedList<Coordinates> player) {
    		//if already at end, return null

    		//order of movement
    		Coordinates[] movement = new Coordinates[4];
    		movement[0] = new Coordinates(0,-1);	//move up first
    		movement[1] = new Coordinates(0, 1);	//move down second
    		movement[2] = new Coordinates(-1,0);	//move left third
    		movement[3] = new Coordinates( 1,0);	//move right fourth
    		
    		//Temp graph to use to move. Spaces alreadh checked will be changed to walls
    		Vertex [][] tempGraph = new Vertex[graph.length][];
    		for(int i = 0; i < graph.length; i++)
    			tempGraph[i] = graph[i].clone();
    		
    		//Que for BFS
    		Queue<Coordinates> que = new LinkedList<Coordinates>();
    		//Includes all coordnates used in que with a backCourse pointer to where it came from
    		LinkedList<Coordinates> list = new LinkedList<Coordinates>();
    		
    		//add initial position of bug
        list.add(start);
        list.getLast().addBack(null);	//adding backcourse to be able to trace back
    		que.add(start);
        
    		//if start and end are already equal, return a list with only one thing
    		if(start.compareTo(end) == 0) {
                return list;
        }
    		
    		//repeat until there is nothing else left
        while(que.peek()!=null) {
             Coordinates curr = que.remove();	//from que
             for(int i = 0; i < 4; i++) {	//for all the 4 differnet types of movements
            	 	//check if movement is valid(is a space or the player)
                if(tempGraph[curr.add(movement[i]).getX()][curr.add(movement[i]).getY()].isSpace() ||
                			tempGraph[curr.add(movement[i]).getX()][curr.add(movement[i]).getY()].isPlayer()) {
                    	
                		Coordinates moved = curr.add(movement[i]);	//the initial postion + the movement so where it is now
                    	//For testing
                		/*
                    	for(Coordnates q: que) {
                    		System.out.print(q.getY() + " " + q.getX() + " . ");
                    	}
                    	System.out.println(moved.getY() + "/" + moved.getX());
                    	*/
                		
                    	list.add(moved);								//contains all movements
                    	list.getLast().addBack(curr);				//where this new movement came from
                    	if(tempGraph[moved.getX()][moved.getY()].isPlayer()) {	//check if reached the player yet
                    		LinkedList<Coordinates> ret = new LinkedList<Coordinates>();
                    		ret.add(list.getLast());								//create a list using backCourses
                    		while(!ret.getLast().equals(start)) {
                    			ret.add(ret.getLast().backCourse);
                    		}
                        return ret;
                     }
                    	//vertexes already checked get replaced with walls so we dont check them again
                    	tempGraph[moved.getX()][moved.getY()] = new Vertex("#", moved.getX(), moved.getY());
                    	que.add(moved);
                    }
                }
            }
        return null;		//no route found
    }
	
}
