package com.example.administrator.ding.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.WelcomePagerAdapter;
import com.example.administrator.ding.fragments.LeadFragment;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private WelcomePagerAdapter adapter;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        viewPager = findViewById(R.id.view_page);
        list = new ArrayList<>();
        Bundle bundle = new Bundle();
        LeadFragment fragment = new LeadFragment();
        bundle.putInt("res", R.drawable.lead1);
        fragment.setArguments(bundle);
        list.add(fragment);

        fragment = new LeadFragment();
        bundle = new Bundle();
        bundle.putInt("res", R.drawable.lead2);
        fragment.setArguments(bundle);
        list.add(fragment);

        fragment = new LeadFragment();
        bundle = new Bundle();
        bundle.putInt("res", R.drawable.lead3);
        fragment.setArguments(bundle);
        list.add(fragment);

        fragment = new LeadFragment();
        bundle = new Bundle();
        bundle.putInt("res", R.drawable.lead4);
        fragment.setArguments(bundle);
        list.add(fragment);

        adapter = new WelcomePagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new MyPagerChangeListener());
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
