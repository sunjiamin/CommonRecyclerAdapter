package com.jojo.jiaminsun.commonrecycleradapterlib.util;


import android.support.v7.widget.RecyclerView;

/**
 * 项目名称：HeartPro
 * 类描述：
 * 创建人：Jiamin.Sun
 * 创建时间：6/3/2016 1:23 PM
 */
public abstract  class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener{


    private int mScrollThreshold=4;

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta) {
            if (dy > 0) {
                //向上
                onScrollUp();
            } else {
                //向下
                onScrollDown();
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);


    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
