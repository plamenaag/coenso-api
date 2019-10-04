package com.primeholding.coenso.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    private ValidationUtil() {

    }

    public static boolean isNameValid(String name) {
        if (name == null) return false;
        Pattern namePattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher nameMatcher = namePattern.matcher(name);

        return nameMatcher.find();
    }
}
