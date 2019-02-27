package picoded.core.struct;

import java.util.Map;

import org.apache.commons.collections4.map.AbstractMapDecorator;

import picoded.core.conv.GenericConvert;

public class ProxyGenericConvertMap<K, V> extends AbstractMapDecorator<K, V> implements
	GenericConvertMap<K, V> {
	
	/**
	 * The static builder for the map - that helps ensure the output is a GenericConvertMap
	 * 
	 * @return  GenericConvertMap equivalent of the given map
	 **/
	public static <A, B> GenericConvertMap<A, B> ensure(Map<A, B> inMap) {
		if (inMap instanceof GenericConvertMap) { // <A,B>
			return (GenericConvertMap<A, B>) inMap;
		}
		return new ProxyGenericConvertMap<A, B>(inMap);
	}
	
	// ------------------------------------------------------
	//
	// Constructors
	//
	// ------------------------------------------------------
	
	/**
	 * Constructor
	 **/
	public ProxyGenericConvertMap() {
		super();
	}
	
	/**
	 * Constructor
	 **/
	@SuppressWarnings("unchecked")
	public ProxyGenericConvertMap(Map<? extends K, ? extends V> m) {
		super((Map<K, V>) m);
	}
	
	// ------------------------------------------------------
	//
	// Internal map handling
	//
	// ------------------------------------------------------
	
	/**
	 * Getter for the internal underlying map
	 * @return underlying storage map
	 */
	public Map<K, V> internalMap() {
		return this.map;
	}
	
	/**
	 * Setter for the internal underlying map
	 * @param inMap to configure as underlying storage map
	 */
	public void internalMap(Map<K, V> inMap) {
		this.map = inMap;
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
