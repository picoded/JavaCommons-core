package picoded.core.conv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import picoded.core.file.FileUtil;

public class MapValueConv_test {
	
	/**
	 * Temp vars - To setup
	 */
	Map<String, Object> unqualifiedMap;

	/**
	 * Setup the temp vars
	 */
	@Before
	public void setUp() {
		unqualifiedMap = new HashMap<String, Object>();
	}
	
	@After
	public void tearDown() {
		
	}
	
	///
	///  Expected exception testing
	///

	/**
	 * Invalid constructor test
	 */
	@Test(expected = IllegalAccessError.class)
	public void invalidConstructor() throws Exception {
		new MapValueConv();
	}

	/**
	 * Simple conversion of qualified keys function
	 */
	@Test
	public void simpleToFullyQualifiedKeysTest() {
		File unqualifiedMapFile = new File("./test/Conv/unqualifiedMap.js");
		String jsonString = FileUtil.readFileToString(unqualifiedMapFile);
		unqualifiedMap = ConvertJSON.toMap(jsonString);
		
		Map<String, Object> qualifiedMap = MapValueConv.convertToFullyQualifyNames(unqualifiedMap, "", ".");

		assertNotNull(qualifiedMap);
		assertEquals("1", qualifiedMap.get("agentID"));
		
		assertEquals("Sam", qualifiedMap.get("clients[0].name"));
		assertEquals("Eugene", qualifiedMap.get("clients[1].name"));
		assertEquals("Murong", qualifiedMap.get("clients[2].name"));
		
		assertEquals("12345", qualifiedMap.get("clients[0].nric"));
		assertEquals("23456", qualifiedMap.get("clients[1].nric"));
		assertEquals("34567", qualifiedMap.get("clients[2].nric"));


		// Single numeric test
		Map<String, Object> map = MapValueConv.convertToFullyQualifyNames(123, null, new String());
		assertNotNull(map);
		assertEquals(123, map.get(""));

		// Single string test
		map = MapValueConv.convertToFullyQualifyNames("abc", null, new String());
		assertNotNull(map);
		assertEquals("abc", map.get(""));

		// Simple map test
		map = new HashMap<String, Object>();
		map.put("abc", "xyz");
		map = MapValueConv.convertToFullyQualifyNames(map, "abc", new String());
		assertEquals("xyz", map.get("abc.abc"));

		// Simple list test
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map = new HashMap<String, Object>();
		map.put("abc", "test");
		list.add(map);
		map = MapValueConv.convertToFullyQualifyNames(list, null, new String());

		assertEquals("test", map.get("[0].abc"));

		// Null delimiter
		map = MapValueConv.convertToFullyQualifyNames(list, null, null);
		assertEquals("test", map.get("[0].abc"));
	}

	/**
	 * Simple Conversion of fully qualified names into nested object
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void fromFullyQualifiedKeysTest() {
		Map<String, Object> unqualifiedMap = new HashMap<String, Object>();
		
		File unqualifiedMapFile = new File("./test/Conv/unqualifiedMap.js");
		String jsonString = FileUtil.readFileToString(unqualifiedMapFile);
		unqualifiedMap = ConvertJSON.toMap(jsonString);
		
		Map<String, Object> qualifiedMap = MapValueConv.convertToFullyQualifyNames(unqualifiedMap, "", ".");
		
		unqualifiedMap.clear();
		unqualifiedMap = MapValueConv.fromFullyQualifiedKeys(qualifiedMap);

		// Retrieve singular value from map
		assertNotNull(unqualifiedMap);
		assertEquals("1", unqualifiedMap.get("agentID"));

		// Retrieve list from map
		assertTrue(unqualifiedMap.get("clients") instanceof List);
		List<Object> innerList = (List<Object>) unqualifiedMap.get("clients");
		assertTrue(innerList.size() == 3);

		// Retrieve object from the list in the map
		Map<String, Object> innerMap = null;
		innerMap = (Map<String, Object>) innerList.get(0);
		assertEquals("Sam", innerMap.get("name"));
		assertEquals("12345", innerMap.get("nric"));

		// Verify the records
		innerMap = (Map<String, Object>) innerList.get(1);
		assertEquals("Eugene", innerMap.get("name"));
		assertEquals("23456", innerMap.get("nric"));

		// Verify the records
		innerMap = (Map<String, Object>) innerList.get(2);
		assertEquals("Murong", innerMap.get("name"));
		assertEquals("34567", innerMap.get("nric"));
	}

	/**
	 * Complex test case for nested objects of an object
	 */
	@Test
	public void chaosMonkeyFinal() {
		File chaosMonkeyFile = new File("./test/Conv/chaosmonkey.js");
		String jsonString = "";
		jsonString = FileUtil.readFileToString(chaosMonkeyFile);
		
		Map<String, Object> jsonMap = ConvertJSON.toMap(jsonString);
		
		Map<String, Object> qualifiedChaosMap = MapValueConv.convertToFullyQualifyNames(jsonMap, "", ".");
		assertNotNull(qualifiedChaosMap);

		// Retrieve some values to check it
		assertEquals("mapValueM", qualifiedChaosMap.get("mapListA[0].mapListB[0].mapListC[0].mapListD[2].mapKeyM"));
		assertEquals("mapValueL", qualifiedChaosMap.get("mapListA[0].mapListB[0].mapListC[0].mapListD[1].mapKeyL"));
		assertEquals("mapLayeredValueE", qualifiedChaosMap.get("mapLayeredList[1][0][1][0].mapLayeredKeyE"));

		Map<String, Object> unqualifiedChaosMap = MapValueConv
			.fromFullyQualifiedKeys(qualifiedChaosMap);

		assertNotNull(unqualifiedChaosMap);

		// Grab one of the layered objects and forms a check
		List<Object> mapLayeredList = (List<Object>) unqualifiedChaosMap.get("mapLayeredList");
		assertNotNull(mapLayeredList);
		assertTrue(mapLayeredList.size() == 2);
		List<Object> mapLayeredKeys = (List<Object>) mapLayeredList.get(0);
		List<Object> mapLayeredKeysCandB = (List<Object>) mapLayeredKeys.get(1);
		Map<String, Object> keyC = (Map<String, Object>) mapLayeredKeysCandB.get(0);
		assertEquals("mapLayeredValueC", keyC.get("mapLayeredKeyC"));

		
	}

