package com.example.gsd.yavii;

import android.app.Activity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_ip_ui);
        ipEt= (EditText) findViewById(R.id.ip_et);
        ipSureBtn= (Button) findViewById(R.id.ip_sure_btn);
        ipSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contants.BASE_URL = "http://"+ipEt.getText().toString()+":8080/yavii/";
                finish();
            }
        });
    }
}
