package com.binhan.flightmanagement.util;

public class StringUtils {
    public static <T> boolean isNullOrEmpty(T value) {
        if(value!=null&&value!="") {
            return false;
        }
        return true;
    }
}
