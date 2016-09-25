package com.example.faceall.facealldetect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements CvCameraViewListener2 {


    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 0, 0, 0);
    private static final Scalar    MAX_FACE_RECT_COLOR = new Scalar(0,0,0, 0);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;
    private static final int       TIME_UPDATE         = 1;
    private static final int       FACEARRAY_SIZE      = 1;
    private static final int       REC_SIZE            = 4;
    private static final String    CITY                = "beijing";
    //handlerWeather 所需参数
    private static final int       UPDATE_WEATHER      = 0;
    //handlerBillboard 所需参数
    private static final int       UPDATE_TEXT         = 0 ;
    private static final int       REFRESH_TEXT        = 1;
    private static final int       REFRESH_WEB         = 2;
    //handlerImage 所需参数
    private static final int       REFRESH_DEC_INFO    = 1;
    private static final int       REFRESH_ATTRIBUTES  = 2;
    private static final int       REFRESH_PREFERENCE  = 3;
    private static final int       REFRESH_CELEBRITY   = 4;
    private static final int       REFRESH_VISIBILITY  = 5;

    private MenuItem               mItemFace50;
    private MenuItem               mItemFace40;
    private MenuItem               mItemFace30;
    private MenuItem               mItemFace20;
    private MenuItem               mItemType;

    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFile;
    private CascadeClassifier      mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = JAVA_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private CameraBridgeViewBase           mOpenCvCameraView;
    private FaMethod               method              = null;
    private TextView 			   timeView            = null;
    private ImageView              weatherImage        = null;
    private Bitmap                 weatherBitmap       = null;
    private Bitmap                 celebrityBitmap     = null;
    private TextView               tempView            = null;
    private TextView               cityView            = null;
    private TextView               xxView              = null;
    private String                 GroupId             ="bG6V7jFLq3lqL4A25iUoKRP44RZaquxfhaHVdYqQ";
    private String                 api_key             ="kB2olylzkIohrtvZpRZwtdoqXUZi5PaGSqcTof10";
    private String                 api_secret          ="DaaIDjaPycSbiEq7BI1zU9MiOUPb4qd8qPlTaF0b";
    private String name,score,beauty,company,label,remark,photourl,age,title,gender,race,exp,memId, memIdTemp ;
    private String scoreCelebrity,nameCelebrityCN,nameCelebrityEN,celebrityID;
    private String scoreCelebrityTemp,nameCelebrityCNTemp,nameCelebrityENTemp;
    private String labelRawString;
    private String labelResult = "";
    private JSONObject             member_JSONObject   = null;
    private static int             faceNum             = 0;
    private WeatherObject           wObject            = null;

    //各个layout的定义
    private RelativeLayout infoLayout = null;
    //	private LinearLayout infoLayout = null;
    private RelativeLayout information = null;
    private LinearLayout recommendLayout = null;
    private LinearLayout welcomeLayout = null;
    private LinearLayout rootLayout = null;
    private LinearLayout attrLayout = null;
    private LinearLayout activityLayout = null;
    private LinearLayout msgLayout = null;
    private LinearLayout webLayout = null;

    //info 的各部分的定义
    private Bitmap infoFace = null;
    private ImageView infoImageView = null;
    private TextView infoNameView = null;
    private TextView infoCompanyView = null;
    private TextView infoTitleView = null;
    private TextView infoBeautyView = null;

    //检测结果的各个元素的定义
    private TextView ageView = null;
    //	private TextView genderView = null;
