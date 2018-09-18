package HW2;

import java.util.SortedSet;
import java.util.TreeSet;

public class combinations {
   public static void main (String[] args) {
      combination ("teststring");
   }

   public static String[] combination (String str) {
      SortedSet<String> list = new TreeSet<String> ();
      int length = str.length ();
      int total = ((Double) Math.pow (2, length)).intValue () - 1;
      for (int i = 0; i < total; i++) {
         String tmp = "";
         char[] charArray = new StringBuilder (Integer.toBinaryString (i)).reverse ().toString ().toCharArray ();
         for (int j = 0; j < charArray.length; j++)
            if (charArray[j] == '1')
               tmp += str.charAt (j);
         list.add (tmp);
      }
      list.add (str);
      return list.toArray (new String[] {});
   }
}