package com.horizon.hearttree.heart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;

/**
 * Created by John on 2017/8/29.
 */

public class FallingBloom extends Bloom {
    private float xSpeed;
    private float ySpeed;

    private Snapshot snapshot;
    private boolean validate;

    FallingBloom(Point position) {
        super(position);
        color |= 0xFF000000;
        scale = sMaxScale;
        initSpeed();
        validate = false;
        snapshot = new Snapshot(Bitmap.createBitmap(sMaxRadius * 2, sMaxRadius * 2, Bitmap.Config.ARGB_8888));
        Log.d("TAG", "create falling bloom");
    }

    public void reset(float x, float y) {
        position.x = x;
        position.y = y;
        color = Color.argb(255, 0xff, CommonUtil.random(0, 240), CommonUtil.random(0, 240));
        angle = CommonUtil.random(360);
        initSpeed();
        validate = false;
    }

    private void initSpeed() {
        ySpeed = CommonUtil.random(1.6f * sFactor, 2.7f * sFactor);
        if(position.x < 0){
            xSpeed = ySpeed * CommonUtil.random(0.5f * sFactor, 1.1f * sFactor);
        }else{
            xSpeed = ySpeed * CommonUtil.random(0.8f * sFactor, 1.5f * sFactor);
        }
    }

    public boolean fall(Canvas canvas, float fMaxY) {
        if(!validate){
            makeSnapshot();
            validate = true;
        }
        float r = getRadius();
        if (position.y - r < fMaxY) {
            position.x -= xSpeed;
            position.y += ySpeed;
            angle += 1f;
            draw(canvas);
            return true;
        } else {
            return false;
        }
    }

    private void makeSnapshot(){
        Paint paint = CommonUtil.getPaint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        Canvas canvas = snapshot.canvas;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.save();
        canvas.translate(sMaxRadius, sMaxRadius);
        canvas.rotate(angle);
        canvas.scale(scale, scale);
        canvas.drawPath(Heart.getPath(), paint);
        canvas.restore();
        paint.setAntiAlias(false);
    }

    private void draw(Canvas canvas) {
        Paint paint = CommonUtil.getPaint();
        paint.setColor(color);
        canvas.save();
        canvas.translate(position.x, position.y);
        canvas.rotate(angle);
        canvas.translate(-sMaxRadius, -sMaxRadius);
        canvas.drawBitmap(snapshot.bitmap, 0, 0, paint);
        canvas.restore();
    }

}