//	private TextView raceView = null;
//	private TextView expView = null;
    private ImageView expView = null;
    private ImageView celePhotoView = null;
    private TextView celeNameView = null;
    private TextView celeScoreView = null;

    //公告栏部分的定义,包括通知、活动以及企业信息展示
    private TextView notification_text = null;
    private WebView webView = null;
    private TextView activity_text = null;


    //两个推荐部分的定义
    private ImageView[] recMemberImageGroup1 = null;
    private ImageView[] recMemberQRGroup1 = null;
    private TextView[]  recMemberName1 = null;
    private TextView[]  recMemberComp1 = null;

    private Vector<String> labelId = null;
    private Vector<String> labelName = null;

    private Vector<Bitmap> memberImageArray1 = null;
    private Vector<String> memberNameArray1 = null;
    private Vector<String> memberCompArray1 = null;
    private Vector<String> memberIdArray1 = null;
    private Vector<String> memberImageUrl1 = null;
    private Vector<Bitmap> resultQRCodeArray1 = null;

    private int QR_WIDTH = 200, QR_HEIGHT = 200;

    //定义阻塞队列
    private BlockingQueue<Mat> faceMatArray = null;

    private JSONObject remarkJSON = null;

    Bitmap mBitmapbg1 = null;
    Bitmap mBitmapbg2 = null;
    Bitmap mBitmapbg3 = null;
    Bitmap mBitmapAngry = null;
    Bitmap mBitmapHappy = null;
    Bitmap mBitmapNatural = null;

    private int _displaywidth = 240;
    private int _displayheight = 400;
    private int _displaypixels = _displaywidth * _displayheight;


    //网络状态信号，0为网络故障，1为网络正常
    private int network = 0;
    //监听网络状态
    private BroadcastReceiver connectionReceiver;

    //welcome_layout中动态刷新图片
    int[] images = null;//图片资源ID
    ArrayList<ImageView> imageSource = null;//图片资源
    ArrayList<View> dots = null;//点
    ViewPager viewPager;//用于显示图片
    MyPagerAdapter  adapter;//viewPager的适配器
    private  int currPage = 0;//当前显示的页
    private  int oldPage = 0;//上一次显示的页
    private Vector<Bitmap> bmpArray = null;

    //公告及企业信息部分
    //控制线程状态的信号,默认为true,在onDestroy中设置成为false
    private boolean state_default = true;
    private boolean state_detect = true;
    private boolean state_background = true;

    private Vector<JSONObject> enterprise_information = new Vector<JSONObject>();
    private Vector<JSONObject> notification = new Vector<JSONObject>();
    private Vector<JSONObject> activity = new Vector<JSONObject>();

    private int current_enterprise_information = 0;
    private int current_notification = 0;
    private int current_activity = 0;

    private int countFrame = 0;

    static {
        if(!OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Init unsuccessfully!");
        }
        else{
            System.loadLibrary("detection_based_tracker");
        }
    }

    public MainActivity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        //网络连接状况
        connectionReceiver = new ConnectionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        try {
            Thread.sleep(2*1000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        wObject = new WeatherObject();
        method = new FaMethod(api_key,api_secret,"v1");
        memId = new String("  ");
        score = new String("1");


        //向量的初始化
        labelId = new Vector<String> ();
        labelName = new Vector<String> ();

        memberImageArray1 = new Vector<Bitmap> ();
        memberNameArray1 = new Vector<String>();
        memberCompArray1 = new Vector<String>();
        memberIdArray1 = new Vector<String>();
        memberImageUrl1 = new Vector<String>() ;
        resultQRCodeArray1 = new Vector<Bitmap>();

        //定义faceMat的阻塞队列，长度为8
        faceMatArray  = new ArrayBlockingQueue<Mat>(FACEARRAY_SIZE);

        //全屏并加载surface_view
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);


        //时间显示   
        timeView = (TextView)super.findViewById(R.id.text_view_time);

        //天气及限行信息显示
        weatherImage = (ImageView)super.findViewById(R.id.image_weather);
        tempView = (TextView)super.findViewById(R.id.text_temperature);
        cityView = (TextView)super.findViewById(R.id.text_city);
        xxView = (TextView)super.findViewById(R.id.text_view_weather);

        //属性名人显示区和通知公告栏部分的定义
        attrLayout = (LinearLayout)findViewById(R.id.attr_layout);
        msgLayout = (LinearLayout)findViewById(R.id.msg_layout);
        webLayout = (LinearLayout)findViewById(R.id.company_web);
        activityLayout = (LinearLayout)findViewById(R.id.activity_layout);
        notification_text = (TextView)findViewById(R.id.notification);
        activity_text = (TextView)findViewById(R.id.activity);
        webView = (WebView)findViewById(R.id.web);


        //infoLayout区显示的定义
        infoLayout = (RelativeLayout)findViewById(R.id.info_layout);
        information= (RelativeLayout)findViewById(R.id.information);
        infoImageView = (ImageView)findViewById(R.id.image_detect);
        infoNameView = (TextView)findViewById(R.id.info_name);
        infoCompanyView = (TextView)findViewById(R.id.info_company);
        infoTitleView = (TextView)findViewById(R.id.info_title);
        infoBeautyView = (TextView)findViewById(R.id.beauty_score);

        ageView = (TextView)findViewById(R.id.attr_age);
        expView = (ImageView)findViewById(R.id.expImage);
        celePhotoView = (ImageView)findViewById(R.id.celebrity_photo);
        celeNameView =(TextView)findViewById(R.id.celebrity_name);
        celeScoreView = (TextView)findViewById(R.id.celebrity_score);

        //recommendLayout区显示的定义
        rootLayout=(LinearLayout)findViewById(R.id.root);
        recommendLayout = (LinearLayout)findViewById(R.id.recommend_layout);
        welcomeLayout = (LinearLayout)findViewById(R.id.welcome_layout);
        recMemberImageGroup1 = new ImageView[REC_SIZE];
        recMemberQRGroup1 = new ImageView[REC_SIZE];
        recMemberName1 = new TextView[REC_SIZE];
        recMemberComp1 = new TextView[REC_SIZE];

        recMemberImageGroup1[0] = (ImageView)findViewById(R.id.label1member0);
        recMemberImageGroup1[1] = (ImageView)findViewById(R.id.label1member1);
        recMemberImageGroup1[2] = (ImageView)findViewById(R.id.label1member2);
        recMemberImageGroup1[3] = (ImageView)findViewById(R.id.label1member3);

        recMemberQRGroup1[0] =(ImageView)findViewById(R.id.label1QRCode0);
        recMemberQRGroup1[1] =(ImageView)findViewById(R.id.label1QRCode1);
        recMemberQRGroup1[2] =(ImageView)findViewById(R.id.label1QRCode2);
        recMemberQRGroup1[3] =(ImageView)findViewById(R.id.label1QRCode3);

        recMemberName1[0] =(TextView)findViewById(R.id.label1name0);
        recMemberName1[1] =(TextView)findViewById(R.id.label1name1);
        recMemberName1[2] =(TextView)findViewById(R.id.label1name2);
        recMemberName1[3] =(TextView)findViewById(R.id.label1name3);

        recMemberComp1[0] =(TextView)findViewById(R.id.label1company0);
        recMemberComp1[1] =(TextView)findViewById(R.id.label1company1);
        recMemberComp1[2] =(TextView)findViewById(R.id.label1company2);
        recMemberComp1[3] =(TextView)findViewById(R.id.label1company3);

        //设置浏览器
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setHorizontalScrollbarOverlay(true);

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求,不用借助系统浏览器
            }
        });

        //JavaCameraView 部分的初始化
        timeView.setText("");
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setZOrderOnTop(true);
        mOpenCvCameraView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mOpenCvCameraView.enableView();


        //设置View的可见顺序
        infoLayout.setVisibility(View.INVISIBLE);
        welcomeLayout.setVisibility(View.INVISIBLE);
        recommendLayout.setVisibility(View.INVISIBLE);
        attrLayout.setVisibility(View.INVISIBLE);
        activityLayout.setVisibility(View.VISIBLE);
        msgLayout.setVisibility(View.VISIBLE);
        webLayout.setVisibility(View.VISIBLE);

        //载入bitmap资源

        try {
            if(mBitmapbg1==null&&mBitmapbg2==null&&mBitmapbg2==null) {
                mBitmapbg1 = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.background1);
                mBitmapbg2 = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.background2);
                mBitmapbg3 = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.background3);
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=2;
            if(mBitmapAngry==null&&mBitmapHappy==null&&mBitmapNatural==null) {
                mBitmapAngry = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.angry);
                mBitmapHappy = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.happy);
                mBitmapNatural = BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.natural);
            }

