package picoded.core.struct;

import java.util.Collection;
import java.util.HashMap;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;

import picoded.core.conv.GenericConvert;

/**
 * Provides a GenericConertMap wrapper around any existing map interface.
 * 
 * This is losely based off AbstractMapDecorator.java in apache commons.
 * With additional support for changing the internalMap directly
 */
public class ProxyGenericConvertMap<K, V> implements GenericConvertMap<K, V> {
	
	/**
	 * The static builder for the map - that helps ensure the output is a GenericConvertMap
	 * 
	 * @return  GenericConvertMap equivalent of the given map
	 **/
	public static <A, B> GenericConvertMap<A, B> ensure(Map<A, B> inMap) {
		// Quick null handling
		if (inMap == null) {
			return null;
		}
		
		if (inMap instanceof GenericConvertMap) { // <A,B>
			return (GenericConvertMap<A, B>) inMap;
		}
		
		return new ProxyGenericConvertMap<A, B>(inMap);
	}
	
	/**
	 * The static builder for the map - that helps ensure the output is a ProxyGenericConvertMap
	 * 
	 * @return  ProxyGenericConvertMap equivalent of the given map
	 **/
	public static <T extends ProxyGenericConvertMap, A, B> T ensure(Class<T> classObj,
		Map<A, B> inMap) {
		// Quick null handling
		if (inMap == null) {
			return null;
		}
		
		// Instance of match
		if (classObj.isInstance(inMap)) {
			return (T) inMap;
		}
		
		// Remapping
		try {
			T ret = classObj.newInstance();
			ret.internalMap(inMap);
			return ret;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * The static builder for the map - that helps ensure the output is a ProxyGenericConvertMap
	 * 
	 * @return  ProxyGenericConvertMap equivalent of the given map as an array array
	 **/
	public static <T extends ProxyGenericConvertMap, A, B> T[] ensureArray(Class<T> classObj,
		Map<A, B>[] mapArray) {
		// Quick null handling
		if (mapArray == null) {
			return null;
		}
		
		// Prepare the return result
		T[] ret = (T[]) Array.newInstance(classObj, mapArray.length);
		
		// Iterate each object
		for (int i = 0; i < mapArray.length; ++i) {
			ret[i] = ProxyGenericConvertMap.ensure(classObj, mapArray[i]);
		}
		
		// Return formatted array
		return ret;
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
		this._decoratedMap = new HashMap<>();
	}
	
	/**
	 * Constructor
	 **/
	@SuppressWarnings("unchecked")
	public ProxyGenericConvertMap(Map<? extends K, ? extends V> m) {
		this._decoratedMap = (Map<K, V>) (Object) m;
	}
	
	// ------------------------------------------------------
	//
	// decorated() function (backwards compatiblity)
	//
	// ------------------------------------------------------
	
	/**
	 * Increasing access scope of internal collection
	 */
	protected transient Map<K, V> _decoratedMap;
	
	/**
	 * Gets the collection being decorated.
	 * All access to the decorated collection goes via this method.
	 *
	 * @return the decorated collection
	 */
	protected Map<K, V> decorated() {
		return _decoratedMap;
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
		return this._decoratedMap;
	}
	
	/**
	 * Setter for the internal underlying map
	 * @param inMap to configure as underlying storage map
	 */
	public void internalMap(Map<K, V> inMap) {
		this._decoratedMap = inMap;
	}
	
	// ------------------------------------------------------
	//
	// Required UnsupportedDefaultMap support
	//
	// ------------------------------------------------------
	
	@Override
	public V get(final Object key) {
		return decorated().get(key);
	}
	
	@Override
	public V put(final K key, final V value) {
		return decorated().put(key, value);
	}
	
	@Override
	public V remove(final Object key) {
		return decorated().remove(key);
	}
	
	@Override
	public Set<K> keySet() {
		return decorated().keySet();
	}
	
	// ------------------------------------------------------
	//
	// Additional overrides (not required)
	//
	// ------------------------------------------------------
	
	@Override
	public void clear() {
		decorated().clear();
	}
	
	@Override
	public boolean containsKey(final Object key) {
		return decorated().containsKey(key);
	}
	
	@Override
	public boolean containsValue(final Object value) {
		return decorated().containsValue(value);
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return decorated().entrySet();
	}
	
	@Override
	public boolean isEmpty() {
		return decorated().isEmpty();
	}
	
	@Override
	public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
		decorated().putAll(mapToCopy);
	}
	
	@Override
	public int size() {
		return decorated().size();
	}
	
	@Override
	public Collection<V> values() {
		return decorated().values();
	}
	
	@Override
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		return decorated().equals(object);
	}
	
	@Override
	public int hashCode() {
		return decorated().hashCode();
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
		return GenericConvert.toString(decorated());
	}
}
