package com.example.administrator.ding.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.List;

public class NormalPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;

    public NormalPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

}
