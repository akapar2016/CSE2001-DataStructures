/*

  Author: Aayush Kapar
  Email: akapar2016@my.fit.edu
  Course: cse2010
  Section: 23
  Description: Homework2 - Class schedule arrangement using recursion

 */

package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class HW2
{
	
	/*
	 * responsible of storing all information from input.
	 * Stores name of course and all the times its offered
	 * Times are stored in arraylist
	 */
	static class CourseProfile {
		private String name;
		ArrayList<times> times = new ArrayList<times>();
		
		private static class times {
			private String days;
			private int startTime;
			private int endTime;
			
			//Defines all 3 variables
			times(String days, int time) {
				this.days = days;
				this.startTime = time;
			 
				/*end time is + 2 if its a 2 day course since those classes
				 * are 1.5 hours long. However since classes only start on the hour,
				 * We can just add 2 to compare whole numbers
				 */
				if (this.days.length() == 3) {
					this.endTime = time + 100;
				} else {
					this.endTime = time + 130; //as if it only goes up to 60
				}
			}
		}
		 
		CourseProfile(String name) {
			this.name = name;
		}
	
		void printFullCourse() {
			System.out.print(name + " ");
			for (int count = 0; count < times.size(); count++) {
				System.out.print(times.get(count).days);
				System.out.printf("%04d %s", times.get(count).startTime, "");
			}
			System.out.println();
		}
		
		void printCourse(int timeIndex) {
			System.out.print(name + " ");
			System.out.println(times.get(timeIndex).days + times.get(timeIndex).startTime);
		}
		
		int startTime(int index) {
			return times.get(index).startTime;
		}
		
		int endTime(int index) {
			return times.get(index).endTime;
		}
		
		String days(int index) {
			return times.get(index).days;
		}
	}
	
	/*
	 * Stores index of the name and time to be used to access
	 * the actual course info from course profile
	 */
	static class CourseIndex{
		private int nameIndex;
		private int timeIndex;
		
		CourseIndex(int name, int time) {
			this.nameIndex = name;
			this.timeIndex = time;
		}
	}
	
	/*
	 * Contains a linked list of all the possiblities of combinations of courses
	 * This linked list contains another linked list of all the courses for that combination
	 */
	static class SchedulePossiblities {
		private LinkedList<LinkedList<CourseIndex>> possiblities = new LinkedList<LinkedList<CourseIndex>>();
		
		SchedulePossiblities() {}
		
		//Add one possiblitiy
		void addToPossiblity(LinkedList<CourseIndex> course) {
			possiblities.add(course);
		}
		
		//return size of all the possiblitities
		int size() {
			return possiblities.size();
		}
		
	}
	
	//scanner used for getting input
	private static Scanner scanner;
	private static SchedulePossiblities schedule = new SchedulePossiblities();
	private static ArrayList<CourseProfile> courses = new ArrayList<CourseProfile>();
	
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

    		//send each line from file to addToCourse
    		while (scanner.hasNextLine()) {
    			addToCourse(scanner.nextLine());
    		}
    		/*
    		 * Setups and runs a recursive method
    		 * Stores all combinations of courses in schedule object
    		 */
    		runPermutations();
    		
    		//Test which combination is possible
    		//check if combination has all classes, and times are not overlapping
    		//also sends the last possible combination to be printed
    		checkPossiblities();
    }
    
    
    /*
     * Add each course from file to the arrayList courses
     * Name is added as a string
     * All the times are added as a linked list inside courseProfile
     */
    static void addToCourse(String inputLine) {
    		@SuppressWarnings("resource")
			final Scanner scan = new Scanner(inputLine);
    		//New CourseProfile
    		courses.add(new CourseProfile(scan.next()));
	    
    		//Add all the times to Linked list times
    		while (scan.hasNext()) {
	    		final String oneOption = scan.next(); //One time and date
	    		final int size = oneOption.length(); //length of input
	    	
	    		String date;
	    		int time;
	    		//if length is 6, has to be a 2 day, 1.5 hour class
	    		if (size == 6) {
	    			date = oneOption.substring(0, 2);
	    			time = Integer.parseInt(oneOption.substring(2, size));
	    		} else {
	    			//if length is 7, has to be a 3 day, 1 hour class
	  	 		date = oneOption.substring(0, 3);
	  	 		time = Integer.parseInt(oneOption.substring(3, size));
	    		}
	    		//add the times to the course
	    		courses.get(courses.size() - 1).times.add(new CourseProfile.times(date, time));
	    }
    }

    /*
     * Used to setup all the inputs for running recursive method.
     */
    static void runPermutations() {
    		int total = 0;
    		//Get total amounts to make a global index size
    		for(int count = 0; count < courses.size();count++) {
    			total += courses.get(count).times.size();
    		}
    		//Array of the global Index size
    		int[] indexArray = new int[total];
    		
    		//adding numbers to the global index size to be sent to recursive method
    		for(int count = 0; count < total;count++) {
    			indexArray[count] = count;
    		}
    		
    		//empty array with size of amount of courses to be used to recursive method
    		int[] temp = new int[courses.size()];
    		permutations(indexArray, temp, 0, total - 1, 0, courses.size());
    }
    
    /*
     * Every time it runs, it send an array of the total amount of courses. It sends 
     * an array of global indexes. These indexes are each connected to one course and one time.
     * It sends this array to checkCombo()
     */
    static void permutations(int[] indexes, int[] temp, int start, int end, int index, int size) {
    		//check after temp array is full
    		if (index == size) {
    			boolean valid = true;
    			int differenceValueBefore = 0;
    			int differenceValueAfter = 0;
    			
    			/*
    			 * verify the indexes are far enough from each other to be in seprate classes
    			 * This will verify that only lists that contain indexes to all classes are added to 
    			 * the schedule class as a possibility
    			 */
    			for(int count = 0; count < courses.size(); count++) {
    				
    				//calculate to have the next value beofore
    				differenceValueAfter = courses.get(count).times.size() + differenceValueBefore;
  
    				//Value of index is not the value for the next class therefore cannot be a possiblity
    				if (temp[count] < differenceValueBefore || temp[count] >= differenceValueAfter) {
    					valid = false;
    					break;
    				}
    				differenceValueBefore = differenceValueAfter;
    			}
    			//if possible, send list to add
    			if(valid) {

    				addToPossibilties(temp);// add to possiblities array
    			}
    			return;
    		
    		}
    		for(int count = start; count <= end; count++) {
	    		temp[index] = indexes[count]; //add to temp array
	    		permutations(indexes, temp, count + 1, end, index + 1, size); //recursive call
    		}
    }

    /*
     * Return object course index using global index
     */
    static CourseIndex getCourseIndex(int globalIndex) {
    		int courseInd = 0;
    		int timeInd = 0;
    		for(int count = 0; globalIndex > 0 && count < courses.size(); count++) {
    			//count for each course = total time indexes for the course
    			int timeCount = courses.get(count).times.size();
    			if(timeCount <= globalIndex) {
    				//subtract indexes of time from global
    				globalIndex = globalIndex - timeCount;
    				courseInd++;
    			} else {
    				break;
    			}
    		}
    		//add the remainder of globalIndexes to make up the timeIndex
    		timeInd = globalIndex;
    		globalIndex = 0;

    		return new CourseIndex(courseInd, timeInd);
    }
    
    /*
     * Add each combination of classes to possibilities list
     */
    static void addToPossibilties(int[] data) {
    		//temp linked list
	    	LinkedList<CourseIndex> possiblity = new LinkedList<CourseIndex>();
	    	//add courseIndex to possiblity list by converting from globalIndex
	    	for(int count = 0; count < data.length;count++) {
	    		//System.out.print(data[count]);
	    		possiblity.add(getCourseIndex(data[count]));
		}
	    	//System.out.println();
	    	//add to schdule list of lists
	    	schedule.addToPossiblity(possiblity);
	    	
	
    }

    /*
     * Returns the quantity of time conflicts to be compared
     * also changes array with 0 and 1s.
     * 0 = valid time
     * 1 = time conflict
     * Both are representing the index
     */
    static int timeConflicts(LinkedList<CourseIndex> courseList, int[] array) {
    		int numberOfTimeConflicts = 0;
    		//arrays of the start and end times of each course
    		int[] startTimesMWF = new int[courseList.size()];
    		int[] endTimesMWF = new int[courseList.size()];
    		int[] startTimesTR = new int[courseList.size()];
    		int[] endTimesTR = new int[courseList.size()];
    		
    		//add to the arrays above according to which day and array
    		for(int count = 0; count < courseList.size(); count++) {
    			if(courses.get(courseList.get(count).nameIndex).days(courseList.get(count).timeIndex).equals("TR")) {
	    			startTimesTR[count] = courses.get(courseList.get(count).nameIndex).startTime(courseList.get(count).timeIndex);
	    			endTimesTR[count] = courses.get(courseList.get(count).nameIndex).endTime(courseList.get(count).timeIndex);
    			} else {
	    			startTimesMWF[count] = courses.get(courseList.get(count).nameIndex).startTime(courseList.get(count).timeIndex);
	    			endTimesMWF[count] = courses.get(courseList.get(count).nameIndex).endTime(courseList.get(count).timeIndex);
    			}
    			array[count] = 0; //clean array
    		}
    		
    		/*
    		 * Check according to which day and time, weather it overlaps with other courses at the same time
    		 * If it does, add one to number of conflicts and change array at the same index with 1
    		 */
    		for(int countCurrent = courseList.size() - 1; countCurrent > 0; countCurrent--) {
    			for(int countAll = 0; countAll < courseList.size(); countAll++) {
	    			if(courses.get(courseList.get(countCurrent).nameIndex).days(courseList.get(countCurrent).timeIndex).equals("TR")) {
	    				if(startTimesTR[countCurrent] >= startTimesTR[countAll] && startTimesTR[countCurrent] < endTimesTR[countAll] && countCurrent > countAll) {
		    				numberOfTimeConflicts ++;
		    				array[countCurrent] = 1;
		    			} 		
	    			} else {
		    			if(startTimesMWF[countCurrent] >= startTimesMWF[countAll] && startTimesMWF[countCurrent] < endTimesMWF[countAll] && countCurrent > countAll) {
		    				numberOfTimeConflicts ++;
		    				array[countCurrent] = 1;
		    			} 
	    			}	
    			}
    		}

    		return numberOfTimeConflicts;
    		
    }
    
    
    /*
     * Check and remove any possibilities that would not be possible
     * Check which combination has the least amount of time conflicts
     * Send this combinaion to be printed
     */
    static void checkPossiblities() {
    		
    		//used to count through all the possiblities
    		Iterator<LinkedList<CourseIndex>> pointer = schedule.possiblities.iterator();
    		
    		int numberOfConflicts = courses.size() * courses.size(); //number of conflicts cannot be larger than this
    		int indexOfFirstConflict = 0; //The first class to be rejected
    		int index = 0; //index of the most possible one
    		int[] indexOfConflicts = new int[courses.size()]; //array of 0 and 1, 1 is a conflict
    		
    		//look through each possiblitility
    		for(int count = 0; count < schedule.possiblities.size(); count++) {
    			
    			LinkedList<CourseIndex> current = pointer.next();
    			
    			//temp values to compare against the values on top
    			int[] tempIndexOfConflicts = new int[courses.size()];
    			int tempConflicts = timeConflicts(current, tempIndexOfConflicts);
    			int tempIndexOfFirstConflict = indexOfFirstConflict;
    			
    			//checking the index of the first time conflict
    			for(int j = 0; j < tempIndexOfConflicts.length; j++) {
    				if(tempIndexOfConflicts[j] == 1) {
    					tempIndexOfFirstConflict = j;
    					break;
    				}
    			}
    		
    			/*
    			 * if another possiblibility has the same amount of time conflicts
    			 * check if index of first conflict is higher. If it is higher, it means 
    			 * the new possibility is more desireable since it would allow to take the more 
    			 * pefered class or time
    			 */
    			if(tempConflicts == numberOfConflicts && tempIndexOfFirstConflict > indexOfFirstConflict) {
    				numberOfConflicts = tempConflicts;
    				index = count;
    				indexOfFirstConflict = tempIndexOfFirstConflict;
    				
        			for(int j = 0; j < tempIndexOfConflicts.length; j++) {
        				indexOfConflicts[j] = tempIndexOfConflicts[j];
        			}
    				
    			}
    			
    			/*
    			 * if another possiblitity has less time conflicts, make this into the new best one
    			 */
    			if(tempConflicts < numberOfConflicts) {
    				numberOfConflicts = tempConflicts;
    				index = count;
    				indexOfFirstConflict = tempIndexOfFirstConflict;
    				
        			for(int j = 0; j < tempIndexOfConflicts.length; j++) {
        				indexOfConflicts[j] = tempIndexOfConflicts[j];
        			}
    				
    			}
	
    		}
    		
    		//send to be printed
		printFinal(index, indexOfConflicts);	
			
    }
    
    //Print final courses possible
    static void printFinal(int index, int[] indexOfConflicts) {
    		LinkedList<CourseIndex> course = schedule.possiblities.get(index);
    		
    		System.out.println("---Course schedule---");
    		for(int count = 0; count < course.size(); count++) {
    			if(indexOfConflicts[count] == 0) {
    				courses.get(course.get(count).nameIndex).printCourse(course.get(count).timeIndex);
    			}
    		}
    		
    		System.out.println("---Courses with a time conflict---");
    		for(int count = 0; count < course.size(); count++) {
    			if(indexOfConflicts[count] == 1) {
    				courses.get(course.get(count).nameIndex).printFullCourse();
    			}
    		}
    		
    		
    }
   
}
