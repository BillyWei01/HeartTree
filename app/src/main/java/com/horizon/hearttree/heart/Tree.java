package com.horizon.hearttree.heart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by John on 2017/8/27.
 */

public class Tree {
    // bloom params
    private static final int BLOOM_NUM = 320;
    private static final int BLOOMING_NUM = BLOOM_NUM / 8;

    // scale factor
    private static final float CROWN_RADIUS_FACTOR = 0.35f;
    private static final float STAND_FACTOR = (CROWN_RADIUS_FACTOR / 0.28f);
    private static final float BRANCHES_FACTOR = 1.3f * STAND_FACTOR;

    private enum Step {
        BRANCHES_GROWING,
        BLOOMS_GROWING,
        MOVING_SNAPSHOT,
        BLOOM_FALLING
    }

    private Step step = Step.BRANCHES_GROWING;

    // branches
    private float branchesDx;
    private float branchesDy;
    private LinkedList<Branch> growingBranches = new LinkedList<>();

    // crown of a tree
    private float bloomsDx;
    private float bloomsDy;
    private LinkedList<Bloom> growingBlooms = new LinkedList<>();
    private LinkedList<Bloom> cacheBlooms = new LinkedList<>();

    // snapshot
    private Paint snapshotPaint = new Paint();
    private Snapshot treeSnapshot;

    // offset
    private float snapshotDx;
    private float xOffset;
    private float maxXOffset;

    // falling blooms
    private float fMaxY;
    private List<FallingBloom> fallingBlooms = new ArrayList<>();

    private float resolutionFactor;

    private boolean flag;
    private OnReadyListener mListener;

    void setReadyListener(OnReadyListener listener){
        mListener = listener;
    }

    Tree(final int canvasWidth, final int canvasHeight) {
        resolutionFactor = canvasHeight / 1080f;

        TreeMaker.init(canvasHeight, CROWN_RADIUS_FACTOR);
        Bloom.initDisplayParam(resolutionFactor);

        // snapshot
        float snapshotWidth = 816f * STAND_FACTOR * resolutionFactor;
        treeSnapshot = new Snapshot(Bitmap.createBitmap(Math.round(snapshotWidth), canvasHeight, Bitmap.Config.ARGB_8888));

        // branches
        float branchesWidth = 375f * BRANCHES_FACTOR * resolutionFactor;
        float branchesHeight = 490f * BRANCHES_FACTOR * resolutionFactor;
        branchesDx = (snapshotWidth - branchesWidth) / 2f - 40f * STAND_FACTOR;
        branchesDy = canvasHeight - branchesHeight;
        growingBranches.add(TreeMaker.getBranches());

        // blooms
        bloomsDx = snapshotWidth / 2f;
        bloomsDy = 435f * STAND_FACTOR * resolutionFactor;
        TreeMaker.fillBlooms(cacheBlooms, BLOOM_NUM);

        // moving snapshot
        maxXOffset = (canvasWidth - snapshotWidth) / 2f - 40f;

        // falling blooms
        fMaxY = canvasHeight - bloomsDy;
        TreeMaker.fillFallingBlooms(fallingBlooms, 3);

        snapshotDx = (canvasWidth - snapshotWidth) / 2f;
    }

    public void draw(Canvas canvas) {
        // background
        canvas.drawColor(0xffffffee);

        //canvas.drawText("I love you", 100, 100, CommonUtil.getPaint());

        // animations
        canvas.save();
        canvas.translate(snapshotDx + xOffset, 0);
        switch (step) {
            case BRANCHES_GROWING:
                drawBranches();
                drawSnapshot(canvas);
                break;
            case BLOOMS_GROWING:
                drawBlooms();
                drawSnapshot(canvas);
                break;
            case MOVING_SNAPSHOT:
                movingSnapshot();
                drawSnapshot(canvas);
                break;
            case BLOOM_FALLING:
                if(!flag && mListener != null){
                    flag = true;
                    mListener.onReady();
                }
                drawSnapshot(canvas);
                drawFallingBlooms(canvas);
                break;
            default:
                break;
        }
        canvas.restore();
    }

    private void drawSnapshot(Canvas canvas) {
        canvas.drawBitmap(treeSnapshot.bitmap, 0, 0, snapshotPaint);
    }

    private void drawBranches() {
        if (!growingBranches.isEmpty()) {
            LinkedList<Branch> tempBranches = null;
            treeSnapshot.canvas.save();
            treeSnapshot.canvas.translate(branchesDx, branchesDy);
            Iterator<Branch> iterator = growingBranches.iterator();
            while (iterator.hasNext()) {
                Branch branch = iterator.next();
                if (!branch.grow(treeSnapshot.canvas, BRANCHES_FACTOR * resolutionFactor)) {
                    iterator.remove();
                    if (branch.childList != null) {
                        if (tempBranches == null) {
                            tempBranches = branch.childList;
                        } else {
                            tempBranches.addAll(branch.childList);
                        }
                    }
                }
            }
            treeSnapshot.canvas.restore();

            if (tempBranches != null) {
                growingBranches.addAll(tempBranches);
            }
        }

        if (growingBranches.isEmpty()) {
            step = Step.BLOOMS_GROWING;
            Log.d("Tree", "draw branches finish");
        }
    }

    private void drawBlooms() {
        while (growingBlooms.size() < BLOOMING_NUM && !cacheBlooms.isEmpty()) {
            growingBlooms.add(cacheBlooms.pop());
        }

        Iterator<Bloom> iterator = growingBlooms.iterator();
        treeSnapshot.canvas.save();
        treeSnapshot.canvas.translate(bloomsDx, bloomsDy);
        while (iterator.hasNext()) {
            Bloom bloom = iterator.next();
            if (!bloom.grow(treeSnapshot.canvas)) {
                iterator.remove();
            }
        }
        treeSnapshot.canvas.restore();

        if (growingBlooms.isEmpty() && cacheBlooms.isEmpty()) {
            step = Step.MOVING_SNAPSHOT;
            Log.d("Tree", "draw blooms finish");
        }
    }

    private void movingSnapshot() {
        if (xOffset > maxXOffset) {
            step = Step.BLOOM_FALLING;
            Log.d("Tree", "draw moving snapshot finish");
        } else {
            xOffset += 4f;
        }
    }

    private void drawFallingBlooms(Canvas canvas) {
        Iterator<FallingBloom> iterator = fallingBlooms.iterator();
        canvas.save();
        canvas.translate(bloomsDx, bloomsDy);
        while (iterator.hasNext()) {
            FallingBloom bloom = iterator.next();
            if (!bloom.fall(canvas, fMaxY)) {
                iterator.remove();
                TreeMaker.recycleBloom(bloom);
            }
        }
        canvas.restore();

        if (fallingBlooms.size() < 3) {
            TreeMaker.fillFallingBlooms(fallingBlooms, CommonUtil.random(1, 2));
        }
    }
}
