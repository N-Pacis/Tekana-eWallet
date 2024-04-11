package rw.pacis.tekanaewallet.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class RandomUtil {

    public static String randomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
