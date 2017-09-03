package com.horizon.hearttree.heart;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by John on 2017/8/29.
 */

public class Snapshot {
    Canvas canvas;
    Bitmap bitmap;
    public Snapshot(Bitmap bitmap){
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
    }
}
