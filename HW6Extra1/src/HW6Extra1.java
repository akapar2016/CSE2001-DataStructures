/*

  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: CSE2010
  Section: 23
  Description: Use graphs to make a user interactive game of Tron

 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HW6Extra1
{
	static Vertex player = null;			//pointer for just the player
	static Vertex exit = null;			//pointer for exit
	static LinkedList<Vertex> bugs = new LinkedList<Vertex>();	//pointer for all bugs
	static ArrayGraph graph;				//playing graph
    
	
	public static void main(String[] args) {
		//getting data from file
		Scanner scanner = null;
		if (args.length == 1) {										//when file name is entered, let scanner use that as input
			final File file = new File(args[0]);
			try {
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found!");
				e.printStackTrace();
			}
		} else {
			System.out.println("File Not Found!"); 					//start with system in(keyboard) if no file input
		}
		
		getGraph(scanner);	//method stores data from file into 2d graph
		//end of getting data from file
		
		graph.printG();		//print full graph
		
		//reset scanner to be user input
		scanner = new Scanner(System.in);
		scanner.reset();
		
		//boolean values to know the end of the game
		boolean bugsWin = false;
		boolean playerWin = false;
		
		
		//runs until player reaches exit, or bug reaches player.
		while(true) {
			//user movement
			System.out.print("Please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
			
			boolean movement = playerMovement(scanner.next().toCharArray()[0]);	//move player
			if(!movement) {														//if movement invalid, restart loop
				System.out.println("Invalid Movement");
				continue;
			}
			
			if(player.coordnates.equals(exit.coordnates)) {						//if Player reaches end, player wins
				System.out.println("Player Wins!");
				playerWin = true;
				break;
			}
			
			graph.printG();						//print player move
			
			//for each bug
			for(int i = 0; i < bugs.size(); i++) {
				
				LinkedList<Vertex> list = bugMovement(bugs.get(i));	//find bug movement
				
				//list is returned null if bug has no where to go
				if(list == null) {
					System.out.println("Bug " + bugs.get(i) + " Cant Move");
				
				//list is returned with only one thing if bug is where the player is
				} else if(list.size() == 1) {
					bugsWin = true;
					System.out.println("A bug is not hungry any more!");
					break;
					
				} else {
					//Print the bug name, distance, and coordnates
					System.out.print("Bug " + bugs.get(i).toString() + ": " 
							+ list.get(0).coordnates.charMovement(list.get(1).coordnates) + " " + (list.size() - 1) + " ");
					for(Vertex print: list) {
						System.out.print("(" + print.coordnates.getY() + "," + print.coordnates.getX() + ") ");
					}
					System.out.println();
					
					//actually move the bug on the graph
					graph.change(list.get(0).coordnates,
							new Vertex(" ", list.get(0).coordnates.getX(), list.get(0).coordnates.getY()));
					
					graph.change(list.get(1).coordnates,
							new Vertex(bugs.get(i).toString(), list.get(1).coordnates.getX(), list.get(1).coordnates.getY()));
					bugs.remove(i);
					bugs.add(i, graph.get(list.get(1).coordnates));
				}
			}
			graph.printG();						//print bug move
			
			for(Vertex bug: bugs) {
				if(bug.coordnates.compareTo(player.coordnates) == 0) {
					System.out.println("A bug is not hungry any more!");
					return;
				}
			}
		}
    }
    
	/**
	 * Places data from file into graph
	 * @param scanner of input file
	 */
    static void getGraph(Scanner scanner) {
	    	//size of array
	    	int xValue = scanner.nextInt();
	    	int yValue = scanner.nextInt();
	    	
	    	graph = new ArrayGraph(xValue, yValue);
	    	
	    	scanner.useDelimiter("");										//count all spaces
	    	for(int y = 0; y < yValue; y++) {
	    		scanner.nextLine();											//skip the new line sign
	    		for(int x = 0; x < xValue; x++) {
	        		graph.graph[x][y] = new Vertex(scanner.next(), x, y);		//place in graph
					//set pointers for player, exit, and bugs
	        		if(graph.graph[x][y].isPlayer()) {
					player = graph.graph[x][y];
				}
				if(graph.graph[x][y].isBug()) {
					bugs.add(graph.graph[x][y]);
				}
				if(graph.graph[x][y].isExit()) {
					exit = graph.graph[x][y];
				}
	        	}
	    	}
    }

    /**
     * Move the player according to input
     * @param input where to move, using u, d,l,r char values
     * @return	boolean if movement was successful
     */
	static boolean playerMovement(char input) {
		
		//move is the value of X or Y to move. 
		Coordinates move = new Coordinates(0, 0);
		if(input == 'd') {
			move.setY(1);
		}
		if(input == 'u') {
			move.setY(-1);
		}
		if(input == 'l') {
			move.setX(-1); 
		}
		if(input == 'r') {
			move.setX(1); 
		}
		//the coordnates of the player
		Coordinates playerCoord = player.coordnates;
		
		//combine both move and player to get the final position
		Coordinates endMovement = playerCoord.add(move);
		
		/*
		 * Check if the player reached the end and if the palyer entered the exit from the top
		 * To win the game, Tron needs to be above the I/O tower,
		 * that is, goes into the cell where the I/O tower is.
		 * Otherwise, it will have problem.
		 */
		if(endMovement == exit.coordnates && playerCoord == exit.coordnates.add(new Coordinates(0,-1))) {
			graph.change(playerCoord, new Vertex(" ", playerCoord.getX(), playerCoord.getY()));
			graph.change(endMovement, new Vertex("T", endMovement.getX(), endMovement.getY()));
			player = graph.get(endMovement);
			
			return true;
		}
		
		//Return movement failed if the player is trying to move into a space that is not empty
		if(!graph.get(endMovement).isSpace()) {
			return false;
		}
		
		//Move player
		graph.change(playerCoord, new Vertex(" ", playerCoord.getX(), playerCoord.getY()));
		graph.change(endMovement, new Vertex("T", endMovement.getX(), endMovement.getY()));
		player = graph.get(endMovement);
		
		return true;
	}
	
	/**
	 * Peforms BFS to find the shortest route
	 * @param bug vertex with coordnates
	 * @return	bug movement coordnates list
	 */
	static LinkedList<Vertex> bugMovement(Vertex bug) {
		//use BFS to find the shortest route
		LinkedList<Coordinates> coords = graph.BFS(bug.coordnates, player.coordnates);
		//if no routes, return null
		if(coords == null) {
			return null;
		}
		
		//convert to Vertex to return
		LinkedList<Vertex> ret = new LinkedList<Vertex>();
		for(int i = coords.size() - 1; i >= 0; i--) {
			ret.add(graph.get(coords.get(i)));
		}
		
		return ret;
		
	}
    

    
    

}
