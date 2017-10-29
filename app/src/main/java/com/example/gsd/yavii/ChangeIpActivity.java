package com.example.gsd.yavii;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gsd.yavii.utils.Contants;

/**
 * Created by Administrator on 2017/3/5.
 */

public class ChangeIpActivity extends Activity{
    private EditText ipEt;
    private Button ipSureBtn;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_ip_ui);
        ipEt= (EditText) findViewById(R.id.ip_et);
        sp=getSharedPreferences("myIp", Context.MODE_PRIVATE);
        String ip=sp.getString("ip","http://42.51.158.205:8088/yavii/");
        ipEt.setText(ip);
        ipSureBtn= (Button) findViewById(R.id.ip_sure_btn);
        ipSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String ipString = "http://"+ipEt.getText().toString()+":8080/yavii/";
                String ipString =ipEt.getText().toString();
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("ip",ipString);
                editor.commit();
                Contants.BASE_URL=sp.getString("ip","http://42.51.158.205:8088/yavii/");
                finish();
            }
        });
    }
}
