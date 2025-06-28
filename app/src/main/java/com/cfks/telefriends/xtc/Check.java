package com.cfks.telefriends.xtc;

public final class Check {
    public static boolean a = true;

    private Check() {
    }

    public static <T> T a(T t) {
        if (a && t == null) {
            throw new IllegalArgumentException();
        }
        return t;
    }

    public static <T> T b(T t) {
        if (a && t == null) {
            throw new IllegalStateException();
        }
        return t;
    }
}