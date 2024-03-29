package datastructures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MinPriorityQueue<Key> implements Iterable<Key> {
    private Key[] pq;
    private int N;
    private Comparator<Key> comparator;

   /**
     * Create an empty priority queue with initial cap
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Create empty priority queue
     */
    public MinPriorityQueue() { this(1); }

   /**
    * Create an empty priority queue with initial cap and comparator
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

   /**
     * Create an empty priority queue with comparator
     */
    public MinPriorityQueue(Comparator<Key> comparator) { this(1, comparator); }

   /**
     * Create a priority queue with items.
     * Sink-based heap model
     * Based on PrincetonU implementation
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < N; i++)
            pq[i+1] = keys[i];
        for (int k = N/2; k >= 1; k--)
            sink(k);
        assert isMinHeap();
    }

    public boolean isEmpty() {
        return N == 0;
    }
    
    public int size() {
        return N;
    }
    
   /**
     * Return the min key on the priority queue
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    //Double size of heap array
    @SuppressWarnings("unchecked")
	private void augment(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= N; i++) temp[i] = pq[i];
        pq = temp;
    }

   /**
     * Add new key to priority queue.
     */
    public void insert(Key x) {
        // augment array size
        if (N == pq.length - 1) augment(2 * pq.length);

        // add x, and move it up to maintain heap invariant
        pq[++N] = x;
        swim(N);
        assert isMinHeap();
    }

   /**
     * Delete and return the smallest key on the priority queue
     */
    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        swap(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N+1] = null;         // avoid loitering and help with GC
        if ((N > 0) && (N == (pq.length - 1) / 4)) augment(pq.length  / 2);
        assert isMinHeap();
        return min;
    }

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            swap(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j < N && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            swap(k, j);
            k = j;
        }
    }

    @SuppressWarnings("unchecked")
	private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        }
        else {
            return comparator.compare(pq[i], pq[j]) > 0;
        }
    }

    private void swap(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > N) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= N && greater(k, left))  return false;
        if (right <= N && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }

   /**
     * Return an iterator that iterates over all of the keys on the priority queue in ascending order.
     */
    public Iterator<Key> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Key> {
        // create new minPQ
        private MinPriorityQueue<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MinPriorityQueue<Key>(size());
            else                    copy = new MinPriorityQueue<Key>(size(), comparator);
            for (int i = 1; i <= N; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }
}
