package com.example.gsd.yavii;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gsd.yavii.utils.GetIp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ConfigActivity extends Activity implements View.OnClickListener {
    EditText etPort,etReceiver;
    CustomEditText etIp;
    Button btConn,btSend;
    OutputStream os;
    InputStream ips;
    Writer writer;
    Reader reader;
    public boolean isConnected = false;
    private MyHandler myHandler;
    DatagramSocket socket;
    String ip;
    int port;
    InetAddress addr ;
    private AutoCompleteTextView etSend;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_ui);

        String [] arr={"@YAV: D298,yavii123,192.168.0.255,808",
                "@AP:D298,yavii123",
                "@IP:192.168.0.255,808",
                "@ID:00001",
                "@MODE:0",
                "@SERVER:0",
                "@SERVER:1",
                "@DT:1",
                "@BAUDR：2400",
                "@BAUDR：4800",
                "@BAUDR：9600",
                "@BAUDR：115200",
                "@HB:1",
                "@CLEAR",
                "@CH:0",
                "@CH:1",
                "@CH:2",
                "@ALL",
                "@END",
                "MODE=N_A_B_CC",
                "DT=N_CC",
                "CC=N_X_CC",
                "COUNT=N_CC",
                "DO=N_AA_BB_CC"
        };




        etIp= (CustomEditText) findViewById(R.id.et_ip);
        etPort=(EditText) findViewById(R.id.et_port);
        btConn=(Button) findViewById(R.id.bt_connect);
        btSend=(Button) findViewById(R.id.bt_send);
        btConn.setOnClickListener(this);
        btSend.setOnClickListener(this);
        etSend= (AutoCompleteTextView) findViewById(R.id.et_send);
        etReceiver=(EditText) findViewById(R.id.et_recevier);
        myHandler = new MyHandler();
        GetIp getIp=new GetIp();
        etIp.setText(getIp.getIp(this));
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.auto_complete_adapter,arr);
        etSend.setAdapter(arrayAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isConnected==false){
            btConn.performClick();
        }

    }

    private void sendData() {
        final String context=etSend.getText().toString().trim();

        new Thread(){
            public void run() {
                byte[] sendBuf;
                sendBuf=context.getBytes();

                DatagramPacket sendPacket
                        = new DatagramPacket(sendBuf , sendBuf.length , addr , port );
                try {
                    socket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }.start();



    }

    private void connectThread() {
        if(!isConnected){
            new Thread(){
                public void run() {
                    connectServer( etIp.getText().toString(),etPort.getText()
                            .toString());
                };
            }.start();
        }else{
            if(socket!=null){
                socket.close();

            }

            btConn.setText("连接");
            isConnected=false;
        }

    }

    protected void connectServer(String ip,String port) {
        try {
            InetAddress ia = InetAddress.getByName(ip);
            socket = new DatagramSocket(Integer.parseInt(port));

            myHandler.sendEmptyMessage(2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInfo(String msg) {
        Toast.makeText(ConfigActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            receiverData(msg.what);
            if(msg.what==1){
                String result = msg.getData().get("msg").toString();
                etReceiver.setText(result);
            }
        }
    }

    public void receiverData(int flag) {
        if(flag==2){
            btConn.setText("断开");
            showInfo("连接成功");
            isConnected=true;
            new Thread(){
                public void run() {
                    while(true){
                        if(isConnected){

                            byte[] buf = new byte[1024];
                            String result = null;

                            DatagramPacket packet = new DatagramPacket(buf,
                                    buf.length);


                            try {
                                socket.receive(packet);
                                port = packet.getPort();
                                int a=port;
                                addr = packet.getAddress();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            result = new String(buf, 0, packet.getLength());
                            if (!result.equals("")){

                                Message msg = new Message();
                                msg.what = 1;
                                Bundle data = new Bundle();
                                data.putString("msg", result);
                                msg.setData(data);
                                myHandler.sendMessage(msg);
                            }

                        }
                    }



                };
            }.start();
        }

    }





    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_connect:
                connectThread();

                break;
            case R.id.bt_send:
                sendData();
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isConnected==true){
            btConn.performClick();
        }
    }
}
