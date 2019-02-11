package com.example.administrator.ding.view.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.WelcomePagerAdapter;
import com.example.administrator.ding.view.fragments.LeadFragment;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private WelcomePagerAdapter adapter;
    private List<Fragment> list;

    private int[] imageResId = {R.drawable.lead1, R.drawable.lead2, R.drawable.lead3, R.drawable.lead4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        viewPager = findViewById(R.id.view_page);
        list = new ArrayList<>();

        addOneFragmentToList(imageResId[0]);
        addOneFragmentToList(imageResId[1]);
        addOneFragmentToList(imageResId[2]);
        addOneFragmentToList(imageResId[3]);

        adapter = new WelcomePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyPagerChangeListener());
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
        list.add(fragment);
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
