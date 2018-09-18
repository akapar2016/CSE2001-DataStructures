/*

Author: Aayush Kapar
Email: akapar2016@my.fit.edu
Course: CSE 2010
Section: 23
Description: Write a class for Tree

*/
package HW3;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Tree<E> {
	
	/**
	 * Node class
	 * Contains parent and children indexes
	 * @author aayushkapar
	 * @param <E>
	 */
	private class Node<E> {
		private E data;							//element itself
		private Integer parent;					//numeric index of parent location
		private LinkedList<Integer> children;	//list of numeric indexes of children locations
		private boolean marker = false;
		/**
		 * @param e element
		 */
		public Node(E e) {
			this.data = e;
			this.children = new LinkedList<Integer>();
			this.parent = null;
		}
		
		/**
		 * 
		 * @param parent index of parent in list
		 * @param e element
		 */
		public Node(int parent, E e) {
			this.data = e;
			this.children = new LinkedList<Integer>();
			this.parent = parent;
		}
		
		/**
		 * @return boolean if is root
		 */
		public boolean isRoot() { return parent == null; }

		/**
		 * @return boolean if is leaf
		 */
		public boolean isLeaf() { return children.size() == 0; }
		
		/**
		 * @param index index on where to add children
		 * @param value index of child
		 */
		public void addChildren(int index, Integer value) {
			this.children.add(index, value);
		}
		
		/**
		 * @param value add child at end of list
		 */
		public void addChildren(Integer value) {
			this.children.add(value);
		}
		
		/**
		 * @return Linked List of children indexes
		 */
		public LinkedList<Integer> getChildIndexes() { return children; }
		
		/**
		 * @return element
		 */
		public E element(){ return data; }
		
		/**
		 * @return parent index
		 */
		public int getParentIndex(){ return parent; }

		/**
		 * marker boolean
		 */
		public void marker() {marker = true; }
		
		/**
		 * @return marker status
		 */
		public boolean isMarker() {return marker; }
		/**
		 * compareTO method
		 * Does not implement comparable
		 * compares element strings
		 * @param e element to compare 
		 * @return int value of string compareTo of data
		 */
		public int compareTo(Node<E> e) {

			return this.data.toString().compareTo(e.data.toString());
		}
	}
	
	//of all nodes
	private ArrayList<Node<E>> indexes = new ArrayList<Node<E>>();
	
	/**
	 * Constuctor
	 * @param e root element
	 */
	public Tree (E e) {
		indexes.add(new Node<E>(e));
	}
	
	/**
	 * Transverse by width
	 * @param e element to find
	 * @return index in arraylist, -1 if not found
	 */
	public int findNode(E e) {
		if(indexes.size() == 0) {
			throw new IllegalStateException();
		}
		
		for(int i = indexes.size() - 1; i >= 0; i--) {	//go from end to front
			if(indexes.get(i).element().equals(e)) {
				return i;
			}
		}
		
		return -1;	
	}
	
	/**
	 * Transverse by width
	 * @param index to start searching from
	 * @param e element to find
	 * @return index in arraylist, -1 if not found
	 */
	public int findNode(int index, E e) {
		if(indexes.size() == 0) {
			throw new IllegalStateException();
		}
		
		for(int i = index; i >= 0; i--) {				//start at index value
			if(indexes.get(i).element().equals(e)) {
				return i;
			}
		}
		
		return -1;	
	}
	
	/**
	 * Find node with specific parent
	 * @param e element to find
	 * @param parentIndex with index of parent
	 * @return index in arraylist, -1 if not found
	 */
	public int findNodeWithParent(E e, int parentIndex) {
		if(indexes.size() == 0) {
			throw new IllegalStateException();
		}
		
		for(int i = 0; i < indexes.size(); i++) {
			if(indexes.get(i).element().equals(e) && indexes.get(i).getParentIndex() == parentIndex) {		//verify if node has same parent index
				return i;
			}
		}
		
		return -1;	
	}
	
	/**
	 * Called from insertChild method
	 * Add childIndex into node inside parentIndex in order
	 * @param parentIndex
	 * @param childIndex
	 */
	public void addLexicographicalToParent(int parentIndex, int childIndex) {
		int size = indexes.get(parentIndex).getChildIndexes().size();					//size of children array inside parent located in arraylist at parent index
		LinkedList<Integer> temp = indexes.get(parentIndex).getChildIndexes();		//temp list of all children
		for(int i = 0; i < size; i++) {
			if(indexes.get(temp.get(i)).compareTo(indexes.get(childIndex)) >= 0) {	//use compareTo from node class which compares strings
				indexes.get(parentIndex).addChildren(i, childIndex);
				return;
			}
		}
		indexes.get(parentIndex).addChildren(childIndex);								//add childIndex to parent
	}
	
	/**
	 * insert child in Lexicographical order inside parent children list
	 * @param parent element
	 * @param child element
	 */
	public void insertChild(E parent, E child) {
		int index = findNode(parent);
		
		Node<E> temp = new Node<E>(index, child);
		indexes.add(temp);
		addLexicographicalToParent(index, indexes.size() - 1);		//add child index to parent node
	}
	
	/**
	 * Add child to parent at end of parent children list
	 * @param parent element
	 * @param child element
	 */
	public void appendChild(E parent, E child) {
		int index = findNode(parent);
		Node<E> temp = new Node<E>(index, child);
		indexes.add(temp);
		indexes.get(index).addChildren(indexes.size() - 1);
	}
	
	/**
	 * Add child with marker to indicate gold winner
	 * @param parent element
	 * @param child element
	 * @param win boolean 
	 */
	public void appendChild(E parent, E child, boolean win) {
		int index = findNode(parent);
		Node<E> temp = new Node<E>(index, child);
		temp.marker();										//only difference from appendChild
		indexes.add(temp);
		indexes.get(index).addChildren(indexes.size() - 1);
	}
	
	/**
	 * @param e element of parent
	 * @return list of children elements
	 */
	public LinkedList<E> getChildren(E e) {
		Node<E> temp = indexes.get(findNode(e));					//parent node
		
		LinkedList<Integer> retIndex = temp.getChildIndexes();	//index of return values
		LinkedList<E> ret = new LinkedList<E>();					//return list

		for(int i = 0; i < temp.getChildIndexes().size(); i++) {
			ret.add(indexes.get(retIndex.get(i)).element());		//add to return list
		}
		return ret;
	}
	
	/**
	 * Return children of a specific child of parent
	 * @param parent1 element
	 * @param parent2 element
	 * @return list of second generation children
	 */
	public LinkedList<E> secondGen(E parent1, E parent2) {
		int index = findNode(parent1);											//index of first parent
		Node<E> par2Node = indexes.get(findNodeWithParent(parent2, index));		//node of second parent who is a child of first parent
		
		LinkedList<Integer> retIndex = par2Node.getChildIndexes();		//index of return values
		LinkedList<E> ret = new LinkedList<E>();							//return list

		for(int i = 0; i < par2Node.getChildIndexes().size(); i++) {
			ret.add(indexes.get(retIndex.get(i)).element());
		}
		return ret;
		
	}
	
	/**
	 * @param e element
	 * @return parent element
	 */
	public E getParent(E e) {
		return indexes.get(indexes.get(findNode(e)).getParentIndex()).element();
	}
	
	/**
	 * When same children exists for multiple parents
	 * @param e element of child
	 * @return list of all parent elements
	 */
	public LinkedList<E> getParents(E e) {
		LinkedList<E> ret = new LinkedList<E>();			//return values
		int index = findNode(e);							//index of child element

		while(index != -1) {
			ret.addFirst(indexes.get(indexes.get(index).getParentIndex()).element());
			index = findNode(index - 1, e);
		}
		return ret;
	}
	
	/**
	 * @return every element that is a leaf
	 */
	public LinkedList<E> getAllLeaf() {
		LinkedList<E> ret = new LinkedList<E>();
		for(int i = 0; i < indexes.size(); i++) {		//increment through each element
			if(indexes.get(i).isLeaf()) {				//check if is leaf
				ret.add(indexes.get(i).element());
			}
		}
		return ret;
	}

	/**
	 * @return every element that is a leaf and has marker as true to indicate winner
	 */
	public LinkedList<E> getAllWinner() {
		LinkedList<E> ret = new LinkedList<E>();
		for(int i = 0; i < indexes.size(); i++) {
			if(indexes.get(i).isLeaf() && indexes.get(i).isMarker()) {		//check if is a not leaf and has marker as true
				ret.add(indexes.get(i).element());
			}
		}
		return ret;
	}
	
	/**
	 * @return parents of all leaves. Will return dupcliates 
	 */
	public LinkedList<E> getLeafParents() {
		LinkedList<E> ret = new LinkedList<E>();
		for(int i = 0; i < indexes.size(); i++) {		//increment through each element (width transverse)
			if(!indexes.get(i).isLeaf() && indexes.get(indexes.get(i).getChildIndexes().get(0)).isLeaf()) {		//check if index is not a leaf and the child is a leaf
				ret.add(indexes.get(i).element());
			}
		}
		return ret;
	}
	
	/**
	 * @return parents of all leaves with marker to indicate winner
	 */
	public LinkedList<E> getLeafParentWinners() {
		LinkedList<E> ret = new LinkedList<E>();
		for(int i = 0; i < indexes.size(); i++) {
			if(indexes.get(i).isMarker() && !indexes.get(i).isLeaf() && indexes.get(indexes.get(i).getChildIndexes().get(0)).isLeaf()) {		//check if it has marker, is not a leaf, and child is a leaf
				ret.add(indexes.get(i).element());
			}
		}
		return ret;
	}
	
	/**
	 * Print all elements
	 */
	public void printAll() {
		for(int i = 1; i < indexes.size(); i++) {
			System.out.println(indexes.get(i).element() + "   " + indexes.get(i).getChildIndexes().toString());

		}
	}

	
	
}
