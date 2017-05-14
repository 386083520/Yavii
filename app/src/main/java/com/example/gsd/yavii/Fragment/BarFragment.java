package com.example.gsd.yavii.Fragment;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.example.gsd.yavii.R;
import com.example.gsd.yavii.WaveActivity;
import com.example.gsd.yavii.utils.Contants;
import com.example.gsd.yavii.utils.HttpUtils;


import java.util.ArrayList;


public class BarFragment extends Fragment {


    /** First chart */
    private BarChartView mChartOne;
    private ImageButton mPlayOne;
    private boolean mUpdateOne;
    SharedPreferences sp;
    int timeType=12;
    public static float max=5;
    public   String[] mLabelsThreeDay= {"1点", "2点", "3点", "4点", "5点", "6点", "7点", "8点", "9点", "10点", "11点", "12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点"};
    public  float[] mValuesThreeDay = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public  String[] mLabelsThreeMonth= {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15","16","17","18","19",
            "20","21","22","23","24","25","26","27","28","29","30日"};
    public  float[] mValuesThreeMonth = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public  String[] mLabelsThreeYear= {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    public  float[] mValuesThreeYear = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};


    public BarFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getActivity().getSharedPreferences("gsd", Context.MODE_PRIVATE);
        timeType=sp.getInt("timeType",12);
        this.setHasOptionsMenu(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.bar, container, false);

