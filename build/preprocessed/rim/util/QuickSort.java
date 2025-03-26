package rim.util;

import java.util.Vector;

/**
 * @author Rizki Eka Saputra Ramadiansyah (rizki.ramadiansyah@gmail.com)
 */

public class QuickSort{
    /**
     *
     * @param array
     * @return
     */
    public static Vector sort(Vector array){
         Vector sort = array;
        sort(sort, 0, sort.size() - 1);
        return sort;
    }

    /**
     *
     * @param array
     * @param start
     * @param end
     */
    public static void sort(Vector array, int start, int end){
        int p;
        if (end > start){
            p = partition(array, start, end);
            sort(array, start, p-1);
            sort(array, p+1, end);
        }
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    protected static int compare(String a, String b){
         int na = a.charAt(0) - '0';
         int nb = b.charAt(0) - '0';
         if (na==nb){
              return a.substring(1).compareTo(b.substring(1));
         }
          return na - nb;
    }

    /**
     *
     * @param array
     * @param start
     * @param end
     * @return
     */
    protected static int partition(Vector array, int start, int end){
        int pivot = start;
        int left, right;
        String partition = (String)array.elementAt(pivot);
          for (int i=start+1; i<=end; i++){
               if (compare(partition, (String)array.elementAt(i))>0){
                    pivot++;
                    swap(array, pivot, i);
               }
          }
          swap(array, start, pivot);
        return pivot;
    }

    /**
     *
     * @param array
     * @param i
     * @param j
     */
    protected static void swap(Vector array, int i, int j){
        Object temp;

        temp = array.elementAt(i);
        array.setElementAt(array.elementAt(j), i);
        array.setElementAt(temp, j);
    }
}