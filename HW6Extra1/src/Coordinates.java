/*

  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: CSE2010
  Section: 23
  Description: Coordinate system

 */

class Coordinates implements Comparable<Coordinates> {
		private Integer x, y;	//value of x and y
		public Coordinates backCourse;	//used to be able to backcouse like a linked structure
		
		Coordinates(int x, int y){
			this.x = x;
			this.y = y;
		}

		/**
		 * Compare Integers. used ot check equality
		 */
		public int compareTo(Coordinates other) {
			return Math.abs(this.x.compareTo(other.x)) + Math.abs(this.y.compareTo(other.y));
		}
		
		/**
		 * change x
		 * @param x int
		 */
		public void setX(int x) {
			this.x = x;
		}	
		
		/**
		 * CHange y
		 * @param y int
		 */
		public void setY(int y) {
			this.y = y;
		}
		
		/**
		 * @return x val int
		 */
		public int getX() { return x; }
		
		/**
		 * @return val int
		 */
		public int getY() {return y; }
		
		/**
		 * combine two differnt coordnates by adding x values and adding y values
		 * @param other coordnates
		 * @return new combined coordnates
		 */
		public Coordinates add(Coordinates other) {
			return new Coordinates(this.x + other.x, this.y + other.y);
		}
		
		/**
		 * Add bacCourse to be able to trade in BFS
		 * @param back Coordnate
		 */
		public void addBack(Coordinates back) {
			backCourse = back;
		}
		
		/**
		 * uses two coordnates to see when it came from and where its going to find a direction in char
		 * @param other coordnates
		 * @return char u, d, l ,r
		 */
		public char charMovement(Coordinates other) {
			int xDiff = this.x - other.x;
			int yDiff = this.y - other.y;
			if(xDiff == 1 && yDiff == 0) {
				return 'l';
			}
			if(xDiff == -1 && yDiff == 0) {
				return 'r';
			}
			if(xDiff == 0 && yDiff == 1) {
				return 'u';
			}
			if(xDiff == 0 && yDiff == -1) {
				return 'd';
			}
			return 'e';
		}
}