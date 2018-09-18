/*
	Author: Aayush Kapar
	Email: akapar2016@my.fit.edu
	Course: CSE 2010
	Section: 23
  	Description: Facilitate the storage and transaction of fitCoins using HeapAdaptablePriorityQueue

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class HW4
{
	//heap priority queue using BuyerKey class as key and Profile for value
	static HeapAdaptablePriorityQueue<BuyerKey,Profile> buyers = new HeapAdaptablePriorityQueue<BuyerKey,Profile>();
	//heap priority queue using SellerKey class as key and Profile for value
	static HeapAdaptablePriorityQueue<SellerKey,Profile> sellers = new HeapAdaptablePriorityQueue<SellerKey,Profile>();
	
    public static void main(String[] args) {
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
			scanner = new Scanner(System.in); 						//start with system in(keyboard) if no file input
		}
		
		while(scanner.hasNextLine()) {								//run till end of file		
			final String input = scanner.nextLine();
			final String[] inputValues = input.split(" ");			//take one line in input and put into array
			
			inputValues[1] = 										//change time imput to make sure its only 6 digits long
					inputValues[1].substring(inputValues[1].length() - 6, inputValues[1].length());
			
			System.out.print(inputValues[0] + " " + inputValues[1]);	//print out function and time
			
			switch (inputValues[0]) {			//check which function to run
				case "EnterBuyOrder" :
				case "EnterSellOrder" :
					enter(inputValues);
					break;
				
				case "CancelBuyOrder" :
				case "CancelSellOrder" :
					cancel(inputValues);
					break;

				case "ChangeBuyOrder" :
				case "ChangeSellOrder" :
					change(inputValues);
					break;
					
				case "DisplayHighestBuyOrder" :
				case "DisplayLowestSellOrder" :
					display(inputValues);
					break;
					
				default:							//if function in invalid, throw
					throw new IllegalArgumentException("Invalid function: " + input);
			}
		}
    }
    
    /**
     * Entry method for both sellers and buyers according to function
     * Checks if seller or buyer according to first function string in array
     * Checks if name is already in the Heap, if not, add to Heap
     * @param input array of inputs from file in order
     */
    static void enter(String[] input) {
    		System.out.printf(" %s %s %s" , input[2], input[3], input[4]);	//print Name, Price, Quantity
    	
    		Profile profile;
    		
    		switch (input[0]) {											//check seller or buyer
    			case "EnterBuyOrder" :
	    	    		if(buyerByName(input[2]) != null) {					//check if user exists
	    	    			System.out.println(" ExistingBuyerError");
	    	    			return;
	    	    		}
	    	    		//declare values
	    	    		BuyerKey bKey = new BuyerKey(input[3], Integer.parseInt(input[1]));
	    	    		profile = new Profile(input[2], Integer.parseInt(input[4]));
	    	    		
	    	    		buyers.insert(bKey, profile);						//insert to buyers heap
	    	    		break;
	    	    		
    			case "EnterSellOrder" :
	    	    		if(sellerByName(input[2]) != null) {					//check if user exists
	    	    			System.out.println(" ExistingSellerError");
	    	    			return;
	    	    		}
	    	    		
	    	    		//declare values
	    	    		SellerKey sKey = new SellerKey(input[3], Integer.parseInt(input[1]));
	    	    		profile = new Profile(input[2], Integer.parseInt(input[4]));
	    	    		
	    		    	sellers.insert(sKey, profile);						//insert to buyers heap
	    	    		break;
    		}
    		
    		//Print into next line, needed at the end since unsure if existing buyer error would occur
    		System.out.println();
    		
    		//check if sell occurs
    		checkTransaction(input[1]);
    }
    
    /**
     * Cancel method for both sellers and buyers according to function
     * Checks if seller or buyer according to first function string in array
     * Checks if name is not in the Heap, if it is,
     * Removes Profile from Heap
     * @param input array of inputs from file in order
     */
	static void cancel(String[] input) {
		System.out.printf(" %s" , input[2]);							//print Name
		
		switch (input[0]) {											//check seller or buyer
			case "CancelBuyOrder" :
				Entry<BuyerKey, Profile> BEntry = buyerByName(input[2]);
				
				if(BEntry == null) {									//check if user exists
					System.out.println(" noBuyerError");
					return;
				}
				
				buyers.remove(BEntry);								//remove from heap
				break;
				
			case "CancelSellOrder" :
				Entry<SellerKey, Profile> SEntry = sellerByName(input[2]);
				
				if(SEntry== null) {									//check if user exists
					System.out.println("  noSellerError");
					return;
				}
				
				sellers.remove(SEntry);								//remove from heap
		}

		//Print into next line, needed at the end since unsure if existing buyer error would occur
		System.out.println();
		
		//check if sell occurs
		checkTransaction(input[1]);
	}
	
	/**
	 * Change method for both sellers and buyers according to function
     * Checks if seller or buyer according to first function string in array
     * Checks if name is not in the Heap, if it is,
     * Changes Profile values from Heap
     * Only changes quantity and price
     * Updates Profile with new timestamp as well
	 * @param input array of inputs from file in order
	 */
	static void change(String[] input) {
		System.out.printf(" %s %s %s" , input[2], input[3], input[4]);	//print Name, Price, Quantity
		switch (input[0]) {
			case "ChangeBuyOrder" :									//check seller or buyer
				Entry<BuyerKey, Profile> bEntry = buyerByName(input[2]);
				
				if(bEntry== null) {									//check if user exists
					System.out.println("  noBuyerError");
					return;
				}
				
				//If there is a change in quantity
				if(Integer.parseInt(input[4]) != bEntry.getValue().quantity) {
					
					//change the quantity
					buyers.replaceValue(bEntry, new Profile(input[2], Integer.parseInt(input[4])));
					//change the timestamp to this new timestamp
					buyers.replaceKey(bEntry, new BuyerKey(bEntry.getKey().price.toPlainString(), Integer.parseInt(input[1])));
				}
				
				//If there is a change in price
				if(bEntry.getKey().price.compareTo(new BigDecimal(input[3])) != 0) {
					//change timestamp and price
					buyers.replaceKey(bEntry, new BuyerKey(input[3], Integer.parseInt(input[1])));
				}
				break;
				
			case "ChangeSellOrder" :
				Entry<SellerKey, Profile> sEntry = sellerByName(input[2]);
				
				if(sEntry== null) {									//check if user exists
					System.out.println("  noSellerError");
					return;
				}
				
				//If there is a change in quantity
				if(Integer.parseInt(input[4]) != sEntry.getValue().quantity) {
					//change the quantity
					sellers.replaceValue(sEntry, new Profile(input[2], Integer.parseInt(input[4])));
					//change the timestamp to this new timestamp
					sellers.replaceKey(sEntry, new SellerKey(sEntry.getKey().price.toPlainString(), Integer.parseInt(input[1])));

				}
				//If there is a change in price
				if(sEntry.getKey().price.compareTo(new BigDecimal(input[3])) != 0) {
					//change timestamp and price
					sellers.replaceKey(sEntry, new SellerKey(input[3], Integer.parseInt(input[1])));
				}
				break;
		}
		
		//Print into next line, needed at the end since unsure if existing buyer error would occur
		System.out.println();
		
		//check if sell occurs
		checkTransaction(input[1]);

	}	
	
	/**
	 * Change method for both sellers and buyers according to function
     * Checks if seller or buyer according to first function string in array
     * Check if Profile is in the heap, if it is not, do not print anything
     * If multiple Profiles have same price, displays Profile with oldest timestamp
	 * @param input array of inputs from file in order
	 */
	static void display(String[] input) {
		switch (input[0]) {										//check seller or buyer
			case "DisplayHighestBuyOrder" :
				if (buyers.isEmpty()) {break; }					//break if there are no buyers
				Entry<BuyerKey, Profile> bEntry = buyers.min();	
				
				//print name, time, price, quantity
				System.out.printf(" %s %06d %s %s",
						bEntry.getValue().name, bEntry.getKey().time, bEntry.getKey().price, bEntry.getValue().quantity);
			break;
			
			case "DisplayLowestSellOrder" :
				if (sellers.isEmpty()) {break; }					//break if there are no sellers
				Entry<SellerKey, Profile> sEntry = sellers.min();
				
				//print name, time, price, quantity
				System.out.printf(" %s %06d %s %s",
						sEntry.getValue().name, sEntry.getKey().time, sEntry.getKey().price, sEntry.getValue().quantity);
			break;
			
		}
		
		//Print into next line.
		System.out.println();
	}
	
	/**
	 * @param name of seller
	 * @return Object Entry with sellerKey and profile
	 */
	static Entry<SellerKey, Profile> sellerByName(String name) {
		
		//look through arraylist of heap and see if any profiles have same name
		for(Entry<SellerKey, Profile> entry : sellers.heap) {
			if(entry.getValue().matchName(name)) {
				return entry;
			}
		}
		return null;		//if there are no Profiles with same name
	}
	
	/**
	 * @param name of seller
	 * @return Object Entry with buyerKey and profile
	 */
	static Entry<BuyerKey, Profile> buyerByName(String name) {
		//look through arraylist of heap and see if any profiles have same name
		for(Entry<BuyerKey, Profile> entry : buyers.heap) {
			if(entry.getValue().matchName(name)) {
				return entry;
			}
		}
		return null;		//if there are no Profiles with same name
	}

	/**
	 * Prints if there is a transaction
	 * Updates quantity of heaps
	 * If quantity == 0, removes from heap
	 * @param time so it can print out time of transaction
	 * @return boolean if there was a sale
	 */
	static void checkTransaction(String time) {
		//keep checking until no more transactions
		while(true) {
			Entry<BuyerKey, Profile> buyerEntry = buyers.min();
			Entry<SellerKey, Profile> sellerEntry = sellers.min();
			
			if(buyerEntry == null || sellerEntry == null) {
				return;
			}
			
			Profile buyer = buyerEntry.getValue();
			Profile seller = sellerEntry.getValue();
			
			BuyerKey buyerKey = buyerEntry.getKey();
			SellerKey sellerKey = sellerEntry.getKey();
			
			if(buyerKey.price.compareTo(sellerKey.price) == -1) {
				return;
			}
			
			
			BigDecimal price = (buyerKey.price.add(sellerKey.price)).divide(new BigDecimal(2));
			
			int quantity = 0;
			
			if(seller.quantity >= buyer.quantity) {
				quantity = buyer.quantity;
				seller.quantity = seller.quantity - quantity;
				buyer.quantity = 0;
				buyers.removeMin();
			} else {
				quantity = seller.quantity;
				seller.quantity = 0;
				buyer.quantity = buyer.quantity - quantity;
				sellers.removeMin();
			}
			
			System.out.printf("%s %s %s%n", "ExecuteBuySellOrders", price, quantity);
			System.out.println("Buyer: " + buyer.name + " " + buyer.quantity);
			System.out.println("Seller: " + seller.name + " " + seller.quantity);
		}

	}

}
