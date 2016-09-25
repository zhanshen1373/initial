package com.example.faceall.facealldetect;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FaApi {

    private final static String URL = "http://tv.faceall.cn/api/";
    private final static String TAG = "FaApi";
    private String apiKey = null;
    private String apiSecret = null;
    private String version = null;

    public FaApi(String apiKey, String apiSecret, String version) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.version = version;
    }

    public JSONObject executeMultipart(String method , List<PostParameter> params)
    {
    	JSONObject jsob = null;
    	MultipartPost post = new MultipartPost(params);
    	try {
			String result = post.send(this.URL+this.version+"/"+method);
    		jsob = new JSONObject(result);
    		
//    		Vector<String> faceArray = new Vector<String>();
//    		for(int i =0;i<jsob.getJSONArray("faces").length();i++)
//    		{
//    			faceArray.add(jsob.getJSONArray("faces").getJSONObject(i).getString("id"));
//    		}		
//    		for(int j =0; j<faceArray.size();j++)
//    		{
//    			Log.d(TAG, "face_id="+faceArray.get(j));
//    		}
    	} catch (Exception e) {
    		Log.e(TAG, "sendPostRequest", e);
    		Log.d(TAG, "==================================================");
		}
    	return jsob;
    }
    
    public JSONArray executeMultipartArray(String method , List<PostParameter> params){
    	JSONArray jary = null;
    	try {
			MultipartPost post = new MultipartPost(params);
			String result = post.send(this.URL+this.version+"/"+method);
			jary = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "sendPostRequest", e);
    		Log.d(TAG, "==================================================");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return jary;
    }
    
    public JSONObject executeGet(String method , List<BasicNameValuePair> params)
    {
    	JSONObject jsob = null;
    	try {
	    	String param = URLEncodedUtils.format(params, "UTF-8");
			Log.d(TAG,param);
			//baseUrl 
			String stringUrl = this.URL+this.version+"/"+method;
			stringUrl = stringUrl+"?"+param;
			
			Log.d(TAG,stringUrl);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(stringUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String response = "";
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");
				jsob = new JSONObject(response);
				Log.d(TAG,TAG+jsob.toString());
				}
    		}catch (Exception e) {
				e.printStackTrace();
			}
		return jsob;
    	
    }
    
    public JSONArray executeGetArray(String method , List<BasicNameValuePair> params)
    {
    	JSONArray jary = null;
    	try {
	    	String param = URLEncodedUtils.format(params, "UTF-8");
			Log.d(TAG,param);
			//baseUrl 
			String stringUrl = this.URL+this.version+"/"+method;
			stringUrl = stringUrl+"?"+param;
			
			Log.d(TAG,stringUrl);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(stringUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String response = "";
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity,"utf-8");
				jary = new JSONArray(response);
				Log.d(TAG,TAG+jary.toString());
				}
    		}catch (Exception e) {
				e.printStackTrace();
			}
		return jary;
    	
    }
    
}
