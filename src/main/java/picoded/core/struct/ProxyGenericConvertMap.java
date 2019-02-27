package picoded.core.struct;

import java.util.Collection;
import java.util.HashMap;
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
		this.map = new HashMap<>();
	}
	
	/**
	 * Constructor
	 **/
	@SuppressWarnings("unchecked")
	public ProxyGenericConvertMap(Map<? extends K, ? extends V> m) {
		this.map = (Map<K,V>)(Object)m;
	}
	
	// ------------------------------------------------------
	//
	// Increase the access scope level of decorated()
	//
	// ------------------------------------------------------
	
	/**
	 * Increasing access scope of internal collection
	 */
	protected transient Map<K, V> map;

	/**
	 * Gets the collection being decorated.
	 * All access to the decorated collection goes via this method.
	 *
	 * @return the decorated collection
	 */
	protected Map<K, V> decorated() {
		return map;
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
