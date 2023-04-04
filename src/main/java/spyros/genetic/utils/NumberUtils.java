package gr.spyros.genetic.utils;

import java.util.Random;

public class NumberUtils {
    public static int getRandom(int low, int high) {
        Random random = new Random();
        return random.nextInt(high - low) + low;
    }
}
