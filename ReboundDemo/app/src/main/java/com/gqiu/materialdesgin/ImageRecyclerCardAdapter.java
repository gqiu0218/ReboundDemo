package com.gqiu.materialdesgin;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerCardAdapter extends RecyclerView.Adapter<ImageRecyclerCardAdapter.SimpleViewHolder> implements View.OnClickListener {
    private List<ImageBean> mDatas = new ArrayList<>();
    private Context mContext;
    private int mCellWidth;
    private OnItemClickListener mListener;


    public ImageRecyclerCardAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int creenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        mCellWidth = (creenWidth - context.getResources().getDimensionPixelSize(R.dimen.divider) * 2) / 2;
    }

    public void setData(List<ImageBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        ImageBean item = mDatas.get(position);
        if (item.getImageWidth() == 0) {
            item.setImageWidth(300);
        }

        SimpleDraweeView draweeView = holder.draweeView;
        ViewGroup.LayoutParams params = draweeView.getLayoutParams();
        params.width = mCellWidth;
        params.height = mCellWidth * item.getImageHeight() / item.getImageWidth();
        draweeView.setLayoutParams(params);

        if (!TextUtils.isEmpty(item.getImageUrl())) {
            draweeView.setTag(item);
            draweeView.setImageURI(Uri.parse(item.getImageUrl()));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }

        ImageBean tag = (ImageBean) v.getTag();
        mListener.onItemClick(v, tag);
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView draweeView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
            draweeView.setOnClickListener(ImageRecyclerCardAdapter.this);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, ImageBean item);
    }
}
