package HW6;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 6 Binary Search Tree
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class Index {
    /**
     * the builder of index tree in natural order.
     * @param fileName the name of the file
     * @return a binary search tree
     */
    public BST<Word> buildIndex(String fileName) {
        // can simply use the same scanner method only with the comparator == null
        return buildIndex(fileName, null);
    }

    /**
     * helper method to determine whether a given string is valid.
     * @param  text  an input word
     * @return boolean value indicates whether the string is valid
     */
    private boolean isValidString(String text) {
        return text.matches("^[A-Za-z]+$");
    }
    /**
     * the builder of index tree using comparator.
     * @param fileName    the name of the file
     * @param comparator  the comparator
     * @return a binary search tree
     */
    public BST<Word> buildIndex(String fileName, Comparator<Word> comparator) {
        BST<Word> tree = new BST<Word>(comparator); // build a new word tree using comparator

        /* I adopted and modified the the following scanner method from HW3Driver.java
         * This code snippet credits to Prof. Lee */
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName));
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                String[] wordsFromText = line.split("\\W");
                for (String word : wordsFromText) {
                    if (isValidString(word)) {
                        if (comparator instanceof IgnoreCase) {
                            // if comparator is ignorecase, have to insert lowercase letters,
                            // otherwise will cause trouble when compare in natural order
                            word = word.toLowerCase();
                        }
                        Word s = tree.search(new Word(word)); // search to see if it already exists
                        if (s == null) { // s doesn't exist in tree yet
                            s = new Word(word);
                            s.addToIndex(lineNumber); // add the line number to the index set
                            tree.insert(s);
                        } else { // already exist, update frequency
                            s.addToIndex(lineNumber);
                            s.setFrequency(s.getFrequency() + 1);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the file.");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return tree;
    }
    /**
     * the builder of index tree using an array and comparator.
     * @param list        an array of Words
     * @param comparator  the comparator
     * @return a binary search tree
     */
    public BST<Word> buildIndex(ArrayList<Word> list, Comparator<Word> comparator) {
        if (list == null) {
            return new BST<Word>(comparator);
        }
        BST<Word> tree = new BST<Word>(comparator);
        for (Word word : list) {
            if (comparator instanceof IgnoreCase) {
                // if comparator is ignorecase, have to insert lowercase letters,
                // otherwise will cause trouble when compare in natural order
                word.setWord(word.getWord().toLowerCase());
            }
            tree.insert(word);
        }
        return tree;
    }
    /**
     * the method to sort in alpha order.
     * @param tree  the binary search tree
     * @return an arrayList of Words sorted by alpha order
     */
    public ArrayList<Word> sortByAlpha(BST<Word> tree) {
        /*
         * Even though there should be no ties with regard to words in BST,
         * in the spirit of using what you wrote,
         * use AlphaFreq comparator in this method.
         */
        ArrayList<Word> res = sortHelper(tree); // build an array from the tree
        res.sort(new AlphaFreq()); // using alpha comparator
        return res;
    }
    /**
     * the private helper method to build an array from the tree.
     * @param tree  the binary search tree
     * @return an arrayList of elements (unsorted)
     */
    private ArrayList<Word> sortHelper(BST<Word> tree) {
        if (tree == null) {
            return new ArrayList<Word>();
        }
        ArrayList<Word> res = new ArrayList<>(); // initialize array
        Iterator<Word> treeIterator = tree.iterator(); // initiate iterator
        while (treeIterator.hasNext()) { // when iterator has next, add next to our array
            res.add(treeIterator.next());
        }
        return res;
    }
    /**
     * the method to sort by frequency.
     * @param tree  the binary search tree
     * @return an arrayList of Words sorted by frequency
     */
    public ArrayList<Word> sortByFrequency(BST<Word> tree) {
        ArrayList<Word> res = sortHelper(tree);
        res.sort(new Frequency());
        return res;
    }
    /**
     * the method to get the Words with the highest frequency.
     * @param tree  the binary search tree
     * @return an arrayList of Words with the highest frequency
     */
    public ArrayList<Word> getHighestFrequency(BST<Word> tree) {
        ArrayList<Word> res = sortHelper(tree);
        res.sort(new Frequency()); // first sort by frequency
        int max = res.get(0).getFrequency();
        ArrayList<Word> highestFreqList = new ArrayList<>(); // build a new array to store the highest frequency
        for (Word word : res) {
            if (word.getFrequency() == max) { // if freq == the highest frequency, add to list
                highestFreqList.add(word);
            } else {
                break; // once < max, break out of loop
            }
        }
        return highestFreqList;
    }
}
