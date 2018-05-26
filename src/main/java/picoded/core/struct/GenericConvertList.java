package picoded.core.struct;

import java.util.List;
import java.util.UUID;

import picoded.core.conv.GenericConvert;
import picoded.core.conv.NestedObjectUtil;
import picoded.core.conv.NestedObjectFetch;
import picoded.core.struct.template.UnsupportedDefaultList;

/**
 * Common list class, used to implement all the generic convert convinence functions in a map interface.
 **/
@SuppressWarnings("all")
public interface GenericConvertList<E> extends UnsupportedDefaultList<E> {
	
	// Static proxy build
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * Ensures the returned map is a GenericConvertMap, doing the conversion if needed.
	 **/
	static <E> GenericConvertList<E> build(List<E> inList) {
		return ProxyGenericConvertList.ensure(inList);
	}
	
	//--------------------------------------------------------------------------------------------------
	//
	// basic GET and FETCH
	//
	//--------------------------------------------------------------------------------------------------

	// Silent varient of get without OUT OF BOUND exception
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * Subtle varient of GET call, where out of bound errors
	 * are surpressed and null is returned.
	 **/
	default E getSubtle(int index) {
		if (index < this.size() && index >= 0) {
			return get(index);
		}
		return null;
	}
	
	// NESTED object fetch
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * Gets an object from the List,
	 * That could very well be, a list inside a list, inside a map, inside a .....
	 *
	 * Note that at each iteration step, it attempts to do a FULL index match first,
	 * before the next iteration depth
	 *
	 * @param index       The input index to fetch, possibly nested
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The fetched object, always possible unless fallbck null
	 **/
	default Object fetchObject(String index, Object fallbck) {
		return NestedObjectFetch.fetchObject(this, index, fallbck);
	}
	
	/**
	 * Default Null fallback, for `fetchObject(index,fallback)`
	 *
	 * @param index       The input index to fetch, possibly nested
	 *
	 * @return         The fetched object, always possible unless fallbck null
	 **/
	default Object fetchObject(String index) {
		return fetchObject(index, null);
	}
	
	//--------------------------------------------------------------------------------------------------
	//
	// GET operations with conversion
	//
	//--------------------------------------------------------------------------------------------------

