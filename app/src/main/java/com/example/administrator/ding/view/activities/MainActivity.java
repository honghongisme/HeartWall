package com.example.administrator.ding.view.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.ding.R;
import com.example.administrator.ding.base.MyApplication;
import com.example.administrator.ding.view.IMainView;
import com.example.administrator.ding.base.SimpleActivity;
import com.example.administrator.ding.model.entities.User;
import com.example.administrator.ding.presenter.impl.MainPresenterImpl;
import com.example.administrator.ding.utils.SystemResHelper;
import com.example.administrator.ding.widgt.ArcMenu;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;


public class MainActivity extends SimpleActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IMainView {

    /**
     * 请求码
     */
    private static final int START_NAIL_ACTIVITY_REQUEST_CODE = 1;

    private DrawerLayout drawer;
    /**
     * 数据显示模块
     */
    private LinearLayout moodModule;
    private LinearLayout planModule;

    private MainPresenterImpl mPresenter;

    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SystemResHelper.setStateBarColor(this);
        mPresenter.getTodayModuleInfo();
    }

    @Override
    protected void initContentViewData() {

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void initView() {
        initToolBar();
        initNavigation();
        initBanner();
        initMenu();

        moodModule = findViewById(R.id.mood_module_layout);
        planModule = findViewById(R.id.plan_module_layout);

        ImageView moodLearnMoreBtn = findViewById(R.id.learn_more_mood_date_btn);
        moodLearnMoreBtn.setOnClickListener(this);

        ImageView planLearnMoreBtn = findViewById(R.id.learn_more_plan_date_btn);
        planLearnMoreBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mPresenter = new MainPresenterImpl(this);
    }

    @Override
    protected void initListener() { }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Heart Wall");
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        TextView nameTv = navHeader.findViewById(R.id.name);
        TextView snoTv = navHeader.findViewById(R.id.sno);
        TextView departmentTv = navHeader.findViewById(R.id.department);

        User user = ((MyApplication)getApplication()).getUser();
        if (user != null) {
            nameTv.setText(user.getName() + "(" + user.getIdentity() + ")");
            snoTv.setText(user.getAccountNumber());
            departmentTv.setText(user.getDepartment());
        }

    }

    private void initMenu() {
        ArcMenu arcMenu = findViewById(R.id.arcMenu);
        // 添加按钮
        arcMenu.addChildArcMenu(R.drawable.ic_mood_good_nail, "心情墙", null);
        arcMenu.addChildArcMenu(R.drawable.ic_mirror, "评论墙", null);
        arcMenu.addChildArcMenu(R.drawable.ic_plan_nail, "计划墙", null);

        arcMenu.layoutChildMenu();
        // 设置当前菜单显示子按钮数量
        arcMenu.setShowMenuBtnNum(3);
        arcMenu.setOnMenuMainBtnClickListener(new ArcMenu.OnMenuMainBtnClickListener() {
            @Override
            public void onClick() {

            }
        });
        arcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClickMenu(View view, int pos, String extraInfo) {
                if(pos == 0) {
                    showProgress("加载数据中，请稍候...");
                    startNailActivity("mood");
                }
                else if (pos == 1){
                    startOneActivity(null, RandomActivity.class);
                }
                else if (pos == 2) {
                    showProgress("加载数据中，请稍候...");
                    startNailActivity("plan");
                }
            }
        });
        arcMenu.setOnMenuItemLongClickListener(new ArcMenu.OnMenuItemLongClickListener() {
            @Override
            public void onLongClickItem(View view, int pos) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.learn_more_mood_date_btn:
                startOneActivity("mood", DateAnalysisActivity.class);
                break;
            case R.id.learn_more_plan_date_btn:
                startOneActivity("plan", DateAnalysisActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.information:
                break;
            case R.id.nailBackpack:
                startOneActivity(null, NailBagListActivity.class);
                break;
            case R.id.describe:
                break;
            case R.id.exit:
                showIsCleanUserInfoDialog();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 确认退出dialog
     */
    private void showIsCleanUserInfoDialog() {
        new AlertDialog.Builder(getContext())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitAccount();
                    }
                })
                .setMessage("是否确定退出当前用户？")
                .create().show();
    }

    /**
     * 退出账号
     */
    private void exitAccount() {
        showProgress("正在退出账号，请稍后...");
        mPresenter.clearAccountInfo();
        Intent i = new Intent(getContext(), LoginActivity.class);
        i.putExtra("tag", "clean");
        startActivity(i);
        hideProgress();
        finish();
    }

    private void startNailActivity(String tag) {
        Intent i = new Intent(getContext(), OperateNailActivity.class);
        i.putExtra("tag", tag);
        startActivityForResult(i, START_NAIL_ACTIVITY_REQUEST_CODE);
    }

    /**
     * startActivity()方式启动activity
     * @param arg
     * @param cls
     */
    private void startOneActivity(String arg, Class<?> cls) {
        Intent i = new Intent(getContext(), cls);
        i.putExtra("arg", arg);
        this.startActivity(i);
    }

    /**
     * 刷新界面更新数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgress();
        mPresenter.getTodayModuleInfo();
    }

    /******************************************** 轮播 start **********************************************/

    /**
     * 初始化banner
     */
    private void initBanner() {
        setBannerDate();
        setBanner();
    }

    /**
     * 设置banner的数据
     */
    private void setBannerDate() {
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.main_bg_01);
        imagePath.add(R.drawable.main_bg_02);
        imagePath.add(R.drawable.main_bg03);
        imageTitle.add("记录你的心情与计划");
        imageTitle.add("钉下你的想法");
        imageTitle.add("每日记录");
    }

    /**
     * 设置banner
     */
    private void setBanner() {
        MyImageLoader mMyImageLoader = new MyImageLoader();
        Banner mBanner = findViewById(R.id.banner);
        //设置样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //轮播图片的文字
        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(5000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        //点击事件
                    }
                })
                //开始调用的方法，启动轮播图。
                .start();
    }

    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

    /******************************************** 轮播 end **********************************************/

    @Override
    public void onGetCurrentMoodModuleInfo(String badNailNumberStr, String pullNumberStr, String goodNailNumberStr, String comment) {
        TextView badNailNumberTv = moodModule.findViewById(R.id.today_nail_nail_num_tv);
        TextView pullNumberTv = moodModule.findViewById(R.id.today_pull_nail_num_tv);
        TextView goodNailNumberTv = moodModule.findViewById(R.id.today_extra_nail_num_tv);
        TextView commentTv = moodModule.findViewById(R.id.today_comment_tv);
        goodNailNumberTv.setVisibility(View.VISIBLE);

        badNailNumberTv.setText(badNailNumberStr);
        pullNumberTv.setText(pullNumberStr);
        goodNailNumberTv.setText(goodNailNumberStr);
        commentTv.setText(comment);
    }

    @Override
    public void onGetCurrentPlanModuleInfo(String nailNumberStr, String pullNumberStr, String comment) {
        TextView nailNumberTv = planModule.findViewById(R.id.today_nail_nail_num_tv);
        TextView pullNumberTv = planModule.findViewById(R.id.today_pull_nail_num_tv);
        TextView commentTv = planModule.findViewById(R.id.today_comment_tv);
        TextView goodNailNumberTv = planModule.findViewById(R.id.today_extra_nail_num_tv);
        goodNailNumberTv.setVisibility(View.GONE);

        nailNumberTv.setText(nailNumberStr);
        pullNumberTv.setText(pullNumberStr);
        commentTv.setText(comment);
    }

}
