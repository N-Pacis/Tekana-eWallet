package rw.pacis.tekanaewallet.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class Utility {
    private static MessageSource messageSource;
    public  String localize(String path) {
        return  Utility.messageSource.getMessage(path, null, LocaleContextHolder.getLocale());
    }
    public static String generateId(String prefix_param, String format_param, int sequence) {
        return prefix_param + String.format(format_param, sequence);
    }


    public static boolean validString(String input) {
        return input!=null && !input.isEmpty();
    }


    public static boolean validLong(long input) {
        return input>0;
    }
}
