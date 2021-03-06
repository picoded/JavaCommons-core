package picoded.core.struct.query.condition;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import picoded.core.struct.query.Query;
import picoded.core.struct.query.QueryType;

public class MoreThan_test {
	
	private MoreThan moreThan = null;
	
	@Before
	public void setUp() {
		
	}
	
	@After
	public void tearDown() {
		
	}
	
	@Test
	public void blankTest() {
		assertNull(moreThan);
	}
	
	@Test
	public void typeTest() {
		moreThan = construct();
		assertEquals(QueryType.MORE_THAN, moreThan.type());
	}
	
	@Test
	public void testValuesTest() {
		moreThan = construct();
		assertFalse(moreThan.testValues(null, null));
	}
	
	@Test
	public void operatorSymbolTest() {
		moreThan = construct();
		assertEquals(">", moreThan.operatorSymbol());
	}
	
	private MoreThan construct() {
		Map<String, Object> defaultArgMap = new HashMap<>();
		return new MoreThan("key", "myKey", defaultArgMap);
	}
}
