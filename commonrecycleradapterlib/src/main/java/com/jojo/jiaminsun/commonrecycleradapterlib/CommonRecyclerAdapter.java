package com.jojo.jiaminsun.commonrecycleradapterlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jojo.jiaminsun.commonrecycleradapterlib.item.AdapterItem;
import com.jojo.jiaminsun.commonrecycleradapterlib.util.IAdapter;
import com.jojo.jiaminsun.commonrecycleradapterlib.util.ItemTypeUtil;
import com.jojo.jiaminsun.commonrecycleradapterlib.util.RecyclerAdapterListener;
import com.jojo.jiaminsun.commonrecycleradapterlib.util.RecyclerViewScrollDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：hellolady
 * 类描述：
 * 创建人：Jiamin.Sun
 * 创建时间：3/9/2016 9:14 AM
 * 修改人：Jiamin.Sun
 * 修改时间：3/9/2016 9:14 AM
 * 修改备注：
 */
public class CommonRecyclerAdapter<T> extends RecyclerView.Adapter implements IAdapter<T> {
    public static final String TYPE_HEADER ="type_cycler_header";  //说明是带有Header的
    public static final String TYPE_FOOTER ="type_cycler_footer";  //说明是带有Footer的

    private Context ctx;
    private List<T> mDataList;
    private Object mItemType;
    private View mHeaderView;
    private View mFooterView;
    private int lastVisibleItemPosition;
    private RecyclerAdapterListener recyclerAdapterListener;
    private boolean loading;
    private boolean enableHeaderView = false;
    private boolean enableFooterView = false;

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void addHeaderView(View headerView) {
        if(enableHeaderView){
            mHeaderView = headerView;
            notifyItemInserted(0);
        }
    }

    public void removeHeaderView(){
        if(enableHeaderView && null!=mHeaderView){
            mHeaderView=null;
            notifyItemRemoved(0);
        }
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void addFooterView(View footerView) {
        if(enableFooterView){
            mFooterView = footerView;
            notifyItemInserted(getItemCount()-1);

        }
    }

    public void removeFooterView(){
        if(enableFooterView && null!=mFooterView){
            mFooterView = null;
            notifyItemRemoved(getItemCount()-1);
        }
    }

    public void setRecyclerAdapterListener(RecyclerAdapterListener l){
        recyclerAdapterListener=l;
    }

    public void setLoading() {
        loading = true;
    }

    public boolean isLoading(){
        return loading;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setEnableHeaderView(boolean enableHeaderView){
        this.enableHeaderView= enableHeaderView;
    }
    public void setEnableFooterView(boolean enableFooterView){
        this.enableFooterView= enableFooterView;
    }


    private ItemTypeUtil mUtil = new ItemTypeUtil();

    protected CommonRecyclerAdapter(@Nullable List<T> data,Context c, RecyclerView recyclerview) {
        if (data == null) {
            data = new ArrayList<>();
        }
        ctx= c;
        mDataList = data;

        if (null!= recyclerview&&recyclerview.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
            recyclerview.addOnScrollListener(new RecyclerViewScrollDetector(){

                @Override
                public void onScrollUp() {
                    if(null!=recyclerAdapterListener){
                        recyclerAdapterListener.onScrollUp();
                    }
                }

                @Override
                public void onScrollDown() {
                    if(null!=recyclerAdapterListener){
                        recyclerAdapterListener.onScrollDown();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItemPosition=linearLayoutManager.findLastVisibleItemPosition();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItemPosition + 1 == recyclerView.getAdapter().getItemCount()) {
                        //com.support.util.common.T.show(me,"到最后啦");
                        if(!loading && null!=recyclerAdapterListener){
                            recyclerAdapterListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(enableHeaderView){
            if(mHeaderView!=null && viewType==mUtil.getIntType(TYPE_HEADER)){
                return new RcvAdapterViewHolder(parent.getContext(), parent, mHeaderView);
            }
        }
        if(enableFooterView){
            if(null!=mFooterView && viewType==mUtil.getIntType(TYPE_FOOTER)){
                return new RcvAdapterViewHolder(parent.getContext(), parent, mFooterView);
            }
        }
        //这里返回一个Holder
        return new RcvAdapterViewHolder(parent.getContext(), parent, createItem(mItemType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mUtil.getIntType(TYPE_HEADER)==getItemViewType(position)){
            return;
        }
        if(mUtil.getIntType(TYPE_FOOTER)==getItemViewType(position)){
            return;
        }
        //这里的holder 是上面onCreateViewHolder返回的Holder
        //这里处理数据的绑定 即给各个view附上显示的值  这里封装是在Holder的Item里面处理
        ((RcvAdapterViewHolder) holder).item
                .handleData(getConvertedData(mDataList.get(position), mItemType), position);
        if(null!=getConvertedData(mDataList.get(position), mItemType)){
            holder.itemView.setTag(getConvertedData(mDataList.get(position), mItemType));
            ((RcvAdapterViewHolder) holder).item.setItemClick(ctx,holder.itemView);
        }
        if(null!=kongclickListener){
            holder.itemView.setOnClickListener(kongclickListener);
        }
    }

    @NonNull
    @Override
    public Object getConvertedData(T data, Object type) {
        return data;
    }


    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mDataList.size();
        }else if(mHeaderView == null && mFooterView != null && enableFooterView){
            return mDataList.size() + 1;
        }else if (mHeaderView != null && mFooterView == null && enableHeaderView){
            return mDataList.size() + 1;
        }else {
            return mDataList.size() + 2;
        }

        //return mDataList.size();
    }

    @Override
    public void setData(@NonNull List<T> data) {
        mDataList=data;
    }
    @Override
    public void addData(@NonNull List<T> data) {
        mDataList.addAll(data);
    }
    public void clearAll() {
        mDataList.clear();
    }
    @Override
    public List<T> getData() {
        return mDataList;
    }

    @Override
    public Object getItemType(T t) {
        return t;//default
    }

    @NonNull
    @Override
    public AdapterItem createItem(Object type) {
        return null;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (enableHeaderView && position == 0 && null!=mHeaderView){
            //第一个item应该加载Header
            return mUtil.getIntType(TYPE_HEADER);
        }
        if (enableFooterView&&position == getItemCount()-1 && null!=mFooterView){
            //最后一个,应该加载Footer
            return mUtil.getIntType(TYPE_FOOTER);
        }
        mItemType = getItemType(mDataList.get(position));
        return mUtil.getIntType(mItemType);

    }

    public void addData(T t) {
        mDataList.add(t);
        notifyItemInserted(mDataList.size()-1);
    }

    public void removeData(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public void updateView(int position,String msgEcho) {
        notifyItemChanged(position);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 内部用到的viewHold
    ///////////////////////////////////////////////////////////////////////////

    private static class RcvAdapterViewHolder extends RecyclerView.ViewHolder   {
        protected AdapterItem item;
        protected RcvAdapterViewHolder(Context context, ViewGroup parent, AdapterItem item) {
            //调用父类构造函数初始化itemView
            super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
            this.item = item;
            this.item.bindViews(itemView);
            this.item.setViews();
           // this.item.setItemClick(context,itemView);

        }

        /**
         * 构造 header footer
         * @param context
         * @param parent
         * @param itemView
         */
        protected RcvAdapterViewHolder(Context context, ViewGroup parent, View itemView) {
            //调用父类构造函数初始化itemView
            super(itemView);
        }


    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public View.OnClickListener kongclickListener;

    public void setKongclickListener(View.OnClickListener kongclickListener){
        this.kongclickListener = kongclickListener;

    }
}
