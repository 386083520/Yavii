package com.example.gsd.yavii.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	/**
	 * 通过URL获取HttpGet请求
	 * @param url
	 * @return HttpGet
	 */
	private static HttpGet getHttpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		return httpGet;
	}
	
	/**
	 * 通过 HttpGet请求获取HttpResponse对象
	 * @param httpGet
	 * @return HttpResponse
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static HttpResponse getHttpResponse(HttpGet httpGet) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(httpGet);
		return response;
	}
	/**
	 * 将URL打包成HttpGet请求，发送，得到查询结果 网络异常 返回 "exception"
	 * @param url
	 * @return resultString
	 */
	public static String getHttpGetResultForUrl(String url){
		HttpGet httpGet = getHttpGet(url);
		String resultString = null;
		
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpResponse response = getHttpResponse(httpGet);
			if(response.getStatusLine().getStatusCode() == 200)
				resultString = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			resultString = "exception";
			e.printStackTrace();
		} catch (IOException e) {
			resultString = "exception";
			e.printStackTrace();
		}
		
		return resultString;
	}
	
}
