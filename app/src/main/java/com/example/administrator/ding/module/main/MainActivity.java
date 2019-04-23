package com.example.administrator.ding.module.main;

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
import com.example.administrator.ding.module.analysis.DateAnalysisActivity;
import com.example.administrator.ding.module.bag.NailBagListActivity;
import com.example.administrator.ding.module.communication.RandomActivity;
import com.example.administrator.ding.base.MyApplication;
import com.example.administrator.ding.module.login.LoginActivity;
import com.example.administrator.ding.module.nail.OperateNailActivity;
import com.example.administrator.ding.base.SimpleActivity;
import com.example.administrator.ding.model.entry.User;
import com.example.administrator.ding.utils.SystemResHelper;
import com.example.administrator.ding.widgt.ArcMenu;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends SimpleActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainContract.View {

    // 请求码
    private static final int START_NAIL_ACTIVITY_REQUEST_CODE = 1;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.mood_module_layout)
    LinearLayout mMoodModuleLl;
    @BindView(R.id.plan_module_layout)
    LinearLayout mPlanModuleLl;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.arcMenu)
    ArcMenu mArcMenu;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainPresenterImpl mPresenter;
    private ArrayList<Integer> mBannerImagePath;
    private ArrayList<String> mBannerImageTitle;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SystemResHelper.setStateBarColor(this);

        mContext = this;
        initView();
        mPresenter = new MainPresenterImpl(this);
        mPresenter.getTodayModuleInfo();
    }

    public void initView() {
        initToolBar();
        initNavigation();
        initBanner();
        initMenu();

        ImageView learnMoreMoodBtn = mMoodModuleLl.findViewById(R.id.learn_more_btn);
        learnMoreMoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOneActivity("mood", DateAnalysisActivity.class);
            }
        });
        ImageView learnMorePlanBtn = mPlanModuleLl.findViewById(R.id.learn_more_btn);
        learnMorePlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOneActivity("plan", DateAnalysisActivity.class);
            }
        });
    }

    private void initToolBar() {
        mToolbar.setTitle("Heart Wall");
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
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
        // 添加按钮
        mArcMenu.addChildArcMenu(R.drawable.ic_mood_good_nail, "心情墙", null);
        mArcMenu.addChildArcMenu(R.drawable.ic_mirror, "评论墙", null);
        mArcMenu.addChildArcMenu(R.drawable.ic_plan_nail, "计划墙", null);

        mArcMenu.layoutChildMenu();
        // 设置当前菜单显示子按钮数量
        mArcMenu.setShowMenuBtnNum(3);
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClickMenu(View view, int pos, String extraInfo) {
                if(pos == 0) {
                    showProgress("加载数据中，请稍候...");
                    startNailActivity("mood");
                }
                else if (pos == 1){
                    Intent i = new Intent(mContext, RandomActivity.class);
                    startActivity(i);
                }
                else if (pos == 2) {
                    showProgress("加载数据中，请稍候...");
                    startNailActivity("plan");
                }
            }
        });
        mArcMenu.setOnMenuMainBtnClickListener(new ArcMenu.OnMenuMainBtnClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nailBackpack:
                Intent i = new Intent(mContext, NailBagListActivity.class);
                startActivity(i);
                break;
            case R.id.exit:
                showIsCleanUserInfoDialog();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 确认退出dialog
     */
    private void showIsCleanUserInfoDialog() {
        new AlertDialog.Builder(mContext)
                .setNegativeButton("取消", null)
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
        startActivity(new Intent(mContext, LoginActivity.class));
        hideProgress();
        finish();
    }

    private void startNailActivity(String tag) {
        Intent i = new Intent(mContext, OperateNailActivity.class);
        i.putExtra("tag", tag);
        startActivityForResult(i, START_NAIL_ACTIVITY_REQUEST_CODE);
    }

    private void startOneActivity(String arg, Class<?> cls) {
        Intent i = new Intent(mContext, cls);
        i.putExtra("arg", arg);
        startActivity(i);
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
        setBannerData();
        setBanner();
    }

    /**
     * 设置banner的数据
     */
    private void setBannerData() {
        mBannerImagePath = new ArrayList<>();
        mBannerImageTitle = new ArrayList<>();
        mBannerImagePath.add(R.drawable.main_bg_01);
        mBannerImagePath.add(R.drawable.main_bg_02);
        mBannerImagePath.add(R.drawable.main_bg03);
        mBannerImageTitle.add("记录你的心情与计划");
        mBannerImageTitle.add("钉下你的想法");
        mBannerImageTitle.add("每日记录");
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
        mBanner.setBannerTitles(mBannerImageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(5000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(mBannerImagePath)
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
        TextView badNailNumberTv = mMoodModuleLl.findViewById(R.id.today_nail_nail_num_tv);
        TextView pullNumberTv = mMoodModuleLl.findViewById(R.id.today_pull_nail_num_tv);
        TextView goodNailNumberTv = mMoodModuleLl.findViewById(R.id.today_extra_nail_num_tv);
        TextView commentTv = mMoodModuleLl.findViewById(R.id.today_comment_tv);
        goodNailNumberTv.setVisibility(View.VISIBLE);

        badNailNumberTv.setText(badNailNumberStr);
        pullNumberTv.setText(pullNumberStr);
        goodNailNumberTv.setText(goodNailNumberStr);
        commentTv.setText(comment);
    }

    @Override
    public void onGetCurrentPlanModuleInfo(String nailNumberStr, String pullNumberStr, String comment) {
        TextView nailNumberTv = mPlanModuleLl.findViewById(R.id.today_nail_nail_num_tv);
        TextView pullNumberTv = mPlanModuleLl.findViewById(R.id.today_pull_nail_num_tv);
        TextView commentTv = mPlanModuleLl.findViewById(R.id.today_comment_tv);
        TextView goodNailNumberTv = mPlanModuleLl.findViewById(R.id.today_extra_nail_num_tv);
        goodNailNumberTv.setVisibility(View.GONE);

        nailNumberTv.setText(nailNumberStr);
        pullNumberTv.setText(pullNumberStr);
        commentTv.setText(comment);
    }

}
