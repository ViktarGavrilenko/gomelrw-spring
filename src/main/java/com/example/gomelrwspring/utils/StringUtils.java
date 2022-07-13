package com.example.gomelrwspring.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringUtils {
    private static final String DATE_PATTERN_FOR_SQL = "yyyy-MM-dd";
    private static final String DATE_PATTERN_FOR_MEN = "d MMMM yyyy";
    private static final String DATE_PATTERN_FOR_WOMEN = "d MMMM";
    private static final String FLAG_MEN = "М";

    public static String convertDateForEmployee(String date, String sex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_FOR_SQL);
        if (sex.equals(FLAG_MEN)) {
            return LocalDate.parse(date, formatter).format(DateTimeFormatter.ofPattern(DATE_PATTERN_FOR_MEN));
        } else {
            return LocalDate.parse(date, formatter).format(DateTimeFormatter.ofPattern(DATE_PATTERN_FOR_WOMEN));
        }
    }
}
