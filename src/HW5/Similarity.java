package HW5;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 5 Word Frequency Map
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */

/** Why I chose this Collections Framework (hashMap) for data storage:
 * This problem asks us to store a word with its associated frequency in data.
 * The dot product method (and the distance method because it uses dotproduct)
 * requires frequent search of given keys in the dataset. I think hashMap is the
 * most reasonable framework in this case. Besides HashMap has the natural capacity
 * to store a { key, value } pair, it also has much better time complexity
 * for the search method than linkedlist or arrayList. In the best case
 * (no collision) it is an O(1) search and in the worst scenario it is O(n)
 * (everything chains under one node) and Java handles this case by switching
 * to red-black tree.
 */
public class Similarity {
    /**
     * map to store words and frequency.
     */
    private Map<String, BigInteger> wordMap;
    /**
     * number of words in the file (or string).
     */
    private BigInteger size; // to handle huge file use BigInteger
    /**
     * number of lines.
     */
    private int line;
    /**
     * Euclidean norm of the file (or string).
     */
    private double norm;
    /**
     * constructor for string.
     * @param string  an input string
     */
    public Similarity(String string) {
        // initialize Similarity object with 0 size, 0 line, 0 norm, empty map
        wordMap = new HashMap<>();
        size = BigInteger.ZERO; // initialize size as 0 in BigInteger
        line = 0;
        norm = 0.0;
        // handle null cases, if string is null, return directly
        if (string == null || string.length() == 0) {
            return;
        }
        add(string); // otherwise, add the word to wordMap
        norm = computeNorm(wordMap); // compute Euclidean norm score
    }
    /**
     * constructor for file.
     * @param file  an input file
     */
    public Similarity(File file) {
        // initialize Similarity object with 0 size, 0 line, 0 norm, empty map
        wordMap = new HashMap<>();
        size = BigInteger.ZERO;
        line = 0;
        norm = 0.0;
        // if file doesnt exist or is null, return
        if (file == null || file.length() == 0) {
            return;
        }
        /* The following try-catch block handling file scanner
         * credit to HW3Driver.java provided by Prof. Lee */
        Scanner scanner = null;
        try {
            scanner = new Scanner(file, "latin1");
            while (scanner.hasNextLine()) {
                String lineOfStrings = scanner.nextLine();
                add(lineOfStrings);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        // calculate norm score
        norm = computeNorm(wordMap);
    }
    /**
     * helper method add words in a line to the frequency map.
     * @param lines  a string of words separated by space
     */
    private void add(String lines) {
        String[] words = lines.split("\\W");
        for (String word: words) {
            if (isValidString(word)) { // treat upper & lowercase as same word
                // if map contains the word, get the frequency value
                // (if doesn't exist, initialize as 0) and then + 1, put back to map
                String lower = word.toLowerCase();
                wordMap.put(lower, wordMap.getOrDefault(lower, BigInteger.ZERO).add(BigInteger.ONE));
                size = size.add(BigInteger.ONE);
            }
        }
        line++;
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
     * helper method to calculate the Euclidean norm.
     * @param  map  the hashMap store all strings
     * @return double value indicates the Euclidean norm
     */
    private double computeNorm(Map<String, BigInteger> map) {
        if (map == null || map.isEmpty()) {
            return 0.0;
        }
        BigInteger normSquare = BigInteger.ZERO;
        for (BigInteger i: map.values()) {
            // ||x|| ^ 2 = x(1)^2 + ... + x(n)^2
            normSquare = normSquare.add(i.multiply(i));
        }
        return Math.sqrt(normSquare.doubleValue());
    }
    /**
     * Method to access the total number of lines.
     * @return int value indicates the number of lines
     */
    public int numOfLines() {
        return line;
    }
    /**
     * Method to access the total number of words.
     * @return BigInteger value indicates the number of words
     */
    public BigInteger numOfWords() {
        return size;
    }
    /**
     * Method to access the size of non-duplicated words.
     * @return int value indicates the number of non-dup words
     */
    public int numOfWordsNoDups() {
        return wordMap.size();
    }
    /**
     * Method to access the Euclidean norm of the map.
     * @return double value indicates the norm
     */
    public double euclideanNorm() {
        return norm;
    }
    /**
     * Method to calculate the dot product of wordMap with another frequency map.
     * @param  map  another word frequency map that stores a set of words
     * @return double value indicates the dot product of the two hashMaps
     */
    public double dotProduct(Map<String, BigInteger> map) {
        /**
         * Why does it not fall into quadratic running time complexity:
         * In this method, I go through the entry set of one hashmap (O(n) in total)
         * and check if the other hashmap has the same key.
         * Because hashmap has close to O(1) search & fetch,
         * my implementation shall not fall into quadratic running time.
         */
        // base case: if input map is null or wordMap is null, freq(map) = 0,
        // dot-product is 0. no need to go through the maps
        if (map == null || map.isEmpty() || wordMap == null || wordMap.isEmpty()) {
            return 0.0;
        }
        BigInteger dot = BigInteger.ZERO;
        // go through map's entry list, if our wordMap has the same key
        // calculate dot product of their frequencies and add to sum
        for (Map.Entry<String, BigInteger> pair : map.entrySet()) {
            String word = pair.getKey();
            if (wordMap.containsKey(word)) {
                // dot product += frequency(map's key) * frequency(wordMap's key)
                dot = dot.add((pair.getValue().multiply(wordMap.get(word))));
            }
        }
        return dot.doubleValue();
    }
    /**
     * Method to calculate the cosine similarity (distance) of two maps.
     * @param map  another word frequency map that stores a set of words
     * @return double value indicates the cosine similarity of the two hashMaps
     */
    public double distance(Map<String, BigInteger> map) {
        // freq(map) = 0 so freq(map) * freq(wordMap) = 0, distance is PI / 2
        if (map == null || map.isEmpty() || wordMap == null || wordMap.isEmpty()) {
            return Math.PI / 2;
        }
        // handle identical map, if they are identical map,
        //  don't need to calculate dot product, distance is 0
        if (map.equals(wordMap)) {
            return 0.0;
        }
        // calculate dot product of wordMap with input map
        return Math.acos(dotProduct(map) / (norm * computeNorm(map)));
    }
    /**
     * Method to get access data of our private wordMap.
     * @return a copy of our private hashMap (to protect our private data)
     */
    public Map<String, BigInteger> getMap() {
        // return a copy
        Map<String, BigInteger> newMap = new HashMap<String, BigInteger>(wordMap);
        return newMap;
    }
}
