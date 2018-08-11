package com.horizon.hearttree.heart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

/**
 * Created by John on 2017/8/26.
 */

public class Branch {
    private static final int BRANCH_COLOR = Color.rgb(35, 31, 32);

    // control points
    private Point[] cp = new Point[3];
    private int currLen;
    private int maxLen;
    private float radius;
    private float part;

    private float growX;
    private float growY;

    LinkedList<Branch> childList;

    Branch(int[] a){
        cp[0] = new Point(a[2], a[3]);
        cp[1] = new Point(a[4], a[5]);
        cp[2] = new Point(a[6], a[7]);
        radius = a[8];
        maxLen = a[9];
        part = 1.0f / maxLen;
    }

    public boolean grow(Canvas canvas, float scaleFactor){
        if(currLen <= maxLen){
            bezier(part * currLen);
            draw(canvas, scaleFactor);
            currLen++;
            radius *= 0.97f;
            return true;
        }else{
            return false;
        }
    }

    private void draw(Canvas canvas, float scaleFactor){
        Paint paint = CommonUtil.getPaint();
        paint.setColor(BRANCH_COLOR);

        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(growX, growY);
        canvas.drawCircle(0,0, radius, paint);
        canvas.restore();
    }

    private void bezier(float t) {
        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;
        growX =  c0 * cp[0].x + c1 * cp[1].x + c2* cp[2].x;
        growY =  c0 * cp[0].y + c1 * cp[1].y + c2* cp[2].y;
    }

    public void addChild(Branch branch){
        if(childList == null){
            childList = new LinkedList<>();
        }
        childList.add(branch);
    }
}
