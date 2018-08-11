package com.horizon.hearttree.heart;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by John on 2017/8/26.
 */

public class TreeView  extends View {
    private Tree tree;
    private OnReadyListener mListener;

    public void setReadyListener(OnReadyListener listener){
        mListener = listener;
    }

    public TreeView(Context context) {
        super(context);
    }

    public TreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(tree == null){
            tree = new Tree(getWidth(), getHeight());
            tree.setReadyListener(mListener);
        }
        tree.draw(canvas);
        postInvalidate();
    }
}
