/*

  Author: Aayush Kapar
  Email: Akapar2016@my.fit.edu
  Course:CSE2010
  Section:23
  Description: Main method for Fit big HW5 project. the usage of maps using skiplist.
  This class contains the importing of data, degination of methods, printing

 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

public class HW5
{
	
	static SkipList<String> map = new SkipList<String>();
	
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
		
		System.out.print(inputValues[0]);	//print out function and time
		
		switch (inputValues[0]) {			//check which function to run
			case "AddActivity" :
				addActivity(inputValues);
				break;
			case "RemoveActivity" :
				removeActivity(inputValues);
				break;
			case "GetActivity" :
				getActivity(inputValues);
				break;
			case "GetActivitiesBetweenTimes" :
				getActivitiesBetweenTimes(inputValues);
				break;
			case "GetActivitiesForOneDay" :
				getActivitiesForOneDay(inputValues);
				break;
			case "GetActivitiesFromEarlierInTheDay" :
				getActivitiesFromEarlierInTheDay(inputValues);
				break;
			case "GetActivitiesForYesterday" :
				getActivitiesForYesterday(inputValues);
				break;
			case "GetActivitiesForLastWeekday" :
				getActivitiesForLastWeekday(inputValues);
				break;
			case "GetActivitiesForLastWeekend" :
				getActivitiesForLastWeekend(inputValues);
				break;
			case "PrintSkipList" :
				printSkipList();
				break;

			default:							//if function in invalid, throw
				throw new IllegalArgumentException("Invalid function: " + input);
			}
		}
	}
	
	/**
	 * Add to skip list
	 * @param input array 1- time 2- activity
	 */
	public static void addActivity(String[] input) {
		Key key = makeKey(input[1]);
		System.out.println(" " + key.toString() + " " + input[2]); //printout
		try {
			map.put(key, input[2]); //to put into map
			
		//throws when nothing is added
		} catch (Exception e) {
	         System.out.println("ExistingActivityError: " + map.get(key));

	      }
	}
	
	/**
	 * Remove from skip list. If not there, prints invalid
	 * @param input array 1- time
	 */
	public static void removeActivity(String[] input) {
		Key key = makeKey(input[1]);
		System.out.print(" " + key.toString());  //printout
		String ret = map.remove(key);
		
		//if nothing is returned, nothing is completed
		if(ret == null) {
			System.out.println(" NoActivityError");
		} else {
			//if something is returned, something was done
			System.out.println(" " + ret);
		}
	}
	
	/**
	 * Get activity with exactly the same time
	 * @param input array 1- time
	 */
	public static void getActivity(String[] input) {
		Key key = makeKey(input[1]);
		System.out.print(" " + key.toString());
		String ret = map.get(key);
		//if nothing is returned, nothing is completed
		if(ret == null) {
			System.out.println(" none");
		} else {
			//if something is returned, something was done
			System.out.println(" " + ret);
		}
	}
	
	/**
	 * Prints whole list of activities within the time period
	 * @param input array 1- start time, 2- end time
	 */
	public static void getActivitiesBetweenTimes(String[] input) {
		//Find floor and ceiling keys
		Key floorKey = makeKey(input[1]);
		Key ceilingKey = makeKey(input[2]);
		System.out.print(" " + floorKey.toString() + " " + ceilingKey.toString());	//printout
		//prints directly 
		map.print(floorKey, ceilingKey);
	}
	
	/**
	 * Prints while list of activities for whole day
	 * @param input array 1-Date(MMDD)
	 */
	public static void getActivitiesForOneDay(String[] input) {
		//Is the floor. Clear out the hours and min to get from beginning of day
		Key inputDate = makeKey(input[1]);
		System.out.print(" " + inputDate.toStringMD());
		inputDate.time.set(Calendar.HOUR, 0);
		inputDate.time.set(Calendar.MINUTE, 0);
		
		//One day added on to act as ceiling
		Key ceilingDate = makeKey(input[1]);
		ceilingDate.time.set(Calendar.HOUR, 0);
		ceilingDate.time.set(Calendar.MINUTE, 0);
		ceilingDate.time.add(Calendar.DAY_OF_MONTH, 1);
		
		map.print(inputDate, ceilingDate);
	}
	
	/**
	 * Prints all activities from the beginning of the day till the time provided
	 * @param input array 1- time
	 */
	public static void getActivitiesFromEarlierInTheDay(String[] input) {
		//floor is the same day with a cleared out hour and min
		Key inputDate = makeKey(input[1]);
		System.out.print(" " + inputDate.toString());
		inputDate.time.set(Calendar.HOUR, 0);
		inputDate.time.set(Calendar.MINUTE, 0);

		//ceiling is the same day with that is inputed
		Key ceilingKey = makeKey(input[1]);
		map.print(inputDate, ceilingKey);
	}
	/**
	 * Prints all activities from the day before
	 * @param input array 1- time
	 */
	public static void getActivitiesForYesterday(String[] input) {
		//floor is the end of the day beofre yesterday
		Key inputDate = makeKey(input[1]);
		System.out.print(" " + inputDate.toString());
		inputDate.time.set(Calendar.HOUR, 0);
		inputDate.time.set(Calendar.MINUTE, 0);
		inputDate.time.add(Calendar.DAY_OF_MONTH, -2);
		
		//ceiling is the end of yesterday
		Key ceilingDate = makeKey(input[1]);
		ceilingDate.time.set(Calendar.HOUR, 0);
		ceilingDate.time.set(Calendar.MINUTE, 0);
		ceilingDate.time.add(Calendar.DAY_OF_MONTH, -1);
		
		map.print(inputDate, ceilingDate);
	}
	/**
	 * Prints all activities for events from the previous weekday
	 * @param input array 1- time 2-day of the week
	 */
	public static void getActivitiesForLastWeekday(String[] input) {
		//floor, set hour and min to 0
		Key inputDate = makeKey(input[1]);
		System.out.print(" " + inputDate.toString() + " " + input[2]);
		inputDate.time.set(Calendar.HOUR, 0);
		inputDate.time.set(Calendar.MINUTE, 0);
		
		//find what weekday we need to look into
		int weekday = 0;
		switch (input[2]) {
			case "Sun":
				weekday = 1;
				break;
			case "Mon":
				weekday = 2;
				break;
			case "Tue":
				weekday = 3;
				break;
			case "Wed":
				weekday = 4;
				break;
			case "Thu":
				weekday = 5;
				break;
			case "Fir":
				weekday = 6;
				break;
			case "Sat":
				weekday = 7;
				break;	
		}
		//go back one by one to see when it will be the same day as we are looking for
		int i = 0;
		for(i = 0; i <= 7; i ++) {
			if(inputDate.time.get(Calendar.DAY_OF_WEEK) == weekday) {
				break;
			}
			inputDate.time.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		//the ceiling stays one day ahead to make a one day gap
		Key ceilingDate = makeKey(input[1]);
		ceilingDate.time.set(Calendar.HOUR, 0);
		ceilingDate.time.set(Calendar.MINUTE, 0);
		ceilingDate.time.add(Calendar.DAY_OF_MONTH, -i + 1);
		
		map.print(inputDate, ceilingDate);
	}
	
	/**
	 * Prints all activities from the weekend prior
	 * @param input array 1- time
	 */
	public static void getActivitiesForLastWeekend(String[] input) {
		//floor ishas 0 for hour and min.
		Key inputDate = makeKey(input[1]);
		System.out.print(" " + inputDate.toString());
		inputDate.time.set(Calendar.HOUR, 0);
		inputDate.time.set(Calendar.MINUTE, 0);
		//if its already a weekend ex if its sunday now its a saturday
		int i = 0;
		inputDate.time.add(Calendar.DAY_OF_MONTH, -2);
		//keep going back to look ofr the next staurday
		for(i = 2; i <= 7; i ++) {
			if(inputDate.time.get(Calendar.DAY_OF_WEEK) == 7) {
				break;
			}
			inputDate.time.add(Calendar.DAY_OF_MONTH, -1);
		}
		
		//ceiling is 2 days ahead of staturday, it is the end of sunday 
		Key ceilingDate = makeKey(input[1]);
		ceilingDate.time.set(Calendar.HOUR, 0);
		ceilingDate.time.set(Calendar.MINUTE, 0);
		ceilingDate.time.add(Calendar.DAY_OF_MONTH, -i + 2);
		

		map.print(inputDate, ceilingDate);
		
	}
	
	public static void printSkipList() {
		System.out.println();
		map.print();	//print method in skiplist
	}
	/**
	 * Main method to make all Keys
	 * @param time in string
	 * @return Object key with time
	 */
	
	static Key makeKey(String time) {
		//if the leading 0 is missing add one
		if (time.length() == 7 || time.length() == 3) {
			time = "0" + time;
		}
		//MM and DD exist in all
		int MM = Integer.parseInt(time.substring(0, 2));
		int DD = Integer.parseInt(time.substring(2, 4));
		if(time.length() > 5) {		//check if there is HH and mm, if so, add them in
			int HH = Integer.parseInt(time.substring(4, 6));
			int mm = Integer.parseInt(time.substring(6, 8));
			return new Key(MM, DD, HH, mm);
		}
		return new Key(MM, DD);
	}
	
}
