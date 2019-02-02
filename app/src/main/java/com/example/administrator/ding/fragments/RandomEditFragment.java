package com.example.administrator.ding.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.example.administrator.ding.R;
import com.example.administrator.ding.bean.MoodBadComment;
import com.example.administrator.ding.base.BaseFragment;
import com.example.administrator.ding.factory.model.CardDateGetHelper;
import com.example.administrator.ding.listener.OnGetCheckResultListener;
import com.example.administrator.ding.activities.RandomActivity;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.widgt.LinesEditText;

public class RandomEditFragment extends BaseFragment {

    private static final int REQUEST_FAILED = 1;
    private static final int REQUEST_INSERT_SUCCESS = 2;
    private static final int REQUEST_INPUT_SENSITIVE = 3;
    private View layout;
    private int isAnonymous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_random_edit, container, false);
        final int id = getArguments().getInt("id");
        final int commentTag = getArguments().getInt("commentTag");

        initHandler();
        initListener(id, commentTag);
        return layout;
    }

    private void initListener(final int id, final int commentTag) {
        final LinesEditText commentEt = layout.findViewById(R.id.comment_et);

        Switch anonymousBtn = layout.findViewById(R.id.anonymous_btn);
        Button confirmBtn = layout.findViewById(R.id.confirm_btn);

        anonymousBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isAnonymous = 1;
                } else {
                    isAnonymous = 0;
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEt.getText().toString().trim();
                String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
                MoodBadComment comment = new MoodBadComment(id, commentTag, date, commentText, isAnonymous);
                System.out.println(id + " ; " + commentTag + " ; " + date + " ; " + commentText + " ; " + isAnonymous);
                saveCommentToServer(comment);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideProgress();
                switch (msg.what) {
                    case REQUEST_FAILED :
                        showShortToast("网络异常！");
                        break;
                    case REQUEST_INSERT_SUCCESS:
                        showShortToast("发布成功！");
                        ((RandomActivity)getActivity()).switchToLookFragment();
                        break;
                    case REQUEST_INPUT_SENSITIVE:
                        showLongToast("输入文本存在敏感词汇，请重新输入！");
                }
            }
        };
    }

    /**
     * 提交评论到服务器
     * @param comment
     */
    private void saveCommentToServer(MoodBadComment comment) {
        showProgress("正在提交数据，请稍等...");
        new CardDateGetHelper().insertOneCommentToServer(comment, new OnGetCheckResultListener() {
            @Override
            public void onSuccess() {
                sendLoadingMessage(REQUEST_INSERT_SUCCESS, null);
            }

            @Override
            public void onFailed() {
                sendLoadingMessage(REQUEST_FAILED, null);
            }

            @Override
            public void onCheckFailed() {
                sendLoadingMessage(REQUEST_INPUT_SENSITIVE, null);
            }
        });
    }

}