package com.example.administrator.ding;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.ding.adapter.PagerAdapter;
import com.example.administrator.ding.module.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeGuideActivity extends AppCompatActivity {

    @BindView(R.id.view_page)
    ViewPager mViewPager;
    private List<Fragment> mList = new ArrayList<>();
    private int[] mImageResId = {R.drawable.lead1, R.drawable.lead2, R.drawable.lead3, R.drawable.lead4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        ButterKnife.bind(this);

        for (int resId : mImageResId) {
            addFragment(resId);
        }
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), mList));
    }

    public void addFragment(int resId) {
        LeadFragment fragment = new LeadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("res", resId);
        fragment.setArguments(bundle);
        mList.add(fragment);
    }

    /**
     * switch
     */
    public void goLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
