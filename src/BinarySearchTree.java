/**
 * Binary search tree implementation.
 * 
 * We do not allow duplicates.
 * 
 * @author Greg Gagne
 */
import java.util.*;

import bridges.base.BSTElement;
import bridges.base.BinTreeElement;

public class BinarySearchTree <K extends Comparable<? super K>> implements SearchTreeInterface<K> 
{
	// the root of the binary search tree
	private BSTElement<K, String> root;

	/**
	 * Create an empty binary search tree
	 */
	public BinarySearchTree() {
		root = null;
	}

	/**
	 * This method has nothing to do with a binary search tree,
	 * but is necessary to provide the bridges visualization.
	 */
	public BSTElement<K, String> getRoot() {
		return root;
	}

	public boolean isEmpty() 
	{
		return root == null;
	}

	/**
	 * Solution that uses recursive helper method.
	 * We disallow duplicate elements in the tree. 
	 */
	public K add(K key) {
		if (contains(key))
			return null;
		else {
			root = add(key, root);

			return key;
		}
	}


	/**
	 * private helper method for adding a node to the binary search tree
	 * @param key
	 * @param subtree
	 * @return the root of the tree
	 */
	private BSTElement<K, String> add(K key, BSTElement<K,String> subtree) {
		if (subtree == null) {
			// we have found the spot for the addition

			// create the new node
			// parameters are (1) label (2) key (3) empty string [not used]
			BSTElement<K, String> newNode = new BSTElement<K, String>(key.toString(), key, "");

			// we also set the color of a new node to red
			newNode.getVisualizer().setColor("red");

			return newNode;
		}

		int direction = key.compareTo(subtree.getKey());

		if (direction < 0) {
			subtree.setLeft( add(key, subtree.getLeft()) );
		}
		else if (direction > 0) {
			subtree.setRight( add(key, subtree.getRight()) );
		}

		return subtree;
	}


	/**
	 * Non-recursive algorithm for addition
	 * This only serves the purpose of demonstrating the
	 * differences between the recursive and non-recursive approaches.
	 */
	/*
	public K add(K key) {
		// we disallow duplicates
		if (contains(key))
			return null;

		// create the new node
		// parameters are (1) label (2) key (3) null [not used]
		BSTElement<K, String> newNode = new BSTElement<K, String>(key.toString(), key, "");
		newNode.getVisualizer().setColor("red");

		// if the tree is empty, set the root to the new node
		if (isEmpty()) {
			root = newNode;
		}
		else {
			// else treat it like an unsuccessful search
			BSTElement<K, String> node = root;
			boolean keepLooking = true;

			while (keepLooking) {
				int direction = key.compareTo(node.getKey());

				if (direction < 0) {
					// go left
					if (node.getLeft() == null) {
						// we found the place for the insert
						node.setLeft(newNode);
						keepLooking = false;
					}
					else
						node = node.getLeft();
				}
				else if (direction > 0) {
					// go right
					if (node.getRight() == null) {
						// we found the place for the insert
						node.setRight(newNode);
						keepLooking = false;
					}
					else
						node = node.getRight();
				}
			}
		}

		return key;
	}
	 */

	public K getLargest() 
	{
		BSTElement<K, String> tmp = root;
		
		while(tmp.getRight() != null)
		{
			tmp = tmp.getRight();
		}

		return tmp.getKey();
	}

	public K getSmallest() 
	{
		BSTElement<K, String> tmp = root;
		
		while(tmp.getLeft() != null)
		{
			tmp = tmp.getLeft();
		}

		return tmp.getKey();
	}

	public boolean contains(K key) 
	{		
		Iterator<K> itr = iterator();
		
		while(itr.hasNext())
		{
			if(itr.next() == key)
			{
				return true;
			}
		}

		return false;
	}
	
	public K remove(K key) 
	{
		BSTElement<K, String> left;
		BSTElement<K, String> right;
		BSTElement<K, String> tmp;
		BSTElement<K, String> succ = null;
		succ.setKey(null);
		Iterator<K> itr = iterator();
		
		if(isEmpty())
			return null;
		else if(!contains(key))
			return null;
		else
		{
			tmp = removeHelper(key, root);
			
			if(tmp.getLeft() != null && tmp.getRight() != null)
			{
				while(itr.hasNext())
				{
					if(itr.next().compareTo(succ.getKey()) > 0)
					{
						succ.setKey(itr.next());
					}
				}
				
				tmp = succ;
				return tmp.getKey();
			}
			else if(tmp.getLeft() != null)
			{
				
			}
			else if(tmp.getRight() != null)
			{
				
			}
			else
				key = null;
		}
		return key;
	}
	
	private BSTElement<K, String> removeHelper(K key, BSTElement<K, String> node)
	{
		Iterator<K> itr = iterator();
		
		while(itr.hasNext())
		{
			if(itr.next() == key)
			{
				return node;
			}
			
		}
		
		return null;
	}
	
	public int size() 
	{
		if(isEmpty())
		{
			return 0;
		}
		else
		{
			Queue<BSTElement<K, String>> q = new LinkedList<BSTElement<K, String>>();
			q.add(root);
			int count = 1;
			
			while(!q.isEmpty())
			{
				BSTElement<K, String> tmp = q.poll();
				
				if(tmp != null)
				{
					if(tmp.getLeft() != null)
					{
						count++;
						
						q.add(tmp.getLeft());
					}
					if(tmp.getRight()  != null)
					{
						count++;
						
						q.add(tmp.getRight());
					}
				}
			}
			
			return count;
		}
	}

	public Iterator<K> iterator() 
	{
		return new BSTIterator();
	}
	
	private class BSTIterator implements Iterator<K>
	{
		private K[] elements;
		private int next;

		private BSTIterator() {
			// create an array large enough to hold all elements in the tree
			elements = (K[])new Comparable[size()];
			next = 0;
			
			// now perform an inorder traversal
			inOrder(root);

			// reset next back to the beginning of the array
			next = 0;
		}

		private void inOrder(BSTElement<K, String> node) 
		{
			if (node != null) 
			{
				inOrder(node.getLeft());
				elements[next++] = node.getKey();
				inOrder(node.getRight());
				
			}
		}
		
		public boolean hasNext() {
			return (next < size());
		}

		public K next() {
			return elements[next++];
		}
	}
}
