package picoded.core.conv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility conversion class, that helps convert Map values from one type to another.
 **/
public class MapValueConv {
	
	/**
	 * Invalid constructor (throws exception)
	 **/
	protected MapValueConv() {
		throw new IllegalAccessError("Utility class");
	}
	
	/**
	 * Converts a Map with List values, into array values
	 **/
	public static <A, B> Map<A, B[]> convertMapOfListToMapOfArray(Map<A, List<B>> source, Map<A, B[]> target,
																  B[] arrayType) {
		/**
		 * Normalize array type to 0 length
		 **/
		arrayType = normalizeArrayType(arrayType);
		
		for (Map.Entry<A, List<B>> entry : source.entrySet()) {
			List<B> value = entry.getValue();
			if (value == null) {
				target.put(entry.getKey(), null);
			} else {
				target.put(entry.getKey(), value.toArray(arrayType));
			}
		}
		
		return target;
	}
	
	/**
	 * Converts a Map with List values, into array values. Target map is created using HashMap
	 **/
	public static <A, B> Map<A, B[]> convertMapOfListToMapOfArray(Map<A, List<B>> source, B[] arrayType) {
		return convertMapOfListToMapOfArray(source, new HashMap<A, B[]>(), arrayType);
	}
	
	/**
	 * Converts a single value map, to an array map
	 **/
	public static <A, B> Map<A, B[]> singleToArray(Map<A, B> source, Map<A, B[]> target,
		B[] arrayType) {
		/**
		 * Normalize array type to 0 length
		 **/
		arrayType = normalizeArrayType(arrayType);
		/**
		 * Convert values
		 **/
		for (Map.Entry<A, B> entry : source.entrySet()) {
			List<B> aList = new ArrayList<B>();
			aList.add(entry.getValue());
			target.put(entry.getKey(), aList.toArray(arrayType));
		}
		return target;
	}
	
	/**
	 * Converts a single value map, to an array map
	 **/
	public static <A, B> Map<A, B[]> singleToArray(Map<A, B> source, B[] arrayType) {
		return singleToArray(source, new HashMap<A, B[]>(), arrayType);
	}
	
	//--------------------------------------------------------------------------------------------------
	//
	//  Fully Qualified KEYS handling
	//
	//--------------------------------------------------------------------------------------------------
	
	public static Map<String, Object> fromFullyQualifiedKeys(Map<String, Object> source) {
		Map<String, Object> finalMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> sourceKey : source.entrySet()) {
			recreateObject(finalMap, sourceKey.getKey(), sourceKey.getValue());
		}
		
