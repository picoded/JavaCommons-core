package picoded.core.struct;

import java.util.List;

import org.apache.commons.collections4.list.AbstractListDecorator;

import picoded.core.conv.GenericConvert;

/**
 * This class provides a static constructor, that builds
 * the wrapper to ensure a GenericConvertMap is returned when needed
 *
 * ### Example Usage
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{.java}
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 **/
public class ProxyGenericConvertList<V> extends AbstractListDecorator<V> implements
	GenericConvertList<V> {
	
	/**
	 * The static builder for the list - that helps ensure the output is a GenericConvertList
	 * 
	 * @return  GenericConvertList equivalent of the given List
	 **/
	public static <V> GenericConvertList<V> ensure(List<V> inList) {
		if (inList instanceof GenericConvertList) { // <V>
			return (GenericConvertList<V>) inList;
		}
		return new ProxyGenericConvertList<V>(inList);
	}
	
	// ------------------------------------------------------
	//
	// Constructors
	//
	// ------------------------------------------------------
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 **/
	public ProxyGenericConvertList() {
		super();
	}
	
	/**
	 * Protected constructor
	 **/
	public ProxyGenericConvertList(List<V> inList) {
		super(inList);
	}
	
	// ------------------------------------------------------
	//
	// Internal list handling
	//
	// ------------------------------------------------------
	
	/**
	 * Getter for the internal underlying map
	 * @return underlying storage map
	 */
	public List<K, V> internalList() {
		return (List<K, V>) this.collection;
	}
	
	/**
	 * Setter for the internal underlying map
	 * @param inList to configure as underlying storage map
	 */
	public void internalList(List<K, V> inList) {
		this.collection = inList;
	}
	
	// ------------------------------------------------------
	//
	// String support
	//
	// ------------------------------------------------------
	
	/**
	 * Implments a JSON to string conversion
	 **/
	@Override
	public String toString() {
		return GenericConvert.toString(this);
	}
}
