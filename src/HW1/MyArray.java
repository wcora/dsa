/**
 * 
 */
package HW1;
/**
 * Implementation of MyArray.
 * @author Cora Wang
 * Andrew ID: jingxua3
 * March 29, 2020
 */
public class MyArray {
    /**
     * the internal array.
     */
    private String[] arr;
    /**
     * the number of items stored in MyArray.
     */
    private int size;

    /**
     * the empty constructor.
     */
    public MyArray() {
        arr = new String[10]; // default array size is 10
        size = 0;
    }

    /**
     * the constructor with capacity argument.
     * @param initialCapacity the initial capacity of the array
     */
    public MyArray(int initialCapacity) {
        if (initialCapacity < 0) {
            arr = new String[10]; // handle negative input
        } else {
            arr = new String[initialCapacity];
        }
        size = 0;
    }

    /**
     * Adds a new string at the end of the list.
     * Time complexity: amortized O(1)
     * @param text the string to add to the list
     */
    public void add(String text) {
       if (text == null || text.isEmpty() || text.equals("")) {
            return;
        }
        if (text.matches("[A-Za-z]+")) {
            if (size == 0) {
                // if declared size is 0, increase to 1 to jump start
                arr = new String[1];
            } else if (size == arr.length) {
                // copy over old array to new array with doubled capacity
                String[] tmp = new String[arr.length * 2];
                for (int i = 0; i < size; i++) {
                    tmp[i] = arr[i];
                }
                arr = tmp;
            }
            arr[size] = text;
            size++;
        }
    }

    /**
     * Searches a string from the list.
     * Time complexity: O(n)
     * @param  key the string to search
     * @return     boolean value indicating whether the string exists
     */
    public boolean search(String key) {
        if (key == null) { // handle edge case
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (key.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the size of the array.
     * Time complexity: O(1)
     * @return     integer value indicating the size of the array
     */
    public int size() {
        return size;
    }

    /**
     * Returns the capacity of the array.
     * Time complexity: O(1)
     * @return     integer value indicating the capacity of the array
     */
    public int getCapacity() {
        return arr.length;
    }

    /**
     * Prints all the words in one line.
     * Time complexity: O(n)
     */
    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * Removes any duplicated strings.
     * Time complexity: O(n^2)
     */
    public void removeDups() {
        int end = 0; // maintains a cleaned list of unique strings
        boolean flag; // true if string is unique
        for (int i = 0; i < size; i++) {
            flag = true;
            for (int j = 0; j < end; j++) {
                if (arr[i].equals(arr[j])) {
                    // if find i is a duplicated string
                    // not worth going through the rest
                    flag = false;
                    break;
                }
            }
            // if i is still unique, put it as next element of the cleaned list
            if (flag && i != end) {
                swap(i, end);
                end++;
            }
        }
        size = end;
    }
    /**
     * Swaps positions of two elements.
     * Time complexity: O(1)
     * @param i  element to swap position
     * @param j  another element to swap position
     */
    private void swap(int i, int j) {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
