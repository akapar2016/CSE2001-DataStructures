/*

  Author: Aayush Kapar
  Email: Akapar2016@my.fit.edu
  Course:CSE2010
  Section:23
  Description: SkipList class
  Uses the basis of Doubly linked List to create a Skip List.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package net.datastructures;

/**
 * A basic doubly linked list implementation. altered to fit SKIPLIST
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class SkipList<E> {

 
  // instance variables of the Skip List
	//Always stay on the top left and top right of the list
	private Node<E> head, tail;
  
  /** Number of elements in the list (not including sentinels) */
  private int size = 0;                      // number of elements in the list only in level 0

  /**the height of the skip list*/
  private int height = 0;
  
  /** Constructs a new empty list. */
  public SkipList() {
	  Node<E> left = new Node<E>(null, Key.neg());
	  Node<E> right = new Node<E>(null, Key.pos());
	  left.setNext(right);
	  right.setPrev(left);
	  
	  head = left;
	  tail = right;
	  
  }

  // public accessor methods
  /**
   * Returns the number of elements in the skip  list.
   * @return number of elements in the skip list
   */
  public int size() { return size; }
  
  public int height() { return height; }

  /**
   * Tests whether the list is empty.
   * @return 
   */
  public boolean isEmpty() { return size == 0; }

  // public update methods
  
  /**
   * Adds an element to the list.
   * @param e   the new element to add
   * @param k 	the key of element
 * @throws Exception if content is already there
   */
  public void put(Key k, E e) throws Exception {
	  //find the floor or the the content already there
	  Node<E> search = searchFloor(k);
	  
	  //verify content is not already there
	  if(get(k) != null) {
		  throw new Exception();  	
	  }

	  /*
	   * Newe = new node to be added
	   * search = is the next node right beofre it
	   * Entry = is the node to be added above
	   */
	  
	  Node<E> newe = new Node<E>(e, k);
	  newe.setPrev(search);
	  newe.setNext(search.getNext());
	  search.getNext().setPrev(newe);
	  search.setNext(newe);
	  size++;
	  
	  //Go up to the height of randHeight
	  int randHeight = FakeRandomHeight.get();
	  while(randHeight > height) {
		  addHeight();
	  }
	  
	  //Add element to the height of randHeight
	  for(int i = 0; i < randHeight; i++) {

		  //when nothing is above go left until something else comes above 
		  //THis will help connect the side to side links above the added node
		  while (search.getUp() == null) {
			  search = search.getPrev();
		  }
		  //go to the top
		  search = search.getUp();
		  
		  //new node with the pointers connect to the bottom
		  Node<E> entry = new Node<E>(e, k);
		  
		  //connect this node to side to side 
		  entry.setPrev(search);
		  entry.setNext(search.getNext());
		  entry.setDown(newe);
		  
		  //connect  up and down
		  search.getNext().setPrev(entry);
		  search.setNext(entry);
		  newe.setUp(entry);
		  
		  //repeat for the side of randheight
		  newe = entry;
	  }

  }

  /**
   * Returns the K element of the list.
   * @return the element with key K (or null if empty)
   */
  public E get(Key k) {
	  //search, if found, check if saem key, if yes return element
    if (isEmpty()) return null;
	Node<E> search = searchFloor(k);
	if(search.key.equals(k)) {
		return search.element;
	} else {
		return null;
	}
  }
  
  /**
   * Removes and returns the K element of the list.
   * @return the removed element (or null if empty)
   */
  public E remove(Key k) {
	  //seach to verify k is in the list 
	  if (isEmpty()) { return null;}
		Node<E> node = searchFloor(k);
		
		if(!k.equals(node.key)) {
			return null;
		}
		
		E element = node.getElement();
		

	while(node != null) {
		Node<E> nextNode = node.getUp();
		removeNode(node);		//remove node method, diconnects all sides 
		node = nextNode;			// next node to remove above 
	}
	
	
	//if there is nothing on the top height, remove top height
	while(head.getNext() == tail && height != 0) {
		removeHeight();
	}
	
	size--;
	return element;
  }
  
