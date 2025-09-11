package com.pokedex.app;

public class Mathmethods {
    public String createHeightString(int height) {
        float newHeight = (float) height;
        newHeight /= 10;
        newHeight *= 3.28;
    }
}