        // Init first chart
        mUpdateOne = true;
        mChartOne = (BarChartView) layout.findViewById(R.id.barchart1);
        mPlayOne = (ImageButton) layout.findViewById(R.id.play1);
        mPlayOne.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                GetAvgValueThread2 thread2=new GetAvgValueThread2();
                thread2.start();


            }
        });

        GetAvgValueThread2 thread2=new GetAvgValueThread2();
        thread2.start();
        GetAvgValueThread2 thread3=new GetAvgValueThread2();
        thread3.start();


        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        showChart(0, mChartOne, mPlayOne);
    }


    /**
     * Show a CardView chart
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart   Chart view
     * @param btn    Play button
     */
    private void showChart(final int tag, final ChartView chart, final ImageButton btn){
        dismissPlay(btn);
        Runnable action =  new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showPlay(btn);
                    }
                }, 500);
            }
        };

        switch(tag) {
            case 0:
                produceOne(chart, action); break;
            case 1:
                break;
            case 2:
                 break;
            default:
        }

    }


    /**
     * Update the values of a CardView chart
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart   Chart view
     * @param btn    Play button
     */
    private void updateChart(final int tag, final ChartView chart, ImageButton btn){

        dismissPlay(btn);

        switch(tag){
            case 0:
                updateOne(chart); break;
            case 1:
                 break;
            case 2:
                break;
            default:
        }
    }


    /**
     * Dismiss a CardView chart
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart   Chart view
     * @param btn    Play button
     */
    private void dismissChart(final int tag, final ChartView chart, final ImageButton btn){

        dismissPlay(btn);

        Runnable action =  new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showPlay(btn);
                        showChart(tag, chart, btn);
                    }
                }, 500);
            }
        };

        switch(tag){
            case 0:
                dismissOne(chart, action); break;
            case 1:
                 break;
            case 2:
                break;
            default:
        }
    }


    /**
     * Show CardView play button
     * @param btn    Play button
     */
    private void showPlay(ImageButton btn){
        btn.setEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(1).scaleX(1).scaleY(1);
        else
            btn.setVisibility(View.VISIBLE);
    }


    /**
     * Dismiss CardView play button
     * @param btn    Play button
     */
    private void dismissPlay(ImageButton btn){
        btn.setEnabled(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(0).scaleX(0).scaleY(0);
        else
            btn.setVisibility(View.INVISIBLE);
    }



    /**
     *
     * Chart 1
     *
     */

    public void produceOne(ChartView chart, Runnable action){
        BarChartView barChart = (BarChartView) chart;

        barChart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                System.out.println("OnClick "+rect.left);
            }
        });

        Tooltip tooltip = new Tooltip(getActivity(), R.layout.barchart_one_tooltip);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1));
            tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0));
        }
        barChart.setTooltips(tooltip);

        BarSet barSet =null;
        if(timeType==12){
            barSet = new BarSet(mLabelsThreeYear, mValuesThreeYear);
        }else if(timeType==24){
            barSet = new BarSet(mLabelsThreeDay, mValuesThreeDay);
        }else if(timeType==30){
            barSet = new BarSet(mLabelsThreeMonth, mValuesThreeMonth);
        }
        barSet.setColor(Color.parseColor("#c33232"));
        barChart.addData(barSet);


        barChart.setSetSpacing(Tools.fromDpToPx(-15));
        barChart.setBarSpacing(Tools.fromDpToPx(35));
        barChart.setRoundCorners(Tools.fromDpToPx(2));

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#8986705C"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        barChart.setBorderSpacing(5)
                .setAxisBorderValues(0,(int)(max/10+1)*10, 2)
                .setGrid(BarChartView.GridType.FULL, gridPaint)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#86705c"))
                .setAxisColor(Color.parseColor("#86705c"));



        final Runnable auxAction = action;
        Runnable chartOneAction = new Runnable() {
            @Override
            public void run() {
                showTooltipOne();
                auxAction.run();
            }
        };
        if(timeType==12){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11};
            barChart.show(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(chartOneAction));
        }else if(timeType==24){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
            barChart.show(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(chartOneAction));
        }else if(timeType==30){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};
            barChart.show(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(chartOneAction));
        }

        //.show()
        ;
    }

    public void updateOne(ChartView chart){

        dismissTooltipOne();


        if(timeType==12){
            chart.updateValues(0, mValuesThreeYear);
        }else if(timeType==24){
            chart.updateValues(0, mValuesThreeDay);
        }else if(timeType==30){
            chart.updateValues(0, mValuesThreeMonth);
        }
        chart.notifyDataUpdate();
    }

    public void dismissOne(ChartView chart, Runnable action){

        dismissTooltipOne();
        if(timeType==12){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11};
            chart.dismiss(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(action));
        }else if(timeType==24){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
            chart.dismiss(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(action));
        }else if(timeType==30){
            int[] order = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};
            chart.dismiss(new Animation()
                    .setOverlap(.5f, order)
                    .setEndAction(action));
        }

    }


    private void showTooltipOne(){

        ArrayList<ArrayList<Rect>> areas = new ArrayList<>();
        areas.add(mChartOne.getEntriesArea(0));


        for(int i = 0; i < areas.size(); i++) {
            for (int j = 0; j < areas.get(i).size(); j++) {

                Tooltip tooltip = new Tooltip(getActivity(), R.layout.barchart_one_tooltip, R.id.value);

                if(timeType==12){
                    tooltip.prepare(areas.get(i).get(j), mValuesThreeYear[j]);
                }else if(timeType==24){
                    tooltip.prepare(areas.get(i).get(j), mValuesThreeDay[j]);
                }else if(timeType==30){
                    tooltip.prepare(areas.get(i).get(j), mValuesThreeMonth[j]);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1));
                    tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0));
                }
                mChartOne.showTooltip(tooltip, true);
            }
        }

    }


    private void dismissTooltipOne(){
        mChartOne.dismissAllTooltips();
    }


    public class GetAvgValueThread2 extends Thread{
        @Override
        public void run() {

                //String getChannelString = "equipId=" + equipId;
                String getCollectAvgString="equipId="+ WaveActivity.equipId+"&channelId="+WaveActivity.channelId+"&type="+ WaveActivity.theType;
                String url = Contants.BASE_URL + Contants.COLLECT_VALUE_AVG_URL + getCollectAvgString;
                String result = HttpUtils.getHttpGetResultForUrl(url);

                Message msg = new Message();
            Bundle b=new Bundle();
            b.putString("result",result);
            msg.setData(b);
                msg.what=88;
                handler.sendMessage(msg);
            }


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==88){
                Bundle b=msg.getData();
                String result=b.getString("result");
                String resultArr[]=result.split(";");
                if(WaveActivity.theType=="year"){
                    for(int i=0;i<12;i++){
                        String yearValue[]=resultArr[i].split(",");
                        mValuesThreeYear[i]=Float.parseFloat(yearValue[1]);
                    }
                    max=mValuesThreeYear[0];
                    for(int i=0;i<mValuesThreeYear.length;i++){
                        if(mValuesThreeYear[i]>max){
                            max=mValuesThreeYear[i];
                        }
                    }
                }else if(WaveActivity.theType=="month"){
                    for(int i=0;i<30;i++){
                        String monthValue[]=resultArr[i].split(",");
                        mValuesThreeMonth[i]=Float.parseFloat(monthValue[1]);
                    }
                    max=mValuesThreeMonth[0];
                    for(int i=0;i<mValuesThreeMonth.length;i++){
                        if(mValuesThreeMonth[i]>max){
                            max=mValuesThreeMonth[i];
                        }
                    }
                }else if(WaveActivity.theType=="day"){
                    for(int i=0;i<24;i++){
                        String dayValue[]=resultArr[i].split(",");
                        mValuesThreeDay[i]=Float.parseFloat(dayValue[1]);
                    }
                    max=mValuesThreeDay[0];
                    for(int i=0;i<mValuesThreeDay.length;i++){
                        if(mValuesThreeDay[i]>max){
                            max=mValuesThreeDay[i];
                        }
                    }
                }

                if(mUpdateOne)
                    updateChart(0, mChartOne, mPlayOne);
                else {
                    dismissChart(0, mChartOne, mPlayOne);
                }
                mUpdateOne = !mUpdateOne;
            }
        }
    };



}
