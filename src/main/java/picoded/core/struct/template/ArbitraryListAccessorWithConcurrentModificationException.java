package picoded.core.struct.template;

import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * In most cases, you probably should use AbstractListIterator, or AbstractListSublist instead.
 *
 * This class does not provide "true" ConcurrentModificationException protection. 
 * But instead fakes it by monitoring the list "size()" value, to detect ConcurrentModificationException.
 *
 * Captures the List size. And performs ConcurrentModificationException checks
 * via the checkForChange functions. Note this is not meant for direct use.
 * This considers a size change as "change" in array, on a "change" detection
 * the status is locked. And throws ConcurrentModificationException subsequently.
 **/
class ArbitraryListAccessorWithConcurrentModificationException<E> {
	
	//
	// Internal tracking variables
	//-------------------------------------------------------------------
	
	protected List<E> base; // List used to build this
	protected int initialSize; // Initial list size
	protected boolean detectedChange; // Change has already been detected
	
	//
	// Constructor and utils
	//-------------------------------------------------------------------
	
	/**
	 * Constructor setting up the base list, and index point
	 *
	 * @param  List to use as base, for get/size operations
	 * @param  index position to iterate from
	 **/
	public ArbitraryListAccessorWithConcurrentModificationException(List<E> inBase) {
		base = inBase;
		initialSize = base.size();
		detectedChange = false;
	}
	
	/**
	 * Checks if any change has occured since previous iteration call.
	 * Throws a ConcurrentModificationException if so.
	 **/
	protected void checkForChange() {
		if (detectedChange) {
			throw new ConcurrentModificationException();
		}
		
		if (base.size() != initialSize) {
			throwChangeException();
		}
	}
	
	/**
	 * Declares a change has occured.
	 * Throws a ConcurrentModificationException.
	 **/
	protected void throwChangeException() {
		detectedChange = true;
		throw new ConcurrentModificationException();
	}
	
	/**
	 * Reset the size capture
	 **/
	protected void resetSizeState() {
		if (detectedChange) {
			throw new ConcurrentModificationException();
		}
		initialSize = base.size();
	}
}
