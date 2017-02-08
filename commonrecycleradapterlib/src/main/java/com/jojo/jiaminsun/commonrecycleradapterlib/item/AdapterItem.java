package com.jojo.jiaminsun.commonrecycleradapterlib.item;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;


/**
 * 项目名称：hellolady
 * 类描述：
 * 创建人：Jiamin.Sun
 * 创建时间：3/9/2016 9:20 AM
 * 修改人：Jiamin.Sun
 * 修改时间：3/9/2016 9:20 AM
 * 修改备注：
 */
public interface AdapterItem<T> {
    /**
     * @return item布局文件的layoutId
     */
    @LayoutRes
    int getLayoutResId();

    /**
     * 初始化views
     */
    void bindViews(final View root);

    /**
     * 设置view的参数
     */
    void setViews();

    /**
     * 根据数据来设置item的内部views
     *
     * @param t    数据list内部的model
     * @param position 当前adapter调用item的位置
     */
    void handleData(T t, int position);

    //void handleData(T t, int position, RecyclerView.ViewHolder viewHolder);

    void setItemClick(Context c, View v);

    //void setItemClick(View.OnClickListener listener);
}
