package HW4;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework Assignment 4: HashTable Implementation with linear probing
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class MyHashTable implements MyHTInterface {
    /**
     * The default table capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;
    /**
     * The load factor.
     */
    private static final double LOAD_FACTOR = 0.5;
    /**
     * The reusable dummy DataItem stands for "deleted item".
     */
    private static final DataItem DELETED = new DataItem();
    /**
     * The size of the table.
     */
    private int size;
    /**
     * The number of collision in the table.
     */
    private int collisionSize;
    /**
     * The DataItem array of the table.
     */
    private DataItem[] hashArray;

    /**
     * The empty constructor.
     */
    public MyHashTable() {
        hashArray = new DataItem[DEFAULT_CAPACITY];
        size = 0;
        collisionSize = 0;
    }
    /**
     * The constructor with capacity parameter.
     * @param capacity  the size of the array
     */
    public MyHashTable(int capacity) {
        if (capacity > 0) {
            hashArray = new DataItem[capacity];
        } else {
            throw new RuntimeException("length <= 0");
        }
        size = 0;
        collisionSize = 0;
    }

    /**
     * Instead of using String's hashCode, you are to implement your own here.
     * You need to take the table length into your account in this method.
     *
     * In other words, you are to combine the following two steps into one step.
     * 1. converting Object into integer value
     * 2. compress into the table using modular hashing (division method)
     *
     * Helper method to hash a string for English lowercase alphabet and blank,
     * we have 27 total. But, you can assume that blank will not be added into
     * your table. Refer to the instructions for the definition of words.
     *
     * For example, "cats" : 3*27^3 + 1*27^2 + 20*27^1 + 19*27^0 = 60,337
     *
     * But, to make the hash process faster, Horner's method should be applied as follows;
     *
     * var4*n^4 + var3*n^3 + var2*n^2 + var1*n^1 + var0*n^0 can be rewritten as
     * (((var4*n + var3)*n + var2)*n + var1)*n + var0
     *
     * Note: You must use 27 for this homework.
     *
     * However, if you have time, I would encourage you to try with other
     * constant values than 27 and compare the results but it is not required.
     * @param input input string for which the hash value needs to be calculated
     * @return int hash value of the input string
     */
    private int hashFunc(String input) {
        int hashCode = 0;
        for (char c : input.toCharArray()) { // only capstures lowercase char
            hashCode = (hashCode * 27 + (c - 'a' + 1)) % hashArray.length;
        }
        return hashCode;
    }

    /**
     * doubles array length and rehash items whenever the load factor is reached.
     * Note: do not include the number of deleted spaces to check the load factor.
     * Remember that deleted spaces are available for insertion.
     */
    private void rehash() {
        // check next prime length after 2 * size of array
        int len = nextPrime(hashArray.length * 2 + 1); // + 1 to ensure always feed odd number
        // copy old array items to new array
        // because hashFunc uses hashArray.length as mod factor
        // we must copy old item to temporary array and initialize array of new length to hashArray
        DataItem[] tmp = hashArray;
        hashArray = new DataItem[len];
        System.out.println("Rehashing " + size + " items, new length is " + len);
        collisionSize = 0; // reset collision
        for (DataItem i : tmp) {
            if (i != null && i != DELETED) {
                // check collision
                int hashVal = hashFunc(i.value);
                int k = hashVal; // for collision detection
                while (hashArray[hashVal] != null) {
                    hashVal++;
                    hashVal %= hashArray.length;
                }
                // for every dataItem i, if it collides with any element in the list,
                // we increment collision size by 1.
                for (DataItem j : hashArray) {
                    if (j != null && hashFunc(j.value) == k) {
                        collisionSize++;
                        break;
                    }
                }
                hashArray[hashVal] = i; // copy over
            }
        }
    }

    /**
     * Helper method to find the next closest prime.
     * @param  len  integer input (always odd)
     * @return a prime number
     */
    private int nextPrime(int len) {
        while (!isPrime(len)) {
            len += 2; // because input len is odd, len +=2 is always odd
        }
        return len;
    }

    /**
     * Helper method check whether the input integer is a prime.
     * @param  len an integer
     * @return boolean value indicate whether it is prime
     */
    private boolean isPrime(int len) {
        // The length of array is at least oldLength * 2 + 1,
        // the constructor implementation ensures hashArray.length > 0.
        // Therefore, it is always true that len >= 1*2+1 = 3.
        // I do not need to check len = 0/1/2 base cases.
        // because len is always odd, it is not possible that len % (2 + 2k) == 0 for k >= 0,
        // we only need to check whether len mod by 3 + 2k == 0.
        for (int i = 3; i < Math.sqrt(len); i = i + 2) {
            if (len % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * private static data item nested class.
     */
    private static class DataItem {
        /**
         * String value.
         */
        private String value;
        /**
         * String value's frequency.
         */
        private int frequency;
        /**
         * The empty constructor of dataItem.
         */
        DataItem() {
            value = "";
            frequency = 1;
        }
        /**
         * The constructor with string parameter.
         * @param s  the string value
         */
        DataItem(String s) {
            value = s;
            frequency = 1;
        }
        /**
         * Increment the frequency of a dataItem.
         */
        public void increment() {
            frequency++;
        }
        /**
         * ToString method of dataItem to print an element in  format.
         * @return string value and frequency in [value, frequency] format
         */
        @Override
        public String toString() {
            if (value.equals("")) {
                return "#DEL#";
            }
            StringBuilder s = new StringBuilder();
            s.append("[");
            s.append(value);
            s.append(", ");
            s.append(frequency);
            s.append("]");
            return s.toString();
        }
    }

    /**
     * Inserts a new String value (word).
     * Frequency of each word to be stored too.
     * @param value String value to add
     */
    @Override
    public void insert(String value) {
        // check string validity
        if (!isValidString(value)) {
            return;
        }
        // get hashcode
        int hashVal = hashFunc(value);
        int k = hashVal; // k is counter for collision detection
        /* insert */
        int counter = hashArray.length;
        // here we need to:
        // First pass: search if this element already exists,
        //             if so, increment frequency (should still loop after seeing DELETED)
        //             put counter to guard corner case where all array fields are #DEL#
        while (hashArray[hashVal] != null && counter >= 0) {
            // if the same element exists, increment the frequency, return
            if (hashArray[hashVal].value.equals(value)) { // search
                hashArray[hashVal].increment();
                return;
            }
            hashVal++;
            counter--;
            hashVal %= hashArray.length;
        }
        // Second pass: element does not exist, insert in next available hashcode position.
        //              Can reuse DELETED.
        hashVal = k;
        counter = hashArray.length;
        while (hashArray[hashVal] != null && hashArray[hashVal] != DELETED && counter >= 0) {
            hashVal++;
            counter--;
            hashVal %= hashArray.length;
        }
        // check for collision
        for (DataItem i : hashArray) {
            if (i != null && hashFunc(i.value) == k) {
                collisionSize++;
                break;
            }
        }
        // empty spot found, insert, increase size
        hashArray[hashVal] = new DataItem(value);
        size++;
        // rehash if necessary
        if (1.0 *  size / hashArray.length > LOAD_FACTOR) {
            rehash();
        }
    }
    /**
     * Helper method to determine whether a given sting is valid.
     * @param  text  a string input
     * @return boolean value indicates whether sting is valid
     */
    private boolean isValidString(String text) { // ditch anything besides lowercase
        return text != null && !text.isEmpty() & !text.equals("") && text.matches("^[a-z]+$");
    }
    /**
     * Returns the size, number of items, of the table.
     * @return the number of items in the table
     */
    @Override
    public int size() {
        return size;
    }
    /**
     * Displays the values of the table.
     * If an index is empty, it shows **
     * If previously existed data item got deleted, then it should show #DEL#
     */
    @Override
    public void display() {
        StringBuilder s = new StringBuilder();
        for (DataItem i : hashArray) {
            if (i == null) {
                s.append("**");
            } else {
                s.append(i);
            }
            s.append(" ");
        }
        System.out.println(s.toString());
    }
    /**
     * Returns true if value is contained in the table.
     * @param key String key value to search
     * @return true if found, false if not found.
     */
    @Override
    public boolean contains(String key) {
        // check string validity
        if (!isValidString(key)) {
            return false;
        }
        int hashVal = hashFunc(key);
        int counter = hashArray.length;
        // if found, return true
        while (hashArray[hashVal] != null && counter >= 0) {
            if (hashArray[hashVal].value.contentEquals(key)) {
                return true;
            }
            hashVal++;
            counter--;
            hashVal %= hashArray.length;
        }
        // did not find, return false
        return false;
    }
    /**
     * Returns the number of collisions in relation to insert and rehash.
     * When rehashing process happens, the number of collisions should be properly updated.
     *
     * The definition of collision is "two different keys map to the same hash value."
     * Be careful with the situation where you could overcount.
     * Try to think as if you are using separate chaining.
     * "How would you count the number of collisions?" when using separate chaining.
     * @return number of collisions
     */
    @Override
    public int numOfCollisions() {
        return collisionSize;
    }
    /**
     * Returns the hash value of a String.
     * Assume that String value is going to be a word with all lowercase letters.
     * @param value value for which the hash value should be calculated
     * @return int hash value of a String
     */
    @Override
    public int hashValue(String value) {
        // check string validity
        if (!isValidString(value)) {
            return -1;
        }
        return hashFunc(value); // return its hashcode
    }
    /**
     * Returns the frequency of a key String.
     * @param key string value to find its frequency
     * @return frequency value if found. If not found, return 0
     */
    @Override
    public int showFrequency(String key) {
        // check string validity
        if (!isValidString(key)) {
            return 0;
        }
        int hashVal = hashFunc(key);
        int counter = hashArray.length;
        // decrement counter to make sure in corner case where
        // all table fields are #DEL# we do not loop pass one cycle
        while (hashArray[hashVal] != null && counter >= 0) {
            // if found, return frequency field
            if (hashArray[hashVal].value.equals(key)) {
               return hashArray[hashVal].frequency;
            }
            hashVal++;
            counter--;
            hashVal %= hashArray.length;
        }
        return 0;
    }
    /**
     * Removes and returns removed value.
     * @param key String to remove
     * @return value that is removed. If not found, return null
     */
    @Override
    public String remove(String key) {
        // check string validity
        if (!isValidString(key)) {
            return null;
        }
        // find string
        int hashVal = hashFunc(key);
        int counter = hashArray.length;
        // decrement counter to make sure in corner case where
        // all table fields are #DEL#, we do not loop pass one cycle
        while (hashArray[hashVal] != null && counter >= 0) {
            // if found, set to DELETED
            if (hashArray[hashVal].value.equals(key)) {
                DataItem tmp = hashArray[hashVal];
                hashArray[hashVal] = DELETED;
                size--;
                return tmp.value;
            }
            counter--;
            hashVal++;
            hashVal %= hashArray.length;
        }
        return null;
    }
}
