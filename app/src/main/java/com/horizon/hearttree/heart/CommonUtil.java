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
     * 获取范围[0, n]的随机数
     * @param
     * @return
     */
    public static int random(int n){
        return RANDOM.nextInt(n+1);
    }

    /**
     * 获取范围[m, n]的整数，
     * 要求 m < n
     * @return
     */
    public static int random(int m, int n){
        int d = n - m;
        return m + RANDOM.nextInt(d+1);
    }

    /**
     * 获取范围[m, n]的浮点数，
     * 要求 m < n
     * @return
     */
    public static float random(float m , float n){
        float d = n - m;
        return m + RANDOM.nextFloat() * d;
    }

    public static Paint getPaint(){
        return PAINT;
    }

}
