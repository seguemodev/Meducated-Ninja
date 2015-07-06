package com.seguetech.zippy;

import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }


}
