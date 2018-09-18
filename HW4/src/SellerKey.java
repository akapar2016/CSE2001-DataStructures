/*
	Author: Aayush Kapar
	Email: akapar2016@my.fit.edu
	Course: CSE 2010
	Section: 23
  	Description: Key for sellers in heap priority queue

 */

import java.math.BigDecimal;

public class SellerKey implements Comparable<SellerKey> {

	BigDecimal price;
	Integer time;
	
	/**
	 * Constructor
	 * @param price to sell, can sell for higher
	 * @param timestamp
	 */
	SellerKey(String price, int time) {
		this.price = new BigDecimal(price);
		this.time = time;
	}
	
	/**
	 * returns compareTo for price
	 * If price are equal, returns compareTo of timestamps
	 */
	public int compareTo(SellerKey other) {
		int compare = this.price.compareTo(other.price);
		
		if(compare != 0) {
			return compare;
		}
		
		return this.time.compareTo(other.time);
	}


}
