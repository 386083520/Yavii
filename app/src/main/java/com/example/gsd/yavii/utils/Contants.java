package com.example.gsd.yavii.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gsd.yavii.ChangeIpActivity;


public class Contants {

	public static String BASE_URL = "http://42.51.158.205:8080/yavii/";
	//public static final String BASE_URL = "http://192.168.253.3:8080/yavii/";
	public static final String LOGIN_URL = "LoginServlet?";
	public static final String DERVICE_LIST_URL="DerviceListServlet?";
	public static final String COLLECT_VALUE_URL="CollectValueServlet?";
	public static final String COLLECT_VALUE_AVG_URL="CollectValueAvgServlet?";
	public static final String GET_CHANNEL_URL="GetChannelServlet?";
	public static final String GET_EQUIP_STATE_URL="EquipStateServlet?";
	public static final String CONTROL_URL="ControlServlet?";
	public static final int LOGIN_ERROR=1;
	public static final int LOGIN_SUCCESS=2;
	public static final int CONTROL_ERROR=20;
	public static final int CONTROL_SUCCESS1=21;
	public static final int CONTROL_SUCCESS2=22;
	public static final int GET_DERVICE_ERROR=1;
	public static final int GET_DERVICE_SUCCESS=2;
	public static final int SERVER_ERROR=3;
	public static final int OTHER_ERROR=4;

}