	// to string conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable, aka null)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default String getString(int index, String fallbck) {
		return GenericConvert.toString(getSubtle(index), fallbck);
	}
	
	/**
	 * Default null fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default String getString(int index) {
		return GenericConvert.toString(getSubtle(index));
	}
	
	// to boolean conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To boolean conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default boolean getBoolean(int index, boolean fallbck) {
		return GenericConvert.toBoolean(getSubtle(index), fallbck);
	}
	
	/**
	 * Default boolean fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string to boolean, always possible
	 **/
	default boolean getBoolean(int index) {
		return GenericConvert.toBoolean(getSubtle(index));
	}
	
	// to Number conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To Number conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default Number getNumber(int index, Number fallbck) {
		return GenericConvert.toNumber(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Number fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default Number getNumber(int index) {
		return GenericConvert.toNumber(getSubtle(index));
	}
	
	// to int conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To int conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default int getInt(int index, int fallbck) {
		return GenericConvert.toInt(getSubtle(index), fallbck);
	}
	
	/**
	 * Default int fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default int getInt(int index) {
		return GenericConvert.toInt(getSubtle(index));
	}
	
	// to long conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To long conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default long getLong(int index, long fallbck) {
		return GenericConvert.toLong(getSubtle(index), fallbck);
	}
	
	/**
	 * Default long fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default long getLong(int index) {
		return GenericConvert.toLong(getSubtle(index));
	}
	
	// to float conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To float conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default float getFloat(int index, float fallbck) {
		return GenericConvert.toFloat(getSubtle(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default float getFloat(int index) {
		return GenericConvert.toFloat(getSubtle(index));
	}
	
	// to double conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To double conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default double getDouble(int index, double fallbck) {
		return GenericConvert.toDouble(getSubtle(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default double getDouble(int index) {
		return GenericConvert.toDouble(getSubtle(index));
	}
	
	// to byte conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To byte conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default byte getByte(int index, byte fallbck) {
		return GenericConvert.toByte(getSubtle(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default byte getByte(int index) {
		return GenericConvert.toByte(getSubtle(index));
	}
	
	// to short conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To short conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default short getShort(int index, short fallbck) {
		return GenericConvert.toShort(getSubtle(index), fallbck);
	}
	
	/**
	 * Default short fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default short getShort(int index) {
		return GenericConvert.toShort(getSubtle(index));
	}
	
	// to UUID / GUID
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To UUID conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted UUID, always possible unless null
	 **/
	default UUID getUUID(int index, Object fallbck) {
		return GenericConvert.toUUID(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To UUID conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default UUID getUUID(int index) {
		return GenericConvert.toUUID(getSubtle(index));
	}
	
	/**
	 * To GUID conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted UUID, always possible unless null
	 **/
	default String getGUID(int index, Object fallbck) {
		return GenericConvert.toGUID(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To GUID conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default String getGUID(int index) {
		return GenericConvert.toGUID(getSubtle(index));
	}
	
	/**
	 * To List<Object> conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Object[], always possible unless null
	 **/
	default List<Object> getObjectList(int index, Object fallbck) {
		return GenericConvert.toList(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To List<Object> conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @default         The converted value
	 **/
	default List<Object> getObjectList(int index) {
		return GenericConvert.toList(getSubtle(index));
	}
	
	// to string array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String[] conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted String[], always possible unless null
	 **/
	default String[] getStringArray(int index, Object fallbck) {
		return GenericConvert.toStringArray(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String[] conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default String[] getStringArray(int index) {
		return GenericConvert.toStringArray(getSubtle(index));
	}
	
	// to object array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To Object[] conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Object[], always possible unless null
	 **/
	default Object[] getObjectArray(int index, Object fallbck) {
		return GenericConvert.toObjectArray(index, fallbck);
	}
	
	/**
	 * Default Null fallback, To Object[] conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @default         The converted value
	 **/
	default Object[] getObjectArray(int index) {
		return GenericConvert.toObjectArray(index);
	}
	
	// Generic string map
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <K extends String, V> GenericConvertMap<K, V> getGenericConvertStringMap(int index,
		Object fallbck) {
		return GenericConvert.toGenericConvertStringMap(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <K extends String, V> GenericConvertMap<K, V> getGenericConvertStringMap(int index) {
		return GenericConvert.toGenericConvertStringMap(getSubtle(index));
	}
	
	// to array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <V> GenericConvertList<V> getGenericConvertList(int index, Object fallbck) {
		return GenericConvert.toGenericConvertList(getSubtle(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <V> GenericConvertList<V> getGenericConvertList(int index) {
		return GenericConvert.toGenericConvertList(getSubtle(index));
	}

	//--------------------------------------------------------------------------------------------------
	//
	// FETCH operations with conversion
	//
	//--------------------------------------------------------------------------------------------------

	// to string conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable, aka null)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default String fetchString(String index, String fallbck) {
		return GenericConvert.toString(fetchObject(index), fallbck);
	}
	
	/**
	 * Default null fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default String fetchString(String index) {
		return GenericConvert.toString(fetchObject(index));
	}
	
	// to boolean conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To boolean conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default boolean fetchBoolean(String index, boolean fallbck) {
		return GenericConvert.toBoolean(fetchObject(index), fallbck);
	}
	
	/**
	 * Default boolean fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string to boolean, always possible
	 **/
	default boolean fetchBoolean(String index) {
		return GenericConvert.toBoolean(fetchObject(index));
	}
	
	// to Number conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To Number conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default Number fetchNumber(String index, Number fallbck) {
		return GenericConvert.toNumber(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Number fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default Number fetchNumber(String index) {
		return GenericConvert.toNumber(fetchObject(index));
	}
	
	// to int conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To int conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default int fetchInt(String index, int fallbck) {
		return GenericConvert.toInt(fetchObject(index), fallbck);
	}
	
	/**
	 * Default int fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default int fetchInt(String index) {
		return GenericConvert.toInt(fetchObject(index));
	}
	
	// to long conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To long conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default long fetchLong(String index, long fallbck) {
		return GenericConvert.toLong(fetchObject(index), fallbck);
	}
	
	/**
	 * Default long fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default long fetchLong(String index) {
		return GenericConvert.toLong(fetchObject(index));
	}
	
	// to float conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To float conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default float fetchFloat(String index, float fallbck) {
		return GenericConvert.toFloat(fetchObject(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default float fetchFloat(String index) {
		return GenericConvert.toFloat(fetchObject(index));
	}
	
	// to double conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To double conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default double fetchDouble(String index, double fallbck) {
		return GenericConvert.toDouble(fetchObject(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default double fetchDouble(String index) {
		return GenericConvert.toDouble(fetchObject(index));
	}
	
	// to byte conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To byte conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default byte fetchByte(String index, byte fallbck) {
		return GenericConvert.toByte(fetchObject(index), fallbck);
	}
	
	/**
	 * Default float fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default byte fetchByte(String index) {
		return GenericConvert.toByte(fetchObject(index));
	}
	
	// to short conversion
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To short conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default short fetchShort(String index, short fallbck) {
		return GenericConvert.toShort(fetchObject(index), fallbck);
	}
	
	/**
	 * Default short fallback, To String conversion of generic object
	 *
	 * @param index       The input value index to convert
	 *
	 * @return         The converted string, always possible unless null
	 **/
	default short fetchShort(String index) {
		return GenericConvert.toShort(fetchObject(index));
	}
	
	// to UUID / GUID
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To UUID conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted UUID, always possible unless null
	 **/
	default UUID fetchUUID(String index, Object fallbck) {
		return GenericConvert.toUUID(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To UUID conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default UUID fetchUUID(String index) {
		return GenericConvert.toUUID(fetchObject(index));
	}
	
	/**
	 * To GUID conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted UUID, always possible unless null
	 **/
	default String fetchGUID(String index, Object fallbck) {
		return GenericConvert.toGUID(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To GUID conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default String fetchGUID(String index) {
		return GenericConvert.toGUID(fetchObject(index));
	}
	
	/**
	 * To List<Object> conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Object[], always possible unless null
	 **/
	default List<Object> fetchObjectList(String index, Object fallbck) {
		return GenericConvert.toList(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To List<Object> conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @default         The converted value
	 **/
	default List<Object> fetchObjectList(String index) {
		return GenericConvert.toList(fetchObject(index));
	}
	
	// to string array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String[] conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted String[], always possible unless null
	 **/
	default String[] fetchStringArray(String index, Object fallbck) {
		return GenericConvert.toStringArray(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String[] conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @return         The converted value
	 **/
	default String[] fetchStringArray(String index) {
		return GenericConvert.toStringArray(fetchObject(index));
	}
	
	// to object array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To Object[] conversion of generic object
	 *
	 * @param index       The input value index to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Object[], always possible unless null
	 **/
	default Object[] fetchObjectArray(String index, Object fallbck) {
		return GenericConvert.toObjectArray(index, fallbck);
	}
	
	/**
	 * Default Null fallback, To Object[] conversion of generic object
	 *
	 * @param input     The input value to convert
	 *
	 * @default         The converted value
	 **/
	default Object[] fetchObjectArray(String index) {
		return GenericConvert.toObjectArray(index);
	}
	
	// Generic string map
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <K extends String, V> GenericConvertMap<K, V> fetchGenericConvertStringMap(String index,
		Object fallbck) {
		return GenericConvert.toGenericConvertStringMap(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <K extends String, V> GenericConvertMap<K, V> fetchGenericConvertStringMap(String index) {
		return GenericConvert.toGenericConvertStringMap(fetchObject(index));
	}
	
	// to array
	//--------------------------------------------------------------------------------------------------
	
	/**
	 * To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 * @param fallbck   The fallback default (if not convertable)
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <V> GenericConvertList<V> fetchGenericConvertList(String index, Object fallbck) {
		return GenericConvert.toGenericConvertList(fetchObject(index), fallbck);
	}
	
	/**
	 * Default Null fallback, To String Map conversion of generic object
	 *
	 * @param key       The input value key to convert
	 *
	 * @return         The converted Map if possible, else null
	 **/
	default <V> GenericConvertList<V> fetchGenericConvertList(String index) {
		return GenericConvert.toGenericConvertList(fetchObject(index));
	}
}
