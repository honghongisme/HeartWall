package com.example.administrator.ding.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding.R;
import com.example.administrator.ding.model.entry.BagListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/7.
 */

public class ListAdapter extends BaseExpandableListAdapter {

    private Activity mContext;
    /**
     * group标题数组
     */
    private String[] mGroupsTitles;
    /**
     * 数据map
     */
    private Map<String, List<BagListItem>> mData;
    /**
     * 计划类型中未完成的个数
     */
    private int mPlanNotFinishNum;



    public ListAdapter(Activity context, String[] groups, Map<String, List<BagListItem>> dateMap, int planNotFinishNum) {
        this.mContext = context;
        this.mGroupsTitles = groups;
        this.mData = dateMap;
        this.mPlanNotFinishNum = planNotFinishNum;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mGroupsTitles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(mGroupsTitles[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(mGroupsTitles[groupPosition]);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(mGroupsTitles[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expand_list_group, null);
        }
        convertView.setTag(R.layout.expand_list_group, groupPosition);
        convertView.setTag(R.layout.expand_list_item, -1);
        TextView text = convertView.findViewById(R.id.group_title_tv);
        text.setText(mGroupsTitles[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expand_list_item, null);
        }
        //设置显示的文字
        setItemText(groupPosition, childPosition, convertView);
        //设置显示的图片
        setPlanItemStateImage(groupPosition, childPosition, convertView);
        return convertView;
    }

    /**
     * 设置计划item前显示状态的图片
     * @param groupPosition
     * @param childPosition
     * @param convertView
     */
    public void setPlanItemStateImage(int groupPosition, int childPosition, View convertView) {
        // 这里处理groupPosition不为0的情况 避免复用导致图片加载混乱
        ImageView stateImageView = convertView.findViewById(R.id.state);
        if(groupPosition == 0) {
            stateImageView.setVisibility(View.VISIBLE);
            if (childPosition < mPlanNotFinishNum) {
                stateImageView.setImageResource(R.drawable.ic_not_finish);
            }else {
                stateImageView.setImageResource(R.drawable.ic_finish);
            }
        } else {
            stateImageView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置每个item显示的文字
     * @param groupPosition
     * @param childPosition
     * @param convertView
     */
    public void setItemText(int groupPosition, int childPosition, View convertView) {
        TextView text1 = convertView.findViewById(R.id.firstDate);
        TextView text2 = convertView.findViewById(R.id.lastDate);
        TextView text3 = convertView.findViewById(R.id.some_content);

        BagListItem item = mData.get(mGroupsTitles[groupPosition]).get(childPosition);
        convertView.setTag(item);
        text1.setText(item.getFirstDate());
        text2.setText(item.getLastDate());
        text3.setText(item.getSomeContent());
    }

    /**
     * 刷新数据
     */
    public void refresh(HashMap<String, List<BagListItem >> dateMap, int notFinishNum) {
        this.mData = dateMap;
        mPlanNotFinishNum = notFinishNum;
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
