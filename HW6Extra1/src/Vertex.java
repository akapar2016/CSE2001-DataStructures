/*

  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: CSE2010
  Section: 23
  Description: Vertex Implementation

 */

public class Vertex implements Comparable<Vertex> {
	
	//boolean values to determine what the vertex is
	private boolean isWall = false;
	private boolean isBug = false;
	private char bugVal;
	private boolean isT = false;
	private boolean isI = false;
	private boolean isSpace = false;
	Coordinates coordnates;	//like a doublely linked list. easier to find where the Vertex is
	/**
	 * take in string and turn the right boolean true. also declare coordnates
	 * @param entry String
	 * @param x int
	 * @param y int
	 */
	Vertex(String entry, int x, int y) {
		if(entry.equals("T")) {
			isT = true;
		} else if(entry.equals("I")) {
			isI = true;
		} else if(entry.equals("#")) {
			isWall = true;
		} else if (entry.equals(" ")) {
			isSpace = true;
		}else {
			isBug = true;
			bugVal = entry.charAt(0);
		}
		coordnates = new Coordinates(x, y);
	}
	
	/**
	 * Boolean to string
	 */
	public String toString(){
		if(isWall) {
			return "#";
		}
		if(isT) {
			return "T";
		}
		if(isI) {
			return "I";
		}
		if(isSpace) {
			return " ";
		}
		return Character.toString(bugVal);
	}
	
	public boolean isPlayer() {
		return isT;
	}
	public boolean isBug() {
		return isBug;
	}
	public boolean isSpace() {
		return isSpace;
	}
	public boolean isExit() {
		return isI;
	}
	
	/**
	 * Compare Coordnates
	 */
	public int compareTo(Vertex other) {
		return this.coordnates.compareTo(other.coordnates);
	}

	
}
