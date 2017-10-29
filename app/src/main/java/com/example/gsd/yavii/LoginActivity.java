package com.example.gsd.yavii;

import java.util.ArrayList;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsd.yavii.utils.Contants;
import com.example.gsd.yavii.utils.HttpUtils;

public class LoginActivity extends Activity {
	EditText etUsername, etPassword;
	CheckBox chk;
	TextView changeIpTv;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	ArrayList<String> usernameArraylist;
	ArrayList<String> passwordArraylist;
	ArrayList<Integer> idArraylist;
	String username;
	String password;
	public String result;
	private ProgressDialog prgDialog;
	SharedPreferences sp2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_ui);
		etUsername = (EditText) findViewById(R.id.etuserName);
		etPassword = (EditText) findViewById(R.id.etuserPassword);
		changeIpTv= (TextView) findViewById(R.id.change_ip_tv);
		changeIpTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it=new Intent(LoginActivity.this,ChangeIpActivity.class);
				startActivity(it);
			}
		});
		chk = (CheckBox) findViewById(R.id.checkSaveName);
		pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = pref.edit();

		String name = pref.getString("userName", "");
		String pass = pref.getString("passWord", "");
		if (name == null) {
			chk.setChecked(false);
		} else {
			chk.setChecked(true);
			etUsername.setText(name);
			etUsername.setSelection(name.length());
			etPassword.setText(pass);
			etPassword.setSelection(pass.length());
		}
		sp2=getSharedPreferences("myIp", Context.MODE_PRIVATE);
		Contants.BASE_URL=sp2.getString("ip","http://42.51.158.205:8088/yavii/");

	}

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btnLogin:
			username = etUsername.getText().toString().trim();
			password = etPassword.getText().toString().trim();
			if (username.equals("")) {
				Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
			} else if (password.equals("")) {
				Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
			} else {

				prgDialog = new ProgressDialog(LoginActivity.this);
				prgDialog.setIcon(R.drawable.progress);
				prgDialog.setTitle("请稍等");
				prgDialog.setMessage("正在登陆，请稍等...");
				prgDialog.setCancelable(true);
				prgDialog.setIndeterminate(true);
				prgDialog.show();
				LoginThread thread = new LoginThread();
				thread.start();
			}
		}
	}

	public class LoginThread extends Thread {
		public void run() {
			String loginString = "username=" + username + "&password="
					+ password;
			String url = Contants.BASE_URL + Contants.LOGIN_URL + loginString;
			result = HttpUtils.getHttpGetResultForUrl(url);
			Message m = new Message();
			if ("0".equals(result))
				m.what = Contants.LOGIN_ERROR;

			else if ("1".equals(result))
				m.what = Contants.LOGIN_SUCCESS;
			else if ("exception".equals(result))
				m.what = Contants.SERVER_ERROR;
			else
				m.what = Contants.OTHER_ERROR;
			proHandle.sendMessage(m);
		};

	}

	public void jumpToConfig(View v){
		Intent it=new Intent(LoginActivity.this,ConfigActivity.class);
		startActivity(it);
	}

	private Handler proHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					LoginActivity.this);

			prgDialog.dismiss();
			switch (msg.what) {
			case Contants.LOGIN_ERROR:
				builder.setIcon(R.drawable.alert_error)
						.setTitle("错误")
						.setMessage("用户名或密码错误，请确认")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									// 点击确定按钮
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
				break;
			case Contants.SERVER_ERROR:
				builder.setIcon(R.drawable.alert_error)
						.setTitle("错误")
						.setMessage("请检查网络连接后再试")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									// 点击确定按钮
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
				break;

			case Contants.LOGIN_SUCCESS:

				builder.setIcon(R.drawable.alert_ok)
						.setTitle("登陆成功")
						.setMessage("恭喜您，登陆成功")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									// 点击确定按钮
									public void onClick(DialogInterface dialog,
											int which) {
										if (chk.isChecked()) {
											editor.putString("userName", username);
											editor.putString("passWord", password);
											editor.commit();

										} else {
											
											editor.remove("passWord");
											editor.commit();
										}
										Toast.makeText(LoginActivity.this, "正在获取可以设备，请稍等...", Toast.LENGTH_LONG).show();
										Intent intent = new Intent();

										intent.setClass(LoginActivity.this,
												DerviceListActivity.class);

										startActivity(intent);

										LoginActivity.this.finish();
									}
								}).show();
				break;
			case Contants.OTHER_ERROR:
                
				builder.setIcon(R.drawable.alert_ok)
						.setTitle("错误")
						.setMessage("未知错误")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									// 点击确定按钮
									public void onClick(DialogInterface dialog,
											int which) {
										
									}
								}).show();
				break;
			}
		}
	};
}
