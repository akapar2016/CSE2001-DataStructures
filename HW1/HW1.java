package HW1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: cse2010
  Section: 23
  Description: Homework1 - product and seller stock management system
 */

public class HW1
{
	/*
	 * Class contains product name and list of sellers.
	 * info about seller include name, stock and shipping cost
	 */
	static class product {
		private String name;
		private SinglyLinkedList<seller> sellers = new SinglyLinkedList<seller>();
		
		//Private seller object that contains info only for the specific product
		private class seller implements Comparable<seller> {
			private String name;
			private Float price;
			private Float shipping;
			private int stock;
			
			seller(String name, Float price) {
				this.name = name;
				this.price = price;
			}
			
			//compare only total price
			//used for sorting
			@Override
			public int compareTo(seller other) {
				final float totalThis = this.price + this.shipping;
				final float totalOther = other.price + other.shipping;
				if((totalThis) <= (totalOther)) {
					return -1;
				}
				return 1;
			}

		}
		
		//product constructor
		product(String name) {
			this.name = name;
		}
		
		//add seller to product seller
		void addProdSeller (String name, float price) {
			sellers.addLast(new seller(name, price));
		}
		
		//find index of seller using just name of seller
		//search only name
		int sellerIndexFromName (String name) {
			final int numOfSellers = sellers.size();
			//verify there are sellers before running loop
			if (numOfSellers == 0) {
				return -1;
			}
			//selection search for name
			for (int index = 0; index < numOfSellers; index++) {
				if (sellers.getFromIndex(index).name.equals(name)) {
					return index;
				}
			}
			//none found
			return -1;
			
		}
		
		//Print all sellers for the product
		//formats each correctly
		void DisplaySellerList() {
			System.out.printf("%10s %13s %13s %11s\n", "seller", "productPrice", "shippingCost", "totalCost ");
			
			//go through each seller
			for(int count = 0; count < sellers.size(); count++) {
				final seller temp = sellers.getFromIndex(count);
				//only print if stock is more than 0
				if (temp.stock > 0) {
					System.out.printf("%10s", temp.name);
					
					//print price
					if (temp.price == null) {
						System.out.printf("%14s", "null");
					} else {
						System.out.printf("%14.2f", temp.price);
					}
					
					//print shipping
					if (temp.shipping == null) {
						System.out.printf("%14s", "null");
					} else {
						System.out.printf("%14.2f", temp.shipping);
					}
					
					//print total
					if (temp.price == null || temp.shipping == null) {
						System.out.printf("%12s\n", "null ");
					} else {
						System.out.printf("%11.2f %s\n", temp.price + temp.shipping, " ");
					}
				}
			}
		}

		//Add to seller in accending order
		public void addAcc(seller tempSeller) {
			//add temp seller to first if size == 0
			if(sellers.size() == 0) {
				sellers.addFirst(tempSeller);
				//TEST// System.out.println("_________________AddedSeller0___________" + tempSeller.name);
				return;
			}
			
			//add temp seller to where it fits between other sellers according to just price
			for (int count = 0; count < sellers.size(); count++) {
				if (sellers.getFromIndex(count).compareTo(tempSeller) >= 0) {
					sellers.addAtIndex(count, tempSeller);
					//TEST// System.out.println("_________________AddedSeller____________" + tempSeller.name);
					return;
				} 
			}
			
			//If no space is found, add to last(Means must to the most expensive)
			sellers.addLast(tempSeller);
			//TEST// System.out.println("_________________AddedSellern____________" + tempSeller.name);
			return;
		  }

		//calc shipping cost for each seller for product
		//Send each seller to be readded to list sorted
		void calcCost (SinglyLinkedList<sellerProfile>  mainSellers) {
			//temp list to store unsorted sellers
			SinglyLinkedList<seller> tempSellers = new SinglyLinkedList<seller>();
			final int size = sellers.size();
			
			//copy all of unsorted list to temp list and remove from unsorted list
			for (int count = 0; count < size; count++) {
				tempSellers.addFirst(sellers.removeFirst());
			}
			
			sellers.clear(); // verify its all clear
			
			for(int count = 0; count < size; count++) {
				//get all used values out
				final seller tempSeller = tempSellers.removeFirst();
				Float price = tempSeller.price;
				int mainSellerIndex = HW1.findSellIndexFromName(tempSeller.name);
				Float minShip = mainSellers.getFromIndex(mainSellerIndex).minShipping;
				Float ship = mainSellers.getFromIndex(mainSellerIndex).shipping;
				
				//look though all sellers in product seller list
				//add shipping price to product seller
				if (price.compareTo(minShip) <= 0) {
					tempSeller.shipping = ship;
				} else {
					//shipping price turns to 0
					tempSeller.shipping = (float) 0;
				}
				
				addAcc(tempSeller); //add to list while sorting
				
			}
		}
	}
	
