package de.sp.tools.string;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * See http://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 *
 */

public class PasswordSecurity {
    // The higher the number of iterations the more 
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 2*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
        suitable for storing in a database. 
        Empty passwords are not supported. */
    public static SaltAndHash getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        
        return new SaltAndHash(Base64.encodeBase64String(salt), hash(password, salt));
        
    }

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    public static boolean check(String password, SaltAndHash stored) throws Exception{

        String hashOfInput = hash(password, Base64.decodeBase64(stored.getSalt()));
        return hashOfInput.equals(stored.getHash());
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey key = f.generateSecret(new PBEKeySpec(
            password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }
}