package HW6;
import java.util.Comparator;
/**
 * 17-683 Data Structures for Application Programmers.
 * Homework 6 Binary Search Tree
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class AlphaFreq implements Comparator<Word> {
    /**
     * the compare method.
     * @param w1 word to compare
     * @param w2 another word to compare
     */
    @Override
    public int compare(Word w1, Word w2) { // alpha by word then by frequency
        int alpha = w1.getWord().compareTo(w2.getWord());
        if (alpha != 0) {
            return alpha;
        } // if alpha comparison returns 0, compare frequency
        return Integer.compare(w1.getFrequency(), w2.getFrequency());
    }
}
