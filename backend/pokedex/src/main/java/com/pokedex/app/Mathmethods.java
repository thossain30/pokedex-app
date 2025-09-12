package com.pokedex.app;

public class Mathmethods {
    public static String createHeightString(int height) {
        double newHeight = (double) height;
        newHeight /= 10;
        newHeight *= 3.28;

        int feet = (int) newHeight;
        double fractionalFeet = newHeight - feet;
        int inches = (int) Math.round(fractionalFeet * 12);

        if (inches == 12) {
            feet++;
            inches = 0;
        }

        return String.format("%d'%d\"", feet, inches);
    }

    public static String createWeightString(int weight) {
        double newWeight = (double) weight;
        newWeight /= 10;
        newWeight *= 2.20462;
        newWeight = Math.round(newWeight * 10.0) / 10.0;
        return String.format("%d lbs.", weight);
    }
}
