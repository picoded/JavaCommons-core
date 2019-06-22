package picoded.core.struct;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProxyGenericConvertMap_test {
	private ProxyGenericConvertMap<String, String> proxyGenericConvertMap = null;
	
	@Before
	public void setUp() {
		proxyGenericConvertMap = new ProxyGenericConvertMap<>();
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void getConstructorTest() {
		Map<String, String> map = new HashMap<>();
		proxyGenericConvertMap = new ProxyGenericConvertMap<>(map);
		assertNull(proxyGenericConvertMap.get("key1"));
	}
	
	@Test
	public void ensureTest() {
		GenericConvertMap<String, String> genericConvertMap = new ProxyGenericConvertMap<>();
		assertNotNull(ProxyGenericConvertMap.ensure(genericConvertMap));
		
	}
	
	@Test
	public void ensureMapConversion() {
		Map<String, String> map = new HashMap<>();
		assertNotNull(ProxyGenericConvertMap.ensure(map));
	}
	
	@Test
	public void toStringTest() {
		Map<String, String> map = new HashMap<>();
		proxyGenericConvertMap = new ProxyGenericConvertMap<>(map);
		assertNotNull(proxyGenericConvertMap.toString());
	}

	//
	// Extended class "ensure" test
	//

	public static class PClass extends ProxyGenericConvertMap<String, String> {
		public PClass() {
			super();
		}
	}

	@Test
	public void ensureArrayConversion() {
		// Map to add
		Map<String, String> map = new HashMap<>();
		map.put("hello", "world");
		// List with map
		List<Map<String,String>> list = new ArrayList<>();
		list.add(map);

		// Ensure conversion to PClass
		PClass[] rArr = null;
		assertNotNull(rArr = ProxyGenericConvertMap.ensureArray(PClass.class, list.toArray()));

		// Validate the result
		assertEquals("world", rArr[0].get("hello"));
	}
	
	@Test
	public void ensureListConversion() {
		// Map to add
		Map<String, String> map = new HashMap<>();
		map.put("hello", "world");
		// List with map
		List<Map<String,String>> list = new ArrayList<>();
		list.add(map);

		// Ensure conversion to PClass
		List<PClass> rList = null;
		assertNotNull(rList = ProxyGenericConvertMap.ensureList(PClass.class, list.toArray()));
		assertEquals("world", rList.get(0).get("hello"));
		assertNotNull(rList = ProxyGenericConvertMap.ensureList(PClass.class, list));
		assertEquals("world", rList.get(0).get("hello"));
	}
	
}
