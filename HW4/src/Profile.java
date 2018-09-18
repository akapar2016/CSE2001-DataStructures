/*
	Author: Aayush Kapar
	Email: akapar2016@my.fit.edu
	Course: CSE 2010
	Section: 23
  	Description: Profiles for buyers and sellers

 */

public class Profile   {
	
	String name;
	int quantity;
	
	/**
	 * Constructor
	 * @param name
	 * @param quantity or stock
	 */
	Profile(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	/**
	 * check if name matches input string
	 * @param name String
	 * @return boolean if true
	 */
	boolean matchName(String name) {
		return this.name.compareTo(name) == 0 ? true:false;
	}
	
}
