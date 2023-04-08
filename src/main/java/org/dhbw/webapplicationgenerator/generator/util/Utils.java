package org.dhbw.webapplicationgenerator.generator.util;

import java.util.Locale;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class must not be instantiated");
    }

    public static String capitalize(String value) {
        value = value.toLowerCase(Locale.ROOT);
        return value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public static String plural(String value) {
       return value + "s";
    }

}
