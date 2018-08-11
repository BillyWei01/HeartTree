package com.horizon.hearttree.heart;


import android.graphics.Path;

/**
 * Created by John on 2017/8/26.
 */

public class Heart {
    private static final Path PATH = new Path();

    private static final float SCALE_FACTOR = 10f;
    private static final float RADIUS = 18 * SCALE_FACTOR;

    static {
        // x = 16 sin^3 t
        // y = 13 cos t - 5 cos 2t - 2 cos 3t - cos 4t
        // http://www.wolframalpha.com/input/?i=x+%3D+16+sin%5E3+t%2C+y+%3D+(13+cos+t+-+5+cos+2t+-+2+cos+3t+-+cos+4t)
        int n = 101;
        Point[] points = new Point[n];
        float t = 0f;
        float d = (float) (2 * Math.PI / (n - 1));
        for (int i = 0; i < n; i++) {
            float x = (float) (16 * Math.pow(Math.sin(t), 3));
            float y = (float) (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
            points[i] = new Point(SCALE_FACTOR * x  , -SCALE_FACTOR * y );
            t += d;
        }

        PATH.moveTo(points[0].x, points[0].y);
        for (int i = 1; i < n; i++) {
            PATH.lineTo(points[i].x, points[i].y);
        }
        PATH.close();
    }


    public static Path getPath(){
        return PATH;
    }

    public static float getRadius(){
        return RADIUS;
    }

}
