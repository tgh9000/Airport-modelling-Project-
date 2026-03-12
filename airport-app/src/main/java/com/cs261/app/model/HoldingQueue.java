package com.cs261.app.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.cs261.app.model.AirCraft.EmergencyStatus;
import com.cs261.app.model.AirCraft.FlightType;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents airport holding queue as a priority queue 
 */
public class HoldingQueue {
	HoldingEntry[] heap; // internally an array
	int size;
	int capacity;
	
	public HoldingQueue(int cap) {
		this.capacity = cap;
		heap = new HoldingEntry[cap];
	}
	
	/**
	 * @return number of elements in the priority queue
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Resize heap to twice its original size once array fills up
	 */
	private void resize() {
		capacity *= 2;
		HoldingEntry[] newHeap = new HoldingEntry[capacity];
		System.arraycopy(heap, 0, newHeap, 0, size);
		this.heap = newHeap;
		newHeap = null;
	}
	/**
	 * Swap two entries, heap[i] and heap[j]
	 * @param i 
	 * @param j
	 */
	private void swap(int i, int j) throws InvalidParameterException {
		if (i < 0 || j < 0){
			throw new InvalidParameterException("Negative indexes");
		}
		HoldingEntry first = heap[i];
		heap[i] = heap[j];
		heap[j] = first;
		first = null;
	}
	/**
	 * returns left child of some node with index i
	 * @param i
	 * @return left child
	 */
	private HoldingEntry getLeft(int i) throws InvalidParameterException{
		if (i >= (capacity / 2) || i < 0) {
			throw new InvalidParameterException();
		}
		return heap[(2 * i) + 1];
	}
	/**
	 * returns right child of some node i
	 * @param i
	 * @return right child
	 */
	private HoldingEntry getRight(int i) throws InvalidParameterException{
		if (i >= (capacity / 2)) {
			throw new InvalidParameterException();
		}
		return heap[(2 * i) + 2];
	}
	/**
	 * return parent of some node i
	 * @param i
	 * @return parent entry or null if it is the root
	 */
	private HoldingEntry getParent(int i) throws InvalidParameterException {
		if (i < 0){
			throw new InvalidParameterException();
		}
		
		if (i == 0) {
			return null;
		}
		return heap[(int) (i - 1) / 2];
	}
	/**
	 * @return true if the heap is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}
	/**
	 * add plane to end of holding queue and then sort priority
	 * @param plane to be added
	 * @param k the serial number of the flight, ie what number arrival flight it is out of all flights that have been set for arrival
	 * so far
	 */
	public void enqueue(int k, AirCraft plane) {
		if (plane.getFlightType() != FlightType.ARRIVAL || k < 0) {
			return;
		}
		if (size >= capacity) {
			resize();
		}
		HoldingEntry newEntry = new HoldingEntry(k, plane);
		heap[size] = newEntry;
		size++;
		upHeap(size - 1);
	}
	/**
	 * get highest priority plane in queue
	 * @return root of heap or null if the queue is empty
	 */
	public AirCraft dequeue() {
		//System.out.println("try dequeue");
		if (isEmpty()){
			return null;
		} 
		AirCraft result = heap[0].getAircraft();
		heap[0] = heap[size - 1]; // move the lowest rightmost node on tree to here
		heap[size - 1] = null; // no duplicate planes
		size--;
		downheap(0);
		//System.out.println("dequeue");
		return result;
	}
	/**
	 * recursively moves i'th node up the heap if it is more important than its parent
	 * @param i node you are moving up the tree
	 */
	private void upHeap(int i) {
		try {
			if (i == 0 || size() < 2) {
				return;
			}

			HoldingEntry parent = getParent(i);
			if (parent == null){
				return;
			}
			// if the i'th entry is more important, swap
			if (heap[i].compareTo(parent) > 0){
				int j = (int) (i - 1) / 2;
				swap(i, j);
				upHeap(j);
			} else {
				return;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * recursively moves i'th node down the tree if it is less important than one of its children
	 * @param i node to down heap from
	 */
	private void downheap(int i) {
		try {
			if (i >= size() || size() < 2) {
				return;
			}

			// find child with the greatest emergency priority
			int check;
			HoldingEntry left = getLeft(i);
			HoldingEntry right = getRight(i);

			//  there has to be at least two nodes for this to run so only check if left or right is null

			if (left == null && right == null){
				return; 
			} else if (left == null && right != null){
				check = (2 * i) + 2;
			} else if (left != null && right == null){
				check = (2 * i) + 1;
			} else {
				if (right.compareTo(left) > 0){ // if right child node is more important than the left child node, compare it against parent
					check = (2 * i) + 2;
				} else {
					check = (2 * i) + 1; // if theyre the same or left is more important just check left
				}	
			}

			// if the node i is strictly less important than its most important child, swap
			if (heap[i].compareTo(heap[check]) < 0){
				swap(i, check); // swap 
				downheap(check); // new index of node i
			} else {
				return; // if they are the same or node i is less important (more than/equal to) dont swap
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Traverse heap O(n)
	 * worst case O(nlogn) if it so happens that every plane has a fuel emergency for some reason
	 * @param t current time in simulation as # of ticks
	 */
	public void updateHolding(int t) {
		for (int i = 0; i < size(); i++) {
			AirCraft p;
			if (getParent(i) == null){
				p = null;
			} else {
				p = getParent(i).getAircraft();
			}
			if (heap[i].getAircraft().updateHoldingFlight(t, p)) { // update holding flight returns true if there is a fuel emergency 
				upHeap(i); // might need to move it up the heap
			} else { // no fuel emergency for now
				continue;
			}
		}
	}

	/**
	 * @return first node in priority queue without removing it
	 */
	public AirCraft top(){
		if (isEmpty()) {
			return null;
		} else {
			return heap[0].getAircraft();
		}
	}
	

	/**
	 * Return the sibling of node i in heap
	 * @param i
	 */
	private HoldingEntry getSibling(int i){
		int parent = (int) (i - 1) / 2;
		int parentLChild = (2 * parent) + 1;

		if (i == parentLChild){
			// get right
			return getRight(parent);
		} else {
			// get left
			return getLeft(parent);
		}
	}

	/**
	 * Detect and remove all diversions, given the plane has passed the emergency wait time. O(nlogn) in worst if all planes in HP are in 
	 * emergency.
	 */
	public void removeDiversions(ArrayList<String> exitedPlanes, int t){
		try {
			if (isEmpty()){
				return;
			}

			/**
			 * The plane at the top, if it is an emergency, has been waiting longer than all other planes in emergency as it has the lowest serial number
			 * therefore if this plane has not passed emergency wait time no planes below it has
			 */
			boolean keepGoing = true;

			while (keepGoing){
				if (this.top().getEmergencyStatus() == EmergencyStatus.NONE){
					keepGoing = false;
				} else {
					if ((t - this.top().getEmergencyTime()) >= Utils.emergencyTime){
						AirCraft front = this.dequeue();
						front.setExitTime(Utils.convertTicksToDate(t + Utils.timeInc));
						front.setZoneStatus(AirCraft.ZoneStatus.DIVERT);
						exitedPlanes.add(front.getCallSign());
						keepGoing = true; // just in case next planes are also in emergency
					} else {
						keepGoing = false;  // otherwise, because the root is the plane that has been in HP longest (in emergency), then don't keep going as the rest are not over EmergencyTime too 
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return hashset of all of the plane ids in the holding pattern right now 
	 */
	public Set<String> getAircraftIds(){
		Set<String> ids = new HashSet<>();
		for (int i = 0; i < size(); i++){
				ids.add(heap[i].getAircraft().getCallSign());
		}
		return ids;
	}	

	/**
	 * Inner class to store key and plane in holding queue
	 */
	private class HoldingEntry {
		int serial; // what number arrival it is in the entire simulation
		AirCraft plane;
		
		HoldingEntry(int s, AirCraft a){
			serial = s;
			plane = a;
		}

		private int getSerial(){
			return this.serial;
		}

		private AirCraft getAircraft(){
			return this.plane;
		}

		/**
		 * @param other holding queue entry its being compared to
		 * @return 1 if this plane is more important than the other plane, 0 if they are of equal importance, -1 if this plane is less important than the other one 
		 */
		private int compareTo(HoldingEntry other) {
			// if one is in emergency and one is not, the plane in emergency is more important  
			if (this.plane.getEmergencyStatus() == EmergencyStatus.NONE && other.getAircraft().getEmergencyStatus() != EmergencyStatus.NONE){
				return -1;
			}
			if (this.plane.getEmergencyStatus() != EmergencyStatus.NONE && other.getAircraft().getEmergencyStatus() == EmergencyStatus.NONE){
				return 1;
			}
			// if they both have no emergency, or if they both have an emergency, the plane that came into the simulation first is more important 
			if (this.serial < other.getSerial()){
				return 1;
			} else if (this.serial > other.getSerial()){
				return -1;
			} else {
				return 0;
			}
		}
	}
}
