package com.example.gsd.yavii;

import java.util.ArrayList;
import java.util.List;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gsd.yavii.utils.Contants;
import com.example.gsd.yavii.utils.HttpUtils;

public class DerviceListActivity extends Activity implements OnItemClickListener{
	private ListView listView;
	List<String> list;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	String manager;
	String result;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dervice_list_ui);
	listView = (ListView) findViewById(R.id.listView);
	
	pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
	editor = pref.edit();

	manager = pref.getString("userName", "");
	DerviceListThread thread=new DerviceListThread();
	thread.start();
	
	
	// 设置ListView的元素被选中时的事件处理监听器
	listView.setOnItemClickListener(this);
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	/*Toast toast=Toast.makeText(DerviceListActivity.this, "正在获取设备数据，请稍候...", Toast.LENGTH_SHORT);
	toast.setGravity(Gravity.CENTER,0,0);
	toast.show();*/

	String text = listView.getItemAtPosition(arg2) + "";
	Intent myIntent = new Intent();
	 myIntent.putExtra("dercice_id", text);
	myIntent.setClass(DerviceListActivity.this, MainActivity.class);
	startActivity(myIntent);
}

public class DerviceListThread extends Thread{
	@Override
	public void run() {
		list = new ArrayList<String>();
		String derviceString="username="+manager;
		String url = Contants.BASE_URL + Contants.DERVICE_LIST_URL+derviceString;
		result = HttpUtils.getHttpGetResultForUrl(url);
		Message m = new Message();
		if ("0".equals(result))
			m.what = Contants.GET_DERVICE_ERROR;

		else if (",".equals(result.substring(result.length()-1, result.length())))
			m.what = Contants.GET_DERVICE_SUCCESS;
		else if ("exception".equals(result))
			m.what = Contants.SERVER_ERROR;
		else
			m.what = Contants.OTHER_ERROR;
		proHandle.sendMessage(m);
	}
}
private Handler proHandle = new Handler() {
	@Override
	public void handleMessage(Message msg) {

		
		switch (msg.what) {
		case Contants.GET_DERVICE_ERROR:
			Toast.makeText(DerviceListActivity.this, "获取数据异常", Toast.LENGTH_LONG).show();
			break;
		case Contants.SERVER_ERROR:
			Toast.makeText(DerviceListActivity.this, "服务器异常", Toast.LENGTH_LONG).show();
			break;

		case Contants.GET_DERVICE_SUCCESS:
            String[] dervice=result.split(",");
            for(int i=0;i<dervice.length;i++){
            	list.add(dervice[i]);
            }
           
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        			DerviceListActivity.this, android.R.layout.simple_list_item_1, list);
        	// 给ListView设置数据适配器
            
        	 listView.setAdapter(adapter);
			break;
		case Contants.OTHER_ERROR:
            
			Toast.makeText(DerviceListActivity.this, "未知异常", Toast.LENGTH_LONG).show();
			break;
		}
	}
};
}