	/**
	 * Convert list to array
	 */
	@Test
	public void convertMapOfListToMapOfArrayTest() {
		Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
		Map<Object, Object[]> mapArrayObj = null;
		map.put("test", null);
		mapArrayObj = MapValueConv.convertMapOfListToMapOfArray(map, null);
		assertNotNull(mapArrayObj);

		// Case 1: Map has null list
		Object[] testArray = mapArrayObj.get("test");
		assertTrue(testArray == null);

		// Case 2: Map has list with two values
		List<Object> values = new ArrayList<>();
		values.add("firstValue");
		values.add("secondValue");
		map.put("test2", values);
		mapArrayObj = MapValueConv.convertMapOfListToMapOfArray(map, new String[]{});
		Object[] valuesArray = mapArrayObj.get("test2");
		assertTrue(valuesArray.length == 2);

		// Case 3: ArrayType is not being used
		List<Object> list = new ArrayList<Object>();
		list.add("abc");
		map.put("test", list);
		mapArrayObj = MapValueConv.convertMapOfListToMapOfArray(map, new String[] { "def" });
		Object[] resultArray = mapArrayObj.get("test");
		assertTrue(resultArray.length == 1);
		assertEquals("abc", resultArray[0]);
	}

	/**
	 * single value to array test
	 */
	@Test
	public void singleToArrayTest() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Object, Object[]> mapArrayObj = null;
		map.put("test", null);
		// Case 1: Null test
		mapArrayObj = MapValueConv.singleToArray(map, new String[] {});
		assertNotNull(mapArrayObj);
		Object[] testArray = mapArrayObj.get("test");
		assertTrue(testArray.length == 1);
		assertTrue(testArray[0] == null);

		// Case 2: Single value to array
		map.put("test", "abc");
		mapArrayObj = MapValueConv.singleToArray(map, new String[] { "def" });
		assertNotNull(mapArrayObj);
		Object[] resultArray = mapArrayObj.get("test");
		assertTrue(resultArray.length == 1);
		assertEquals("abc", resultArray[0]);
	}

	/**
	 * recreate object from qualified maps
	 */
	@Test
	public void recreateObjectTest() {
		Map<String, Object> unqualifiedMap = new HashMap<String, Object>();
		// Fully qualified map
		Map<String, Object> qualifiedMap = new HashMap<String, Object>();

		// Empty Map
		unqualifiedMap = MapValueConv.fromFullyQualifiedKeys(qualifiedMap);
		assertNotNull(unqualifiedMap);

		File unqualifiedMapFile = new File("./test/Conv/unqualifiedMap.js");
		String jsonString = FileUtil.readFileToString(unqualifiedMapFile);

		unqualifiedMap = ConvertJSON.toMap(jsonString);
		qualifiedMap = MapValueConv.convertToFullyQualifyNames(unqualifiedMap, "", ".");
		// Convert back to unqualifiedMap from fullyQualifiedMap
		unqualifiedMap = MapValueConv.fromFullyQualifiedKeys(qualifiedMap);

		// Value in map
		assertEquals("1", unqualifiedMap.get("agentID"));

		// Recreation of object convert into list instead of array
		assertTrue(unqualifiedMap.get("clients") instanceof List);
		// List in map
		List<Object> clients = ((List<Object>) unqualifiedMap.get("clients"));
		assertTrue(clients.size() == 3);
		
		// Obtain the object value in array
		Map<String, Object> client = (Map<String, Object>) clients.get(0);
		assertEquals("Sam", client.get("name"));
		assertEquals("12345", client.get("nric"));
	}
}