//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeResource(getResources(), R.drawable.pic2, options);
//		int imageHeight = options.outHeight;
//		int imageWidth = options.outWidth;
//		String imageType = options.outMimeType;
//		Log.d(TAG,"ImageHeight=" +imageHeight+"ImageWidth = "+imageWidth+" type: "+imageType);

            options.inSampleSize = 4;
            if(bmpArray==null){
                bmpArray = new Vector<Bitmap>();
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic1,options));
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic2,options));
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic3,options));
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic4,options));
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic5,options));
                bmpArray.add(BitmapFactory.decodeResource(this.getApplication().getResources(), R.drawable.pic6,options));
            }
        } catch (OutOfMemoryError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //静态加载
        try {
            // load cascade file from application resources
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if (mJavaDetector.empty()) {
                Log.e(TAG, "Failed to load cascade classifier");
                mJavaDetector = null;
            } else
                Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

            mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

            cascadeDir.delete();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
        }

        initViewPager();

        if(isNetworkConnected(this))
        {
            //开启三个独立线程
            new TimeThread().start();
            new WeatherThread().start();
            new BillboardThread("BillboardThread").start();
            new ReflashThread("ReflashThread").start();
            Thread proThread1 = new Thread(new ProcessFaceMat(faceMatArray));
//     		Thread proThread2 = new Thread(new ProcessFaceMat(faceMatArray));
            proThread1.start();
//            proThread2.start();
            Thread listenThread = new Thread(new ListenThread(faceMatArray));
            listenThread.start();
        }
        else Toast.makeText(this, "网络故障", Toast.LENGTH_LONG).show();
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //时间刷新句柄
    private Handler handlerClock = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what){
                case TIME_UPDATE:

                    timeView.setText(TimeString.StringData());
                    break;

                default:
                    break;
            }
        }
    };

    //天气刷新句柄
    private Handler handlerWeather = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what){
                case UPDATE_WEATHER:
                    cityView.setText(wObject.getCityName());
                    weatherImage.setImageBitmap(weatherBitmap);
                    char symbol = 176;
                    tempView.setText(wObject.getTemperature()+String.valueOf(symbol)+"C");
                    if(wObject.getIs_xianxing()==0)
                    {
                        xxView.setText("PM2.5: "+wObject.getPm25()+"\t\t\t"+"今日不限行");
                    }else if(wObject.getIs_xianxing()==1){
                        try {
                            xxView.setText("PM2.5: "+wObject.getPm25()+"\t\t"+"今日限行："+wObject.getWeihao().getInt(0)+", "+wObject.getWeihao().getInt(1));
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //公告界面刷新句柄
    private Handler Billboardhandler = new Handler(){
        public void handleMessage(Message msg)
        {
            switch(msg.what){
                case UPDATE_TEXT:
                    Log.e(TAG,"UPDATE_TEXT");
                    JSONArray response =  (JSONArray)msg.obj;
                    //清空vector(JSONObject)的长度
                    enterprise_information.clear();
                    notification.clear();
                    activity.clear();
                    if(response!=null){
                        for(int i = 0;i<response.length();i++)
                        {
                            try {
                                if(response.getJSONObject(i).getString("type").equals("notification"))
                                    notification.add(response.getJSONObject(i));
                                else if(response.getJSONObject(i).getString("type").equals("activity"))
                                    activity.add(response.getJSONObject(i));
                                else if(response.getJSONObject(i).getString("type").equals("enterprise_information"))
                                {
                                    Log.e(TAG,response.getJSONObject(i).toString());
                                    enterprise_information.add(response.getJSONObject(i));
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;

                case REFRESH_TEXT:
                    //分别刷新两个模块

                    if(notification.size()!=0)
                    {
                        try {
                            notification_text.setText(notification.get(current_notification).getString("content"));
                            current_notification = (current_notification+1)%notification.size();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(activity.size()!=0)
                    {
                        try {
                            activity_text.setText(activity.get(current_activity).getString("content"));
                            current_activity = (current_activity+1)%activity.size();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case REFRESH_WEB:
                    if(enterprise_information.size()!=0)
                    {
                        Log.e(TAG,"webView");
                        try {
                            webView.loadUrl(enterprise_information.get(0).getString("content"));
                            Log.e(TAG, enterprise_information.get(0).getString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                default:
                    break;
            }
        }
    };

    //检测结果刷新句柄

    private Handler handlerImage = new Handler() {
        ExpHashTable expHashTable = new ExpHashTable();
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what) {
                case 0:
                    break;

                case REFRESH_DEC_INFO:
                    rootLayout.setBackgroundDrawable(new BitmapDrawable(mBitmapbg1));
//	    			rootLayout.setBackgroundResource(R.drawable.background1);

                    msgLayout.setVisibility(View.INVISIBLE);
                    webLayout.setVisibility(View.INVISIBLE);
                    activityLayout.setVisibility(View.INVISIBLE);

                    attrLayout.setVisibility(View.VISIBLE);
                    information.setVisibility(View.VISIBLE);
                    infoImageView.setImageBitmap(infoFace);
                    infoNameView.setText(name);
                    break;
                case REFRESH_ATTRIBUTES:
                    //更新人脸的attributes信息
                    try {
                        remarkJSON = new JSONObject(msg.obj.toString());
                        Log.d(TAG,"Received Remark: "+ msg.obj.toString());
                        company = remarkJSON.getString("company");
                        label = remarkJSON.getString("label");
                        title = remarkJSON.getString("title");
                        String labeltemp = "";
                        JSONArray lb = new JSONArray(label);
                        for(int k = 0;k<lb.length();k++)
                        {
                            labeltemp=labeltemp+lb.getJSONObject(k).getString("label")+"\t";
                        }
                        labelResult = labeltemp;
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    DecimalFormat df =new DecimalFormat("#.0");
                    Log.d(TAG,"Score"+ score);
                    Log.d(TAG,"Company"+company);
                    ageView.setText(age+"岁");
                    infoBeautyView.setText(beauty);
                    infoTitleView.setText(title);
                    infoCompanyView.setText(company);
//						genderView.setText(expHashTable.get(gender));
//						raceView.setText(expHashTable.get(race));
//					expView.setText(expHashTable.get(exp));
                    if(exp.equals("Angry"))
                    {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapAngry));
                    }else if(exp.equals("Happy")) {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapHappy));
                    }else {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapNatural));
                    }
                    if(celebrityBitmap!=null)
                    {
                        celePhotoView.setImageBitmap(celebrityBitmap);
                        //注释部分为画圆形
//						celePhotoView.setImageBitmap(getRoundCornerImage(celebrityBitmap,50));
                        if(!(nameCelebrityCN.equals("")))
                            celeNameView.setText(nameCelebrityCN);
                        else
                            celeNameView.setText(nameCelebrityEN);
                    }
                    celebrityBitmap=null;
                    celeScoreView.setText(df.format(Double.parseDouble(scoreCelebrity)*100)+"%");

//						infoLabelView.setText(labelResult);		

                    infoLayout.setVisibility(View.VISIBLE);

                    break;

                case REFRESH_PREFERENCE:
                    //可能感兴趣的人刷新部分
                    try {
                        if(memberImageArray1.size()>REC_SIZE){
                            memberImageArray1.setSize(REC_SIZE);
                        }
                        if(resultQRCodeArray1.size()>REC_SIZE){
                            resultQRCodeArray1.setSize(REC_SIZE);
                        }

                        for(int n = 0;n<REC_SIZE;n++)
                        {
                            recMemberImageGroup1[n].setImageBitmap(null);
                            recMemberQRGroup1[n].setImageBitmap(null);
                            recMemberName1[n].setText("");
                            recMemberComp1[n].setText("");
                        }

                        Vector<Bitmap> cpMemberImageArray = new Vector<Bitmap>(memberImageArray1);
                        Vector<Bitmap> cpResultQRCodeArray = new Vector<Bitmap>(resultQRCodeArray1);
                        Vector<String> cpMemberNameArray = new Vector<String>(memberNameArray1);
                        Vector<String> cpMemberCompArray = new Vector<String>(memberCompArray1);

                        Iterator<Bitmap> it1 = cpMemberImageArray.iterator();
                        Iterator<Bitmap> it3 = cpResultQRCodeArray.iterator();
                        Iterator<String> it2 = cpMemberNameArray.iterator();
                        Iterator<String> it4 = cpMemberCompArray.iterator();

                        int a =0;
                        welcomeLayout.setVisibility(View.INVISIBLE);
                        recommendLayout.setVisibility(View.VISIBLE);
                        while(it1.hasNext() && it3.hasNext()) {
                            Bitmap bitTemp = (Bitmap)it1.next();
                            Bitmap QR = (Bitmap)it3.next();
                            String nmTemp = (String)it2.next();
                            String cmpTemp = (String)it4.next();
                            recMemberImageGroup1[a].setImageBitmap(bitTemp);
                            recMemberQRGroup1[a].setImageBitmap(QR);
                            recMemberName1[a].setText(nmTemp);
                            recMemberComp1[a].setText(cmpTemp);
                            a++;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.e(TAG,"Error!",e);
                        e.printStackTrace();
                    }
                    break;

                case REFRESH_CELEBRITY:
                    //设置可见状态

                    msgLayout.setVisibility(View.INVISIBLE);
                    webLayout.setVisibility(View.INVISIBLE);
                    activityLayout.setVisibility(View.INVISIBLE);

                    attrLayout.setVisibility(View.VISIBLE);
                    recommendLayout.setVisibility(View.INVISIBLE);
                    infoLayout.setVisibility(View.VISIBLE);
                    information.setVisibility(View.INVISIBLE);
                    welcomeLayout.setVisibility(View.VISIBLE);
                    rootLayout.setBackgroundDrawable(new BitmapDrawable(mBitmapbg2));
//	    			rootLayout.setBackgroundResource(R.drawable.background2);

                    //刷新名人部分
                    infoImageView.setImageBitmap(infoFace);
                    infoBeautyView.setText(beauty);
                    ageView.setText(age+"岁");
//					genderView.setText(expHashTable.get(gender));
//					raceView.setText(expHashTable.get(race));
//					expView.setText(expHashTable.get(exp));
                    if(exp.equals("Angry"))
                    {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapAngry));
                    }else if(exp.equals("Happy")) {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapHappy));
                    }else {
                        expView.setImageDrawable(new BitmapDrawable(mBitmapNatural));
                    }
                    DecimalFormat df1 =new DecimalFormat("#.0");
                    if(celebrityBitmap!=null)
                    {
                        celePhotoView.setImageBitmap(celebrityBitmap);
                        //注释为画圆形
//						celePhotoView.setImageBitmap(getRoundCornerImage(celebrityBitmap,50));
                        if(!(nameCelebrityCN.equals("")))
                            celeNameView.setText(nameCelebrityCN);
                        else
                            celeNameView.setText(nameCelebrityEN);
                    }
                    celeScoreView.setText(df1.format(Double.parseDouble(scoreCelebrity)*100)+"%");

                    break;

                case REFRESH_VISIBILITY:
                    infoLayout.setVisibility(View.INVISIBLE);
                    welcomeLayout.setVisibility(View.INVISIBLE);
                    recommendLayout.setVisibility(View.INVISIBLE);
                    attrLayout.setVisibility(View.INVISIBLE);
                    activityLayout.setVisibility(View.VISIBLE);
                    msgLayout.setVisibility(View.VISIBLE);
                    webLayout.setVisibility(View.VISIBLE);
                    rootLayout.setBackgroundDrawable(new BitmapDrawable(mBitmapbg3));
                    break;

                default:
                    break;
            }
        }
    };

    private Handler handlerppt = new Handler(){
        @Override
        public void handleMessage (Message msg) {
            switch(msg.what){
                case 0:
                    viewPager.setCurrentItem(currPage);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPause()
    {
        super.onPause();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

//        if(mBitmapbg1 != null  && !mBitmapbg1.isRecycled())  
//        {  
//            mBitmapbg1.recycle();  
//            mBitmapbg1 = null;  
//        }    
//        
//        if(mBitmapbg2 != null  && !mBitmapbg2.isRecycled())  
//        {  
//            mBitmapbg2.recycle();  
//            mBitmapbg2 = null;  
//        }  
//        
//        if(mBitmapbg3 != null  && !mBitmapbg3.isRecycled())  
//        {  
//            mBitmapbg3.recycle();  
//            mBitmapbg3 = null;  
//        }  
//        
////        if(celebrityBitmap != null  && !celebrityBitmap.isRecycled())  
////        {  
////        	celebrityBitmap.recycle();  
////        	celebrityBitmap = null;  
////        }  
//        
////        if(infoFace != null  && !infoFace.isRecycled())  
////        {  
////        	infoFace.recycle();  
////        	infoFace = null;  
////        }  
//        
//        if(mBitmapAngry != null  && !mBitmapAngry.isRecycled())  
//        {  
//        	mBitmapAngry.recycle();  
//        	mBitmapAngry = null;  
//        }
//        
//        if(mBitmapHappy != null  && !mBitmapHappy.isRecycled())  
//        {  
//        	mBitmapHappy.recycle();  
//        	mBitmapHappy = null;  
//        }
//        
//        if(mBitmapNatural != null  && !mBitmapNatural.isRecycled())  
//        {  
//        	mBitmapNatural.recycle();  
//        	mBitmapNatural = null;  
//        }
//        
//        this.onDestroy();

    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(mBitmapbg1 != null  && !mBitmapbg1.isRecycled())
        {
            mBitmapbg1.recycle();
            mBitmapbg1 = null;
        }

        if(mBitmapbg2 != null  && !mBitmapbg2.isRecycled())
        {
            mBitmapbg2.recycle();
            mBitmapbg2 = null;
        }

        if(mBitmapbg3 != null  && !mBitmapbg3.isRecycled())
        {
            mBitmapbg3.recycle();
            mBitmapbg3 = null;
        }

        //      if(celebrityBitmap != null  && !celebrityBitmap.isRecycled())
        //      {
        //      	celebrityBitmap.recycle();
        //      	celebrityBitmap = null;
        //      }

        //      if(infoFace != null  && !infoFace.isRecycled())
        //      {
        //      	infoFace.recycle();
        //      	infoFace = null;
        //      }

        if(mBitmapAngry != null  && !mBitmapAngry.isRecycled())
        {
            mBitmapAngry.recycle();
            mBitmapAngry = null;
        }

        if(mBitmapHappy != null  && !mBitmapHappy.isRecycled())
        {
            mBitmapHappy.recycle();
            mBitmapHappy = null;
        }

        if(mBitmapNatural != null  && !mBitmapNatural.isRecycled())
        {
            mBitmapNatural.recycle();
            mBitmapNatural = null;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mOpenCvCameraView!=null)
            mOpenCvCameraView.enableView();

//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();

        state_default = false;
        state_background = false;
        state_detect = false;


        if(mBitmapbg1 != null  && !mBitmapbg1.isRecycled())
        {
            mBitmapbg1.recycle();
            mBitmapbg1 = null;
        }

        if(mBitmapbg2 != null  && !mBitmapbg2.isRecycled())
        {
            mBitmapbg2.recycle();
            mBitmapbg2 = null;
        }

        if(celebrityBitmap != null  && !celebrityBitmap.isRecycled())
        {
            celebrityBitmap.recycle();
            celebrityBitmap = null;
        }

        if(infoFace != null  && !infoFace.isRecycled())
        {
            infoFace.recycle();
            infoFace = null;
        }

        if(mBitmapAngry != null  && !mBitmapAngry.isRecycled())
        {
            mBitmapAngry.recycle();
            mBitmapAngry = null;
        }

        if(mBitmapHappy != null  && !mBitmapHappy.isRecycled())
        {
            mBitmapHappy.recycle();
            mBitmapHappy = null;
        }

        if(mBitmapNatural != null  && !mBitmapNatural.isRecycled())
        {
            mBitmapNatural.recycle();
            mBitmapNatural = null;
        }

        for(int i =0; i<bmpArray.size();i++)
        {
            if(bmpArray.get(i)!=null && !bmpArray.get(i).isRecycled())
            {
                bmpArray.get(i).recycle();
            }
        }
        bmpArray.clear();


        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        MatOfRect faces = new MatOfRect();

        if (mDetectorType == JAVA_DETECTOR) {
            if (mJavaDetector != null)
                mJavaDetector.detectMultiScale(mGray, faces, 1.2, 5, 0, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                        new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        else if (mDetectorType == NATIVE_DETECTOR) {
            if (mNativeDetector != null)
                mNativeDetector.detect(mGray, faces);
        }
        else {
            Log.e(TAG, "Detection method is not selected!");
        }

        Rect[] facesArray = faces.toArray();
        double faceArea = 0 ;
        int maxMark=0;

        for (int i = 0; i < facesArray.length; i++) {
            if(facesArray[i].area()> faceArea) {
                maxMark = i;
                faceArea = facesArray[i].area();
            }
        }

        for (int i = 0; i < facesArray.length; i++) {
//        	Point pointTL = new Point(facesArray[i].tl().x-10,facesArray[i].tl().y-10);
//    		Point pointBR = new Point(facesArray[i].br().x+10,facesArray[i].br().y+10);
//        	Log.d(TAG,"The coordination of the point: "+facesArray[i].tl().x+"  "+facesArray[i].br());
            if(i==maxMark) {
                Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), MAX_FACE_RECT_COLOR, 3);
//        		Core.rectangle(mRgba, pointTL, pointBR, MAX_FACE_RECT_COLOR, 3);
            }
            else{
                Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 1);
//        		Core.rectangle(mRgba, pointTL, pointBR, FACE_RECT_COLOR, 1);
            }
        }
        if(facesArray.length>0&&countFrame>=3)
        {
            countFrame =0;
            Log.d(TAG,"The length of faceArray before Add = "+faceMatArray.size());
            for (int i = 0; i < facesArray.length; i++) {
                if(i==maxMark){
//        			 Point pointTL = new Point(facesArray[i].tl().x-10,facesArray[i].tl().y-10);
//            		 Point pointBR = new Point(facesArray[i].br().x+10,facesArray[i].br().y+10);
//            		 Rect facetemp = new Rect(pointTL,pointBR);
                    if(faceMatArray.size()>=FACEARRAY_SIZE) {
                        try {
                            //                		Log.d("queue","The queue is full and then add one to the array");
                            faceMatArray.take();
                            //               		Log.d("queue","Successfully remove the faces from the array");
                            faceMatArray.put(mRgba.submat(facesArray[i]).clone());
                            //						Log.d("queue","Successfully add the faces to the array");
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            Log.e("queue","Remove or Add Error Occurs",e);
                            e.printStackTrace();
                        }
                    }
                    else{
                        Log.d("queue","Add the faces to a not-full queue");
                        try {
                            faceMatArray.put(mRgba.submat(facesArray[i]).clone());
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            Log.e("queue","Add error occurs!",e);
                            e.printStackTrace();
                        }
                    }
                }else {continue;}
            }
        } else if(facesArray.length>0&&countFrame<3){
            Log.d(TAG,"Count Frame = "+countFrame);
            countFrame++;
        }
        return mRgba;

    }

    public  Bitmap createQRImage(String content, int widthPix, int heightPix, String nameStr) {
        Bitmap bitmap = null;
        try {
            if (content == null || "".equals(content)) {
                return null;
            }

            //配置参数 
            Map<EncodeHintType, Object> hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别 
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度 
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4 

            // 图像数据转换，使用了矩阵转换 
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片， 
            // 两个for循环是图片横列扫描的结果 
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888 
            bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            if (nameStr != null) {
                bitmap = addLogo(bitmap, nameStr);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap addLogo(Bitmap src, String str) {
        if (src == null) {
            return null;
        }

        //获取图片的宽高 
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int scareSize = (int) (0.8f*srcWidth);
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        //logo大小为二维码整体大小的1/5 
        float scaleFactor = srcWidth * 1.0f / 5 /scareSize ;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            Paint paint_blcak = generatePaint(Color.WHITE, Style.FILL, 3);
            //构造一个矩形  
            android.graphics.Rect rect1 = new android.graphics.Rect(0,0,srcWidth,srcHeight);
            //在平移画布前用绿色画下边框  
            canvas.drawRect(rect1, paint_blcak);
            paint.setColor(Color.BLACK);
            paint.setTextSize(70);
            paint.setStrokeWidth(3);
            FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            // 转载请注明出处：http://blog.csdn.net/hursing  
            int baseline = (rect1.bottom + rect1.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()  
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(str, rect1.centerX(), baseline, paint);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    private Paint generatePaint(int color,Paint.Style style,int width)
    {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        return paint;
    }

    //监听网络状态的广播类
    public class ConnectionReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                network = 0;
                Toast.makeText(getApplicationContext(), "网络故障",
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "unconnect");
            }else{
                network = 1;
//		    	 Toast.makeText(getApplicationContext(), "网络正常",
//					     Toast.LENGTH_LONG).show();
                Log.d(TAG, "connect");
            }
        }
    }

//	public static Bitmap readBitMap(Context context, int resId) { 
//		BitmapFactory.Options opt = new BitmapFactory.Options(); 
//		opt.inPreferredConfig = Bitmap.Config.RGB_565; 
//		opt.inPurgeable = true; 
//		opt.inInputShareable = true; 
//		// 获取资源图片 
//		InputStream is = context.getResources().openRawResource(resId); 
//		return BitmapFactory.decodeStream(is, null, opt); 
//	}

    private class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return images.length;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //判断将要显示的图片是否和现在显示的图片是同一个
            //arg0为当前显示的图片，arg1是将要显示的图片
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//			System.out.println("destroyItem==" + position);
            //销毁该图片
            container.removeView(imageSource.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //初始化将要显示的图片，将该图片加入到container中，即viewPager中
            container.addView(imageSource.get(position));
//			System.out.println("instantiateItem===" + position +"===="+container.getChildCount());
            return imageSource.get(position);
        }
    }

    public void initViewPager() {
        images = new int[]{
                R.drawable.pic1,
                R.drawable.pic2,
                R.drawable.pic3,
                R.drawable.pic4,
                R.drawable.pic5,
                R.drawable.pic6
        };
        //将要显示的图片放到list集合中
        imageSource = new ArrayList<ImageView>();
        for(int i = 0; i < images.length;i++){
            ImageView image = new ImageView(this);
            image.setBackgroundDrawable(new BitmapDrawable(bmpArray.get(i)));
            imageSource.add(image);
        }

        //获取显示的点（即文字下方的点，表示当前是第几张）
        dots = new ArrayList<View>();
        dots.add(findViewById(R.id.dot1));
        dots.add(findViewById(R.id.dot2));
        dots.add(findViewById(R.id.dot3));
        dots.add(findViewById(R.id.dot4));
        dots.add(findViewById(R.id.dot5));
        dots.add(findViewById(R.id.dot6));

        //显示图片的VIew
        viewPager = (ViewPager) findViewById(R.id.vp);
        //为viewPager设置适配器
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        //为viewPager添加监听器，该监听器用于当图片变换时，标题和点也跟着变化
        MyPageChangeListener listener = new MyPageChangeListener();
        viewPager.setOnPageChangeListener(listener);

        //开启定时器，每隔3秒自动播放下一张（通过调用线程实现）（与Timer类似，可使用Timer代替）
        ScheduledExecutorService scheduled =  Executors.newSingleThreadScheduledExecutor();
        //设置一个线程，该线程用于通知UI线程变换图片
        ViewPagerTask pagerTask = new ViewPagerTask();
        scheduled.scheduleAtFixedRate(pagerTask, 2, 3, TimeUnit.SECONDS);
    }



    //监听ViewPager的变化
    private class MyPageChangeListener implements OnPageChangeListener{
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            //当显示的图片发生变化之后
            //改变点的状态
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            dots.get(oldPage).setBackgroundResource(R.drawable.dot_normal);
            //记录的页面
            oldPage = position;
            currPage = position;
        }
    }

    //轮播界面的线程
    private class ViewPagerTask implements Runnable{
        @Override
        public void run() {
            //改变当前页面的值
            currPage =(currPage+ 1)%images.length;
            //发送消息给UI线程
            handlerppt.sendEmptyMessage(0);
        }
    }

    public Bitmap getBitmapFromUrl(String Url, int displaypixels, Boolean isBig)
    {
        Bitmap bmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        byte[] be = new byte[1024];
        try {
            URL url = new URL(Url);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            be=bos.toByteArray();
            if(be.length!=0) {
                try {
                    //开始解决OOM问题
                    opts.inJustDecodeBounds = true;
                    bmp=BitmapFactory.decodeByteArray(be, 0, be.length,opts);
                    opts.inSampleSize = computeSampleSize(opts,-1,displaypixels);
                    opts.inJustDecodeBounds = false;
                    //结束
                    bmp = BitmapFactory.decodeByteArray(be, 0, be.length,opts);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp;
    }

    private int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    private int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public synchronized void changeLayer(int param)
    {
        if(param==0){
            state_detect = false;
            state_background = true;
        }else {
            state_detect = true;
            state_background = false;
        }
    }

    public synchronized void refreshDisplay(String scoreCele, String cn, String en, String memId,String score, Mat mat, String name, String photourl, String remark, String ID)
    {
        JSONObject job = null;
        JSONArray jry = null;
        scoreCelebrity=new String(scoreCele);
        nameCelebrityCN=new String(cn);
        nameCelebrityEN=new String(en);

        if(Float.parseFloat(score)>0.92)
        {
            Log.d(TAG,"memId = "+memId);

            //把当前的人脸存为bitmap进行显示
            Bitmap bmAuto= Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, bmAuto);
            Log.d(TAG,"Bitmap has saved to the bmpArray");
            //当前人脸的存储
            infoFace = bmAuto.copy(Bitmap.Config.ARGB_8888, true);
            //回收内存
            if((bmAuto!=null)&&(bmAuto.isRecycled()==false))
            {
                bmAuto.recycle();
                bmAuto=null;
            }
            //释放mat资源
            mat.release();

            //获取相似的名人的脸
            String celebrityURL = "http://faceall.cn/static/img/celebrity/"+ID+".jpg";

            Log.e(TAG, "BEGIN TO GET THE FACE OF CELEBRITY");

            Bitmap bmTemp=getBitmapFromUrl(celebrityURL,_displaypixels,true);
            celebrityBitmap = bmTemp.copy(Bitmap.Config.ARGB_8888, true);
            if((bmTemp!=null)&&(bmTemp.isRecycled()==false))
            {
                bmTemp.recycle();
                bmTemp=null;
            }

            try {
                job = new JSONObject(remark);
                labelRawString = job.getString("label");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG,"Remark Error! ",e);
            }
            Log.d(TAG,"LabelString: "+labelRawString);

            //清空上一个返回相似的人的结果
            labelId.clear();
            labelName.clear();
            memberIdArray1.clear();
            memberImageUrl1.clear();
            memberNameArray1.clear();
            memberCompArray1.clear();

            Log.d(TAG,"清空前的结果： "+memberImageArray1.size()+"    "+memberImageArray1.size());
            memberImageArray1.clear();
            resultQRCodeArray1.clear();
            Log.d(TAG,"清空后的结果： "+memberImageArray1.size()+"    "+memberImageArray1.size());

            //获取检测到的人的labelId
            try {
                jry = new JSONArray(labelRawString);
                for(int i =0;i<jry.length();i++)
                {
                    labelName.add(jry.getJSONObject(i).getString("label"));
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            JSONArray memberArrayTemp = null;
            //存在label则刷新下两排
            if(labelName.size()>0)
            {
                memberArrayTemp = method.member_recommend(memId,GroupId);
                Log.e(TAG, "Member Info with the same ID:"+memberArrayTemp.toString());
                for(int k=0;k< memberArrayTemp.length(); k++)
                {
                    if(memberIdArray1.size()>=REC_SIZE)
                    {
                        continue;
                    }else{
                        try {
                            if(!(memberArrayTemp.getJSONObject(k).getString("member_id").equals(memId)))
                            {
                                JSONObject jsonTemp = memberArrayTemp.getJSONObject(k);
                                memberIdArray1.add(jsonTemp.getString("member_id"));
                                memberImageUrl1.add(jsonTemp.getString("photo"));

                                JSONObject remarkTemp = new JSONObject(jsonTemp.getString("remark"));

                                String member = jsonTemp.getString("member");
                                memberNameArray1.add(member);

                                String company = remarkTemp.getString("company");
                                memberCompArray1.add(company);

                                String phone  = new String("");
                                if(remarkTemp.has("phone"))
                                    phone = remarkTemp.getString("phone");

                                String title = remarkTemp.getString("title");
                                JSONArray labelTemp = remarkTemp.getJSONArray("label");
                                String labels = "";
                                for (int p=0;p<labelTemp.length();p++)
                                {
                                    labels = labels+labelTemp.getJSONObject(p).getString("label")+' ';
                                    if(p>=2)
                                        break;
                                }
                                String QRString ="职位："+title+'\n'+"联系方式："+phone+"\n"+"标签："+labels;
                                resultQRCodeArray1.add(createQRImage(QRString,QR_WIDTH,QR_HEIGHT,null));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.d(TAG,"Image   URL : "+memberImageUrl1.toString());
                        //刷新第一排的推荐显示

                    }
                }

                for(int p = 0; p<memberImageUrl1.size(); p++)
                {
                    String http = "http://tv.faceall.cn/photo/"+memberImageUrl1.get(p);
//					byte[] betemp = new byte[1024];
//					ByteArrayOutputStream bostemp = null;
//					InputStream istemp = null;
//					HttpURLConnection conntemp = null;

                    Bitmap bmp=getBitmapFromUrl(http,_displaypixels,true);
                    memberImageArray1.add(bmp.copy(Bitmap.Config.ARGB_8888, true));
                    if((bmp!=null)&&(bmp.isRecycled()==false))
                    {
                        bmp.recycle();
                        bmp=null;
                    }
                }

                handlerImage.sendMessage(handlerImage.obtainMessage(1));

                handlerImage.sendMessage(handlerImage.obtainMessage(2,remark));

                handlerImage.sendMessage(handlerImage.obtainMessage(3));

            }else if(labelId.size()==0) {
                handlerImage.sendMessage(handlerImage.obtainMessage(4));
            }
        }
        else{
            //把当前的人脸存为bitmap进行显示
            Bitmap bmAuto= Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, bmAuto);
            Log.d(TAG,"Bitmap has saved to the bmpArray");

            //当前人脸的存储
            infoFace = bmAuto.copy(Bitmap.Config.ARGB_8888, true);
            //回收内存
            if((bmAuto!=null)&&(bmAuto.isRecycled()==false))
            {
                bmAuto.recycle();
                bmAuto=null;
            }

            //释放mat资源
            mat.release();

            //获取相似的名人的脸
            String celebrityURL = "http://faceall.cn/static/img/celebrity/"+ID+".jpg";

            Bitmap bmTemp=getBitmapFromUrl(celebrityURL,_displaypixels,true);
            celebrityBitmap = bmTemp.copy(Bitmap.Config.ARGB_8888, true);
            if((bmTemp!=null)&&(bmTemp.isRecycled()==false))
            {
                bmTemp.recycle();
                bmTemp=null;
            }

            //清空上一个返回相似的人的结果
            labelId.clear();
            labelName.clear();
            memberIdArray1.clear();
            memberImageUrl1.clear();
            memberNameArray1.clear();
            memberCompArray1.clear();

            Log.d(TAG,"清空前的结果： "+memberImageArray1.size()+"    "+memberImageArray1.size());
            memberImageArray1.clear();
            resultQRCodeArray1.clear();
            Log.d(TAG,"清空后的结果： "+memberImageArray1.size()+"    "+memberImageArray1.size());

            handlerImage.sendMessage(handlerImage.obtainMessage(4));
        }

    }

    public static synchronized int getFaceNum()
    {
        return faceNum;
    }

    public static synchronized void setFaceNum(int num)
    {
        faceNum=num;
    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels)
    {
        //创建一个和原始图片一样大小位图
        Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        //创建带有位图roundConcerImage的画布
        Canvas canvas = new Canvas(roundConcerImage);
        //创建画笔
        Paint paint = new Paint();
        //创建一个和原始图片一样大小的矩形
        android.graphics.Rect rect = new android.graphics.Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 去锯齿
        paint.setAntiAlias(true);
        //画一个和原始图片一样大小的圆角矩形
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getHeight() / 2-3, paint);
        //canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        //设置相交模式
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //把图片画到矩形去
        canvas.drawBitmap(bitmap, null, rect, paint);
        return roundConcerImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemFace50 = menu.add("Face size 50%");
        mItemFace40 = menu.add("Face size 40%");
        mItemFace30 = menu.add("Face size 30%");
        mItemFace20 = menu.add("Face size 20%");
        mItemType   = menu.add(mDetectorName[mDetectorType]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemFace50)
            setMinFaceSize(0.5f);
        else if (item == mItemFace40)
            setMinFaceSize(0.4f);
        else if (item == mItemFace30)
            setMinFaceSize(0.3f);
        else if (item == mItemFace20)
            setMinFaceSize(0.2f);
        else if (item == mItemType) {
            int tmpDetectorType = (mDetectorType + 1) % mDetectorName.length;
            item.setTitle(mDetectorName[tmpDetectorType]);
            setDetectorType(tmpDetectorType);
        }
        return true;
    }

    private void setMinFaceSize(float faceSize) {
        mRelativeFaceSize = faceSize;
        mAbsoluteFaceSize = 0;
    }

    private void setDetectorType(int type) {
        if (mDetectorType != type) {
            mDetectorType = type;

            if (type == NATIVE_DETECTOR) {
                Log.i(TAG, "Detection Based Tracker enabled");
                mNativeDetector.start();
            } else {
                Log.i(TAG, "Cascade detector enabled");
                mNativeDetector.stop();
            }
        }
    }
    //实时返回系统时间
    private class TimeThread extends Thread{
        public void run() {
            // TODO Auto-generated method stub
            while(state_default)
            {
                try {
                    handlerClock.sendMessage(handlerClock.obtainMessage(TIME_UPDATE));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //刷新公告界面线程
    private class BillboardThread extends Thread{
        public BillboardThread(String name)
        {
            super(name);
        }
        public void run() {
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while(state_default)
            {
                if(network ==1){
                    Message message = new Message();
                    try {
                        JSONArray resultArr =method.get_all_articles();
                        message.what = UPDATE_TEXT;
                        message.obj  = resultArr;
                        Billboardhandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(32*60*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //定时界面刷新计时
    public class ReflashThread extends Thread{

        int countNumber = 100;

        public ReflashThread(String name)
        {
            super(name);
        }
        public void run()
        {
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while(state_default)
            {
                try{
                    Billboardhandler.sendMessage(Billboardhandler.obtainMessage(REFRESH_TEXT));
                    if(countNumber>=60)
                    {
                        countNumber = 0;
                        Billboardhandler.sendMessage(Billboardhandler.obtainMessage(REFRESH_WEB));
                        Log.e(TAG,"=========REFRESH THE WEB===========");
                    }
                    else{ countNumber++; }
                    Log.e(TAG,"reflash run");
                    Thread.sleep(1*61*1000);  // 这里设置:隔多长时间
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    //返回天气和限行信息
    private class WeatherThread extends Thread{
        public void run() {
            while (state_default)
            {
                if(network==1) {
                    JSONObject weatherResult = null;
                    try {
                        weatherResult = method.life_weather(CITY);
                        wObject.setPm25(weatherResult.getString("pm25"));
                        Log.d(TAG,"PM2.5 : "+weatherResult.getString("pm25"));
                        String text = weatherResult.getString("now");
                        Log.d(TAG,"Condition String: "+weatherResult.getString("now"));
                        wObject.setTemperature(new JSONObject(text).getString("temperature"));
                        Log.d(TAG,"Temperature Now: "+wObject.getTemperature());
                        wObject.setCondition(new JSONObject(text).getString("condition_text"));
                        Log.d(TAG,"Condition Now: "+wObject.getCondition());
                        wObject.setCode(new JSONObject(text).getString("condition_code"));
                        Log.d(TAG,"Weather Code: "+wObject.getCode());

                        String weatherURL = "http://files.heweather.com/cond_icon/"+wObject.getCode()+".png";

                        try {
                            URL url = new URL(weatherURL);
                            URLConnection con = url.openConnection();
                            InputStream is = con.getInputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            while((len = is.read(buffer)) != -1) {
                                bos.write(buffer, 0, len);
                            }
                            bos.close();
                            byte[] be=bos.toByteArray();
                            weatherBitmap=BitmapFactory.decodeByteArray(be, 0, be.length);

                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        weatherResult = method.life_weihao(CITY);
                        Log.d(TAG,"XianXing Information: "+weatherResult.toString());
                        wObject.setIs_xianxing(weatherResult.getInt("is_xianxing"));
                        Log.d(TAG,"XianXing or not: "+ Integer.toString(wObject.getIs_xianxing()));
                        if(wObject.getIs_xianxing()==1)
                        {
                            wObject.setWeihao(weatherResult.getJSONArray("weihao"));
                            Log.d(TAG,"weihao : "+wObject.getWeihao().toString());
                        }
                        wObject.setCityName(weatherResult.getString("city_name"));
                        Log.d(TAG,"City Information1 "+weatherResult.getString("city_name"));
                        Log.d(TAG,"City Information2: "+wObject.getCityName());
                        handlerWeather.sendMessage(handlerWeather.obtainMessage(0,wObject));

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Log.e(TAG,"Weather Error",e);
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(60*60*1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class ListenThread implements Runnable {

        private BlockingQueue<Mat> queue;
        private int number;

        public ListenThread(BlockingQueue<Mat> q)
        {
            this.queue = q;
            this.number = 0;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while(state_default)
            {
                if(queue.isEmpty()){
                    if(number>=10) {
                        number = 0;
                        changeLayer(0);
                        handlerImage.sendMessage(handlerImage.obtainMessage(5));
                    } else{
                        number++;
                    }
                } else {
                    number = 0;
                    changeLayer(1);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private class ProcessFaceMat implements Runnable {
        private BlockingQueue<Mat> queue;
        public ProcessFaceMat(BlockingQueue<Mat> q)
        {
            this.queue = q;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
//			Log.e(TAG,"=================detect state: "+state_detect);
            while(state_default)
            {
//				Log.e(TAG,"=============DETECT RUN=============");
                if(network==1) {
                    try {
                        Log.d("queue","The length of queue before Process: "+queue.size());
//						SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
//						String date = sDateFormat.format(new java.util.Date());
                        String filepath = Environment.getExternalStorageDirectory()
                                .toString()+File.separator+"testpicture";
                        File path = new File(filepath);
                        if(!path.exists())
                        {
                            path.mkdirs();
                        }
                        String fileName = Environment.getExternalStorageDirectory()
                                .toString()+File.separator+"testpicture"+File.separator+"test.jpg";
                        Mat mat = queue.take();
                        Log.d("queue","Take a face out of the faceMatArray");
                        Mat matTemp = mat.clone();
                        Imgproc.cvtColor(mat, matTemp, Imgproc.COLOR_RGBA2BGR);
                        Highgui.imwrite(fileName,matTemp);
                        Log.d(TAG,"Bitmap has saved as a file");
                        matTemp.release();
                        Log.d(TAG,"MAT HAS RELEASED");

                        JSONArray jry = null;
                        JSONObject job = null;

                        //根据fileName 寻址去检测人脸
                        jry = method.detection_detect(fileName);
                        if(jry == null)
                        {
                            continue;
                        }
                        Log.d(TAG,jry.toString());

                        //返回检测到到faceId，如果没有则跳过这个循环
                        if(jry.length()==0)
                        {
                            Log.e(TAG,"NO FACE DETECTED");
                            continue;
                        }
                        //提取检测到的faceId
                        String faceId = "";
                        try {
                            faceId = jry.getJSONObject(0).getString("id");
                            Log.d(TAG,"Detect Complete");
                            Log.d(TAG, jry.toString());
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.e(TAG,"Detect Error",e);
                        }catch(NullPointerException e){
                            e.printStackTrace();
                        }

                        jry = null;

                        //调用get_attribute返回已检测到的人脸信息
                        if(faceId.equals(""))
                        {
                            continue;
                        }

                        job = method.detection_attributes(faceId);

                        if(job == null)
                        {
                            continue;
                        }

                        Log.d(TAG,"New Feature Arrays: "+job.toString());

                        try {
                            age = job.getString("age");
                            beauty = job.getString("beauty");
                            gender = job.getJSONArray("gender").getJSONObject(0).getString("kind");
                            race = job.getJSONArray("race").getJSONObject(0).getString("kind");
                            exp = job.getJSONArray("expression").getJSONObject(0).getString("kind");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.e(TAG,"Detect Error",e);
                        }

                        job = null;

                        //调用recognition_celebrity辨识名人
                        nameCelebrityCNTemp = "";
                        nameCelebrityENTemp = "";
//						scoreCelebrityTemp = "";
                        job = method.recognition_celebrity(faceId);

                        if(job == null)
                            continue;

//						Log.e(TAG,"Celebrity Arrays: "+job.toString());
                        try {
                            scoreCelebrityTemp=job.getJSONArray("people").getJSONObject(0).getString("score");
                            nameCelebrityCNTemp=job.getJSONArray("people").getJSONObject(0).getString("chinese_name");
                            nameCelebrityENTemp=job.getJSONArray("people").getJSONObject(0).getString("english_name");
                            celebrityID = job.getJSONArray("people").getJSONObject(0).getString("id");
                        } catch (JSONException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                        //调用detection_recognize辨识人脸
                        jry = method.detection_recognize(faceId, GroupId,"3");

                        if(jry == null)
                            continue;

                        Log.d(TAG,"Recognize Result: "+jry.toString());

                        try {
                            member_JSONObject = jry.getJSONObject(0);
                            if(member_JSONObject==null)
                                continue;
                            name = member_JSONObject.getString("name");
                            memId = member_JSONObject.getString("id");
                            score = member_JSONObject.getString("score");
                            photourl = member_JSONObject.getString("photo");
                            remark = member_JSONObject.getString("remark");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Log.e(TAG,"Recognize Error",e);
                        }
                        Log.d(TAG,"Remark Information: "+remark);

                        refreshDisplay(scoreCelebrityTemp,nameCelebrityCNTemp,nameCelebrityENTemp,memId,score, mat, name, photourl, remark, celebrityID);
                        mat.release();

                    } catch (InterruptedException e) {
                        Log.e(TAG,"Take out Error");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
