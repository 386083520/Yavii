package com.example.gsd.yavii.Fragment;

import android.animation.PropertyValuesHolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.example.gsd.yavii.R;
import com.example.gsd.yavii.WaveActivity;
import com.example.gsd.yavii.utils.Contants;
import com.example.gsd.yavii.utils.HttpUtils;

/**e
 * Created by gsd on 2016/9/15.
 */
public class LineFragment extends Fragment {
    private LineChartView mChartThree;
    private ImageButton mPlayThree;
    SharedPreferences sp;

    int timeType=24;

    String type;
    private boolean mUpdateThree;

   public static float max=5;

    public   String[] mLabelsThreeDay= {"1点", "2点", "3点", "4点", "5点", "6点", "7点", "8点", "9点", "10点", "11点", "12点","13点","14点","15点","16点","17点","18点","19点","20点","21点","22点","23点","24点"};
    public  float[] mValuesThreeDay = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public  String[] mLabelsThreeMonth= {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12","13","14","15","16","17","18","19",
            "20","21","22","23","24","25","26","27","28","29","30日"};
    public  float[] mValuesThreeMonth = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    public  String[] mLabelsThreeYear= {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    public  float[] mValuesThreeYear = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getActivity().getSharedPreferences("gsd",Context.MODE_PRIVATE);

        timeType=sp.getInt("timeType",12);

        this.setHasOptionsMenu(true);
        if(timeType==12){
            type="year";
        }else if(timeType==30){
            type="month";
        }else if(timeType==24){
            type="day";
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.line, container, false);

        mUpdateThree = true;
        mChartThree = (LineChartView) layout.findViewById(R.id.linechart3);
        mPlayThree = (ImageButton) layout.findViewById(R.id.play3);
        mPlayThree.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                GetAvgValueThread1 thread3=new GetAvgValueThread1();
                thread3.start();

            }
        });
        GetAvgValueThread1 thread1=new GetAvgValueThread1();
        thread1.start();
        GetAvgValueThread1 thread2=new GetAvgValueThread1();
        thread2.start();
        //showChart(2, mChartThree, mPlayThree);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        showChart(2, mChartThree, mPlayThree);
    }

    private void showChart(final int tag, final LineChartView chart, final ImageButton btn){
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

        switch(tag){
            case 0:
                 break;
            case 1:

            case 2:
                produceThree(chart, action); break;
            default:
        }

    }
    private void dismissChart(final int tag, final LineChartView chart, final ImageButton btn){

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
                break;
            case 1:
                 break;
            case 2:
                dismissThree(chart, action); break;
            default:
        }

    }

    private void showPlay(ImageButton btn){
        btn.setEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(1).scaleX(1).scaleY(1);
        else
            btn.setVisibility(View.VISIBLE);
    }

    private void dismissPlay(ImageButton btn){
        btn.setEnabled(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(0).scaleX(0).scaleY(0);
        else
            btn.setVisibility(View.INVISIBLE);
    }

    public void produceThree(LineChartView chart, Runnable action){

        Tooltip tip = new Tooltip(getActivity(), R.layout.linechart_three_tooltip, R.id.value);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));

            tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA,0),
                    PropertyValuesHolder.ofFloat(View.SCALE_X,0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y,0f));
        }

        chart.setTooltips(tip);

        LineSet dataset = null;
        if(timeType==12){
            dataset = new LineSet(mLabelsThreeYear, mValuesThreeYear);
        }else if(timeType==24){
            dataset = new LineSet(mLabelsThreeDay, mValuesThreeDay);
        }else if(timeType==30){
            dataset = new LineSet(mLabelsThreeMonth, mValuesThreeMonth);
        }

        dataset.setColor(Color.parseColor("#FF58C674"))
                .setDotsStrokeThickness(Tools.fromDpToPx(2))
                .setDotsStrokeColor(Color.parseColor("#FF58C674"))
                .setDotsColor(Color.parseColor("#eef1f6"));
        chart.addData(dataset);





        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#308E9196"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));

        chart.setBorderSpacing(1)
                .setAxisBorderValues(0, (int)max+3, 1)
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#FF8E9196"))
                .setXAxis(false)
                .setYAxis(false)
                .setStep((int)max/5+1)
                .setBorderSpacing(Tools.fromDpToPx(5))
                .setGrid(ChartView.GridType.VERTICAL, gridPaint);

        Animation anim = new Animation().setEndAction(action);

        chart.show(anim);
    }

    public void updateThree(LineChartView chart){
        chart.dismissAllTooltips();

        if(timeType==12){
            chart.updateValues(0, mValuesThreeYear);
        }else if(timeType==24){
            chart.updateValues(0, mValuesThreeDay);
        }else if(timeType==30){
            chart.updateValues(0, mValuesThreeMonth);
        }
        chart.notifyDataUpdate();
    }

    private void updateChart(final int tag, final LineChartView chart, ImageButton btn){

        dismissPlay(btn);

        switch(tag){
            case 0:
                break;
            case 1:
                 break;
            case 2:
                updateThree(chart); break;
            default:
        }
    }

    public static void dismissThree(LineChartView chart, Runnable action){
        chart.dismissAllTooltips();
        chart.dismiss(new Animation().setEndAction(action));
    }


    public class GetAvgValueThread1 extends Thread{
        @Override
        public void run() {

                //String getChannelString = "equipId=" + equipId;
                String getCollectAvgString="equipId="+WaveActivity.equipId+"&channelId="+WaveActivity.channelId+"&type="+ WaveActivity.theType;
                String url = Contants.BASE_URL + Contants.COLLECT_VALUE_AVG_URL + getCollectAvgString;
                String result = HttpUtils.getHttpGetResultForUrl(url);
                if(result.substring(0,2).equals("1,")){
                    Message msg = new Message();
                    Bundle b=new Bundle();
                    b.putString("result",result);

                    msg.setData(b);
                    msg.what=89;

                    handler.sendMessage(msg);
                }


            }


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==89){
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
                Log.i("ListLength",mValuesThreeDay.length+"-"+mValuesThreeMonth.length+"-"+mValuesThreeYear.length);


                // showChart(2, mChartThree, mPlayThree);
                if(mUpdateThree){
                    updateChart(2, mChartThree, mPlayThree);
                }

                else{
                    dismissChart(2, mChartThree, mPlayThree);
                }

                mUpdateThree = !mUpdateThree;

            }
        }
    };


}
