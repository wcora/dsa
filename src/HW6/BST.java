package HW6;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 6 Binary Search Tree
 * @param <T> the data type of this BST
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class BST<T extends Comparable<T>> implements Iterable<T>, BSTInterface<T> {
    /**
     * the root node of tree.
     */
    private Node<T> root;
    /**
     * the comparator method.
     */
    private Comparator<T> comparator;
    /**
     * the constructor of the tree.
     */
    public BST() {
        this(null);
    }
    /**
     * the constructor with the comparator parameter.
     * @param comp the comparator
     */
    public BST(Comparator<T> comp) {
        comparator = comp;
        root = null;
    }
    /**
     * the method returns the comparator.
     * @return the comparator
     */
    public Comparator<T> comparator() {
        return comparator;
    }
    /**
     * the method to get the root value.
     * @return the root of tree
     */
    public T getRoot() {
        if (root == null) {
            return null;
        }
        return root.data;
    }
    /**
     * the method to get the height of the tree.
     * @return the height of tree
     */
    public int getHeight() { // #edge = #level - 1
        if (root == null) { // if root is null, height = 0, not -1
            return 0;
        }
        return getHeightHelper(root) - 1; // recursion helper
    }
    /**
     * the private helper method to get the height recursively.
     * @param cur the current node
     * @return the leve so far
     */
    private int getHeightHelper(Node<T> cur) {
        if (cur == null) { // base case: if current node is null, then return sum of level = 0 to previous level
            return 0;
        }
        // recursive case: current node is not null, add 1 to current height,
        // recursively call the left child and the right child and use the deeper one as height
        return 1 + Math.max(getHeightHelper(cur.left), getHeightHelper(cur.right));
    }
    /**
     * the method to get the number of nodes in the tree.
     * @return the number of nodes
     */
    public int getNumberOfNodes() {
        return getNumberHelper(root); // recursion helper
    }
    /**
     * the private helper method to get the number of nodes recursively.
     * @param cur the current node
     * @return the number of nodes so far
     */
    private int getNumberHelper(Node<T> cur) {
        if (cur == null) { // base case: current node is null, return sum of node = 0 to previous level
            return 0;
        }
        // recursive case: current node is not null, add 1 to sum of nodes and
        // recursively call the left child and the right child
        return 1 + getNumberHelper(cur.left) + getNumberHelper(cur.right);
    }
    /**
     * the method to search for a value.
     * @param toSearch the value to search
     * @return the found data field
     */
    @Override
    public T search(T toSearch) {
        if (root == null) { // if root is null or keyword is null, return null
            return null;
        }
        if (toSearch == null) {
            return null;
        }
        return searchHelper(root, toSearch); // recursion helper
    }
    /**
     * the private method to search the value recursively.
     * @param cur the current node
     * @param key the key value to search
     * @return the found data field
     */
    private T searchHelper(Node<T> cur, T key) {
        if (cur == null) { // base case 1: current node is null, return to previous level
            return null;
        }
        // base case 2: key found, return cur.data, done. (don't return key becauze comparison method could vary)
        if (cur.data.compareTo(key) == 0 && comparator == null
                || comparator != null && comparator.compare(cur.data, key) == 0) { // if current value equals key, return
            return cur.data;
        }
        // recursive case: if key not found, depending on cur.data's order compared to key's
        // decides to recursively call the left subtree or right subtree
        if (cur.data.compareTo(key) < 0 && comparator == null
                || comparator != null && comparator.compare(cur.data, key) < 0) { // if current value < key, go right
            return searchHelper(cur.right, key);
        } else {
            return searchHelper(cur.left, key); // if current value > key, go left
        }
    }
    /**
     * the method to insert a new node.
     * @param toInsert the value to insert
     */
    @Override
    public void insert(T toInsert) {
        if (toInsert == null) { // if keyword is null, return
            return;
        }
        if (root == null) { // if root == null, just make a new node and let it be the root
            root = new Node<T>(toInsert);
            return;
        }
        insertHelper(root, root, toInsert, false); // recursion helper
    }
    /**
     * the private helper method to find where to insert the value recursively.
     * @param parent   the parent node of the current node
     * @param cur      the current node
     * @param toInsert the key value to insert
     * @param isLeft   boolean value indicates whether we are currently on a left branch
     */
    private void insertHelper(Node<T> parent, Node<T> cur, T toInsert, boolean isLeft) {
        // base case 1: hit the bottom level where cur is null -> connect its parent with new Node(toInsert)
        if (cur == null) {
            if (isLeft) {
                // whether to insert new node as the left child or the right
                // depends on if we are at the left branch or the right
                parent.left = new Node<T>(toInsert);
            } else {
                parent.right = new Node<T>(toInsert);
            }
            return;
        }
        // base case 2: if element exists and comparator is null (natural order), don't touch it
        if (cur.data.compareTo(toInsert) == 0 && comparator == null) {
            return;
        // base case 3: if comparator exists and values are the same using this comparator, don't touch it either
        } else if (comparator != null && comparator.compare(cur.data, toInsert) == 0) {
            return;
        }
        // recursive case: if current value < element using either natural order or comparator,
        // recursively call the right subtree
        if (cur.data.compareTo(toInsert) < 0 && comparator == null
                || comparator != null && comparator.compare(cur.data, toInsert) < 0) {
            insertHelper(cur, cur.right, toInsert, false);
        } else { // otherwise, recursively call the left subtree
            insertHelper(cur, cur.left, toInsert, true);
        }
    }
    /**
     * the method to iteratively iterate through the tree.
     * @return the iterator of this BST
     */
    @Override
    public Iterator<T> iterator() {
        return new BSTiterator();
    }
    /**
     * the iterator method of this binary search tree to iteratively iterate through the list of values (in-order).
     */
    private class BSTiterator implements Iterator<T> {
        /**
         * a stack to keep track of the left children.
         */
        private Stack<Node<T>> stack;
        /**
         * the constructor.
         */
        BSTiterator() {
            stack = new Stack<>(); // build a new stack and populate all its left children
            Node<T> cur = root;
            while (cur != null) {
                stack.push(cur);
                cur = cur.left; // guarantee to go to the left most (smallest)
            }
        }
        /**
         * the method to see if the stack still has next.
         * @return whether the stack is empty
         */
        @Override
        public boolean hasNext() {
             return !stack.isEmpty();
        }
        /**
         * the method to call on the next element.
         * @return next data field
         */
        @Override
        public T next() {
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }
            // nxtNode is the left most element at the moment,
            // its data should be returned in the end
            Node<T> nxtNode = stack.pop();
            if (nxtNode.right != null) {
                // then check if nextNode has a right child,
                // if so, add all right child's left children,
                // the one on top of stack shall be the next node to print
                Node<T> cur = nxtNode.right;
                while (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                }
            }
            return nxtNode.data; // the value to return
        }
    }
    /**
     * the node class.
     * @param <T> the data type
     */
    private static class Node<T> {
        /**
         * the data field.
         */
        private T data;
        /**
         * the pointer to left child.
         */
        private Node<T> left;
        /**
         * the pointer to right child.
         */
        private Node<T> right;
        /**
         * the default constructor.
         * @param d  data value
         */
        Node(T d) {
            this(d, null, null);
        }
        /**
         * the constructor with parameter.
         * @param d  data value
         * @param l  the left node
         * @param r  the right node
         */
        Node(T d, Node<T> l, Node<T> r) {
            data = d;
            left = l;
            right = r;
        }
    }
}
