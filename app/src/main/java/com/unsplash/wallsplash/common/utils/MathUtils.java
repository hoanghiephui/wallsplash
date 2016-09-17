package com.unsplash.wallsplash.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Math utils.
 */

public class MathUtils {

    public static int getRandomInt(int max) {
        return new Random().nextInt(max);
    }

    public static List<Integer> getPageList(int max) {
        List<Integer> oldList = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            oldList.add(i);
        }

        List<Integer> newList = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            newList.add(oldList.get(getRandomInt(oldList.size())));
        }

        return newList;
    }

    public static String abbrNum(double number, int decPlaces) {
        if (number < 1000) {
            return (int) number + "";
        }
        String result = number + "";
        // 2 decimal places => 100, 3 => 1000, etc
        decPlaces = (int) Math.pow(10, decPlaces);

        // Enumerate number abbreviations
        String[] abbrev = {"k", "m", "b", "t"};

        // Go through the array backwards, so we do the largest first
        for (int i = abbrev.length - 1; i >= 0; i--) {

            // Convert array index to "1000", "1000000", etc
            int size = (int) Math.pow(10, (i + 1) * 3);

            // If the number is bigger or equal do the abbreviation
            if (size <= number) {
                // Here, we multiply by decPlaces, round, and then divide by decPlaces.
                // This gives us nice rounding to a particular decimal place.
                number = ((double) Math.round(number * decPlaces / size)) / (double) decPlaces;

                // Handle special case where we round up to the next abbreviation
                if ((number == 1000) && (i < abbrev.length - 1)) {
                    number = 1;
                    i++;
                }

                // Add the letter for the abbreviation
                if (number - (long) number == 0.0) {
                    result = (long) number + abbrev[i];
                } else {
                    result = number + abbrev[i];
                }


                // We are done... stop
                break;
            }
        }

        return result;
    }
}
