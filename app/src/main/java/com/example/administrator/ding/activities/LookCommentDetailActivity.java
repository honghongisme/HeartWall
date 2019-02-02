package com.example.administrator.ding.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.ding.R;
import com.example.administrator.ding.bean.CommentItem;
import com.example.administrator.ding.factory.model.LoadingCommentItem;
import com.example.administrator.ding.utils.SystemResHelper;

import java.util.ArrayList;

public class LookCommentDetailActivity extends AppCompatActivity {

    private LinearLayout container;
    private ConstraintLayout loadingCl;
    private int x, y, id;
    private boolean visible = false;
    private static final int REQUEST_RESULT_SUCCESS = 1;
    private static final int REQUEST_RESULT_FAILED = 2;
    private static final int REQUEST_RESULT_EMPTY = 3;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideLoadingProgress();

            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp1.gravity = Gravity.CENTER_HORIZONTAL;
            switch (msg.what) {
                case REQUEST_RESULT_SUCCESS:
                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    View childView;
                    ArrayList<CommentItem> commentItems = (ArrayList<CommentItem>) msg.obj;
                    for (CommentItem item : commentItems) {
                        childView = inflater.inflate(R.layout.single_comment, null);
                        TextView nameTv = childView.findViewById(R.id.name_tv);
                        TextView dateTv = childView.findViewById(R.id.date_tv);
                        TextView contentTv = childView.findViewById(R.id.comment_tv);
                        nameTv.setText(item.getUserName());
                        dateTv.setText(item.getDate());
                        contentTv.setText(item.getContent());
                        container.addView(childView, layoutParams);
                    }
                    break;
                case REQUEST_RESULT_FAILED:
                    TextView errorTv = findViewById(R.id.error_tv);
                    errorTv.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_RESULT_EMPTY:
                    TextView emptyTv = findViewById(R.id.empty_tv);
                    emptyTv.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_comment_detail);

        initToolbar();
        // 设置通知栏颜色
        SystemResHelper.setStateBarColor(this);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String date = i.getStringExtra("date");
        String content = i.getStringExtra("content");
        setMainContent(name, date, content);

        String type = i.getStringExtra("type");
        if ("bad".equals(type)) {
            visible = i.getIntExtra("visible", 0) == 1;
            if (visible) {
                showLoadingProgress();
                x = i.getIntExtra("x", 0);
                y = i.getIntExtra("y", 0);
                id = i.getIntExtra("id", 0);
                container = findViewById(R.id.container_ll);
            } else {
                TextView visibleTv = findViewById(R.id.visible_tv);
                visibleTv.setVisibility(View.VISIBLE);
            }
        } else if ("good".equals(type)){
            TextView cantCommentTv = findViewById(R.id.cant_comment_tv);
            cantCommentTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化toolbar控件
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("评论");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && visible) {
            System.out.println("---------------onWindowFocusChanged");
            new LoadingCommentItem().getCommentItemList(this, id, x, y, new LoadingCommentItem.OnGetRequestResultListener() {
                @Override
                public void onSuccess(ArrayList<CommentItem> commentItems) {
                    System.out.println("--------------size = " + commentItems.size());
                    if (commentItems.size() == 0) {
                        sendLoadingMessage(REQUEST_RESULT_EMPTY, null);
                    } else {
                        Message msg = new Message();
                        msg.obj = commentItems;
                        sendLoadingMessage(REQUEST_RESULT_SUCCESS, msg);
                    }
                }

                @Override
                public void onFailed() {
                    sendLoadingMessage(REQUEST_RESULT_FAILED, null);
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    private void setMainContent(String name, String date, String content) {
        TextView nameTv = findViewById(R.id.name_tv);
        TextView dateTv = findViewById(R.id.date_tv);
        TextView contentTv = findViewById(R.id.content_tv);

        nameTv.setText(name);
        dateTv.setText(date);
        contentTv.setText(content);
    }

    /**
     * 发送 加载message
     */
    private void sendLoadingMessage(int msgWhat, Message msg) {
        if (msg == null) {
            msg = new Message();
        }
        msg.what = msgWhat;
        handler.sendMessage(msg);
    }

    private void showLoadingProgress() {
        loadingCl = findViewById(R.id.loading_container_cl);
        loadingCl.setVisibility(View.VISIBLE);
        ImageView imageView = findViewById(R.id.progress_bar_iv);
        final AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
    }

    private void hideLoadingProgress() {
        loadingCl.setVisibility(View.GONE);
    }
}