		return finalMap;
	}

	/**
	 * Recursively loop through the source object to form the absolute path to every single item and place them
	 * inside a Map<String, Object> to return back to the user.
	 *
	 * @param source The source to loop through
	 * @param parentName The current parent path
	 * @param delimiter The delimiter to seperate each level
	 * @return a Map of absolute paths
	 */
	public static Map<String, Object> convertToFullyQualifyNames(Object source, String parentName, String delimiter){
		// Create a temporary storage for the full paths
		Map<String, Object> currentFullPaths = new HashMap<>();

		// Preset variables if it does not exist
		delimiter  = (delimiter == null || delimiter.isEmpty()) ? "." : delimiter;
		parentName = (parentName == null || parentName.isEmpty()) ? "" : parentName;

		// Tag numbers directly to the path
		if(source instanceof Number){
			currentFullPaths.put(parentName, source);

		// Object is a Map object, loop through recursively for the inner keys
		} else if(source instanceof Map){
			Map<String, Object> sourceMap = (Map<String, Object>) source;
			for(String key : sourceMap.keySet()){
				// Set current name
				String currentName = (parentName.isEmpty()) ? key : parentName + delimiter + key;

				// Call recursively to check for the object type
				currentFullPaths.putAll(convertToFullyQualifyNames(sourceMap.get(key), currentName, delimiter));
			}

		// Object is a List object, traverse each object and tag with indices
		} else if(source instanceof List){
			List<Object> sourceList = (List<Object>) source;
			int index = 0;
			for(Object object : sourceList){
				// // Set current name with the current index count
				String currentName = parentName + "[" + index + "]";

				// Call recursively to check for the object type
				currentFullPaths.putAll(convertToFullyQualifyNames(object, currentName, delimiter));
				// Increment the index
				index++;
			}
		} else {
			// All other object types fall into this category to just convert to String if possible.
			currentFullPaths.put(parentName, source.toString());
		}

		return currentFullPaths;
	}
	
	@SuppressWarnings("unchecked")
	private static void recreateObject(Object source, String key, Object value) {
		if (key.contains("]") && key.contains(".")) {
			if (key.indexOf(']') < key.indexOf('.')) {
				String[] bracketSplit = key.split("\\[|\\]|\\.");
				bracketSplit = sanitiseArray(bracketSplit);
				
				if (bracketSplit.length > 1 && stringIsNumber(bracketSplit[0])) { //numbers only
					int index = Integer.parseInt(bracketSplit[0]);
					List<Object> sourceList = (List<Object>) source;
					// Get source list
					sourceList = getSourceList(sourceList, index);
					// Check String is words and recursive call of recreateObject method
					sourceList = checkStringIsWords(sourceList, index, key, value, bracketSplit);
					// Check String is number and recursive call of recreateObject method
					checkStringIsNumber(sourceList, index, key, value, bracketSplit);
				} else if (source instanceof Map) {
					Map<String, Object> sourceMap = (Map<String, Object>) source;
					List<Object> element = (List<Object>) sourceMap.get(bracketSplit[0]);
					// Get element list
					element = getElementList(sourceMap, element, bracketSplit);
					key = key.substring(bracketSplit[0].length(), key.length());
					recreateObject(element, key, value);
				}
			}
		} else {
			Map<String, Object> sourceMap = (Map<String, Object>) source;
			sourceMap.put(key, value);
		}
	}
	
	private static List<Object> getElementList(Map<String, Object> sourceMap, List<Object> element,
		String[] bracketSplit) {
		if (element == null) {
			element = new ArrayList<Object>();
			sourceMap.put(bracketSplit[0], element);
		}
		return element;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Object> checkStringIsNumber(List<Object> sourceList, int index, String key,
		Object value, String[] bracketSplit) {
		if (stringIsNumber(bracketSplit[1])) { //put list [1, 0, secondLayer0]
			Object retrievedValue = sourceList.get(index);
			List<Object> newList = new ArrayList<Object>();
			
			if (retrievedValue instanceof List) {
				newList = (List<Object>) retrievedValue;
			}
			
			sourceList.remove(index);
			sourceList.add(index, newList);
			
			key = key.substring(key.indexOf(']') + 1, key.length());
			recreateObject(newList, key, value);
		}
		return sourceList;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Object> checkStringIsWords(List<Object> sourceList, int index, String key,
		Object value, String[] bracketSplit) {
		if (stringIsWord(bracketSplit[1])) { //put map
			Object retrievedValue = sourceList.get(index);
			Map<String, Object> newMap = new HashMap<String, Object>();
			
			if (retrievedValue instanceof Map) {
				newMap = (Map<String, Object>) retrievedValue;
			}
			
			sourceList.remove(index);
			sourceList.add(index, newMap);
			
			key = key.substring(key.indexOf('.') + 1, key.length());
			recreateObject(newMap, key, value);
		}
		return sourceList;
	}
	
	private static List<Object> getSourceList(List<Object> sourceList, int index) {
		if (index >= sourceList.size()) {
			for (int i = sourceList.size(); i <= index; ++i) {
				sourceList.add(new Object());
			}
		}
		return sourceList;
	}

	/**
	 * Returns a length zero array that has a specific type B.
	 * This method is necessary due to some complication of Java concerning how Java treats Map objects during run time.
	 * One of the use cases is when we want to specify the object type for a key in a Map object.
	 *
	 * @param in the array type to be specified
	 * @return a length 0 array or null
	 */
	protected static <B> B[] normalizeArrayType(B[] in) {
		if (in != null && in.length > 0) {
			// The reason why we want to do this is because of
			// https://docs.oracle.com/javase/8/docs/api/java/util/List.html#toArray--
			in = Arrays.copyOfRange(in, 0, 0);
		}
		return in;
	}
	
	private static String[] sanitiseArray(String[] source) {
		List<String> holder = new ArrayList<String>();
		for (int i = 0; i < source.length; ++i) {
			if (!source[i].isEmpty()) {
				holder.add(source[i]);
			}
		}
		return holder.toArray(new String[] {});
	}
	
	private static boolean stringIsNumber(String source) {
		if (source.matches("[0-9]+")) {
			return true;
		}
		return false;
	}
	
	private static boolean stringIsWord(String source) {
		if (!source.substring(0, 1).matches("[0-9]+")) {
			return true;
		}
		return false;
	}
}
