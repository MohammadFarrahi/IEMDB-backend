package ie.iemdb.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;

public class PasswordEncoder {
    private static final int strength = 10;
    private static final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder(strength, new SecureRandom());

    public static String encode(String plainPassword) {
        return bCrypt.encode(plainPassword);
    }
    public static boolean matches(String rawPassword, String encodedPassword) {
        return bCrypt.matches(rawPassword, encodedPassword);
    }

}