/**
 * Returns Iterator for the submap
 * @param k1 key 1
 * @param k2 key 2
 * @return iterable of node<E>
 */
  public Iterable<Node<E>> subMap(Key k1, Key k2) {
	  Node<E> nodeFloor = searchFloor(k1);
	  Node<E> nodeCeiling = searchCeiling(k2);
	  
	  //class for iterator methods
	  class subMapIterator implements Iterator<Node<E>> {
		private Node<E> node = nodeFloor;
		@Override
		public boolean hasNext() {
			return node.key.compareTo(k2) > 0;
		}
		@Override 
		public Node<E> next() {
			return node.getNext();
		}
		@Override 
		public void remove() {
            throw new UnsupportedOperationException();
        }
	  }
	  //class to use it as an iterable
	  class subMapIterable implements Iterable<Node<E>> {
		  
		@Override 
		public Iterator<Node<E>> iterator() {
			return new subMapIterator();
		}
		 
	  }
	  //return class above
	  return new subMapIterable();
	  
	  
  }
  
  /**
   * Prints everything between both keys
   * @param k1 floor key
   * @param k2 ceiling key
   */
  public void print(Key k1, Key k2) {
	  //search for the top
	  Node<E> node = searchCeiling(k1);
	  //keep printing as there is elements comparable
	  while(node.element != null && node.key.compareTo(k2) < 0) {
		  System.out.print(" " + node.key.toString() + ":" + node.element.toString());
		  node = node.getNext();
	  }
	  //if there is nothing, print none
	  if(node == searchCeiling(k1)) {
		  System.out.print(" none");
	  }
	  System.out.println();
  }
  
  /**
   * Print whole skip list according to height
   */
  public void print() {
	  Node<E> pointer = head;	//goes through each node
	  Node<E> starter = head;	//stays at the left side of line
	  int emptyHeight = height + 1;	//one extra empty height
	  System.out.println("(S" + emptyHeight + ") empty");
	  //print each line starting with stating height
	  for(int i = height; i >= 0; i--) {
		  System.out.print("(S" + i + ")");
		  pointer = pointer.getNext();
		  //pring everything until reaching tail 
		  while(pointer.element != null) {
			  System.out.print(" " + pointer.key.toString() + ":" + pointer.element.toString());
			  pointer = pointer.getNext();
		  }
		  System.out.println();
		  //the head portion on the left goes down line by line
		  starter = starter.getDown();
		  pointer = starter;
	  }
  }
  
  // private update methods 
  /**
   * 
   * @param k key
   * @return node or floor of node
   */
  private Node<E> searchFloor(Key k) {
	  
	  
	  Node<E> pointer = head;	//start at head
	  if(size == 1) {
		  return pointer.getNext();
	  }
	  
	  while(true) {
		  // look at one height until doesnt meet compare to requrements then drop down and repeat till bottom
		  while(pointer.getNext() != null &&
				  pointer.getNext().compareTo(new Node(null, k)) <= 0) {
			  pointer = pointer.getNext();
			  
		  }
		  //drop dowm
		  if(pointer.getDown() != null) {
			  pointer = pointer.getDown();
		  } else {
			  break;
		  }
	  }
	  return pointer;
  }
  
  /**
   * One higher than floor
   * @param k key
   * @return floor . next 
   */
  private Node<E> searchCeiling(Key k) {
	  
	  return searchFloor(k).getNext();
  }
  
  /**
   * Add height when rand height is above height
   */
  private void addHeight() {
	  //2 new noes
	  Node<E> newLeft = new Node(null, Key.neg());
	  Node<E> newRight = new Node(null, Key.pos());
	  
	  //link to above the head and tail
	  newLeft.setNext(newRight);
	  newRight.setPrev(newLeft);
	  
	  newLeft.setDown(head);
	  newRight.setDown(tail);
	  
	  head.setUp(newLeft);
	  tail.setUp(newRight);
	  
	  //then these 2 become the new head and tail
	  head = newLeft;
	  tail = newRight;
	  height++;
  }
  
  /**
   * disconnect head and tail. there is nothing to reach these anymore
   */
  private void removeHeight() {
	  
	  if(height == 0) {
		  return;
	  }
	  head = head.getDown();
	  tail = tail.getDown();
	  
	  head.setUp(null);
	  tail.setUp(null);

	  height--;
  }
  

  /**
   * Removes the given node from the list and returns its element.
   * @param node    the node to be removed (must not be a sentinel)
   */
  private E removeNode(Node<E> node) {
    Node<E> predecessor = node.getPrev();
    Node<E> successor = node.getNext();
    predecessor.setNext(successor);
    successor.setPrev(predecessor);
    return node.getElement();
  }

} 
