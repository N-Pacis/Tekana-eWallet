package rw.pacis.tekanaewallet.utils;

import java.time.LocalDateTime;
import java.util.Random;

public class RandomUtil {
    public static String randomRefNumber(String prefix) {
        long epochMilli = LocalDateTime.now().atZone(Constants.DEFAULT_ZONE_OFFSET).toInstant().toEpochMilli();
        return prefix + epochMilli;
    }

    public static String randomRefNumber() {
        long epochMilli = LocalDateTime.now().atZone(Constants.DEFAULT_ZONE_OFFSET).toInstant().toEpochMilli();
        return String.valueOf(epochMilli);
    }

    public static String randomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
