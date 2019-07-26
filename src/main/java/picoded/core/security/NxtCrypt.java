package picoded.core.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Password hashing and crypt function : This uses PBEKeySpec (which will be upgraded in the future).
 * This is conceptually similar to [PHP password_hash] (http://sg2.php.net/manual/en/function.password-hash.php),
 * but however is not backwards compatible with older hash methods, like DES
 *
 * *******************************************************************************
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~{.java}
 *
 * String rawPass = "Swordfish";
 * String passHash = NxtCrypt.getPassHash( rawPass );
 *
 * assertNotNull("Password hash generated", passHash );
 * assertTrue("Validated password hash as equal", NxtCrypt.validatePassHash( passHash, rawPass) );
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 * *******************************************************************************
 *
 * *Terminology refrence*
 * SaltedHash -> Hash of password, with salt applied
 * PassHash -> Full string database entry of salt, encryption scheme, and passHash
 *
 * *******************************************************************************
 *
 * Techinical Note: This is a static functions only class
 *
 * *******************************************************************************
 *
 * TODO list
 * * remove apache commons, for apache commons3
 * * from/toHex usage instead of base64
 * * validateSaltedHash( saltedHash, salt, rawPassword ) to implement
 * * [optional] migrate to from/toHex instead of Base64. This removes the apache.commons.* dependancy
 * * hashInfo : User text friendly version of current password encryption scheme
 * * needsRehash : Indicate true, on legacy hashes
 * * in document example usage.
 * * configurable class default values, with class functions, in addition of static global defaults.
 * * Should fallback to global default if not set.
 **/
public class NxtCrypt {
	
	/**
	 * default constructor
	 **/
	protected NxtCrypt() {
	}
	
	/**
	 * Reusable crypt objects
	 **/
	protected static SecretKeyFactory pbk = null;
	
	/**
	 * Reusable Random objects objects
	 **/
	protected static SecureRandom secureRand = null;
	
	/**
	 * Hash storage seperator, @ is intentionally used as opposed to $, as to make the stored
	 * passHash obviously not "php password_hash" format.
	 **/
	private static String seperator = "@";
	
	/**
	 * Definable default salt length
	 **/
	protected static int defaultSaltLength = 32; //bytes
	
	/**
	 * Definable default salt iterations
	 **/
	protected static int defaultIterations = 1500;
	
	/**
	 * Definable default salt keylength
	 **/
	protected static int defaultKeyLength = 256;
	
	/**
	 * Setup the default setting for SecureRandom
	 **/
	protected static boolean isStrongSecureRandom = false;
	
	protected static String securityKey = "PBKDF2WithHmacSHA1";
	
	/**
	 * Compares two byte arrays in length-constant time. This comparison method
	 * is used so that password hashes cannot be extracted from an on-line
	 * system using a timing attack and then attacked off-line.
	 *
	 * @param   a       the first byte array
	 * @param   b       the second byte array
	 * @return          true if both byte arrays are the same, false if not
	 **/
	public static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}
	
	/**
	 * String varient to the slowEquals(byte[] a, byte[] b)
	 *
	 * @param   a       the first byte array
	 * @param   b       the second byte array
	 * @return          true if both byte arrays are the same, false if not
	 **/
	public static boolean slowEquals(String a, String b) {
		return NxtCrypt.slowEquals(a.getBytes(), b.getBytes());
	}
	
	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param   hex         the hex string
	 * @return              the hex string decoded into a byte array
	 **/
	protected static byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}
	
	/**
	 * Converts a byte array into a hexadecimal string.
	 *
	 * @param   array       the byte array to convert
	 * @return              a length*2 character string encoding the byte array
	 **/
	protected static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			String str = "%0" + paddingLength + "d";
			return String.format(str, 0) + hex;
		} else {
			return hex;
		}
	}
	
	/**
	 * Setup static reuse object / default hash objects
	 **/
	protected static void setupReuseObjects() throws NoSuchAlgorithmException {
		if (NxtCrypt.pbk == null) {
			NxtCrypt.pbk = SecretKeyFactory.getInstance(securityKey);
		}
		if (NxtCrypt.secureRand == null) {
			if (!NxtCrypt.isStrongSecureRandom) {
				//
				// Using just plain old SecureRandom by default now.
				// Frankly speaking I personally feel this is "secure enough",
				// Cause its over 9000x easier to do social engineering attacks,
				// then a side channel timing attack.
				//
				// Unless of course your opponent is NSA. =/
				//
				// Or more frankly, one of their admins setting their password as "I-am-@wsome-123"
				//
				// https://tersesystems.com/2015/12/17/the-right-way-to-use-securerandom/
				//
				NxtCrypt.secureRand = new SecureRandom();
			} else {
				//
				// Originally the secure random module uses SHA1PRNG AKA
				// `NxtCrypt.secureRand = SecureRandom.getInstance("SHA1PRNG");`
				//
				// Now it uses java 8 SecureRandom.getInstanceStrong();
				// Which will hopefully make the entropy starvation issue better
				// in certain environments.
				//
				NxtCrypt.secureRand = SecureRandom.getInstanceStrong();
			}
		}
	}
	
	/**
	 * Generic SecurityException varient for setupReuseObjects
	 **/
	protected static void setupReuseObjects_generic() {
		try {
			NxtCrypt.setupReuseObjects();
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityException(e);
		}
	}
	
	/**
	 * Gets the salted hash of the raw password only (not the entire passHash)
	 **/
	public static String getSaltedHash(String rawPassword, byte[] salt, int iteration, int keyLen) {
		if (rawPassword == null || rawPassword.length() == 0) {
			throw new IllegalArgumentException("Empty/NULL passwords are not supported.");
		}
		if (salt == null) {
			throw new IllegalArgumentException("Empty/NULL salts are not supported.");
		}
		
		if (iteration <= 0) {
			iteration = NxtCrypt.defaultIterations;
		}
		if (keyLen <= 0) {
			keyLen = NxtCrypt.defaultKeyLength;
		}
		
		setupReuseObjects_generic();
		
		SecretKey key;
		try {
			PBEKeySpec kSpec = new PBEKeySpec(rawPassword.toCharArray(), salt, iteration, keyLen);
			
			key = NxtCrypt.pbk.generateSecret(kSpec);
		} catch (Exception e) {
			throw new SecurityException(e);
		}
		
		return Base64.encodeBase64String(key.getEncoded());
	}
	
	/**
	 * String salt varient of getSaltedHash (instead of byte[])
	 **/
	public static String getSaltedHash(String rawPassword, String salt, int iteration, int keyLen) {
		if (salt == null || salt.length() == 0) {
			throw new IllegalArgumentException("Empty/NULL salts are not supported.");
		}
		
		return getSaltedHash(rawPassword, Base64.decodeBase64(salt), iteration, keyLen);
	}
	
	/**
	 * Default values varient of getSaltedHash
	 **/
	public static String getSaltedHash(String rawPassword, byte[] salt) {
		return getSaltedHash(rawPassword, salt, defaultIterations, defaultKeyLength);
	}
	
	/**
	 * Default values, and string salt varient of getSaltedHash
	 **/
	public static String getSaltedHash(String rawPassword, String salt) {
		return getSaltedHash(rawPassword, salt, defaultIterations, defaultKeyLength);
	}
	
	/**
	 * Valid random string characters
	 **/
	private static char[] randomstringChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879"
		.toCharArray();
	
	/**
	 * Generate a random byte array of strings at indicated length
	 * Note: that the generated string array is strictly "alphanumeric" character spaces chars,
	 * This is intentional as this format can be safely stored in databases, and passed around
	 *
	 * @param   Length of string to output
	 *
	 * @return  Random string output of specified length
	 **/
	public static String randomString(int len) {
		// Setup the SecureRandom or reuse if possible
		setupReuseObjects_generic();
		
		// Entropy resuffling
		SecureRandom rand = new SecureRandom();
		char[] buff = new char[len];
		
		// For each character extract it
		for (int i = 0; i < len; ++i) {
			// reseed rand once you've used up all available entropy bits
			if ((i % 10) == 0) {
				rand.setSeed(secureRand.nextLong()); // 64 bits of random!
			}
			buff[i] = randomstringChars[rand.nextInt(randomstringChars.length)];
		}
		return new String(buff);
	}
	
	/**
	 * Generate a random byte array at indicated length
	 **/
	public static byte[] randomBytes(int len) {
		// Setup the SecureRandom or reuse if possible
		setupReuseObjects_generic();
		
		// Get those random bytes
		byte[] ret = new byte[len];
		secureRand.nextBytes(ret);
		
		// And return it
		return ret;
	}
	
	/**
	 * Gets the full password hash of [salt@protocall@hash] (currently only PBKeySpec)
	 *
	 * ********************************************************************************
	 *
	 * Notes on protocall format
	 * * P#N-#K = PBKeySpec, with #N number of iterations & #K keylength
	 **/
	private static String getPassHash(String rawPassword, int saltLen, int iteration, int keyLen) {
		saltLen = NxtCrypt.defaultSaltLength;
		iteration = defaultIterations;
		keyLen = defaultKeyLength;
		setupReuseObjects_generic();
		
		byte[] salt = NxtCrypt.secureRand.generateSeed(saltLen);
		
		return Base64.encodeBase64String(salt) + seperator + "P" + iteration + "-" + keyLen
			+ seperator + getSaltedHash(rawPassword, salt, iteration, keyLen);
	}
	
	/**
	 * Default values varient of getPassHash
	 **/
	public static String getPassHash(String rawPassword) {
		return getPassHash(rawPassword, 0, 0, 0);
	}
	
	/**
	 * Extract out the salted hash from the full passHash. see getPassHash
	 **/
	public static String extractSaltedHash(String passHash) {
		String[] splitStr = passHash.split(seperator, 3);
		
		if (splitStr.length < 3) {
			throw new SecurityException("Invalid salted hash of less then 3 component");
		}
		
		return splitStr[2];
	}
	
	/**
	 * Extract out the salt from the full passHash. see getPassHash
	 **/
	public static String extractSalt(String passHash) {
		String[] splitStr = passHash.split(seperator, 3);
		
		if (splitStr.length < 3) {
			throw new SecurityException("Invalid salted hash of less then 3 component");
		}
		
		return splitStr[0];
	}
	
	/**
	 * Validates the password hash against the raw password given
	 **/
	public static boolean validatePassHash(String passHash, String rawPassword) {
		String[] splitStr = passHash.split(seperator, 3);
		
		if (splitStr.length < 3) {
			throw new SecurityException("Invalid salted hash of less then 3 component: "
				+ Arrays.toString(splitStr));
		}
		
		String salt = splitStr[0];
		String hash = splitStr[2];
		
		//String type;
		int iteration = 0;
		int keyLen = 0;
		
		if (splitStr[1].length() >= 1) {
			if ("P".equals((splitStr[1]).substring(0, 1))) {
				String[] splitProtocol = (splitStr[1]).substring(1).split("-", 2);
				
				if (splitProtocol.length >= 2) {
					iteration = Integer.parseInt(splitProtocol[0]);
					keyLen = Integer.parseInt(splitProtocol[1]);
				} else {
					throw new SecurityException("Unknown hash P settings : " + splitStr[1]);
				}
				//type = "P";
			} else {
				throw new SecurityException("Unknown hash type : " + splitStr[1]);
			}
		}
		
		String toCheckHash = getSaltedHash(rawPassword, salt, iteration, keyLen);
		
		if (NxtCrypt.slowEquals(hash, toCheckHash)) {
			return true;
		}
		return false;
	}
}
