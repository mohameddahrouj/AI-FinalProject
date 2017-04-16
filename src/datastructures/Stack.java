package datastructures;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> { 

	private int N;
    private Node first;
    
    /**
     * Internal linked list class
     */
    private class Node {
        private Item item;
        private Node next;
    }
    
	public Stack() {
		first = null;
		N = 0;		
	}	
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public int size() {
		return N;
	}
	
	/**
     * Add a new item to the beginning of the stack.
     */
	public void push(Item item) {
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.next = oldfirst;
		
		N++;
	}
	
	/**
     * Delete and return the first item in the stack (LIFO based)
     */
	public Item pop() {
		if (isEmpty()) throw new NoSuchElementException("Stack underflow");
		Item item = first.item;
		first = first.next;
		N--;		
		return item;
	}
	
	public boolean contains(Item item) {
		for (Item it: this) {
			if (it.equals(item)) return true;
		}
		
		return false;
	}
	
	/**
     * Return an iterator that iterates over all of the items in the stack (LIFO based)
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
