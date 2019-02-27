package picoded.core.struct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.list.AbstractListDecorator;

import picoded.core.conv.GenericConvert;

/**
 * This class provides a static constructor, that builds
 * the wrapper to ensure a GenericConvertList is returned when needed
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
		_decoratedList = new ArrayList<>();
	}
	
	/**
	 * Protected constructor
	 **/
	public ProxyGenericConvertList(List<V> inList) {
		_decoratedList = inList;
	}
	
	// ------------------------------------------------------
	//
	// decorated() function (backwards compatiblity)
	//
	// ------------------------------------------------------
	
	/**
	 * Increasing access scope of internal collection
	 */
	protected List<V> _decoratedList;

	/**
	 * Gets the collection being decorated.
	 * All access to the decorated collection goes via this method.
	 *
	 * @return the decorated collection
	 */
	@Override
	protected List<V> decorated() {
		return this._decoratedList;
	}

	// ------------------------------------------------------
	//
	// Internal list handling
	//
	// ------------------------------------------------------
	
	/**
	 * Getter for the internal underlying list
	 * @return underlying storagV list
	 */
	public List<V> internalList() {
		return this._decoratedList;
	}
	
	/**
	 * Setter for the internal underlying list
	 * @param inList to configurV as underlying storagV list
	 */
	public void internalList(List<V> inList) {
		this._decoratedList = inList;
	}
	
	// ------------------------------------------------------
	//
	// Required UnsupportedDefaultList support
	//
	// ------------------------------------------------------
	
	@Override
	public V get(final int index) {
		return decorated().get(index);
	}

	@Override
	public void add(final int index, final V object) {
		decorated().add(index, object);
	}

	@Override
	public V remove(final int index) {
		return decorated().remove(index);
	}

	@Override
	public int size() {
		return decorated().size();
	}

	// ------------------------------------------------------
	//
	// Additional overrides (not required)
	//
	// ------------------------------------------------------
	
	@Override
	public boolean addAll(final int index, final Collection<? extends V> coll) {
		return decorated().addAll(index, coll);
	}

	@Override
	public int indexOf(final Object object) {
		return decorated().indexOf(object);
	}

	@Override
	public int lastIndexOf(final Object object) {
		return decorated().lastIndexOf(object);
	}

	@Override
	public ListIterator<V> listIterator() {
		return decorated().listIterator();
	}

	@Override
	public ListIterator<V> listIterator(final int index) {
		return decorated().listIterator(index);
	}

	@Override
	public V set(final int index, final V object) {
		return decorated().set(index, object);
	}

	@Override
	public List<V> subList(final int fromIndex, final int toIndex) {
		return decorated().subList(fromIndex, toIndex);
	}

	@Override
	public boolean add(final V object) {
		return decorated().add(object);
	}

	@Override
	public boolean addAll(final Collection<? extends V> coll) {
		return decorated().addAll(coll);
	}

	@Override
	public void clear() {
		decorated().clear();
	}

	@Override
	public boolean contains(final Object object) {
		return decorated().contains(object);
	}

	@Override
	public boolean isEmpty() {
		return decorated().isEmpty();
	}

	@Override
	public Iterator<V> iterator() {
		return decorated().iterator();
	}

	@Override
	public boolean remove(final Object object) {
		return decorated().remove(object);
	}

	@Override
	public Object[] toArray() {
		return decorated().toArray();
	}

	@Override
	public <T> T[] toArray(final T[] object) {
		return decorated().toArray(object);
	}

	@Override
	public boolean containsAll(final Collection<?> coll) {
		return decorated().containsAll(coll);
	}

	@Override
	public boolean removeAll(final Collection<?> coll) {
		return decorated().removeAll(coll);
	}

	@Override
	public boolean retainAll(final Collection<?> coll) {
		return decorated().retainAll(coll);
	}
	
	// ------------------------------------------------------
	//
	// Additional overrides (not required)
	//
	// ------------------------------------------------------
	
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
