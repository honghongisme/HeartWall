package com.example.administrator.ding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.model.entry.CommentItem;

import java.util.ArrayList;

public class RandomLookAdapter extends RecyclerView.Adapter<RandomLookAdapter.ItemViewHolder>{

    private OnItemClickListerner mListener;
    private LayoutInflater mInflater;
    private ArrayList<CommentItem> mData;

    public RandomLookAdapter(Context context, ArrayList<CommentItem> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mData = mDatas;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.random_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, int i) {
        final CommentItem data = mData.get(i);
        itemViewHolder.name.setText(data.getUserName());
        itemViewHolder.date.setText(data.getDate());
        itemViewHolder.content.setText(data.getContent());

        if(mListener !=null){
            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=itemViewHolder.getAdapterPosition();
                    mListener.onLongItemClick(itemViewHolder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView name;
        TextView date;
        TextView content;


        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.type_icon);
            name = (TextView) itemView.findViewById(R.id.name_tv);
            date = (TextView) itemView.findViewById(R.id.date_tv);
            content = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }

    public interface OnItemClickListerner{
        void onLongItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListerner listener){
        this.mListener =listener;
    }

}
