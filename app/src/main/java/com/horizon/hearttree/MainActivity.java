package com.horizon.hearttree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.horizon.hearttree.heart.OnReadyListener;
import com.horizon.hearttree.heart.TreeView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tipsTv =findViewById(R.id.tips_tv);
        TreeView treeView = findViewById(R.id.tree_view);
        treeView.setReadyListener(new OnReadyListener() {
            @Override
            public void onReady() {
                showTips(tipsTv);
            }
        });
    }

    private void showTips(final TextView tipsTv){
        tipsTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipsTv.setVisibility(View.VISIBLE);
                Animation animation= AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.fade_in);
                tipsTv.startAnimation(animation);
            }
        }, 1000L);
    }
}
