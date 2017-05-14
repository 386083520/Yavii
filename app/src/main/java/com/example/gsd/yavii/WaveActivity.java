package com.example.gsd.yavii;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gsd.yavii.Fragment.BarFragment;
import com.example.gsd.yavii.Fragment.DrawerFragment;
import com.example.gsd.yavii.Fragment.LineFragment;
import com.example.gsd.yavii.utils.NoFastClickUtils;

/**
 * Created by gsd on 2016/9/15.
 */

public class WaveActivity extends AppCompatActivity implements DrawerFragment.NavigationDrawerCallbacks {


    private DrawerFragment mDrawerFragment;
    SharedPreferences.Editor ed;
    SharedPreferences sp;
    SharedPreferences sp2;
    public static String equipId;
    public static String channelId;
    int type=0;

    public static String theType="year";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wave_ui);
        sp=getSharedPreferences("gsd", Context.MODE_PRIVATE);
        ed=sp.edit();
        sp2=this.getSharedPreferences("gsd2",Context.MODE_PRIVATE);
        equipId=sp2.getString("equipId","");
        channelId=sp2.getString("channelId","");

        mDrawerFragment = (DrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position){
            case 0:
                if(NoFastClickUtils.isFastClick()){
                  Toast.makeText(WaveActivity.this,"请稍后点击",Toast.LENGTH_SHORT).show();
                }else{
                    choiceFragmentLine();
                }

                break;
            case 1:
                if(NoFastClickUtils.isFastClick()){
                    Toast.makeText(WaveActivity.this,"请稍后点击",Toast.LENGTH_SHORT).show();
                }else{
                    choiceFragmentBar();
                }

                break;


            default:
                break;
        }
    }





    private void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


            if (!mDrawerFragment.isDrawerOpen()) {
                getMenuInflater().inflate(R.menu.menu, menu);
                restoreActionBar();
                return true;
            }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



            switch (item.getItemId()){

                case R.id.day:
                    if(NoFastClickUtils.isFastClick()){
                        Toast.makeText(WaveActivity.this,"请稍后点击",Toast.LENGTH_SHORT).show();

                    }else{
                        theType="day";
                        ed.putInt("timeType",24);
                        ed.commit();
                        if(type==0){
                            choiceFragmentLine();
                        }else if(type==1){
                            choiceFragmentBar();
                        }

                    }
                    return true;

                case R.id.month:
                    if(NoFastClickUtils.isFastClick()){
                        Toast.makeText(WaveActivity.this,"请稍后点击",Toast.LENGTH_SHORT).show();

                    }else{
                        theType="month";
                        ed.putInt("timeType",30);
                        ed.commit();
                        if(type==0){
                            choiceFragmentLine();
                        }else if(type==1){
                            choiceFragmentBar();
                        }

                    }
                    return true;
                case R.id.year:
                    if(NoFastClickUtils.isFastClick()){
                        Toast.makeText(WaveActivity.this,"请稍后点击",Toast.LENGTH_SHORT).show();

                    }else{
                        theType="year";
                        ed.putInt("timeType",12);
                        ed.commit();
                        if(type==0){
                            choiceFragmentLine();
                        }else if(type==1){
                            choiceFragmentBar();
                        }

                    }
                    return true;
                default:
                    return super.onOptionsItemSelected(item);



        }




    }

    public void choiceFragmentLine(){
        type=0;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LineFragment())
                .commit();
    }
    public void choiceFragmentBar(){
        type=1;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new BarFragment())
                .commit();
    }

    public void onMenuClick(View view){

    }

    public void onPlay(View view){


    }

}
