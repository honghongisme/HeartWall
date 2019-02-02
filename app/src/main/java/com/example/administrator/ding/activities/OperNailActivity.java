package com.example.administrator.ding.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.NormalPagerAdapter;
import com.example.administrator.ding.fragments.MoodNailFragment;
import com.example.administrator.ding.fragments.PlanNailFragment;

import java.util.ArrayList;
import java.util.List;

public class OperNailActivity extends AppCompatActivity {

    /**
     * 刷新主界面返回码
     */
    private static final int RESULT_CODE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oper_nail);

        // get data from activity
        String tag = getIntent().getStringExtra("tag");

        Bundle bundle = new Bundle();
        PlanNailFragment planNailFragment = new PlanNailFragment();
        planNailFragment.setArguments(bundle);
        MoodNailFragment moodNailFragment = new MoodNailFragment();
        moodNailFragment.setArguments(bundle);

        //add Fragment to List
        List<Fragment> list = new ArrayList<>();
        if ("plan".equals(tag)) {
            list.add(planNailFragment);
            list.add(moodNailFragment);
        } else if ("mood".equals(tag)){
            list.add(moodNailFragment);
            list.add(planNailFragment);
        }

        // set viewPager
        ViewPager myViewPager = (ViewPager) findViewById(R.id.view_page);
        NormalPagerAdapter adapter = new NormalPagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CODE_OK);
    }
}
