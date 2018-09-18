

  /**
   * Node of a doubly linked list, which stores a reference to its
   * element and to both the previous and next node in the list.
   */
  class Node<E> implements Comparable<Node<E>>{

    /** The element stored at this node */
    E element;               // reference to the element stored at this node
    
    Key key;

    
    /** A reference to the preceding node in the list */
    private Node<E> prev;            // reference to the previous node in the list

    /** A reference to the subsequent node in the list */
    private Node<E> next;            // reference to the subsequent node in the list
    
    /** A reference to the top node in the list */
    private Node<E> up;            // reference to the subsequent node in the list
    
    /** A reference to the bottom node in the list */
    private Node<E> down;            // reference to the subsequent node in the list
    

    /**
     * Creates a node with the given element and next node.
     *
     * @param e  the element to be stored
     * @param p  reference to a node that should precede the new node
     * @param n  reference to a node that should follow the new node
     * @param u  reference to a node that should follow the upper new node
     * @param d  reference to a node that should follow the lower new node
     */
    public Node(E e,  Key k) {
      element = e;
      key = k;

    }

    // public accessor methods
    /**
     * Returns the element stored at the node.
     * @return the element stored at the node
     */
    public E getElement() { return element; }

    /**
     * Returns the node that precedes this one (or null if no such node).
     * @return the preceding node
     */
    public Node<E> getPrev() { return prev; }

    /**
     * Returns the node that follows this one (or null if no such node).
     * @return the following node
     */
    public Node<E> getNext() { return next; }
    
    /**
     * Returns the node that is above this one (or null if no such node).
     * @return the above node
     */
    public Node<E> getUp() { return up; }
    
    /**
     * Returns the node that is below this one (or null if no such node).
     * @return the below node
     */
    public Node<E> getDown() { return down; }
    
    public Node<E> getFollowing() { return next != null ? next:down; }
    
	// Update methods
    /**
     * Sets the node's previous reference to point to Node n.
     * @param p    the node that should precede this one
     */
    public void setPrev(Node<E> p) { prev = p; }

    /**
     * Sets the node's next reference to point to Node n.
     * @param n    the node that should follow this one
     */
    public void setNext(Node<E> n) { next = n; }
    
    /**
     * Sets the node's above reference to point to Node n.
     * @param p    the node that should be above this one
     */
    public void setUp(Node<E> u) { up = u; }

    /**
     * Sets the node's below reference to point to Node n.
     * @param n    the node that should be below this one
     */
    public void setDown(Node<E> d) { down = d; }


	@Override
	public int compareTo(Node<E> n) {
		return key.compareTo(n.key);
	}
  }