	/*
	 * Class contains seller profile
	 * Includes name, min for free shipping, and shipping cost
	 */
	static class sellerProfile {
		private String name;
		private Float shipping;
		private Float minShipping;
		
		sellerProfile(String name) {
			this.name = name;
		}
		sellerProfile(String name, Float ship, Float minShip) {
			this.name = name;
			this.shipping = ship;
			this.minShipping = minShip;
		}
		void addShip(float minShip, float ship) {
			this.minShipping = minShip;
			this.shipping = ship;
		}
	}
	
	//Lists used for storing all products and sellerProfiles
	private static SinglyLinkedList<product> products = new SinglyLinkedList<product>();
	private static SinglyLinkedList<sellerProfile> mainSellerProfiles = new SinglyLinkedList<sellerProfile>();
	//scanner used for getting input
	private static Scanner scanner;
	
	//add product with name from input array
	static void addProduct (String name) {
		final product temp = new product(name);
		products.addLast(temp);
	}
	
	//find index of product using just name of products
	static int findProdIndexFromName (String name) {
		//verify there are products before running loop
		if (products.size() == 0) {
			return -1;
		}
		//selection search for name
		for (int index = 0; index < products.size(); index++) {
			if (products.getFromIndex(index).name.equals(name)) {
				return index;
			}
		}
		//none found
		return -1;
	}
	
	//find index of seller using just name of product
	static int findSellIndexFromName (String name) {
		//verify there are products before running loop
		if (mainSellerProfiles.size() == 0) {
			return -1;
		}
		//selection search for name
		for (int index = 0; index < mainSellerProfiles.size(); index++) {
			if (mainSellerProfiles.getFromIndex(index).name.equals(name)) {
				return index;
			}
		}
		//none found
		return -1;
	}
	
	/*
	 *  ------------------------------------------------------
	 *  Bottom methods are direct functions called from file
	 *  ------------------------------------------------------
	 */
	

	/*Set price of  product
	 * 	If product is on list, work with that product
	 * 	If product is not on list, add new product to list
	 * 	Set price of product with new seller if same seller is not found
	 * 	Set price of product with existing seller if found
	 */
	static void setProductPrice (String[] input) {

		final String productName = input[1];
		final String sellerName = input[2];
		final float price = Float.parseFloat(input[3]);
		
		//Check if product is already on list
		int productIndex = findProdIndexFromName(productName);
		int sellerIndex;
		if (-1 == productIndex) {
			addProduct(productName);
			productIndex = products.size() - 1;
		}
		//Check if seller is already on list and get index
		sellerIndex = findSellIndexFromName(sellerName);
		
		//if seller is not on list
		if(sellerIndex == -1) {
			
			//add new seller to sellers list
			mainSellerProfiles.addFirst(new sellerProfile(sellerName));
			//add new seller to product sellers list with price
			products.getFromIndex(productIndex).addProdSeller(sellerName, price);
			return;
			
		} else {
			
			//get index of seller in the product sellers list
			final int prodSellerIndex = products.getFromIndex(productIndex).sellerIndexFromName(sellerName);
			//if seller is not in product sellers list, add seller in assending order
			if (prodSellerIndex == -1) {
				products.getFromIndex(productIndex).addProdSeller(sellerName, price);
			} else {
				products.getFromIndex(productIndex).sellers.getFromIndex(prodSellerIndex).price = price;
			}
			
		}
	}
	
	/*Set shipping cost of seller
		 * goes through each product.
		 * adds this seller if not found
		 * add info for seller if found
		 * Each seller is on every single product(waste of space)
	 */
	static void setShippingCost(String[] input) {
		//get values from input
		final String sellerName = input[1];
		final float ship = Float.parseFloat(input[3]);
		final float minShip = Float.parseFloat(input[2]);
		
		//find main seller profile for that seller and add shipping info
		final int sellerIndex = findSellIndexFromName(sellerName);
		if (sellerIndex == -1) {
			//add new seller profile
			mainSellerProfiles.addFirst(new sellerProfile(sellerName, minShip, ship));
		} else {
			//add new shipping info to seller profile
			mainSellerProfiles.getFromIndex(sellerIndex).addShip(minShip, ship);
		}
		
	}
	
