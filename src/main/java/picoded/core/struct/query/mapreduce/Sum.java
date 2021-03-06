package picoded.core.struct.query.mapreduce;

import java.math.BigDecimal;

/**
 * Sum function for MapReduceBase
 **/
public class Sum extends MapReduceBase {
	/**
	 * mapping function used to process a single value as it comes in
	 * 
	 * @param   value used in mapping, this value is possible null if the parameter does not exist
	 */
	public void map(BigDecimal val, Object rawVal) {
		// Add up those value if its provided
		if (val != null) {
			// Initialize the res variable (if needed)
			if (res == null) {
				res = val;
			} else {
				res = res.add(val);
			}
		}
	}
}