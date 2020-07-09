package HW2;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 17-683 Data Structures for Application Programmers.
 * Homework Assignment 2 Solve Josephus problem
 * using different data structures
 * and different algorithms and compare running times
 *
 * Which method I will use: I will use the List implementation of linkedlist.
 * Both queue implementations are more expensive because in each round we have to
 * add almost the same amount of elements we just removed. For ArrayDeque,
 * it is amortized O(1) per operation, and two times (offer + remove) rotation % size operations per round.
 * Queue linkedlist is the worst because it allocates a new node per offer - very expensive in Java.
 * The list implementation is cheap because we calculate the index and remove one element
 * (O(n) search * O(1) delete) in each round and never add new nodes.
 *
 * Andrew ID: jingxua3
 * @author Cora Wang
 */
public class Josephus {

    /**
     * Uses ArrayDeque class as Queue/Deque to find the survivor's position.
     *
     * @param size Number of people in the circle that is bigger than 0
     * @param rotation Elimination order in the circle. The value has to be greater than 0
     * @return The position value of the survivor
     */
    public int playWithAD(int size, int rotation) {
        if (size <= 0) {
            throw new RuntimeException("size <= 0");
        }
        if (rotation <= 0) {
            throw new RuntimeException("rotation <= 0");
        }
        Queue<Integer> ad = new ArrayDeque<>();
        for (int i = 1; i < size + 1; i++) {
            ad.offer(i);
        }
        while (ad.size() > 1) {
            // eliminate position without rotating more than size
            int pos = rotation % ad.size(); // if rotate > size just rotate the mod
            if (pos == 0) { // if mod is 0, rotate size to remove last
                pos = ad.size();
            }
            for (int j = pos; j > 1; j--) {
                ad.offer(ad.poll()); // rotate until the one to remove is at head
            }
            ad.poll(); // remove one element from head
        }
        return ad.poll();
    }

    /**
     * Uses LinkedList class as Queue/Deque to find the survivor's position.
     *
     * @param size Number of people in the circle that is bigger than 0
     * @param rotation Elimination order in the circle. The value has to be greater than 0
     * @return The position value of the survivor
     */
    public int playWithLL(int size, int rotation) {
        if (size <= 0) {
            throw new RuntimeException("size <= 0");
        }
        if (rotation <= 0) {
            throw new RuntimeException("rotation <= 0");
        }
        Queue<Integer> ll = new LinkedList<>();
        for (int i = 1; i < size + 1; i++) {
            ll.add(i);
        }
        while (ll.size() > 1) {
            // eliminate position without rotating more than size
            int pos = rotation % ll.size(); // if rotate > size just rotate the mod
            if (pos == 0) { // if mod is 0, rotate size to remove last
                pos = ll.size();
            }
            for (int j = pos; j > 1; j--) {
                ll.offer(ll.poll()); // rotate until the one to remove is at head
            }
            ll.poll(); // remove one element from head
        }
        return ll.poll();
    }

    /**
     * Uses LinkedList class to find the survivor's position.
     *
     * However, do NOT use the LinkedList as Queue/Deque
     * Instead, use the LinkedList as "List"
     * That means, it uses index value to find and remove a person to be executed in the circle
     *
     * Note: Think carefully about this method!!
     * When in doubt, please visit one of the office hours!!
     *
     * @param size Number of people in the circle that is bigger than 0
     * @param rotation Elimination order in the circle. The value has to be greater than 0
     * @return The position value of the survivor
     */
    public int playWithLLAt(int size, int rotation) {
        if (size <= 0) {
            throw new RuntimeException("size <= 0");
        }
        if (rotation <= 0) {
            throw new RuntimeException("rotation <= 0");
        }
        List<Integer> ll = new LinkedList<>();
        for (int i = 1; i < size + 1; i++) {
            ll.add(i);
        }
        int removal = 0;
        // need to make sure in each round,
        // remove the correct element without shifting elements around
        while (ll.size() > 1) {
            // still calculate rotation position
            int pos = rotation % ll.size();
            if (pos == 0) {
                pos = ll.size();
            }
            // we need to keep track of the start element of each round:
            // removal index should be previous start index + position to rotate  - 1 % current size
            // and because it is removed, this index automatically becomes the new start element
            // pseudo-code:
            // start = 0;
            // removal = (start - 1 + pos) % ll.size();
            // ll.remove(removal);
            // start = removal;

            // simplified syntax
            removal = (removal - 1 + pos) % ll.size();
            ll.remove(removal);
        }
        return ll.remove(0);
    }

}
