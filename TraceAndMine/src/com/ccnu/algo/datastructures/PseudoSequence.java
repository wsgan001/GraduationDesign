package com.ccnu.algo.datastructures;

import java.util.List;


public class PseudoSequence {

	// the corresponding sequence in the original database
	protected Sequence sequence;

	// the first itemset of this pseudo-sequence  in the original sequence
	protected int firstItemset;
	// the first item of this pseudo-sequence in the original sequence
	protected int firstItem;
	
	/**
	 * Default constructor
	 */
	protected PseudoSequence(){
		
	}
	
	/**
	 * Get the original sequence corresponding to this projected sequence.
	 * @return the original sequence
	 */
	public Sequence getOriginalSequence() {
		return sequence;
	}


	/**
	 * Create a pseudo-sequence from a sequence that is a pseudo sequence.
	 * @param sequence the original pseudo-sequence.
	 * @param indexItemset the itemset where the pseudo-sequence should start in terms of the original sequence.
	 * @param indexItem the item where the pseudo-sequence should start in terms of the original sequence.
	 */
	protected PseudoSequence(PseudoSequence sequence, int indexItemset, int indexItem){
		// remember the original sequence
		this.sequence = sequence.sequence;
		// record the position of where the pseudo-sequence starts
		// in terms of the original pseudo-sequence
		this.firstItemset = indexItemset + sequence.firstItemset;
		if(this.firstItemset == sequence.firstItemset){
			this.firstItem = indexItem + sequence.firstItem;
		}else{
			this.firstItem = indexItem; 
		}
	}
	
	/**
	 * Create a pseudo-sequence from a sequence that is an original sequence.
	 * @param sequence the original sequence.
	 * @param indexItemset the itemset where the pseudo-sequence should start in terms of the original sequence.
	 * @param indexItem the item where the pseudo-sequence should start in terms of the original sequence.
	 */
	protected  PseudoSequence(Sequence sequence, int indexItemset, int indexItem){
		// remember the original sequence
		this.sequence = sequence;
		// remember the starting position of this pseudo-sequence in terms
		// of the original sequence.
		this.firstItemset = indexItemset;
		this.firstItem = indexItem;
	}

	/**
	 * Return the size of this pseudo-sequence in terms of itemsets.
	 * @return the size.
	 */
	protected int size() {
		// the size is the size of the original sequence minus
		// the itemset where this pseudo-sequence start
		int size = sequence.size() - firstItemset;
		// if the size is 1 and it the only itemset is empty, return 0
		if(size == 1 && sequence.getItemsets().get(firstItemset).size() == 0){
			return 0;
		}
		// return the size
		return size;
	}

	/**
	 * Return the size in terms of items of an itemset at a given position
	 * (overloaded).
	 * @param index the position of the itemset
	 * @return the number of items in that itemset
	 */
	public int getSizeOfItemsetAt(int index) {
		// We obtain the size of the itemset by looking at the original
		// sequence. To obtain the position of the itemset we do
		//   index + firstItemset.
		int size = sequence.getItemsets().get(index + firstItemset).size();
		// if it is the first itemset of the pseudo-sequence
		if(isFirstItemset(index)){
			// we remove some items if this itemset is cut at left.
			size -=  firstItem;
		}
		return size; // return the size
	}

	/**
	 * Return true if this itemset is cut at left (a postfix).
	 * @param indexItemset the position of the given itemset.
	 * @return true if it is cut at left.
	 */
	protected boolean isPostfix(int indexItemset) {
		// if it is the first itemset of the pseudo-sequence
		// and it is cut at left, we return true.
		return indexItemset == 0  && firstItem !=0;
	}

	/**
	 * Method to check if an itemset is the first one of a pseudo-sequence
	 * @param index  the position of an itemset
	 * @return true if it is the first one.
	 */
	protected boolean isFirstItemset(int index) {
		return index == 0;
	}
	
	/**
	 * Method to check if an itemset is the last one of a pseudo-sequence
	 * @param index  the position of an itemset
	 * @return true if it is the last one.
	 */
	protected boolean isLastItemset(int index) {
		return (index + firstItemset) == sequence.getItemsets().size() -1;
	}

	/**
	 * Get an item at a given position inside a given itemset
	 * @param indexItem the position of the item
	 * @param indexItemset the position of the itemset
	 * @return the item.
	 */
	public Integer getItemAtInItemsetAt(int indexItem, int indexItemset) {
		// if it is in the first itemset
		if(isFirstItemset(indexItemset)){
			// we need to consider if the itemset was cut at the left
			// by adding "firstItem"
			return getItemset(indexItemset).get(indexItem + firstItem);
		}else{// otherwise
			return getItemset(indexItemset).get(indexItem);
		}
	}

	/**
	 * Get the itemset at a given position
	 * @param index the position of the itemset
	 * @return the itemset
	 */
	public List<Integer> getItemset(int index) {
		return sequence.get(index+firstItemset);
	}

	/**
	 * Get the sequence ID of this sequence.
	 * @return a sequence ID (integer)
	 */
	protected int getId() {
		return sequence.getId();
	}

	/**
	 * Print this pseudo-sequence to System.out.
	 */
	public void print() {
		System.out.print(toString());
	}

	/**
	 * Get a string representation of this sequence.
	 */
	public String toString() {
		StringBuilder r = new StringBuilder();
		// for each itemset
		for(int i=0; i < size(); i++){
			// for each item
			for(int j=0; j < getSizeOfItemsetAt(i); j++){
				// append the item
				r.append(getItemAtInItemsetAt(j, i).toString());
				// if it is in a postfix, we add a "*" symbol beside the item
				if(isPostfix(i)){
					r.append('*');
				}

//				if(!isLastItemset(i) ){
					r.append(' ');
//				}
			}
			r.append(" -1 "); // end of an itemset
		}
		r.append(" -2 ");
		// return the string
		return r.toString();
	}

	/**
	 * Get the position of an item inside an itemset.
	 * @param indexItemset the given itemset position
	 * @param idItem the item that we want to search.
	 * @return the position of the item or -1 if it is not found
	 */
	protected int indexOfBis(int indexItemset, int idItem) {
		// for each item in that itemset
		for(int i=0; i < getSizeOfItemsetAt(indexItemset); i++){
			// check if equals to the item that we search
			if(getItemAtInItemsetAt(i, indexItemset) == idItem){
				return i; // if equal, return the current position
			}else if(getItemAtInItemsetAt(i, indexItemset) > idItem){
				continue;
			}
		}
		return -1; // not found, return -1.
	}
	
	/**
	 * Get the position of an item inside an itemset.
	 * @param indexItemset the given itemset position
	 * @param idItem the item that we want to search.
	 * @return the position of the item or -1 if it is not found
	 */
	protected int indexOf(int sizeOfItemsetAti, int indexItemset, int idItem) {
		// for each item in that itemset
		for(int i=0; i <sizeOfItemsetAti; i++){
			// check if equals to the item that we search
			if(getItemAtInItemsetAt(i, indexItemset) == idItem){
				return i; // if equal, return the current position
			}else if(getItemAtInItemsetAt(i, indexItemset) > idItem){
				continue;
			}
		}
		return -1; // not found, return -1.
	}
	
	@Override
	public boolean equals(Object obj) {
		PseudoSequence temp = (PseudoSequence) obj;
		return temp.getId() == getId() && firstItemset == temp.firstItemset &&
				temp.firstItem == this.firstItem;
	}

}
