package picoded.core.conv;

import java.util.*;

/**
 * Utility conversion class, that helps convert Map values from one type to another.
 **/
public class ListValueConv {
	
	/**
	 * Invalid constructor (throws exception)
	 **/
	protected ListValueConv() {
		throw new IllegalAccessError("Utility class");
	}


	/**
	 * Convert objects in a list to string array
	 * This method will attempts to convert the object into string, else it will be replaced with null value
	 *
	 * @param listObj List of objects to be converted
	 * @return String array of the result
	 */
	public static String[] objectListToStringArray(List<Object> listObj) {
		String[] stringArr = new String[listObj.size()];
		for (int a = 0; a < listObj.size(); ++a) {
			Object obj = listObj.get(a);
			stringArr[a] = obj != null ? GenericConvert.toString(obj, null) : null;
		}
		return stringArr;
	}

	/**
	 * Convert objects in the list to string and place them into a list
	 * This method will attempts to convert the object into string, else it will be replaced with null value
	 *
	 * @param listObj List of objects to be converted
	 * @return String list of the result
	 */
	public static List<String> objectToString(List<Object> listObj) {
		List<String> stringList = new ArrayList<String>();
		for (Object obj : listObj) {
			stringList.add(obj != null ? GenericConvert.toString(obj, null) : null);
		}
		return stringList;
	}

	/**
	 * Remove duplicate values in List<String>, ignoring order of the original list
	 *
	 * @param list List of strings that may contain duplicate values
	 * @return unique list of strings
	 */
	public static List<String> deduplicateValuesWithoutArrayOrder(List<String> list) {
		Set<String> set = new HashSet<String>();
		set.addAll(list);
		return new ArrayList<String>(set);
	}

	/**
	 * Convert List<V> into Set<String>
	 * The items in the list will be converted to String using toString() function
	 *
	 * @param list List to be converted
	 * @return a String Set
	 */
	public static <V> Set<String> toStringSet(List<V> list) {
		Set<String> ret = new HashSet<String>();
		for (V item : list) {
			ret.add(item.toString());
		}
		return ret;
	}
}
