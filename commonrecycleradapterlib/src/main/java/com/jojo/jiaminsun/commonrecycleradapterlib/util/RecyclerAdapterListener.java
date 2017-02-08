package com.jojo.jiaminsun.commonrecycleradapterlib.util;

/**
 * Created by 82328 on 2016/8/3.
 */

public interface RecyclerAdapterListener {
    //加载更多
    void onLoadMore();
    //向下滑动
    void onScrollUp();
    //向上滑动
    void onScrollDown();
}
