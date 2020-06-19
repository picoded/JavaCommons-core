package picoded.core.exception;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ExceptionUtils_test {
	
	//
	// Expected exception testing
	//
	
	/// Invalid constructor test
	@Test(expected = IllegalAccessError.class)
	public void invalidConstructor() throws Exception {
		new ExceptionUtils();
	}
	
	/// Check that exception logging
	@Test
	public void getStackTrace() {
		String stackTrace = null;
		try {
			throw new RuntimeException("TEST EXCEPTION");
		} catch(Exception e) {
			stackTrace = ExceptionUtils.getStackTrace(e);
		}
		assertNotNull(stackTrace);
	}
}
