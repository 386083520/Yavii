package com.example.gsd.yavii;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsd.yavii.SnappingStep.SnappingStepper;
import com.example.gsd.yavii.SnappingStep.listener.SnappingStepperValueChangeListener;
import com.example.gsd.yavii.utils.Contants;
import com.example.gsd.yavii.utils.HttpUtils;
import com.example.gsd.yavii.utils.Utils;

import java.sql.Timestamp;

public class MainActivity extends Activity implements OnClickListener,SnappingStepperValueChangeListener {
	private String equipId, channelId;
	int max,theChannelNumber;
	float returnCapactiy;
	String returnUnit,returnTime="";
	boolean isTrue=true;
	private float degree = 0.0f;
	private ImageView noodle;
	private Button control_bt1,control_bt2;
	private LinearLayout btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	SharedPreferences.Editor ed;
	SharedPreferences sp;
	private TextView control_tv1,control_tv2,showTv1, showTv2, showTv3, showTv4, showTv5, showTv6,
			showTv7, showTv8,stateTv1;
    private SnappingStepper stepperCustom1,stepperCustom2;
	private TextView updateTimeTv,now_channel_button, the_equip, now_value, max_value,
			now_channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui);
		Intent intent = getIntent();
		equipId = intent.getStringExtra("dercice_id");
		initView();
		sp=getSharedPreferences("gsd2", Context.MODE_PRIVATE);
		ed=sp.edit();
		the_equip.setText(equipId);
		channelId = "1";
		isTrue=true;

	}



	public void initView() {
		stateTv1= (TextView) findViewById(R.id.stateTv1);

		updateTimeTv= (TextView) findViewById(R.id.updateTimeTv);
		control_bt1=(Button) findViewById(R.id.control_bt1);
		control_bt2=(Button) findViewById(R.id.control_bt2);
		noodle = (ImageView) findViewById(R.id.iv_needle);
		the_equip = (TextView) findViewById(R.id.the_number);
		control_tv1=(TextView) findViewById(R.id.control_tv1);
		control_tv2=(TextView) findViewById(R.id.control_tv2);
		now_value = (TextView) findViewById(R.id.now_value);
		max_value = (TextView) findViewById(R.id.max_value);
		now_channel = (TextView) findViewById(R.id.now_channel);
		stepperCustom1= (SnappingStepper) findViewById(R.id.stepperCustom1);
		stepperCustom2= (SnappingStepper) findViewById(R.id.stepperCustom2);
		stepperCustom1.setOnValueChangeListener(this);
		stepperCustom2.setOnValueChangeListener(this);
		btn1 = (LinearLayout) findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		btn2 = (LinearLayout) findViewById(R.id.btn2);
		btn2.setOnClickListener(this);
		btn3 = (LinearLayout) findViewById(R.id.btn3);
		btn3.setOnClickListener(this);
		btn4 = (LinearLayout) findViewById(R.id.btn4);
		btn4.setOnClickListener(this);
		btn5 = (LinearLayout) findViewById(R.id.btn5);
		btn5.setOnClickListener(this);
		btn6 = (LinearLayout) findViewById(R.id.btn6);
		btn6.setOnClickListener(this);
		btn7 = (LinearLayout) findViewById(R.id.btn7);
		btn7.setOnClickListener(this);
		btn8 = (LinearLayout) findViewById(R.id.btn8);
		btn8.setOnClickListener(this);
		now_channel_button = (TextView) findViewById(R.id.now_channel);
		now_channel_button.setOnClickListener(this);
		showTv1 = (TextView) findViewById(R.id.showTv1);
		showTv2 = (TextView) findViewById(R.id.showTv2);
		showTv3 = (TextView) findViewById(R.id.showTv3);
		showTv4 = (TextView) findViewById(R.id.showTv4);
		showTv5 = (TextView) findViewById(R.id.showTv5);
		showTv6 = (TextView) findViewById(R.id.showTv6);
		showTv7 = (TextView) findViewById(R.id.showTv7);
		showTv8 = (TextView) findViewById(R.id.showTv8);
		startThread();
	}

	private void startThread() {
		MyThread channelOneThread = new MyThread("1", "0");
		channelOneThread.start();
		MyThread channelTwoThread = new MyThread("2", "0");
		channelTwoThread.start();
		MyThread channelThreeThread = new MyThread("3", "0");
		channelThreeThread.start();
		MyThread channelFourThread = new MyThread("4", "0");
		channelFourThread.start();
		if(theChannelNumber==8){
			MyThread channelFiveThread = new MyThread("5", "0");
			channelFiveThread.start();
			MyThread channelSexThread = new MyThread("6", "0");
			channelSexThread.start();
			MyThread channelSevenThread = new MyThread("7", "0");
			channelSevenThread.start();
			MyThread channelEightThread = new MyThread("8", "0");
			channelEightThread.start();
		}
	}

	@Override
	public void onClick(View v) {

		Toast toast=Toast.makeText(MainActivity.this, "正在获取，请稍等...", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.show();
		switch (v.getId()) {
		
		case R.id.btn1:
			channelId = "1";
			break;
		case R.id.btn2:
			channelId = "2";
			break;
		case R.id.btn3:
			channelId = "3";
			break;
		case R.id.btn4:
			channelId = "4";
			break;
		case R.id.btn5:
			channelId = "5";
			break;
		case R.id.btn6:
			channelId = "6";
			break;
		case R.id.btn7:
			channelId = "7";
			break;
		case R.id.btn8:
			channelId = "8";
			break;
		}
	}

	@Override
	public void onValueChange(View view, int value) {
		String controlCode="";
        switch (view.getId()){
			case R.id.stepperCustom1:
                //TODO
				if(control_bt1.getText().toString().equals("通道0-关")){
					//TODO
					controlCode="DO=0_1000_"+(100-Integer.parseInt(String.valueOf(stepperCustom1.getValue())))*10+"_";
					int channel=Contants.CONTROL_SUCCESS1;
					ControlThread2 thread1=new ControlThread2(controlCode, channel);
					thread1.start();
				}else{
					Utils.showToast(MainActivity.this,"无法调节亮度，请先打开开关");
				}

				break;
			case R.id.stepperCustom2:
				if(control_bt2.getText().toString().equals("通道1-关")){
					//controlCode="DO=1_1000_1000_";
					controlCode="DO=1_1000_"+(100-Integer.parseInt(String.valueOf(stepperCustom2.getValue())))*10+"_";
					int channel2=Contants.CONTROL_SUCCESS2;
					ControlThread2 thread2=new ControlThread2(controlCode, channel2);
					thread2.start();
				}else{
					Utils.showToast(MainActivity.this,"无法调节亮度，请先打开开关");
				}

				break;
		}
	}

	public class MyThread extends Thread {
		private String channelId;
		private String passChannelId;

		public MyThread(String channelId, String passChannelId) {
			super();
			this.channelId = channelId;
			this.passChannelId = passChannelId;
		}

		@Override
		public void run() {
			while (isTrue) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				Bundle b = new Bundle();
				msg.what = Integer.parseInt(channelId)
						+ Integer.parseInt(passChannelId);
				String collectString = "equipId=" + equipId + "&channelId="
						+ channelId;
				String url = Contants.BASE_URL + Contants.COLLECT_VALUE_URL
						+ collectString;
				String result = HttpUtils.getHttpGetResultForUrl(url);
				if (result == null) {
                    
				} else if (",".equals(result.substring(result.length() - 1,
						result.length()))) {
					String[] collectValues = result.split(",");
					String capacity = collectValues[0];
					String unit = collectValues[1];
					b.putFloat("capacity", Float.parseFloat(capacity));
					b.putString("unit", unit);
					msg.setData(b);
					handler.sendMessage(msg);
				} else {

				}
			}
		}
	}
	
	public class MyThread2 extends Thread {
		@Override
		public void run() {
			while (isTrue) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = new Message();
				Bundle b = new Bundle();
				msg.what = Integer.parseInt(channelId)+Integer.parseInt("10");
				String collectString = "equipId=" + equipId + "&channelId="
						+ channelId;
				String url = Contants.BASE_URL + Contants.COLLECT_VALUE_URL
						+ collectString;
				String result = HttpUtils.getHttpGetResultForUrl(url);
				if (result == null) {

				} else if (",".equals(result.substring(result.length() - 1,
						result.length()))) {
					String[] collectValues = result.split(",");
					String capacity = collectValues[0];
					String unit = collectValues[1];
					String updateTime=collectValues[2];
					b.putFloat("capacity", Float.parseFloat(capacity));
					b.putString("unit", unit);
					b.putString("updateTime",updateTime);
					msg.setData(b);
					handler.sendMessage(msg);
				} else {

				}
			}
		}
	}
	
	public class MyThread3 extends Thread{
		@Override
		public void run() {
			Message msg = new Message();
			Bundle b = new Bundle();
			
			String getChannelString = "equipId=" + equipId;
			String url = Contants.BASE_URL + Contants.GET_CHANNEL_URL
					+ getChannelString;
			String result = HttpUtils.getHttpGetResultForUrl(url);
			if (result == null) {

			} else if (",".equals(result.substring(result.length() - 1,
					result.length()))) {
				String[] theChannels = result.split(",");
				theChannelNumber=4;
				for(int i=0;i<theChannels.length;i++){
					if(Integer.parseInt(theChannels[i])>4||equipId.substring(0,1).equals("8")){
						theChannelNumber=8;
					}
				}

				if(theChannelNumber==8){
					msg.what=23;
				}else{
					msg.what=24;
				}
				handler.sendMessage(msg);
			} else {

			}
			
		}
	}
	public class Thread4 extends Thread{
		@Override
		public void run() {
			Message msg = new Message();
			Bundle b = new Bundle();
			String getEquipId = "equipId=" + equipId;
			String url = Contants.BASE_URL + Contants.GET_EQUIP_STATE_URL
					+ getEquipId;
			String result = HttpUtils.getHttpGetResultForUrl(url);
			if (result == null&&result=="0") {

			}else{
				b.putString("equipState",result);
				msg.what=111;
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv1.setText(capacity + "" + unit);
			}
			if (msg.what == 2) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv2.setText(capacity + "" + unit);
			}
			if (msg.what == 3) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv3.setText(capacity + "" + unit);
			}
			if (msg.what == 4) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv4.setText(capacity + "" + unit);
			}
			if (msg.what == 5) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv5.setText(capacity + "" + unit);
			}
			if (msg.what == 6) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv6.setText(capacity + "" + unit);
			}
			if (msg.what == 7) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv7.setText(capacity + "" + unit);
			}
			if (msg.what == 8) {
				Bundle b = msg.getData();
				float capacity = b.getFloat("capacity");
				String unit = b.getString("unit");
				showTv8.setText(capacity + "" + unit);
			}
			if (msg.what == 11) {
				controlPin(msg);
			}
			if (msg.what == 12) {
				controlPin(msg);
			}
			if (msg.what == 13) {
				controlPin(msg);
			}
			if (msg.what == 14) {
				controlPin(msg);
			}
			if (msg.what == 15) {
				controlPin(msg);
			}
			if (msg.what == 16) {
				controlPin(msg);
			}
			if (msg.what == 17) {
				controlPin(msg);
			}
			if (msg.what == 18) {
				controlPin(msg);
			}
			if(msg.what==23){
				control_bt1.setVisibility(View.GONE);
				control_bt2.setVisibility(View.GONE);
				control_tv1.setVisibility(View.GONE);
				control_tv2.setVisibility(View.GONE);
				//stateTv1.setVisibility(View.GONE);

				stepperCustom1.setVisibility(View.GONE);
				stepperCustom2.setVisibility(View.GONE);
				btn5.setVisibility(View.VISIBLE);
				btn6.setVisibility(View.VISIBLE);
				btn7.setVisibility(View.VISIBLE);
				btn8.setVisibility(View.VISIBLE);
			}
			if(msg.what==24){
				btn5.setVisibility(View.GONE);
				btn6.setVisibility(View.GONE);
				btn7.setVisibility(View.GONE);
				btn8.setVisibility(View.GONE);
				control_bt1.setVisibility(View.VISIBLE);
				control_bt2.setVisibility(View.VISIBLE);
				control_tv1.setVisibility(View.VISIBLE);
				control_tv2.setVisibility(View.VISIBLE);
				stateTv1.setVisibility(View.VISIBLE);

				stepperCustom1.setVisibility(View.VISIBLE);
				stepperCustom2.setVisibility(View.VISIBLE);
			}
			if(msg.what==111){
				Bundle b=msg.getData();
				String state=b.getString("equipState");
				if(state=="1"){

				}else if(state=="2"){

				}else if(state=="3"){

				}
			}
			if(msg.what==Contants.SERVER_ERROR){
				//Toast.makeText(MainActivity.this, "服务器错误", Toast.LENGTH_LONG).show();
			}
			if (msg.what==Contants.CONTROL_ERROR) {
				Toast.makeText(MainActivity.this, "插入失败", Toast.LENGTH_LONG).show();
			}
			if(msg.what==Contants.CONTROL_SUCCESS1){
				if(control_bt1.getText().toString().equals("通道0-开")){
				control_bt1.setText("通道0-关");
				control_bt1.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.on2));
				}
				else if(control_bt1.getText().toString().equals("通道0-关")){
					control_bt1.setText("通道0-开");
					control_bt1.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.off2));
				}
				else{
					
				}
			}
            if(msg.what==Contants.CONTROL_SUCCESS2){
            	if(control_bt2.getText().toString().equals("通道1-开")){
    				control_bt2.setText("通道1-关");
    				control_bt2.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.on2));
    				}
    				else if(control_bt2.getText().toString().equals("通道1-关")){
    					control_bt2.setText("通道1-开");
    					control_bt2.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.off2));
    				}
    				else{
    					
    				}
			}
			RotateAnimation animation = new RotateAnimation(degree, degree,
					Animation.RELATIVE_TO_SELF, 1.0f,
					Animation.RELATIVE_TO_SELF, 1.0f);
			animation.setDuration(1000);
			animation.setFillAfter(true);
			noodle.startAnimation(animation);

		}

		private void controlPin(Message msg) {
			now_channel.setText(channelId);
			Bundle b = msg.getData();
			returnCapactiy = b.getFloat("capacity");
			returnUnit = b.getString("unit");
			returnTime=b.getString("updateTime");
			getTimeDis(returnTime);
			degree = getdeDegree();
			now_value.setText(returnCapactiy + "");
			max_value.setText(max + "" + returnUnit);
			updateTimeTv.setText("更新时间："+returnTime+"");
		}

		private float getdeDegree() {
			switch (returnUnit) {
			case "V":
				degree = returnCapactiy / 10 * 180;
				max = 10;
				break;
			case "v":
				degree = returnCapactiy / 10 * 180;
				max = 10;
				break;
			case "mA":
				degree = returnCapactiy / 20 * 180;
				max = 20;
				break;
			case "dB":
				degree = returnCapactiy / 100 * 180;
				max = 100;
				break;
			case "RPM":
				degree = returnCapactiy / 3000 * 180;
				max = 3000;
				break;
			default:
				degree = returnCapactiy / 100 * 180;
				max = 100;
				break;
			}
			return degree;
		};
	};

	public long getTimeDis(String time){
		long timedis=(new Timestamp(System.currentTimeMillis()).getTime()-Timestamp.valueOf(time).getTime())/(1000);
		if(timedis>10){
			stateTv1.setText("离线");
			stateTv1.setTextColor(Color.parseColor("#FF0000"));
		}else{
			stateTv1.setText("在线");
			stateTv1.setTextColor(Color.parseColor("#0000FF00"));

		}
		return timedis;
	}
	
	public void controller1(View v){
		String controlCode="";
		if(returnTime.equals("")){
			Toast.makeText(MainActivity.this,"请稍候",Toast.LENGTH_SHORT).show();
		}else{
			if(getTimeDis(returnTime)>10){
				Utils.showToast(MainActivity.this,"您的设备已掉线，请检查硬件");
			}else{
				if(control_bt1.getText().toString().equals("通道0-关")){
					controlCode="DO=0_1000_1000_";
					//TODO
					//controlCode="DO=0_1000_"+Integer.parseInt(String.valueOf(stepperCustom1.getValue()))*10+"_";
				}
				else if(control_bt1.getText().toString().equals("通道0-开")){
					controlCode="DO=0_1000_0_";
				}
				int channel=Contants.CONTROL_SUCCESS1;
				ControlThread thread=new ControlThread(controlCode, channel);
				thread.start();
			}
		}



		
	}
    public void controller2(View v){
    	String controlCode="DO=1_1000_1000_";
		if(returnTime.equals("")){

			Toast.makeText(MainActivity.this,"请稍候",Toast.LENGTH_SHORT).show();
		}else{
			if(getTimeDis(returnTime)>10){
				Utils.showToast(MainActivity.this,"您的设备已掉线，请检查硬件");
			}else{
				if(control_bt2.getText().toString().equals("通道1-关")){
					controlCode="DO=1_1000_1000_";
					//controlCode="DO=1_1000_"+Integer.parseInt(String.valueOf(stepperCustom2.getValue()))*10+"_";
				}
				else if(control_bt2.getText().toString().equals("通道1-开")){
					controlCode="DO=1_1000_0_";
				}
				int channel=Contants.CONTROL_SUCCESS2;
				ControlThread thread2=new ControlThread(controlCode, channel);
				thread2.start();
			}
		}



	}
    public class ControlThread extends Thread{
      private String controlCode;
      int channel;
    	

		public ControlThread(String controlCode, int channel) {
		super();
		this.controlCode = controlCode;
		this.channel = channel;
	}


		@Override
    	public void run() {
    		String controlString = "controlOrder=" +controlCode+ equipId;
			String url = Contants.BASE_URL + Contants.CONTROL_URL
					+ controlString;
			String result = HttpUtils.getHttpGetResultForUrl(url);
			Message m = new Message();
			if ("0".equals(result))
				m.what = Contants.CONTROL_ERROR;

			else if ("1".equals(result))
				m.what = channel;
			else if ("exception".equals(result))
				m.what = Contants.SERVER_ERROR;
			else
				m.what = Contants.OTHER_ERROR;
			handler.sendMessage(m);
    	}
    }

	public class ControlThread2 extends Thread{
		private String controlCode;
		int channel;


		public ControlThread2(String controlCode, int channel) {
			super();
			this.controlCode = controlCode;
			this.channel = channel;
		}


		@Override
		public void run() {
			String controlString = "controlOrder=" +controlCode+ equipId;
			String url = Contants.BASE_URL + Contants.CONTROL_URL
					+ controlString;
			String result = HttpUtils.getHttpGetResultForUrl(url);
		}
	}

	public void getWave(View v){
		ed.putString("equipId",equipId);
		ed.putString("channelId",channelId);
		ed.commit();

		Intent it=new Intent(MainActivity.this,WaveActivity.class);
		startActivity(it);
		Toast.makeText(MainActivity.this,"波形显示完整之前请勿切换显示方式，否则将引起异常",Toast.LENGTH_LONG).show();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		isTrue=false;
	};
	@Override
	protected void onResume() {
		
		super.onResume();
		isTrue=true;
		MyThread3 getChannelThread=new MyThread3();
		getChannelThread.start();
		MyThread2 choiceChannelThread = new MyThread2();
		choiceChannelThread.start();
		startThread();
	}
	@Override
	protected void onPause() {
		
		super.onPause();
		isTrue=false;
	}

}
