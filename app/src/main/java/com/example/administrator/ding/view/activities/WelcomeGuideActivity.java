package com.example.administrator.ding.view.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.NormalPagerAdapter;
import com.example.administrator.ding.view.fragments.LeadFragment;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private NormalPagerAdapter mAdapter;
    private List<Fragment> mList;

    private int[] mImageResId = {R.drawable.lead1, R.drawable.lead2, R.drawable.lead3, R.drawable.lead4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        mViewPager = findViewById(R.id.view_page);
        mList = new ArrayList<>();

        addOneFragmentToList(mImageResId[0]);
        addOneFragmentToList(mImageResId[1]);
        addOneFragmentToList(mImageResId[2]);
        addOneFragmentToList(mImageResId[3]);

        mAdapter = new NormalPagerAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());
    }

    /**
     *
     * @param resId
     */
    public void addOneFragmentToList(int resId) {
        LeadFragment fragment = new LeadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("res", resId);
        fragment.setArguments(bundle);
        mList.add(fragment);
    }

    /**
     * switch
     */
    public void clickCallBack() {
        startActivity(new Intent(WelcomeGuideActivity.this, LoginActivity.class));
        this.finish();
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {

        }
    }
}
