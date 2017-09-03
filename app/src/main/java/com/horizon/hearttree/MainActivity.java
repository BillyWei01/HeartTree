package com.horizon.hearttree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.horizon.hearttree.heart.TreeView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = new TreeView(this);
        setContentView(view);
    }
}
