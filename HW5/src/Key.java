/*

  Author: Aayush Kapar
  Email: Akapar2016@my.fit.edu
  Course:CSE2010
  Section:23
  Description: Key object used as a the key for the skiplist. Key is based on the Gregorian Calendar

 */
import java.util.Calendar;
import java.util.GregorianCalendar;

class Key implements Comparable<Key>{
		Calendar time;
		/**
		 * Constuctor, Year defulted to 2018
		 * @param month
		 * @param dayOfMonth
		 * @param hourOfDay
		 * @param minute
		 */
		Key(int month, int dayOfMonth, int hourOfDay, int minute) {
			this.time = new GregorianCalendar(2018, month - 1, dayOfMonth, hourOfDay, minute);

		}
		/**
		 * COnstructor
		 * @param month
		 * @param dayOfMonth
		 */
		Key(int month, int dayOfMonth) {
			this.time = new GregorianCalendar(2018, month - 1, dayOfMonth);
		}
		
		//Highest value in skiplist
		public static Key pos() {  return new Key(12,31); }
		
		//Lowest value in skiplist
		public static Key neg() {  return new Key(01,01); }

		/**
		 * Convert time in Calendar class into string to be printed
		 * @return string
		 */
		public String toString() {
			java.util.Date date =  time.getTime();
			String ret = "";
			ret = ret + String.format("%02d", date.getMonth() + 1);
			ret = ret + String.format("%02d", date.getDate());
			ret = ret + String.format("%02d", date.getHours());
			ret = ret + String.format("%02d", date.getMinutes());
			return ret;
		}
		
		/**
		 * Convert time with only month and date
		 * @return
		 */
		public String toStringMD() {
			java.util.Date date =  time.getTime();
			String ret = "";
			ret = ret + String.format("%02d", date.getMonth() + 1);
			ret = ret + String.format("%02d", date.getDate());
			return ret;
		}
		
		/**
		 * Compares time only
		 */
		@Override
		public int compareTo(Key arg0) {
			return time.compareTo(arg0.time);
		}
		
		/**
		 * compares time only
		 * @param arg0
		 * @return
		 */
		public boolean equals(Key arg0) {
			return time.equals(arg0.time);
		}

		
	}