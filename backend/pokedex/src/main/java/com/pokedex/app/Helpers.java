package com.pokedex.app;

public class Helpers {
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

    public static String getGenerationString(int generation) {
        switch (generation) {
            case 1: return "generation-i";
            case 2: return "generation-ii";
            case 3: return "generation-iii";
            case 4: return "generation-iv";
            case 5: return "generation-v";
            case 6: return "generation-vi";
            case 7: return "generation-vii";
            case 8: return "generation-viii";
            case 9: return "generation-ix";
            default: throw new IllegalArgumentException("Invalid generation number: " + generation);
        }
    }

    // For getting which generations a pokemon is available in based on the generation-specific pokemon endpoint, which uses generation strings instead of numbers. 
    public static int getGenNumFromGenString(String genString) {
        switch (genString) {
            case "generation-i": return 1;
            case "generation-ii": return 2;
            case "generation-iii": return 3;
            case "generation-iv": return 4;
            case "generation-v": return 5;
            case "generation-vi": return 6;
            case "generation-vii": return 7;
            case "generation-viii": return 8;
            case "generation-ix": return 9;
            default: throw new IllegalArgumentException("Invalid generation string: " + genString);
        }
    }

    /* For setting pokemon descriptions based on game version, 
        as the description flavor text can differ between game versions in the same generation.
    */
    public static String[] getGameVersionsFromGen(int generation) {
        switch (generation) {
            case 1: return new String[] {"red", "blue", "yellow"};
            case 2: return new String[] {"gold", "silver", "crystal"};
            case 3: return new String[] {"ruby", "sapphire", "emerald"};
            case 4: return new String[] {"diamond", "pearl", "platinum"};
            case 5: return new String[] {"black", "white", "black-2", "white-2"};
            case 6: return new String[] {"x", "y"};
            case 7: return new String[] {"sun", "moon", "ultra-sun", "ultra-moon"};
            case 8: return new String[] {"sword", "shield"};
            case 9: return new String[] {"scarlet", "violet"};
            default: throw new IllegalArgumentException("Invalid generation number: " + generation);
        }
    }

    // For defaulting to a game version string for type sprites, as they are the same between different game versions in the same generation.
    public static String getGameVersionForTypeString(int generation) {
        switch (generation) {
            case 1: return "red-blue";
            case 2: return "crystal";
            case 3: return "emerald";
            case 4: return "platinum";
            case 5: return "black-white";
            case 6: return "x-y";
            case 7: return "sun-moon";
            case 8: return "sword-shield";
            case 9: return "scarlet-violet";
            default: throw new IllegalArgumentException("Invalid generation number: " + generation);
        }
    }
}
