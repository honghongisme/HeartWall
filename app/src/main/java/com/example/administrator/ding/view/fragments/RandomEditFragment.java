package com.example.administrator.ding.view.fragments;

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
import com.example.administrator.ding.model.entities.MoodBadComment;
import com.example.administrator.ding.base.SimpleFragment;
import com.example.administrator.ding.model.impl.CardDateGetHelper;
import com.example.administrator.ding.presenter.OnGetCheckResultListener;
import com.example.administrator.ding.view.activities.RandomActivity;
import com.example.administrator.ding.utils.DateUtil;
import com.example.administrator.ding.widgt.LinesEditText;

public class RandomEditFragment extends SimpleFragment {

    private static final int REQUEST_FAILED = 1;
    private static final int REQUEST_INSERT_SUCCESS = 2;
    private static final int REQUEST_INPUT_SENSITIVE = 3;
    private View mLayout;
    private int mIsAnonymous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mLayout = inflater.inflate(R.layout.fragment_random_edit, container, false);
        final int id = getArguments().getInt("id");
        final int commentTag = getArguments().getInt("commentTag");

        initHandler();
        initListener(id, commentTag);
        return mLayout;
    }

    private void initListener(final int id, final int commentTag) {
        final LinesEditText commentEt = mLayout.findViewById(R.id.comment_et);

        Switch anonymousBtn = mLayout.findViewById(R.id.anonymous_btn);
        Button confirmBtn = mLayout.findViewById(R.id.confirm_btn);

        anonymousBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsAnonymous = 1;
                } else {
                    mIsAnonymous = 0;
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEt.getText().toString().trim();
                String date = DateUtil.getDateStr("yyyy-MM-dd HH:mm:ss");
                MoodBadComment comment = new MoodBadComment(id, commentTag, date, commentText, mIsAnonymous);
                System.out.println(id + " ; " + commentTag + " ; " + date + " ; " + commentText + " ; " + mIsAnonymous);
                saveCommentToServer(comment);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
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
                mHandler.sendEmptyMessage(REQUEST_INSERT_SUCCESS);
            }

            @Override
            public void onFailed() {
                mHandler.sendEmptyMessage(REQUEST_FAILED);
            }

            @Override
            public void onNotPassSensitiveWordsCheck() {
                mHandler.sendEmptyMessage(REQUEST_INPUT_SENSITIVE);
            }
        });
    }

}
