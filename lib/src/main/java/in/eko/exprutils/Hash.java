package in.eko.exprutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class consists exclusively of static methods that generate a cryptographic hash of the given string.
 * All the methods return an empty string if the input string is null.
 * @author Kumar Abhishek (https://abhi.page/)
 */
public class Hash {

    // Suppress default constructor for noninstantiability
    private Hash() {
        throw new AssertionError();
    }

    /**
     * Generate SHA-256 hash of a string
     * @param input The input string to generate hash for
     * @return The hash of the string. Returns an empty string if the provided input is null.
     */
    public static String sha256(String input) {
        return getHash(input, "SHA-256");
    }

    /**
     * Generate SHA-512 hash of a string
     * @param input The input string to generate hash for
     * @return The hash of the string. Returns an empty string if the provided input is null.
     */
    public static String sha512(String input) {
        return getHash(input, "SHA-512");
    }

    /**
     * Generate MD5 hash of a string
     * @param input The input string to generate hash for
     * @return The hash of the string. Returns an empty string if the provided input is null.
     */
    public static String md5(String input) {
        return getHash(input, "MD5");
    }

    /**
     * Generate hash of a string
     * @param input The input string to generate hash for
     * @param type The hashing algorithm: `MD5`, `SHA-256`, `SHA512`, etc
     * @return The hash of the string. Returns an empty string if the provided input is null.
     * @throws RuntimeException if the hashing algorithm provided is invalid.
     */
    private static String getHash(String input, String type) {

        if (input == null) {
            return "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