	/*Increase product inventory
	 *	increase by specified on input
	 *	return updated stock
	 *DOES NOT WORK IF PRODUCT OR SELLER HAVE NOT BEEN ADDED ALREADY
	 *HOWEVER ASSIGNMENT DOES NOT REQUIRE IT(meets requirements for assignment)
	 */
	static int increaseInventory (String[] input) {
		//get values
		final String productName = input[1];
		final String sellerName = input[2];
		final float quantity = Float.parseFloat(input[3]);
		
		//find index of product and seller to increase
		final int productIndex = findProdIndexFromName(productName);
		final int sellerIndex = products.getFromIndex(productIndex).sellerIndexFromName(sellerName);
		
		//update the inventory
		products.getFromIndex(productIndex).sellers.getFromIndex(sellerIndex).stock += quantity;
		return products.getFromIndex(productIndex).sellers.getFromIndex(sellerIndex).stock;
	}
	
	/*Decrease product inventory by specified amount
	 *	if not enough in stock, cancel full order
	 *	return updated stock or not enough message
	 *DOES NOT WORK IF PRODUCT OR SELLER HAVE NOT BEEN ADDED ALREADY
	 *HOWEVER ASSIGNMENT DOES NOT REQUIRE IT(meets requirements for assignment)
	 */
	static int customerPurchase (String[] input) {
		//get values
		final String productName = input[1];
		final String sellerName = input[2];
		final float quantity = Float.parseFloat(input[3]);
			
		//find index of product and seller to increase
		final int productIndex = findProdIndexFromName(productName);
		final int sellerIndex = products.getFromIndex(productIndex).sellerIndexFromName(sellerName);
				
		//check if there is enough in stock
		if (products.getFromIndex(productIndex).sellers.getFromIndex(sellerIndex).stock < quantity) {
			return -1;
		}
		//update the inventory
		products.getFromIndex(productIndex).sellers.getFromIndex(sellerIndex).stock -= quantity;
		return products.getFromIndex(productIndex).sellers.getFromIndex(sellerIndex).stock;
	}
	
	/*Display all sellers with stock on screen
	 *	use DisplaySellerList from product class
	 */
	static void displaySellerList (String[] inputs) {
		final int index = findProdIndexFromName(inputs[1]);
		products.getFromIndex(index).calcCost(mainSellerProfiles);
		products.getFromIndex(index).DisplaySellerList();
	}
	
	
    public static void main(String[] args) {
    		if (args.length == 1) {
    			//when file name is entered, let scanner use that as input
    			final File file = new File(args[0]);
    			try {
					scanner = new Scanner(file);
				} catch (FileNotFoundException e) {
					System.out.println("File Not Found!");
					e.printStackTrace();
				}
    		} else {
    			scanner = new Scanner(System.in); //start with system in(keyboard)
    		}
    		
    		//run till end of file
    		while(scanner.hasNextLine()) {		
    			//take one line in input and put into array
    			final String input = scanner.nextLine();
    			final String[] inputValues = input.split(" ");
    			//all possible functions required
    			
    			//for checking runtime
    			//final double time0 = System.currentTimeMillis();
    			
    			switch (inputValues[0]) {
    				case "SetProductPrice" :
    					System.out.println(input);
    					setProductPrice(inputValues);
    					break;
    					
    				case "SetShippingCost" :
    					setShippingCost(inputValues);
    					System.out.println(input);
    					break;
    					
    				case "IncreaseInventory" :
    					final int ret = increaseInventory(inputValues);
    					System.out.println(input + " " + ret);
    					break;
    					
    				case "CustomerPurchase" :
    					final int ret1 = customerPurchase(inputValues);
    					if (ret1 == -1) {
        					System.out.println(input + " NotEnoughInventoryError");
    					} else {
    						System.out.println(input + " " + ret1);
    					}
    					break;
    					
    				case "DisplaySellerList" :
    					System.out.println(input);
    					displaySellerList(inputValues);
    					break;
    					
    				default:
    					throw new IllegalArgumentException("Invalid function: " + input);
    			}
    		
    			//for checking runtime
    			//final double time1 = System.currentTimeMillis();
    			//System.out.println(time1 - time0);
    		
    		}
    }
}