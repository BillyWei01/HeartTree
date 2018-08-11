package com.horizon.hearttree.heart;

import android.graphics.Paint;

import java.util.Random;

/**
 * Created by John on 2017/8/27.
 */

public class CommonUtil {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final Paint PAINT = new Paint();

    /**
     * ger random form range [0, n]
     */
    public static int random(int n){
        return RANDOM.nextInt(n+1);
    }

    /**
     * ger random form range [m, n]
     * require m < n
     */
    public static int random(int m, int n){
        int d = n - m;
        return m + RANDOM.nextInt(d+1);
    }

    /**
     * ger float random form range [m, n]
     * require m < n
     */
    public static float random(float m , float n){
        float d = n - m;
        return m + RANDOM.nextFloat() * d;
    }

    public static Paint getPaint(){
        return PAINT;
    }

}
