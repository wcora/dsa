package HW6;
import java.util.HashSet;
import java.util.Set;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 6 Binary Search Tree
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class Word implements Comparable<Word> {
    /**
     * the string value.
     */
    private String word;
    /**
     * the list of line indices.
     */
    private Set<Integer> index;
    /**
     * the frequency of this string.
     */
    private int frequency;
    /**
     * the constructor with value input.
     * @param w  the string value
     */
    public Word(String w) {
        word = w;
        index = new HashSet<>();
        frequency = 1;
    }
    /**
     * the method to add a new line to index.
     * @param line  the line to add
     */
    public void addToIndex(Integer line) {
        index.add(line);
    }
    /**
     * the method to get the string value.
     * @return the string value of Word
     */
    public String getWord() {
        return word;
    }
    /**
     * the method to get the frequency.
     * @return the frequency of Word
     */
    public int getFrequency() {
        return frequency;
    }
    /**
     * the method to set the string value.
     * @param w  the string value to change to
     */
    public void setWord(String w) {
        word = w;
    }
    /**
     * the method to get the frequency.
     * @param f  the frequency to change to
     */
    public void setFrequency(int f) {
        frequency = f;
    }
    /**
     * the method to get a copy of the indices.
     * @return a copy of the set
     */
    public Set<Integer> getIndex() {
        // return a copy
        return new HashSet<Integer>(index);
    }
    /**
     * the natural comparison method.
     * @return boolean value indicates the order
     */
    @Override
    public int compareTo(Word w) {
        return word.compareTo(w.word);
    }
    /**
     * the method defines the printing convention.
     * @return the string to print
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(word).append(" ").append(frequency).append(" ").append(index);
        return s.toString();
    }
}
