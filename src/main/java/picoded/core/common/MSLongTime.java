package picoded.core.common;

/**
 * Represents common units of time in milliseconds
 * With commonly confused units intentionally not represented,
 * forcing users to use "AVG_MONTH" or "AVG_YEAR" instead
 **/
public class MSLongTime {
	
	// Common second to week time units
	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;
	
	// Common monthly unit of 28, 30, 31 days
	public static final long DAY_28 = 28 * DAY;
	public static final long DAY_30 = 30 * DAY;
	public static final long DAY_31 = 31 * DAY;
	
	// 365 days, a common unit for a year
	public static final long DAY_365 = 365 * DAY;
	
	// Taking the average of a month to be 30.4375 days
	// which take into account leap years 
	public static final long AVG_MONTH = DAY * 304375 / 10000;
	
	// Average year, taking into account leap years
	public static final long AVG_YEAR = 12 * AVG_MONTH;
	
}