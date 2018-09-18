/*

Author: Aayush Kapar
Email: akapar2016@my.fit.edu
Course: CSE 2010
Section: 23
Description: HW3 use tree to sort Olympic winners and events

*/

/*
 TEST CASE - OUTPUT 2
 
 Output 1
Line 5 - GetAthleteWithMostMedals 2 Wust Morrison

It is not in lexicographic order however on assignment it says GetAthleteWithMostMedals numberOfMedals athlete [athlete2 ... in alphabetical order if ties exist]

My output is GetAthleteWithMostMedals 2 Morrison Wust
 */

package HW3;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class HW3 {

	private static Tree<String> yearEvent;					//global Tree of event

	/**
	 * @param 1 - data file name, 2 - query file name
	 */
    public static void main(String[] args) {
    		Scanner input = null, queries = null;				//Both file scanners

    		if (args.length == 2) {
     		File file = new File(args[0]);					//Data file scanner
     		try {
 				input = new Scanner(file);
 			} catch (FileNotFoundException e) {
 				System.out.println("File Not Found!");
 				e.printStackTrace();
 			}
     		
     		file = new File(args[1]);						//Queries file scanner
     		try {
 				queries = new Scanner(file);
 			} catch (FileNotFoundException e) {
 				System.out.println("File Not Found!");
 				e.printStackTrace();
 			}

     	} else {
     		System.out.println("No Filename");				//if no file names are found
     		return;
     	}

    		addEvent(input.nextLine());							//add main event - head
    		
    		while(input.hasNext()) {								//add all other lines in data file into tree
    			inputs(input.nextLine());						//adds each line one by one
    		}
    		
    		//yearEvent.printAll();								//test case to print all in tree and their children
    		
    		while(queries.hasNext()) {
    			queries(queries.nextLine());						//run through each query
    		}
    }
    
    /**
     * Adding initial line of the event name
     * Adds all the different sports as well
     * Initializes tree
     * @param input full line of input
     */
    static void addEvent(String input) {
    		Scanner inputValues = new Scanner(input); 				//Use scanner to look through each line
    		String parent = inputValues.next();						//first word in input is the Sport year
    		yearEvent = new Tree<String>(parent);					//initialize tree with top parent
    		while(inputValues.hasNext()) {
    			yearEvent.insertChild(parent, inputValues.next());	//insert each child(sport) in lexicographic order
    		}
    }

    /**
     * Add each line of input to the tree
     * reads line by line from main
     * @param input full line of input
     */
    static void inputs(String input) {
    		Scanner inputValues = new Scanner(input); 				//Use scanner to look through each line
		String parent = inputValues.next();
		
		boolean winner = true;
		
		while(inputValues.hasNext()) {
			String str = inputValues.next();						//useful to be able to check if input is person or not
			if(str.contains(":")) {
				String person = str.substring(0, str.indexOf(":"));
				String country = str.substring(str.indexOf(":") + 1, str.length());
				if(winner) {												//check if its the first input of a person. the first input is the gold winner so its a  winner
					yearEvent.appendChild(parent, person, winner);		//if person, do not add in lexicographic order
					yearEvent.appendChild(person, country, winner);
					winner = false;
				} else {
					yearEvent.appendChild(parent, person);				//if person, do not add in lexicographic order
					yearEvent.appendChild(person, country);
				}
			} else {
				yearEvent.insertChild(parent, str);				//if not person, add in lexicographic order
			}
		}
    }

    /**
     * Ran from main method
     * Uses  switch to see what kind of query was the line
     * Runs appropriate method to answer the query
     * @param input full line of query input
     */
    static void queries(String input) {
    		Scanner inputValues = new Scanner(input);				//Use scanner to look through each line
    		String str = inputValues.next();							//first line is the type of query
    		switch(str) {
    			case "GetEventsBySport":
    				getEventsBySport(inputValues.next());
    				break;
    			
    			case "GetWinnersAndCountriesBySportAndEvent":
    				getWinnersAndCountriesBySportAndEvent(inputValues.next(), inputValues.next());
    				break;
    			
    			case "GetGoldMedalistAndCountryBySportAndEvent":
    				getGoldMedalistAndCountryBySportAndEvent(inputValues.next(), inputValues.next());
    				break;
    			
    			case "GetAthleteWithMostMedals":
    				getAthleteWithMostMedals();
    				break;
    			
    			case "GetAthleteWithMostGoldMedals":
    				getAthleteWithMostGoldMedals();
    				break;
    			
    			case "GetCountryWithMostMedals":
    				getCountryWithMostMedals();
    				break;
    			
    			case "GetCountryWithMostGoldMedals":
    				getCountryWithMostGoldMedals();
    				break;
    			
    			case "GetSportAndEventByAthlete":
    				getSportAndEventByAthlete(inputValues.next());
    				break;
    		}
    }
    
    /**
     * Print all children of the sport
     * @param sport 
     */
    static void getEventsBySport(String sport) {
    		LinkedList<String> children = new LinkedList<String> (yearEvent.getChildren(sport));	//getting list of all the children
    		
    		System.out.print("GetEventsBySport " + sport);				//print first label
    		
    		for(int i = 0; i < children.size(); i++) {					//print each child
    			System.out.print(" " + children.get(i));
    		}
    		System.out.println();
    		
    }

    /**
     * Print all of children of event
     * event needs to be a children of sport
     * @param sport
     * @param event
     */
    static void getWinnersAndCountriesBySportAndEvent (String sport, String event) {
    		LinkedList<String> children = (yearEvent.secondGen(sport, event));				//finds children of event that is a child of the sport
    		
    		System.out.print("GetWinnersAndCountriesBySportAndEvent " + sport + " " + event);		//print first label
    		
    		for(int i = 0; i < children.size(); i++) {					//print each child
    			System.out.print(" " + children.get(i) + ":"  + yearEvent.getChildren(children.get(i)).get(0));
    		}
    		System.out.println();
    		
    }

    /**
     * Print first child of event
     * event needs to be a children of sport
     * @param sport
     * @param event
     */
    static void getGoldMedalistAndCountryBySportAndEvent (String sport, String event) {
		LinkedList<String> children = (yearEvent.secondGen(sport, event));				//finds children of event that is a child of the sport
		
		System.out.print("GetGoldMedalistAndCountryBySportAndEvent " + sport + " " + event);	//print first label

		System.out.println(" " + children.get(0) + ":"  + yearEvent.getChildren(children.get(0)).get(0));	//print only first child
    }
    
    /**
     * Class holds highest value and list of indexes
     * Allows to return 2 values from a method
     */
    private static class FreqAndIndex<E>{
    		private int highestValue;
    		private ArrayList<E> index;
    		
    		//constructor
    		FreqAndIndex(int highestValue, ArrayList<E> index){
    			this.highestValue = highestValue;
    			this.index = index;
    		}
    }
    
    /**
     * @param list of elements to test
     * @return object which contains highest frequency and indexes with the highest frequency
     */
    static FreqAndIndex getMostFrequentFromList(LinkedList<String> list) {
    		ArrayList<String> values = new ArrayList<String>();			//array of values without repeats
		int[] frequency = new int[list.size()];						//array of freq according to index
		
		//finding freq for each element and creating values array
		for(int i = 0; i < list.size(); i++) {
			if(!values.contains(list.get(i))) {
				values.add(list.get(i));
				
			}
			frequency[values.indexOf(list.get(i))] ++;
		}

		//testing which element has the highest value
		int highestValue = 0;
		ArrayList<Integer> index = new ArrayList<Integer>();
		for(int i = 0; i < frequency.length; i++) {
			if (frequency[i] == highestValue) {
				index.add(list.indexOf(values.get(i)));
			}
			if (frequency[i] > highestValue) {
				highestValue = frequency[i];
				index.clear();
				index.add(list.indexOf(values.get(i)));
				
			}

		}

		
		return new FreqAndIndex<Integer>(highestValue, index);
    }
    
    /**
     * Print athlete with most metals in alphabetial order
     */
    static void getAthleteWithMostMedals() {
		LinkedList<String> list = yearEvent.getLeafParents();				//get list of all parents with leaves
		Collections.sort(list);											//sort list
		FreqAndIndex temp = getMostFrequentFromList(list);				//returns index of highest values
		int highestValue = temp.highestValue;
		ArrayList<Integer> index = temp.index;
		
		System.out.print("GetAthleteWithMostMedals " + highestValue);		//print first label
		
		for(int i = 0; i < index.size(); i++) {
			System.out.print(" " + list.get(index.get(i)));				//print all other values
		}
		System.out.println();
    }
    
    /**
     * Print athlete with most  gold metals in alphabetial order
     */
    static void getAthleteWithMostGoldMedals() {
		LinkedList<String> list = yearEvent.getLeafParentWinners();		//get list of all parents with leaves with boolean marker that states winner		
		Collections.sort(list);											//sort list
		FreqAndIndex temp = getMostFrequentFromList(list);				//returns index of highest values
		int highestValue = temp.highestValue;
		ArrayList<Integer> index = temp.index;
		
		System.out.print("GetAthleteWithMostGoldMedals " + highestValue);	//print first label
		
		for(int i = 0; i < index.size(); i++) {
			System.out.print(" " + list.get(index.get(i)));				//print all other values
		}
		System.out.println();
    }
    
    /**
     * Print country with most metals in alphabetial order
     */
    static void getCountryWithMostMedals() {
    		LinkedList<String> list = yearEvent.getAllLeaf();					//get list of all leaves	 already sorted
    		FreqAndIndex temp = getMostFrequentFromList(list);				//returns index of highest values
    		int highestValue = temp.highestValue;
    		ArrayList<Integer> index = temp.index;
    		
    		System.out.print("GetCountryWithMostMedals " + highestValue);		//print first label
    		
    		for(int i = 0; i < index.size(); i++) {
    			System.out.print(" " + list.get(index.get(i)));				//print all other values
    		}
    		System.out.println();
    }
    
    /**
     * Print athlete with most gold metals in alphabetial order
     */
    static void getCountryWithMostGoldMedals() {
		LinkedList<String> list = yearEvent.getAllWinner();				//get list of all leaves	 already sorted
		FreqAndIndex temp = getMostFrequentFromList(list);				//returns index of highest values
		int highestValue = temp.highestValue;
		ArrayList<Integer> index = temp.index;

		System.out.print("GetCountryWithMostGoldMedals " + highestValue);	//print first label
		
		for(int i = 0; i < index.size(); i++) {
			System.out.print(" " + list.get(index.get(i)));				//print all other values
		}
		System.out.println();
    }
    
    /**
     * Prints sport and event with athlete input
     * @param athlete
     */
    static void getSportAndEventByAthlete (String athlete) {
    		LinkedList<String> parents = yearEvent.getParents(athlete);	//get all parents af athlete. athlete may have more than one even
    		
    		System.out.print("GetSportAndEventByAthlete " + athlete);		//print first label
    		
    		if(parents.size() == 0) {
    			System.out.print(" none");
    		}
    		for(int i = 0; i < parents.size(); i++) {					//print each child
    			System.out.print( " " + yearEvent.getParent(parents.get(i)) + ":" + parents.get(i));
    		}
    		System.out.println();
    }
    
    
    
}
