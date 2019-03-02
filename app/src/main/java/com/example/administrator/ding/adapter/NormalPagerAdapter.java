package com.example.administrator.ding.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.List;

public class NormalPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> mList;

    public NormalPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mList.size();//有几个页面
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
