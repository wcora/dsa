package HW6;
import java.util.Comparator;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 6 Binary Search Tree
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class Frequency implements Comparator<Word> {
    /**
     * the compare method.
     * @param w1 word to compare
     * @param w2 another word to compare
     */
    @Override
    public int compare(Word w1, Word w2) { // compare by frequency of two words
        return Integer.compare(w2.getFrequency(), w1.getFrequency());
    }
}
