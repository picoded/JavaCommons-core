package picoded.core.struct.query.utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import picoded.core.struct.ProxyGenericConvertMap;
import picoded.core.struct.query.Aggregation;

/**
 * Utility interface, to provide a standardised interface to query
 * Collection for various results.
 * 
 * Ideally this should be optimized for the relevent backend.
 */
public interface CollectionQueryInterface<V extends Map<String, Object>> {
	
	// Query operations (to optimize on specific implementation)
	//--------------------------------------------------------------------------
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  The DataObject[] array
	 **/
	default V[] query(String whereClause, Object[] whereValues) {
		return query(whereClause, whereValues, null, 0, 0);
	}
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 * @param   query string to sort the order by, use null to ignore
	 *
	 * @return  The DataObject[] array
	 **/
	default V[] query(String whereClause, Object[] whereValues, String orderByStr) {
		return query(whereClause, whereValues, orderByStr, 0, 0);
	}
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 * @param   query string to sort the order by, use null to ignore
	 * @param   offset of the result to display, use -1 to ignore
	 * @param   number of objects to return max, use -1 to ignore
	 *
	 * @return  The DataObject[] array
	 **/
	V[] query(String whereClause, Object[] whereValues, String orderByStr, int offset, int limit);
	
	// Query any varients
	//--------------------------------------------------------------------------
	
	/**
	 * Performs a search query, and returns one or any of the valid DataObjects
	 * 
	 * This _may_ be more performant (depending on use case)
	 * 
	 * If multiple object matches the where clause criteria, it does NOT gurantee
	 * which object it will return.
	 * 
	 * This is designed can be used with an optimized cache if avaliable, with potentially stale results.
	 * Before falling back into more "reliable" standard query.
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  Any valid DataObject found
	 **/
	default V queryAny(String whereClause, Object[] whereValues) {
		V[] resID = query(whereClause, whereValues, null, 0, 1);
		if (resID.length > 0) {
			return resID[0];
		}
		return null;
	}
	
	// Query wrapping
	//--------------------------------------------------------------------------
	
	/**
	 * Get a DataObject, and returns it. Skips existance checks if required
	 * Wrapped in an ProxyGenericConvertMap compatible class
	 *
	 * @param  classObj for passing over class type
	 * @param  object GUID to fetch
	 * @param  boolean used to indicate if an existance check is done for the request
	 *
	 * @return  The ProxyGenericConvertMap[] array
	 **/
	default <T extends ProxyGenericConvertMap> T getWrap(Class<T> classObj, String oid) {
		return ProxyGenericConvertMap.ensure(classObj, get(oid));
	}
	
	/**
	 * Performs a search query, and returns the respective result, 
	 * wrapped in an ProxyGenericConvertMap compatible class
	 *
	 * @param   classObj for passing over class type
	 * @param   where query statement
	 * @param   where clause values array
	 * @param   query string to sort the order by, use null to ignore
	 * @param   offset of the result to display, use -1 to ignore
	 * @param   number of objects to return max, use -1 to ignore
	 *
	 * @return  The ProxyGenericConvertMap[] array
	 **/
	default <T extends ProxyGenericConvertMap> T[] queryWrap(Class<T> classObj, String whereClause,
		Object[] whereValues, String orderByStr, int offset, int limit) {
		
		// Does the original query
		V[] arr = query(whereClause, whereValues, orderByStr, offset, limit);
		
		// Quick null response handling
		if (arr == null) {
			return null;
		}
		
		// Prepare the return result
		Object[] ret = new Object[arr.length];
		for (int i = 0; i < arr.length; ++i) {
			ret[i] = ProxyGenericConvertMap.ensure(classObj, arr[i]);
		}
		
		// Return wrapped DataObjects
		return (T[]) ret;
	}
	
	/**
	 * Performs a search query, and returns one or any of the valid result
	 * wrapped in an ProxyGenericConvertMap compatible class
	 * 
	 * This _may_ be more performant (depending on use case)
	 * 
	 * If multiple object matches the where clause criteria, it does NOT gurantee
	 * which object it will return.
	 * 
	 * This is designed can be used with an optimized cache if avaliable, with potentially stale results.
	 * Before falling back into more "reliable" standard query.
	 *
	 * @param   classObj for passing over class type
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  The DataObject[] array
	 **/
	default <T extends ProxyGenericConvertMap> T queryWrapAny(Class<T> classObj, String whereClause,
		Object[] whereValues) {
		V resID = queryAny(whereClause, whereValues);
		if (resID != null) {
			return ProxyGenericConvertMap.ensure(classObj, resID);
		}
		return null;
	}
	
	// Query list varients
	//--------------------------------------------------------------------------
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  The DataObject list
	 **/
	default List<V> queryList(String whereClause, Object[] whereValues) {
		return Arrays.asList(query(whereClause, whereValues, null, 0, 0));
	}
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 * @param   query string to sort the order by, use null to ignore
	 *
	 * @return  The DataObject list
	 **/
	default List<V> queryList(String whereClause, Object[] whereValues, String orderByStr) {
		return Arrays.asList(query(whereClause, whereValues, orderByStr, 0, 0));
	}
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 * @param   query string to sort the order by, use null to ignore
	 * @param   offset of the result to display, use -1 to ignore
	 * @param   number of objects to return max, use -1 to ignore
	 *
	 * @return  The DataObject[] array
	 **/
	default List<V> queryList(String whereClause, Object[] whereValues, String orderByStr, int offset, int limit) {
		return Arrays.asList(query(whereClause, whereValues, orderByStr, offset, limit));
	}
	
	// Query count (to optimize on specific implementation)
	//--------------------------------------------------------------------------
	
	/**
	 * Performs a search query, and returns the respective DataObjects
	 *
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  The total count for the query
	 **/
	default long queryCount(String whereClause, Object[] whereValues) {
		return query(whereClause, whereValues).length;
	}
	
	// Aggregation operations (to optimize on specific implementation)
	//--------------------------------------------------------------------------
	
	/**
	 * Performs a search query, and returns the respective aggregation result
	 *
	 * @param   aggregationTerms used to compute the result
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  BigDecimal[] array of the aggregation result
	 **/
	default BigDecimal[] aggregation(String[] aggregationTerms, String whereClause,
		Object[] whereValues) {
		// 1. Initialize the aggregation object (fail fast)
		Aggregation agg = Aggregation.build(aggregationTerms);
		
		// 2. Get the query result, as a collection
		V[] resArray = query(whereClause, whereValues);
		List<Object> resCollection = (List<Object>) (List<?>) Arrays.asList(resArray);
		
		// 3. compute the aggregation (in a single pass)
		return agg.compute(resCollection);
	}
	
	/**
	 * Performs a search query, and returns the respective aggregation result
	 *
	 * @param   singleAggregationTerm used to compute the result
	 * @param   where query statement
	 * @param   where clause values array
	 *
	 * @return  corresponding BigDecimal result
	 **/
	default BigDecimal singleAggregation(String singleAggregationTerm, String whereClause,
		Object[] whereValues) {
		return aggregation(new String[] { singleAggregationTerm }, whereClause, whereValues)[0];
	}
	
}