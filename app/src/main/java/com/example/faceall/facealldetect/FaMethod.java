package com.example.faceall.facealldetect;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FaMethod {
	private static final String DETECTION_DETECT                 = "detection/detect";
	private static final String DETECTION_LANDMARK               = "detection/landmark";
	private static final String DETECTION_LANDMARK68             = "detection/landmark68";
	private static final String DETECTION_FEATURE                = "detection/feature";
	private static final String DETECTION_ATTRIBUTES             = "detection/attributes";
	private static final String FACE_GET_INFO                    = "face/get_info";
	private static final String IMAGE_GET_INFO                   = "image/get_info";
	private static final String IMAGE_GET_FILE                   = "image/get_file";
	private static final String FACESET_CREATE                   = "faceset/create";
	private static final String FACESET_DELETE                   = "faceset/delete";
	private static final String FACESET_ADD_FACES                = "faceset/add_faces";
	private static final String FACESET_REMOVE_FACES             = "faceset/remove_faces";
	private static final String FACESET_GET_INFO                 = "faceset/get_info";
	private static final String FACESET_SET_INFO                 = "faceset/set_info";
	private static final String FACESET_TRAIN                    = "faceset/train";
	private static final String FACESET_GET_LIST                 = "faceset/get_list";
	private static final String RECOGNITION_CLUSTER              = "recognition/cluster";
	private static final String RECOGNITION_COMPARE_FACE         = "recognition/compare_face";
	private static final String RECOGNITION_COMPARE_FACE_FACESET = "recognition/compare_face_faceset";
	private static final String RECOGNITION_CELEBRITY            = "detection/celebrity";
	private static final String RECOGNITION_COMPARE_FEATURE      = "recognition/compare_feature";
	private static final String OBJECT_ROCOGNIZE                 = "object/recognize";
	
	private static final String DETECTION_RECOGNIZE = "detection/recognize";
	private static final String USER_SIGNIN = "user/signin";
	private static final String USER_SET_NICKNAME = "user/set_nickname";
	private static final String USER_SET_PASSWORD = "user/set_password";
	private static final String USER_GET_INFO = "user/get_info";
	private static final String USER_RESET_PASSWORD = "user/reset_password";
	private static final String USER_AUTHENTICATE = "user/authenticate";
	private static final String USER_RESEND_AUTHENTICATION_MAIL = "user/resend_auth_mail";
	private static final String USER_AUTHENTICATE_RESET_PASSWORD = "user/reset_authenticate";
	private static final String API_RESET = "api/reset";
	private static final String MEMBER_CREATE_MEMBER = "member/create";
	private static final String MEMBER_DELETE_MEMBER = "member/delete";
	private static final String MEMBER_SET_INFO = "member/set_info";
	private static final String MEMBER_SET_PHOTO = "member/set_photo";
	private static final String MEMBER_GET_INFO = "member/get_info";
	private static final String MEMBER_GET_LIST = "member/get_list";
	private static final String MEMBER_RECOMMEND = "member/recommend";
	private static final String GROUP_CREATE_GROUP = "group/create";
	private static final String GROUP_DELETE_GROUP = "group/delete";
	private static final String GROUP_SET_INFO = "group/set_info";
	private static final String GROUP_GET_GROUP_INFO = "group/get_info";
	private static final String GROUP_ADD_MEMBER = "group/add_mem";
	private static final String GROUP_REMOVE_MEMBER = "group/remove_mem";
	private static final String GROUP_GET_GROUP_LIST = "group/get_list";
	private static final String GROUP_TRAIN = "group/train";
	private static final String LABEL_GET_MEMBER = "label/get_member";
	private static final String ATTENDANCE_CHECK_IN = "attendance/checkin";
	private static final String ATTENDANCE_GET_INFO = "attendance/get_info";
	private static final String BILLBOARD_GET_ALL_ARTICLES = "billboard/get_all_articles";
	private static final String LIFE_WEATHER = "life/weather";
	private static final String LIFE_WEIHAO = "life/weihao";
 
    public String apiKey    =null;
    public String apiSecret =null;
    public String apiVersion=null;

	private FaApi api=null;  
	
	
	//构造方法
    public  FaMethod(String apiKey,String apiSecret,String apiVersion)
    {
    	this.apiKey    =apiKey;
    	this.apiSecret =apiSecret;
    	this.apiVersion=apiVersion;
    	api            =new FaApi(apiKey,apiSecret,apiVersion);
    }
    //************************************
	// Method:      detection_detect
	// Description：对给定的图片进行人脸检测
	// Returns:     JSONObject类型数据，包括faceID和ImageID
	// Parameter:   String imageFile  图片存储路径，标准格式如下"C:\\Users\\Desktop\\1.jpg"
	//************************************
	public JSONArray detection_detect(String filePath)
	{	   	
		 File FILE = new File(filePath);
		 List<PostParameter> params = new ArrayList<PostParameter>();
		 params.add(new PostParameter<String>("api_key", this.apiKey));
		 params.add(new PostParameter<String>("api_secret", this.apiSecret));
		 params.add(new PostParameter<File>("img_file", FILE));
		 return api.executeMultipartArray(DETECTION_DETECT, params);
	}
	
	//************************************
	// Method:      detection_landmark
	// Description：计算给定脸的关键点
	// Returns:     JSONObject类型数据，返回值为计算后的结果
	// Parameter:   String faceId   在detection_detect方法中返回的JSONObject类中提取的faceId字符串
	//************************************
	public JSONObject detection_landmark(String faceId)
	{	   
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("face_id", faceId));
		return api.executeGet(DETECTION_LANDMARK, params);
	}
	
	public JSONArray detection_recognize(String face_id, String group_id, String limit)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("face_id", face_id)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("limit", limit));
		return api.executeGetArray(DETECTION_RECOGNIZE, params);	
	}
	
	public JSONObject recognition_celebrity(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("face_id", face_id)); 
		return api.executeGet(RECOGNITION_CELEBRITY,params);
	}
	
	public JSONObject detection_attributes(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("face_id", face_id)); 
		return api.executeGet(DETECTION_ATTRIBUTES, params);
	}
	
	public JSONObject member_create(String name, String remark, String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("face_id", face_id)); 
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("remark", remark));
		return api.executeGet(MEMBER_CREATE_MEMBER, params);	
	}
	
	public JSONObject member_delete(String member_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("member_id", member_id)); 
		return api.executeGet(MEMBER_CREATE_MEMBER, params);	
	}
	
	public JSONObject member_set_info(String member_id, String name, String remark, String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("member_id", member_id)); 
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("remark", remark));
		params.add(new BasicNameValuePair("face_id", face_id)); 
		return api.executeGet(MEMBER_SET_INFO, params);	
	}
	
	public JSONObject member_set_photo(String member_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("member_id", member_id)); 
		return api.executeGet(MEMBER_SET_PHOTO, params);	
	}
	
	public JSONObject member_get_info(String member_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("member_id", member_id)); 
		return api.executeGet(MEMBER_GET_INFO, params);	
	}
	
	public JSONObject member_get_list()
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		return api.executeGet(MEMBER_GET_LIST, params);	
	}
	
	public JSONArray member_recommend(String member_id,String group_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("member_id",member_id));
		params.add(new BasicNameValuePair("group_id",group_id));
		return api.executeGetArray(MEMBER_RECOMMEND, params);	
	}
	
	public JSONObject user_signin(String username, String mail, String password, String nickname)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("mail", mail)); 
		params.add(new BasicNameValuePair("password", password));
		
		return api.executeGet(USER_SIGNIN, params);	
	}
	
	public JSONObject user_set_nickname(String username, String password, String nickname)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("nickname", nickname)); 
		return api.executeGet(USER_SET_NICKNAME, params);	
	}
	
	public JSONObject user_set_password(String username, String password,String new_password)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("new_password", new_password)); 
		return api.executeGet(USER_SET_PASSWORD, params);	
	}
	
	public JSONObject user_reset_password(String username, String mail)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("mail", mail));
		return api.executeGet(USER_RESET_PASSWORD, params);	
	}
	
	public JSONObject user_get_info(String username, String password)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password));
		return api.executeGet(USER_GET_INFO, params);	
	}
	
	public JSONObject user_authenticate(String session)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("session", session)); 
		return api.executeGet(USER_AUTHENTICATE, params);	
	}
	
	public JSONObject user_reset_authenticate(String username, String password)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password));
		return api.executeGet(USER_AUTHENTICATE_RESET_PASSWORD, params);	
	}
	
	public JSONObject user_resend_auth_mail(String username, String password)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password));
		return api.executeGet(USER_RESEND_AUTHENTICATION_MAIL, params);	
	}
	
	public JSONObject api_reset(String username, String password)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username)); 
		params.add(new BasicNameValuePair("password", password)); 
		return api.executeGet(API_RESET, params);	
	}
	
	public JSONObject group_create(String name,String remark)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("name", name)); 
		params.add(new BasicNameValuePair("remark", remark)); 
		return api.executeGet(GROUP_CREATE_GROUP, params);	
	}
	
	public JSONObject group_delete(String group_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id)); 
		return api.executeGet(GROUP_DELETE_GROUP, params);	
	}
	
	public JSONObject group_add_mem(String group_id, String member_id, String remark)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("member_id", member_id)); 
		params.add(new BasicNameValuePair("remark", remark)); 
		return api.executeGet(GROUP_ADD_MEMBER, params);	
	}
	
	public JSONObject group_remove_mem(String group_id,String member_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("member_id", member_id)); 
		return api.executeGet(GROUP_REMOVE_MEMBER, params);	
	}
	
	public JSONObject group_get_info(String group_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		return api.executeGet(GROUP_GET_GROUP_INFO, params);	
	}
	
	public JSONObject group_set_info(String group_id,String name, String remark)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("name", name)); 
		params.add(new BasicNameValuePair("remark", remark));
		return api.executeGet(GROUP_SET_INFO, params);	
	}
	
	public JSONArray group_get_list()
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		return api.executeGetArray(GROUP_GET_GROUP_LIST, params);	
	}
	
	public JSONObject group_train(String group_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		return api.executeGet(GROUP_TRAIN, params);	
	}
	
	public JSONArray label_get_member(String label_id, String group_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("label_id", label_id));
		params.add(new BasicNameValuePair("group_id", group_id));
		return api.executeGetArray(LABEL_GET_MEMBER, params);
	}
	
	public JSONObject attendance_checkin(String group_id,String member_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("member_id", member_id)); 
		return api.executeGet(ATTENDANCE_CHECK_IN, params);	
	}
	
	public JSONArray get_all_articles()
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		return api.executeGetArray(BILLBOARD_GET_ALL_ARTICLES, params);
	}
	
	public JSONObject attendance_get_info(String group_id,String member_id,String start_at,String end_at)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("group_id", group_id));
		params.add(new BasicNameValuePair("member_id", member_id)); 
		params.add(new BasicNameValuePair("start_at", start_at));
		params.add(new BasicNameValuePair("end_at", end_at)); 
		return api.executeGet(ATTENDANCE_GET_INFO, params);	
	}
	
	public JSONObject life_weather(String city)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("city", city)); 
		return api.executeGet(LIFE_WEATHER, params);	
	}
	
	public JSONObject life_weihao(String city)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey)); 
		params.add(new BasicNameValuePair("api_secret", this.apiSecret)); 
		params.add(new BasicNameValuePair("city", city)); 
		return api.executeGet(LIFE_WEIHAO, params);	
	}
}