package com.cwunder.recipe._shared;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class IdGenerator {
    private static final char[] alphabet = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final int size = 12;

    public static String genId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }
}
