package com.example.administrator.ding.module.nail;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.ding.R;
import com.example.administrator.ding.adapter.PagerAdapter;
import com.example.administrator.ding.module.nail.mood.MoodNailFragment;
import com.example.administrator.ding.module.nail.plan.PlanNailFragment;

import java.util.ArrayList;
import java.util.List;

public class OperateNailActivity extends AppCompatActivity {

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
        ViewPager myViewPager = findViewById(R.id.view_page);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CODE_OK);
    }
}
