package com.primeholding.coenso.util;

import java.util.Random;

public class CodeGeneratorUtil {
    static final int LEFT_LIMIT = 42; // ASCII equivalent => '*'
    static final int RIGHT_LIMIT = 122; // ASCII equivalent => 'z'
    static final int STRING_LENGTH = 20;
    static final int BACKSLASH = 92; // ASCII equivalent => '\'
    Random rand = new Random();

    /***
     * Generates a random String consisting of 20 alphanumeric or symbol characters
     *
     * @return String consisting of 20 characters chosen randomly
     */
    public String generateCode() {
        StringBuilder buffer = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomLimitedInt = LEFT_LIMIT + rand.nextInt(RIGHT_LIMIT - LEFT_LIMIT + 1);
            if (randomLimitedInt == BACKSLASH)
                randomLimitedInt++; // '\' is used for escape in JSON, thus it's replaced by ']'
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
}