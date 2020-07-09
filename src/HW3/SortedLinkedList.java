package HW3;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 3 SortedLinkedList implementation with Recursion
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class SortedLinkedList implements MyListInterface {

    /**
     * Nested node class.
     */
    private static class Node {
        /**
         * String value stored in a node.
         */
        private String val;
        /**
         * Pointer to the next node.
         */
        private Node next;
        /**
         * Empty constructor.
         */
        Node() {
            val = "";
            next = null;
        }
        /**
         * Constructor with string and next pointer parameters.
         * @param s   the string to store in this node
         * @param nxt the next node
         */
        Node(String s, Node nxt) {
            val = s;
            next = nxt;
        }
    }

    /**
     * Head of the SortedLinkedList.
     */
    private Node head;
    /**
     * Empty constructor.
     */
    public SortedLinkedList() {
        head = null;
    }
    /**
     * Constructor with string parameter.
     * @param list  an array of unsorted strings
     */
    public SortedLinkedList(String[] list) {
        if (list != null && list.length != 0) {
            sort(list, 0);
        } else {
            head = null;
        }
    }
    /**
     * Heler method sort that sorts the array recursively.
     * @param list  an array of unsorted strings
     * @param i     the index of the string in list
     */
    private void sort(String[] list, int i) {
        /**
         * How recursion works:
         * Helper takes the entire unsorted list & the index of element to be added.
         * Base case checks if we've reached the end of list, if so, return.
         * If not, add list[i] to the sorted linkedlist and recursively call on
         * list with incremented index i.
         */
        if (i > list.length - 1) {
            return; // stop adding
        }
        // add the current node
        add(list[i++]); // add method takes care of sorting, increment i after added
        sort(list, i); // recursively add next node
    }
    /**
     * Inserts a new String.
     * Do not throw exceptions if invalid word is added (Gently ignore it).
     * No duplicates allowed and maintain the order in ascending order.
     * @param value String to be added.
     */
    @Override
    public void add(String value) {
        if (value == null || value.length() == 0) {
            return; // empty string case
        }
        if (head == null) { // head doesn't exist
            head = new Node(value, null);
        } else if (head.val.compareTo(value) > 0) { // value smaller than head
            head = new Node(value, head);
        } else { // head exists and val > head.val
            addSorted(value, null, head);
        }
    }
    /**
     * Add string to the appropriate sorted position.
     * @param  value the string to add
     * @param  prev  the previous node
     * @param  cur   the current node
     */
    private void addSorted(String value, Node prev, Node cur) {
        /**
         * How recursion works:
         * Helper takes the string to add, the previous node, and the current node.
         * Base cases check if reached the end of list, and if value is a duplicate.
         * if reached end of list, add value to the end; if value already exists, ignore. Return.
         * Otherwise, compare value with current node value in the sorted linkedlist.
         * If correct position is found, insert value between prev and cur, return.
         * else, recursively call on prev.next (cur) and cur.next.
         */
        if (cur == null) {
            prev.next = new Node(value, null);
            return;
        }
        if (cur.val.compareTo(value) == 0) { // gently ignore duplicated case
            return;
        }
        if (cur.val.compareTo(value) > 0) { // cur > value, insert
            prev.next = new Node(value, cur);
            return;
        }
        addSorted(value, cur, cur.next); // cur < value, recurse
    }

    /**
     * Checks the size (number of data items) of the list.
     * @return the size of the list
     */
    @Override
    public int size() { // count size recursively
        return getSize(head);
    }
    /**
     * Helper method recursively sum the size of the list.
     * @param  cur current node in hand
     * @return the size of the list
     */
    private int getSize(Node cur) {
        /**
         * How recursion works:
         * Helper takes the current node.
         * Base case checks if we've reached the end of list, if so, return 0.
         * Otherwise, add 1 (account for current node) to the recursive call on the next node.
         */
        if (cur == null) { // reached end of list
            return 0;
        }
        return 1 + getSize(cur.next); // count current node, recurse to next node
    }
    /**
     * Displays the values of the list.
     */
    @Override
    public void display() {
        if (head == null) { // head doesn't exist, still print empty list []
            System.out.println("[]");
        } else { // print the list using StringBuilder
            StringBuilder s = new StringBuilder();
            s.append("[");
            builder(head, s);
            s.append("]");
            System.out.println(s.toString());
        }
    }

    /**
     * Helper method append the values of the list to a StringBuilder.
     * @param cur  the current node
     * @param s    the StringBuilder that appended the strings in the list so far
     */
    private void builder(Node cur, StringBuilder s) {
        /**
         * How recursion works:
         * Helper takes the current node and StringBuilder s (better efficiency than String class).
         * Append current node to StringBuilder first, then,
         * Base case checks if next node reaches the end of list, if so, return.
         * Otherwise, append parenthesis, and recursively call on next node and the StringBuilder.
         */
        s.append(cur.val); // append current node value
        if (cur.next == null) { // base case handles last node.next (to avoid extra ',')
            return;
        }
        s.append(", ");
        builder(cur.next, s); // recurse to next node
    }
    /**
     * Returns true if the key value is in the list.
     * @param key String key to search
     * @return true if found, false if not found
     */
    @Override
    public boolean contains(String key) {
        if (key != null && key.length() != 0) { // check if key is legal
            return getKey(key, head);
        }
        return false; // if key not legal, return false
    }
    /**
     * Helper method recursively go through the list to find key.
     * @param key  the string to find
     * @param cur  the current node
     * @return the string at index
     */
    private boolean getKey(String key, Node cur) {
        /**
         * How recursion works:
         * Helper takes the key string & the current node.
         * Base case checks if we reached the end of list, if so, means key is not found, return.
         * If not, compare key with the value of the current node, if match, return.
         * Otherwise, recursively call on the next node.
         */
        if (cur == null) { // handle end of list
            return false;
        }
        if (cur.val.compareTo(key) == 0) { // key found, return true
            return true;
        }
        return getKey(key, cur.next); // key not found, continue to go through the list
    }

    /**
     * Returns true is the list is empty.
     * @return true if it is empty, false if it is not empty
     */
    @Override
    public boolean isEmpty() {
        return head == null; // just need to check if head pointer is null
    }

    /**
     * Removes and returns the first String object of the list.
     * @return String object that is removed. If the list is empty, returns null
     */
    @Override
    public String removeFirst() {
        if (head == null) {
            return null; // nothing to remove
        }
        String s = head.val;
        head = head.next; // chop head off
        return s; // return removed string
    }

    /**
     * Removes and returns String object at the specified index.
     * @param index index to remove String object
     * @return String object that is removed
     * @throws RuntimeException for invalid index value (index < 0 || index >= size())
     */
    @Override
    public String removeAt(int index) {
        if (index < 0) { // if index < 0, throw error
            throw new RuntimeException("index < 0");
        }
        if (head == null) { // if head == null, return null
            return null;
        }
        if (index == 0) { // if index == 0 & head != null, remove first node
            return removeFirst();
        }
        return removeKey(index - 1, head, head.next); // already passed 0, start from index - 1
    }
    /**
     * Helper method recursively pass each node and remove node at index.
     * @param index  distance to target index
     * @param prev   the node prior to the current node
     * @param cur    the current node
     * @return the string at index
     */
    private String removeKey(int index, Node prev, Node cur) {
        /**
         * How recursion works:
         * Helper takes the distance towards the target, the previous node, and the current node.
         * Base case checks if index is too large, if so, throw an error;
         * Then, checks if we reached the element to remove, if so, connect prev with cur.next, return.
         * If not, recursively call on prev.next (cur) and cur.next, along with distance decreased by 1.
         */
        if (cur == null) { // index is larger than length of string
            throw new RuntimeException("index >= size");
        }
        if (index == 0) { // reached index, connect prev directly with cur.next
            prev.next = cur.next;
            return cur.val;
        }
        return removeKey(index - 1, cur, cur.next);  // recurse to next node
    }
}

