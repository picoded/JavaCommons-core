package picoded.core.conv;

//Target test class
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListValueConv_test {
	
	List<Object> listObj = null;
	
	@Before
	public void setUp() {
		listObj = new ArrayList<Object>();
	}
	
	@After
	public void tearDown() {
		
	}
	
	/**
	 * Expected exception testing
	 */
	
	/// Invalid constructor test
	@Test(expected = IllegalAccessError.class)
	public void invalidConstructor() throws Exception {
		new ListValueConv();
		
	}

	/**
	 * Convert List of objects to string array
	 */
	@Test
	public void objectListToStringArray() {
		// Case 1: Value of the converted array with index 0
		listObj.add("str");
		assertEquals("str", ListValueConv.objectListToStringArray(listObj)[0]);

		// Case 2: Null test after conversion
		listObj = new ArrayList<Object>();
		listObj.add(null);
		assertNull(ListValueConv.objectListToStringArray(listObj)[0]);

		// Case 3: Empty String in the list
		listObj = new ArrayList<Object>();
		listObj.add("");
		assertEquals("", ListValueConv.objectListToStringArray(listObj)[0]);

		// @TODO: Case 4: Convert List of numeric values to String array
	}

	/**
	 * Convert object to String
	 */
	@Test
	public void objectToString() {

		// Case 1: Convert List object to List String
		listObj = new ArrayList<Object>();
		listObj.add("str");
		assertEquals("str", ListValueConv.objectToString(listObj).get(0));

		// Case 2: Convert List of null value to List String
		listObj = new ArrayList<Object>();
		listObj.add(null);
		assertNull(ListValueConv.objectToString(listObj).get(0));

		// Case 3: Convert empty String in List to List String
		listObj = new ArrayList<Object>();
		listObj.add("");
		assertEquals("", ListValueConv.objectToString(listObj).get(0));

		// @TODO: Case 4: Convert List of numeric values to List String
	}

	/**
	 * Removal of duplicate values in list
	 */
	@Test
	public void deduplicateValuesWithoutArrayOrder() {
		// Case 1: Remove duplicate values
		List<String> listStr = new ArrayList<String>();
		listStr.add("str");
		listStr.add("str");
		assertEquals("str", ListValueConv.deduplicateValuesWithoutArrayOrder(listStr).get(0));
		// @TODO: check length of the new List

		// Case 2: Remove duplicate null values
		listStr = new ArrayList<String>();
		listStr.add(null);
		listStr.add(null);
		assertNull(ListValueConv.deduplicateValuesWithoutArrayOrder(listStr).get(0));
		// @TODO: check length of the new List
	}

	/**
	 * Convert List into Set String
	 */
	@Test
	public void toStringSet() {
		List<String> listStr = new ArrayList<String>();
		listStr.add("str");
		listStr.add("str");
		assertNotNull(ListValueConv.toStringSet(listStr));
	}
	
}
