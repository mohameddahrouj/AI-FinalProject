package datastructures;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> { 

	private int N;
    private Node first;
    private Node last;

    /**
     * Internal linked list class
     */
    private class Node {
        private Item item;
        private Node next;
    }
    
	public Queue() {
		first = null;
		last = null;
		N = 0;		
	}	
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public int size() {
		return N;
	}
	
	/**
     * Add a new item to the end of the queue.
     */
	public void enqueue(Item item) {
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		
        if (isEmpty()) first = last;        
        else oldlast.next = last;
        
		N++;
	}
	
	/**
     * Delete and return the first item on the queue (FIFO based)
     */
	public Item dequeue() {
		if (isEmpty()) throw new NoSuchElementException("Queue underflow");
		Item item = first.item;
		first = first.next;
		N--;
		if (isEmpty()) last = null;
		return item;
	}
	
	/**
     * Return an iterator that iterates over all of the items on the queue (FIFO based)
     */
	public Iterator<Item> iterator() {
		return new ListIterator();
	}
	
    private class ListIterator implements Iterator<Item> {
    	private Node current = first;

    	public boolean hasNext() { return current != null;						}
    	public void remove() 	 { throw new UnsupportedOperationException();   }
		
		public Item next() { 
			if (!hasNext()) throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
    }
}
