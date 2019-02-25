package com.example.administrator.ding.view.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.MyExpandableListAdapter;
import com.example.administrator.ding.base.SimpleActivity;
import com.example.administrator.ding.config.MyApplication;
import com.example.administrator.ding.model.impl.NailListDataModel;
import com.example.administrator.ding.presenter.OnGetRequestResultListener;
import com.example.administrator.ding.model.entities.*;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.utils.NetStateCheckHelper;
import com.example.administrator.ding.utils.SystemResHelper;
import com.example.administrator.ding.widgt.BrowerInfoDialog;
import com.example.administrator.ding.widgt.EditInfoDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NailBagListActivity extends SimpleActivity {

    /**
     * intent里的用户信息
     */
    private User user;
    /**
     * list的group名称数组
     */
    private final String[] groupTitles = {"计划钉子","好运钉子", "厄运钉子"};
    /**
     * ExpandableListView的适配器Adapter
     */
    private MyExpandableListAdapter adapter;
    /**
     * ExpandableListView
     */
    private ExpandableListView expandableListView;
    /**
     * 计划group里未完成的child数量
     */
    private int notFinishNum;

    private NailListDataModel model;

    private static final int PLAN_GROUP_POSITION = 0;
    private static final int MOOD_GOOD_GROUP_POSITION = 1;
    private static final int MOOD_BAD_GROUP_POSITION = 2;
    private static final int REQUEST_RESULT_FAILED = 3;
    private static final int REQUEST_RESULT_SUCCESS = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initContentViewData() { }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_nail_bag_list;
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void initView() {
        initToolbar();
        SystemResHelper.setStateBarColor(this);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        user = ((MyApplication)getApplication()).getUser();
        model = new NailListDataModel(getContext());

        expandableListView = findViewById(R.id.expand_list);
        adapter = new MyExpandableListAdapter(this, groupTitles, getData(), notFinishNum);
        expandableListView.setAdapter(adapter);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_RESULT_SUCCESS:
                        // 刷新列表
                        // notifyDataSetChanged()会检查date是否更新，如果直接调用此方法，检查的还是原来的date，因此要传入修改的date
                        // 传入修改数据后再调用 notifyDataSetChanged()在滑动或列表伸缩的时候列表才会重绘，因此让列表伸缩一次即可
                        adapter.refresh(getData(), notFinishNum);
                        expandableListView.collapseGroup(0);
                        expandableListView.expandGroup(0);
                        showShortToast("拔出成功！详情请查看背包");
                        break;
                    case REQUEST_RESULT_FAILED:
                        showLongToast("网络异常，请检查网络设置");
                        break;
                }
            }
        };
    }

    @Override
    protected void initListener() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                BagListItem item = (BagListItem) v.getTag();
                if(groupPosition == PLAN_GROUP_POSITION) {
                    if (childPosition < notFinishNum) {
                        // 在墙上的
                        String record = model.getPlanNailDetails(item.getFirstDate());
                        showListenerNailDetailsDialog(item.getFirstDate(), record);
                    }else {
                        // 取下的
                        String[] records = model.getPlanPullNailDetails(item.getFirstDate(), item.getLastDate());
                        popNailDetailsDialog(item.getFirstDate(), records[0], item.getLastDate(), records[1]);
                    }
                }else if (groupPosition == MOOD_GOOD_GROUP_POSITION){
                    MoodGoodNail nail = model.getGoodNailDetailsByDate(item.getFirstDate());
                    Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
                    i.putExtra("name", getTopShowName(user.getName(), nail.getVisibility(), nail.getAnonymous()));
                    i.putExtra("date", nail.getFirstDate());
                    i.putExtra("content", nail.getRecord());
                    i.putExtra("type", "good");
                    startActivity(i);
                } else if (groupPosition == MOOD_BAD_GROUP_POSITION) {
                    MoodBadNail nail = model.getBadNailDetailsByDate(item.getFirstDate());
                    Intent i = new Intent(getContext(), LookCommentDetailActivity.class);
                    i.putExtra("id", user.getId());
                    i.putExtra("x", nail.getX());
                    i.putExtra("y", nail.getY());
                    i.putExtra("name", getTopShowName(user.getName(), nail.getVisibility(), nail.getAnonymous()));
                    i.putExtra("date", nail.getFirstDate());
                    i.putExtra("content", nail.getRecord());
                    i.putExtra("visible", nail.getVisibility());
                    i.putExtra("type", "bad");
                    startActivity(i);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NailBagListActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化toolbar控件
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("钉子背包");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 初始化带有拔出button的dialog并弹出
     * @param firstDate
     * @param firstRecord
     */
    private void showListenerNailDetailsDialog(final String firstDate, final String firstRecord) {
        BrowerInfoDialog dialog = new BrowerInfoDialog(getContext());
        dialog.show();
        dialog.setData(firstDate, firstRecord, "", "未完待续");
        dialog.setOnGoNailClickListener(new BrowerInfoDialog.OnGoNailClickListener() {
            @Override
            public void onClick() {
                if(NetStateCheckHelper.isNetWork(getContext())) {
                    final String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
                    final EditInfoDialog editInfoDialog = new EditInfoDialog(getContext());
                    editInfoDialog.show();
                    editInfoDialog.setData(2, date);
                    editInfoDialog.setDialogBtnClickListener(new EditInfoDialog.DialogBtnClickListener() {
                        @Override
                        public void doConfirm(final String text) {
                            editInfoDialog.dismiss();

                            PlanPullNail planPullNail = new PlanPullNail(user.getId(), firstDate, firstRecord, date, text);
                            model.saveLastEditInfoToServer(planPullNail, new OnGetRequestResultListener() {
                                @Override
                                public void onSuccess() {
                                    model.saveLastEditInfo(firstDate, firstRecord, date, text);
                                    model.updateDateData(4, "yyyy-MM-dd EEE");
                                    model.updateDateData(5, "yyyy-MM");
                                    sendLoadingMessage(REQUEST_RESULT_SUCCESS);
                                }

                                @Override
                                public void onFailed() {
                                    sendLoadingMessage(REQUEST_RESULT_FAILED);
                                }
                            });
                        }
                        @Override
                        public void doCancel() {
                            editInfoDialog.dismiss();
                            showShortToast("取消编辑！");
                        }

                        @Override
                        public void isVisible(boolean isChecked) {

                        }

                        @Override
                        public void isAnonymous(boolean isChecked) {

                        }
                    });
                } else {
                    showLongToast("网络异常，请检查网络设置");
                }
            }
        });
    }

    /**
     * 初始化详细信息dialog并弹出
     * @param firstDate
     * @param firstRecord
     * @param lastDate
     * @param lastRecord
     */
    private void popNailDetailsDialog(String firstDate, String firstRecord, String lastDate, String lastRecord) {
        BrowerInfoDialog dialog = new BrowerInfoDialog(getContext());
        dialog.show();
        dialog.setData(firstDate, firstRecord, lastDate, lastRecord);
    }

    /**
     * 获取数据
     */
    private HashMap<String, List<BagListItem >> getData() {
        HashMap<String, List<BagListItem>> dateMap = new HashMap<>();
        ArrayList<BagListItem> list = new ArrayList<>();
        model.getPlanGroupRoomDataList(list, "plan_nail");
        notFinishNum = list.size();
        model.getPlanGroupRemoveDataList(list, "plan_pull_nail");
        dateMap.put(groupTitles[0], list);

        list = new ArrayList<>();
        model.getMoodGoodGroupDataList(list);
        dateMap.put(groupTitles[1], list);

        list = new ArrayList<>();
        model.getMoodBadGroupDataList(list);
        dateMap.put(groupTitles[2], list);

        return dateMap;
    }

    private String getTopShowName(String name, int visible, int anonymous) {
        if (visible == 1 && anonymous == 1) {
            return "匿名用户";
        }
        return name;
    }

}
