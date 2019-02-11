package com.example.administrator.ding.view.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.RandomLookAdapter;
import com.example.administrator.ding.base.BaseFragment;
import com.example.administrator.ding.model.entities.CommentItem;
import com.example.administrator.ding.model.impl.CardDateGetHelper;
import com.example.administrator.ding.view.activities.RandomActivity;
import com.race604.flyrefresh.FlyRefreshLayout;

import java.util.ArrayList;

public class RandomLookFragment extends BaseFragment implements FlyRefreshLayout.OnPullRefreshListener {

    private static final int REQUEST_RANDOM_SUCCESS = 0;
    private static final int REQUEST_FAILED = 1;
    private RecyclerView mRv;
    private ArrayList<CommentItem> mDatas;
    private ArrayList<Integer> commentTagList;
    private View layout;

    private RandomLookAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private FlyRefreshLayout mFlylayout;


    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideProgress();
                switch (msg.what) {
                    case REQUEST_RANDOM_SUCCESS:
                        ArrayList<CommentItem> items = (ArrayList<CommentItem>) msg.obj;

                        for (CommentItem i : items) {
                            System.out.println(i.getDate() + " ; " + i.getContent() + " ; ");
                        }

                        mDatas.clear();
                        mDatas.addAll(items);
                        mAdapter.notifyDataSetChanged();
                        mLayoutManager.scrollToPosition(0);

                        break;
                    case REQUEST_FAILED:
                        showShortToast("网络异常， 请稍候再试");
                        break;
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_random_look, container, false);
        mRv = (RecyclerView) layout.findViewById(R.id.rv);
        showProgress("加载数据中，请稍候...");

        // set Toolbar
        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        toolbar.setTitle("评论墙");
        RandomActivity activity = (RandomActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        initData();
        initHandler();

        mDatas = new ArrayList<>();
        mFlylayout = (FlyRefreshLayout) layout.findViewById(R.id.fly_refresh_layout);
        mLayoutManager = new LinearLayoutManager(activity);
        mAdapter = new RandomLookAdapter(activity, mDatas);
        mAdapter.setOnItemClickListener(new RandomLookAdapter.OnItemClickListerner() {
            @Override
            public void onLongItemClick(View view, int position) {
                showShortToast(position+"");
                int commentTag = commentTagList.get(position);
                if(commentTag == 0) {
                    showShortToast("该钉子不能评论！");
                } else {
                    ((RandomActivity)getActivity()).switchToEditFragment(commentTag);
                }
            }
        });

        // set paper plane visible
        View actionButton = mFlylayout.getHeaderActionButton();
        ((FloatingActionButton) actionButton).setCompatElevation(0);

        mFlylayout.setOnPullRefreshListener(this);
        mRv.setLayoutManager(mLayoutManager);
        mRv.setAdapter(mAdapter);

        return layout;
    }


    private void initData() {
        CardDateGetHelper helper = new CardDateGetHelper();
        helper.requestDataFromServer(new CardDateGetHelper.OnGetRequestResultListener() {
            @Override
            public void onFailed() {
                sendLoadingMessage(REQUEST_FAILED, null);
            }

            @Override
            public void onSuccess(ArrayList<CommentItem> items, ArrayList<Integer> commentTags) {
                commentTagList = commentTags;
                Message msg = new Message();
                msg.obj = items;
                sendLoadingMessage(REQUEST_RANDOM_SUCCESS, msg);
            }
        });
    }

    @Override
    public void onRefresh(FlyRefreshLayout view) {
        View child = mRv.getChildAt(0);
        if (child != null) {
            bounceAnimateView(child.findViewById(R.id.icon));
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlylayout.onRefreshFinish();
            }
        }, 2000);
    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
        initData();
    }

    private void bounceAnimateView(View view) {
        if (view == null) {
            return;
        }

        Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.start();
    }

}
