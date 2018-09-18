/*
	Author: Aayush Kapar
	Email: akapar2016@my.fit.edu
	Course: CSE 2010
	Section: 23
  	Description: Key for buyers in heap priority queue

 */
import java.math.BigDecimal;

public class BuyerKey implements Comparable<BuyerKey> {

	BigDecimal price;
	Integer time;
	
	/**
	 * Constructor
	 * @param price to buy, cannot buy higher
	 * @param timestamp
	 */
	BuyerKey(String price, int time) {
		this.price = new BigDecimal(price);
		this.time = time;
	}
	
	/**
	 * returns the opposite compareTo for price to sort in accending order
	 * If price are equal, returns compareTo of timestamps
	 */
	public int compareTo(BuyerKey other) {
		int compare = this.price.compareTo(other.price);
		
		if(compare != 0) {
			return convert(compare);
		}
		
		return this.time.compareTo(other.time);
	}
	
	/**
	 * @param value 1 or -1
	 * @return the opposite of 1 or -1
	 */
	private int convert(int value) {
		return value == 1 ?  -1: 1;
	}


}
