package com.example.administrator.ding.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.example.administrator.ding.R;
import com.example.administrator.ding.bean.User;
import com.example.administrator.ding.fragments.RandomEditFragment;
import com.example.administrator.ding.fragments.RandomLookFragment;

import java.util.ArrayList;


public class RandomActivity extends AppCompatActivity {

    private FragmentManager manager;
    private ArrayList<Fragment> list;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_look);

        user = getIntent().getParcelableExtra("user");

        list = new ArrayList<>();
        RandomLookFragment lookFragment = new RandomLookFragment();
        RandomEditFragment editFragment = new RandomEditFragment();
        list.add(lookFragment);
        list.add(editFragment);

        manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, lookFragment).commit();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void switchToEditFragment(int index) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putInt("id", user.getId());
        bundle.putInt("commentTag", index);
        list.get(1).setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, list.get(1)).commit();
    }

    public void switchToLookFragment() {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, list.get(0)).commit();
    }

    @Override
    public void onBackPressed() {
        // 如果在edit界面，则返回至look，否则退出
        if (list.get(1).isVisible()) {
            switchToLookFragment();
            System.out.println("---------switch");
        } else {
            super.onBackPressed();
            System.out.println("---------exit");
        }
    }
}
