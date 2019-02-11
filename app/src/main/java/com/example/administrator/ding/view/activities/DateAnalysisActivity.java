package com.example.administrator.ding.view.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.administrator.ding.R;
import com.example.administrator.ding.base.BaseActivity;
import com.example.administrator.ding.model.impl.DateAnalysisModel;
import com.example.administrator.ding.utils.DateUtil;

import com.example.administrator.ding.utils.SystemResHelper;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class DateAnalysisActivity extends BaseActivity {

    /**
     * 曲线根布局
     */
    private ConstraintLayout LineChartCl, ColumnarChartCl;

    /**
     * 每条曲线名称
     */
    private String[] planLineTitles = {"钉入总数", "拔出总数"};
    /**
     * 折线的每条曲线的横轴值(两条曲线相同)
     */
    private ArrayList<String> planLineChartXValues;
    /**
     * 折线的每条曲线的纵轴值
     */
    private ArrayList<int[]> planLineChartYValues;
    /**
     * 柱状图每条曲线的纵轴值
     */
    private ArrayList<int[]> planColumnarChartYValues;
    /**
     * 柱状图每条曲线的横轴值
     */
    private ArrayList<String> planColumnarChartXValues;

    /**
     * 每条曲线名称
     */
    private String[] moodLineTitles = {"(好)钉总数 ", "(好)拔总数", "(厄)钉总数 ", "(厄)拔总数 "};
    /**
     * 折线的每条曲线的横轴值(两条曲线相同)
     */
    private ArrayList<String> moodLineChartXValues;
    /**
     * 折线的每条曲线的纵轴值
     */
    private ArrayList<int[][]> moodLineChartYValues;
    /**
     * 柱状图每条曲线的纵轴值
     */
    private ArrayList<int[][]> moodColumnarChartYValues;
    /**
     * 柱状图每条曲线的横轴值
     */
    private ArrayList<String> moodColumnarChartXValues;

    /**
     * 数据model层
     */
    private DateAnalysisModel model;
    /**
     * intent获取的tag，表示类型
     */
    private String tag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setChartDate();
        // 绘制图表
        if ("plan".equals(tag)) {
            paintPlanLineChart();
            paintPlanColumnarChart();
        }else {
            paintMoodLineChart();
            paintMoodColumnarChart();
        }
    }

    @Override
    protected void initContentViewData() { }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_date_analysis;
    }

    @Override
    protected Context getContext() {
        return this;
    }

    @Override
    public void initView() {
        initToolbar();
        // 设置通知栏颜色
        SystemResHelper.setStateBarColor(this);

        LineChartCl = findViewById(R.id.line_chart_cl);
        ColumnarChartCl = findViewById(R.id.columnar_chart_cl);
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("arg");
        model = new DateAnalysisModel(getContext());

        // 获取图表需要展示的数据
        if ("plan".equals(tag)) {
            model.getPlanLineChartDate(new DateAnalysisModel.OnGetPlanDateListener() {
                @Override
                public void onSuccess(ArrayList<String> xValues, ArrayList<int[]> yValues) {
                    planLineChartXValues = xValues;
                    planLineChartYValues = yValues;
                }
            });
            model.getPlanColumnarChartDate(new DateAnalysisModel.OnGetPlanDateListener() {
                @Override
                public void onSuccess(ArrayList<String> xValues, ArrayList<int[]> yValues) {
                    planColumnarChartXValues = xValues;
                    planColumnarChartYValues = yValues;
                }
            });
        }else {
            model.getMoodLineChartDate(new DateAnalysisModel.OnGetMoodDataListener() {
                @Override
                public void onSuccess(ArrayList<String> xValues, ArrayList<int[][]> yValues) {
                    moodLineChartXValues = xValues;
                    moodLineChartYValues = yValues;
                }
            });
            model.getMoodColumnarChartDate(new DateAnalysisModel.OnGetMoodDataListener() {
                @Override
                public void onSuccess(ArrayList<String> xValues, ArrayList<int[][]> yValues) {
                    moodColumnarChartXValues = xValues;
                    moodColumnarChartYValues = yValues;
                }
            });
        }

    }

    @Override
    protected void initListener() { }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DateAnalysisActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化toolbar控件
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("数据分析");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 设置显示每个图的时间
     */
    private void setChartDate() {
        setChartDate(true, DateUtil.getDateStr("yyyy-MM"));
        setChartDate(false, DateUtil.getDateStr("yyyy年"));
    }

    /**
     * 绘制计划折线图
     */
    private void paintPlanLineChart() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series1 = new TimeSeries(planLineTitles[0]);
        TimeSeries series2 = new TimeSeries(planLineTitles[1]);
        for(int i = 0; i < planLineChartXValues.size(); i++) {
            // 这里add的x和下面设置x标签addXTextLabel添加的x对应
            series1.add(i, planLineChartYValues.get(i)[0]);
            series2.add(i, planLineChartYValues.get(i)[1]);
        }
        // 添加数据集
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        // 渲染曲线
        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        XYSeriesRenderer renderer2 = new XYSeriesRenderer();
        setLineRenderer(renderer1, Color.BLUE);
        setLineRenderer(renderer2, Color.RED);
        // 渲染坐标轴
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setLineChartSettings(renderer);
        setPlanLineChartXTextLabel(renderer);

        renderer.addSeriesRenderer(renderer1);
        renderer.addSeriesRenderer(renderer2);

        // 将图像add到根布局中
        GraphicalView view = ChartFactory.getLineChartView(getContext(), dataset, renderer);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        params.topToBottom = R.id.date_start_desc_tv;
        params.topMargin = 50;
        LineChartCl.addView(view, 0, params);
    }

    /**
     * 设置折线图横轴标签
     * @param renderer
     */
    public void setPlanLineChartXTextLabel(XYMultipleSeriesRenderer renderer) {
        for(int i = 0; i < 7; i++) {
            renderer.addXTextLabel(i, planLineChartXValues.get(i));
        }
    }

    /**
     * 配置折线图渲染器（主要坐标轴）
     * @param renderer
     */
    public void setLineChartSettings(XYMultipleSeriesRenderer renderer) {
        // 设置为X轴的标题
        renderer.setXTitle("日期");
        // 设置y轴的标题
        renderer.setYTitle("数量");
        // 设置轴刻度文字大小
        renderer.setLabelsTextSize(30);
        // 设置轴标题文本大小
        renderer.setAxisTitleTextSize(30);
        // 设置图表标题
        renderer.setChartTitle("");
        // 设置图表标题文字的大小
        renderer.setChartTitleTextSize(40);
        //设置图例文字大小
        renderer.setLegendTextSize(30);
        // 设置y轴最小值是0
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(10);
        // 设置Y轴刻度个数
        renderer.setYLabels(5);
        // 取消X坐标的数字zjk,只有自己定义横坐标是才设为此值
        renderer.setXLabels(0);
        renderer.setXAxisMin(-1);
        renderer.setXAxisMax(7);
        // 显示网格
        renderer.setShowGrid(true);
        // 设置为不可平移
        renderer.setPanEnabled(false, false);
        // 设置为不可缩放
        renderer.setZoomEnabled(false, false);

        // 刻度值相对于刻度的位置
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        //设置图形四周的留白
        renderer.setMargins(new int[]{110,70,150,80});
        // 设置点的大小
        renderer.setPointSize(5);

        setCoordinateBasicColor(renderer);
    }

    /**
     * 曲线渲染
     * @param renderer
     * @param color
     */
    private void setLineRenderer(XYSeriesRenderer renderer, int color) {
        // 设置颜色
        renderer.setColor(color);
        // 设置点的样式
        renderer.setPointStyle(PointStyle.CIRCLE);
        // 填充点（显示的点是空心还是实心）
        renderer.setFillPoints(true);
        // 将点的值显示出来
        renderer.setDisplayChartValues(true);
        // 显示的点的值与图的距离
        renderer.setChartValuesSpacing(10);
        // 点的值的文字大小
        renderer.setChartValuesTextSize(25);
        // 将点的值显示出来
        renderer.setDisplayChartValues(true);
        // 设置线宽
        renderer.setLineWidth(3);
    }

    /**
     * @param flag true显示lineChartText，反之显示columnarChartText
     * @param showText
     */
    private void setChartDate(boolean flag, String showText) {
        if(flag) {
            ArrayList<String> weeks = DateUtil.getCurrentWeeksDate("yyyy-MM-dd");
            TextView tv1 = LineChartCl.findViewById(R.id.date_start_desc_tv);
            TextView tv2 = LineChartCl.findViewById(R.id.date_end_desc_tv);
            tv1.setText(weeks.get(0));
            tv2.setText(weeks.get(6));
        }else {
            TextView tv2 = ColumnarChartCl.findViewById(R.id.month_desc_tv);
            tv2.setText(showText);
        }
    }

    /**
     * 绘制柱状图
     */
    private void paintPlanColumnarChart() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        CategorySeries series1 = new CategorySeries(planLineTitles[0]);
        CategorySeries series2 = new CategorySeries(planLineTitles[1]);
        for(int i = 0; i < planColumnarChartXValues.size(); i++) {
            series1.add(planColumnarChartYValues.get(i)[0]);
            series2.add(planColumnarChartYValues.get(i)[1]);
        }
        //添加数据集
        dataset.addSeries(series1.toXYSeries());
        dataset.addSeries(series2.toXYSeries());
        //渲染曲线
        SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer ();
        SimpleSeriesRenderer  renderer2 = new SimpleSeriesRenderer ();
        setColumnarRenderer(renderer1, Color.BLUE, Paint.Align.RIGHT);
        setColumnarRenderer(renderer2, Color.RED, Paint.Align.LEFT);
        //渲染坐标轴
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setColumnarChartSettings(renderer);
        setPlanColumnarChartXTextLabel(renderer);

        renderer.addSeriesRenderer(renderer1);
        renderer.addSeriesRenderer(renderer2);

        GraphicalView view = ChartFactory.getBarChartView(getContext(), dataset, renderer, null);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        params.topToBottom = R.id.month_desc_tv;
        params.topMargin = 50;
        ColumnarChartCl.addView(view, 0, params);
    }

    /**
     * 设置单个柱状图渲染器
     * @param renderer
     * @param color
     */
    public void setColumnarRenderer(SimpleSeriesRenderer  renderer, int color, Paint.Align align) {
        renderer.setColor(color);
        renderer.setDisplayChartValues(true);
        renderer.setChartValuesTextAlign(align);
        renderer.setChartValuesTextSize(20);
    }

    /**
     * 柱状图渲染
     * @param renderer
     */
    public void setColumnarChartSettings(XYMultipleSeriesRenderer renderer) {
        // 设置图表标题
        renderer.setChartTitle("");
        // 设置图表标题文字的大小
        renderer.setChartTitleTextSize(40);
        // 设置为X轴的标题
        renderer.setXTitle("月份");
        // 设置X轴的最小最大数字和刻度数量
        renderer.setXLabels(0);
        renderer.setXAxisMin(0);
        renderer.setXAxisMax(13);
        // 设置为Y轴的标题
        renderer.setYTitle("数量/个");
        // 设置Y轴的最小数字和最大数字
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(100);
        renderer.setYLabels(5);
        // 设置轴标题文本大小
        renderer.setAxisTitleTextSize(30);
        // 设置轴刻度文字大小
        renderer.setLabelsTextSize(30);
        //设置图例文字大小
        renderer.setLegendTextSize(30);
        // 刻度值相对于刻度的位置
        renderer.setYLabelsAlign(Paint.Align.RIGHT);

        // 消除锯齿
        renderer.setAntialiasing(true);
        // 柱子间宽度,值越大，柱子越窄
        renderer.setBarSpacing(1);
        // 显示网格
        renderer.setShowGrid(true);
        // 设置为不可平移
        renderer.setPanEnabled(false, false);
        // 设置为不可缩放
        renderer.setZoomEnabled(false, false);
        //设置图形四周的留白 上  左  下 右
        renderer.setMargins(new int[]{100,80,110,70});

        setCoordinateBasicColor(renderer);

    }

    /**
     * 设置柱状图横轴坐标
     * @param renderer
     */
    private void setPlanColumnarChartXTextLabel(XYMultipleSeriesRenderer renderer) {
        for(int i = 1; i < 13; i++) {
            //这里的i和坐标轴上的刻度对应，设置从1开始，空出0刻度
            renderer.addXTextLabel(i, planColumnarChartXValues.get(i - 1));
        }
    }

    /**
     * 设置坐标轴基础色
     * @param renderer
     */
    private void setCoordinateBasicColor(XYMultipleSeriesRenderer renderer) {
        Resources resources = getResources();
        renderer.setMarginsColor(resources.getColor(R.color.mainActivityFuncGridBg));
        renderer.setGridColor(resources.getColor(R.color.axes));
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(resources.getColor(R.color.mainActivityFuncGridBg));
        renderer.setAxesColor(resources.getColor(R.color.axes));
        renderer.setXLabelsColor(resources.getColor(R.color.black));
        renderer.setYLabelsColor(0, resources.getColor(R.color.black));
    }




    /**
     * 绘制心情折线图
     */
    private void paintMoodLineChart() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series1 = new TimeSeries(moodLineTitles[0]);
        TimeSeries series2 = new TimeSeries(moodLineTitles[1]);
        TimeSeries series3 = new TimeSeries(moodLineTitles[2]);
        TimeSeries series4 = new TimeSeries(moodLineTitles[3]);
        for(int i = 0; i < moodLineChartXValues.size(); i++) {
            // 这里add的x和下面设置x标签addXTextLabel添加的x对应
            series1.add(i, moodLineChartYValues.get(i)[0][0]);
            series2.add(i, moodLineChartYValues.get(i)[0][1]);
            series3.add(i, moodLineChartYValues.get(i)[0][2]);
            series4.add(i, moodLineChartYValues.get(i)[0][3]);
        }
        // 添加数据集
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        // 渲染曲线
        XYSeriesRenderer renderer1 = new XYSeriesRenderer();
        XYSeriesRenderer renderer2 = new XYSeriesRenderer();
        XYSeriesRenderer renderer3 = new XYSeriesRenderer();
        XYSeriesRenderer renderer4 = new XYSeriesRenderer();
        setLineRenderer(renderer1, Color.BLUE);
        setLineRenderer(renderer2, Color.RED);
        setLineRenderer(renderer3, Color.YELLOW);
        setLineRenderer(renderer4, Color.WHITE);
        // 渲染坐标轴
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setLineChartSettings(renderer);
        setMoodLineChartXTextLabel(renderer);

        renderer.addSeriesRenderer(renderer1);
        renderer.addSeriesRenderer(renderer2);
        renderer.addSeriesRenderer(renderer3);
        renderer.addSeriesRenderer(renderer4);

        // 将图像add到根布局中
        GraphicalView view = ChartFactory.getLineChartView(getContext(), dataset, renderer);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        params.topToBottom = R.id.date_start_desc_tv;
        params.topMargin = 50;
        LineChartCl.addView(view, 0, params);
    }

    /**
     * 绘制心情柱状图
     */
    private void paintMoodColumnarChart() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        CategorySeries series1 = new CategorySeries(moodLineTitles[0]);
        CategorySeries series2 = new CategorySeries(moodLineTitles[1]);
        CategorySeries series3 = new CategorySeries(moodLineTitles[2]);
        CategorySeries series4 = new CategorySeries(moodLineTitles[3]);
        for(int i = 0; i < moodColumnarChartXValues.size(); i++) {
            series1.add(moodColumnarChartYValues.get(i)[0][0]);
            series2.add(moodColumnarChartYValues.get(i)[0][1]);
            series3.add(moodColumnarChartYValues.get(i)[0][2]);
            series4.add(moodColumnarChartYValues.get(i)[0][3]);
        }
        //添加数据集
        dataset.addSeries(series1.toXYSeries());
        dataset.addSeries(series2.toXYSeries());
        dataset.addSeries(series3.toXYSeries());
        dataset.addSeries(series4.toXYSeries());
        //渲染曲线
        SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer ();
        SimpleSeriesRenderer  renderer2 = new SimpleSeriesRenderer ();
        SimpleSeriesRenderer  renderer3 = new SimpleSeriesRenderer ();
        SimpleSeriesRenderer  renderer4 = new SimpleSeriesRenderer ();
        setColumnarRenderer(renderer1, Color.BLUE, Paint.Align.RIGHT);
        setColumnarRenderer(renderer2, Color.RED, Paint.Align.LEFT);
        setColumnarRenderer(renderer3, Color.YELLOW, Paint.Align.RIGHT);
        setColumnarRenderer(renderer4, Color.WHITE, Paint.Align.RIGHT);
        //渲染坐标轴
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setColumnarChartSettings(renderer);
        setMoodColumnarChartXTextLabel(renderer);

        renderer.addSeriesRenderer(renderer1);
        renderer.addSeriesRenderer(renderer2);
        renderer.addSeriesRenderer(renderer3);
        renderer.addSeriesRenderer(renderer4);

        GraphicalView view = ChartFactory.getBarChartView(getContext(), dataset, renderer, null);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        params.topToBottom = R.id.month_desc_tv;
        params.topMargin = 50;
        ColumnarChartCl.addView(view, 0, params);
    }

    /**
     * 设置折线图横轴标签
     * @param renderer
     */
    private void setMoodLineChartXTextLabel(XYMultipleSeriesRenderer renderer) {
        for(int i = 0; i < 7; i++) {
            renderer.addXTextLabel(i, moodLineChartXValues.get(i));
        }
    }

    /**
     * 设置柱状图横轴坐标
     * @param renderer
     */
    private void setMoodColumnarChartXTextLabel(XYMultipleSeriesRenderer renderer) {
        for(int i = 1; i < 13; i++) {
            //这里的i和坐标轴上的刻度对应，设置从1开始，空出0刻度
            renderer.addXTextLabel(i, moodColumnarChartXValues.get(i - 1));
        }
    }

}
