package login.credentialsvalidator;

import org.apache.commons.codec.digest.DigestUtils;

public class Hashing {
    public static String hash(String password){
        return DigestUtils.sha256Hex(password);
    }
}
