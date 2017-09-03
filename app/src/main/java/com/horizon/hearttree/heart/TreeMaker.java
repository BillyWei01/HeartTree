package com.horizon.hearttree.heart;

import java.util.List;

/**
 * Created by John on 2017/8/26.
 */

public class TreeMaker {

    private static float r;
    private static float c;

    private static int p;
    private static FallingBloom[] sRecycleBlooms = new FallingBloom[8];

    /**
     * 初始化参数
     * @param canvasHeight 画布的高度
     * @param crownRadiusFactor 树冠半径的缩放因子
     */
    public static void init(int canvasHeight, float crownRadiusFactor){
        r = canvasHeight * crownRadiusFactor;
        c = r * 1.35f;
    }

    /**
     * 构造树枝
     * @return 返回挂着树枝的树干
     */
    public static Branch getBranches() {
        // 共10列，分别是id, parentId, 贝塞尔曲线控制点(3点，6列)， 最大半径， 长度
        int[][] data = new int[][]{
                {0, -1, 217, 490, 252, 60, 182, 10, 30, 100},
                {1, 0, 222, 310, 137, 227, 22, 210, 13, 100},
                {2, 1, 132, 245, 116, 240, 76, 205, 2, 40},
                {3, 0, 232, 255, 282, 166, 362, 155, 12, 100},
                {4, 3, 260, 210, 330, 219, 343, 236, 3, 80},
                {5, 0, 221, 91, 219, 58, 216, 27, 3, 40},
                {6, 0, 228, 207, 95, 57, 10, 54, 9, 80},
                {7, 6, 109, 96, 65, 63, 53, 15, 2, 40},
                {8, 6, 180, 155, 117, 125, 77, 140, 4, 60},
                {9, 0, 228, 167, 290, 62, 360, 31, 6, 100},
                {10, 9, 272, 103, 328, 87, 330, 81, 2, 80}
        };
        int n = data.length;

        Branch[] branches = new Branch[n];
        for (int i = 0; i < n; i++) {
            branches[i] = new Branch(data[i]);
            int parent = data[i][1];
            if (parent != -1) {
                branches[parent].addChild(branches[i]);
            }
        }
        return branches[0];
    }

    /**
     * 回收花瓣
     * @param bloom
     */
    public static void recycleBloom(FallingBloom bloom){
        if(p < sRecycleBlooms.length){
            while (true){
                float x = CommonUtil.random(-c, c);
                float y = CommonUtil.random(-c, c);
                if (inHeart(x, y, r)) {
                    bloom.reset(x, -y);
                    break;
                }
            }
            sRecycleBlooms[p++] =bloom;
        }
    }


    public static void fillFallingBlooms(List<FallingBloom> blooms, int num) {
        int n = 0;
        while(n < num && p > 0){
            blooms.add(sRecycleBlooms[--p]);
            n++;
        }
        while(n < num){
            float x = CommonUtil.random(-c, c);
            float y = CommonUtil.random(-c, c);
            if (inHeart(x, y, r)) {
                blooms.add(new FallingBloom(new Point(x, -y)));
                n++;
            }
        }
    }

    public static void fillBlooms(List<Bloom> blooms, int num) {
        int n = 0;
        while (n < num) {
            float x = CommonUtil.random(-c, c);
            float y = CommonUtil.random(-c, c);
            if (inHeart(x, y, r)) {
                blooms.add(new Bloom(new Point(x, -y)));
                n++;
            }
        }
    }

    private static boolean inHeart(float px, float py, float r) {
        //  (x^2+y^2-1)^3-x^2*y^3=0
        float x = px / r;
        float y = py / r;
        float sx = x * x;
        float sy = y * y;
        float a = sx + sy - 1;
        return a * a * a - sx * sy * y < 0;
    }
}

