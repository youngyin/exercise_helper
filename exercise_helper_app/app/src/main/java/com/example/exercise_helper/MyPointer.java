package com.example.exercise_helper;

public class MyPointer {
    private static String mode = null;

    private static final String UPDATE_MODE = "update"; // main -> diary (click recyclerview)
    private static final String CREATE_EXERCISE_MODE = "create_connected_exercise"; // main -> exercise -> diary
    private static final String CREATE_MODE = "create"; // main -> diary (click image button)

    private static Dictionary dictionary = null;

    public static String getMode() {
        return mode;
    }

    public static String getUPDATE_MODE() {
        return UPDATE_MODE;
    }

    public static String getCREATE_EXERCISE_MODE() {
        return CREATE_EXERCISE_MODE;
    }

    public static String getCREATE_MODE() {
        return CREATE_MODE;
    }

    public static Dictionary getDictionary() {
        return dictionary;
    }

    public static void setMode(String m) {
        mode = m;
    }

    public static void setDictionary(Dictionary item) {
        dictionary = item;
    }

    // reset
    public static void resetAll(){
        dictionary = null;
        mode = null;
    }
}
