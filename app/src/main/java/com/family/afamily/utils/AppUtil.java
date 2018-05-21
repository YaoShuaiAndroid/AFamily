package com.family.afamily.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ys on 16/7/30.
 */

public class AppUtil {
    AppUtil appUtil;
//	/**
//	 * 通用，设置顶部布局在状态栏的下面
//	 * (设置状态栏透明之后，写的布局会覆盖在状态栏上面)
//	 * @param context
//	 * @param top_view
//	 */
//	public static void setTopLay(Context context,View regist_topLay) {
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.MATCH_PARENT, (int) context.getResources()
//						.getDimension(R.dimen.top_height));
//		params.setMargins(0, MyApplication.getStatus_height(), 0, 0);
//		regist_topLay.setLayoutParams(params);
//	}


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     *
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity){
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    // 判断有无网络
    public static boolean checkNetWork(Context context) {
        boolean flag = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo info : networkInfo) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id){
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(  String mobiles){
        boolean flag = false;
        try{
            String pattern = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
            Pattern regex = Pattern.compile(pattern);
            Matcher m = regex .matcher(mobiles);
            flag = m.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    //获取key
    public static String get_token(Context context) {
        SharedPreferences settings = context.getSharedPreferences("person",
                context.MODE_PRIVATE);
        String token = settings.getString("token", "");
        return token;
    }

    /**
     * @param context 上下文对象
     * @param key 存储键
     * @param value 存储值
     */
    //设置share存储
    public static void set_share(Context context,String key,String value) {
        SharedPreferences settings = context.getSharedPreferences("person",
                context.MODE_PRIVATE);

        SharedPreferences.Editor editor=settings.edit();
        editor.putString(key,value);
        editor.commit();
    }

    //清除key
    public static void clearToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences("person",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("token","");
        edit.commit();
    }

    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }


    // 获取屏幕宽度和高度
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    //根据时间戳获取指定格式的时间字符串yyyy-MM-dd HH:mm
    public static String getStrTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(new Date(System.currentTimeMillis()));
    }

    public static String getStrTime_year() {
        DateFormat df = new SimpleDateFormat("yyyy");

        return df.format(new Date());
    }

    //根据时间戳获取指定格式的时间字符串yyyy-MM-dd HH:mm
    public static String getStrTime1() {
        DateFormat df = new SimpleDateFormat("MM-dd");

        return df.format(new Date());
    }


    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    //毫秒转换为时间
    public static String get_time(int  time){
        Date date = new Date();
        SimpleDateFormat sdf=null;
        if(time>3600000){
             sdf = new SimpleDateFormat("HH:mm:ss");
        }else{
             sdf = new SimpleDateFormat("mm:ss");
        }

        date.setTime(time);
        String str = sdf.format(date);
        return str;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static void getStatusHeight(Activity context, View v) {
        int version = getAndroidOSVersion();
        Log.e("version", version + "");
        if (version >= 19) {
            int statusHeight = -1;
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz
                        .getField("status_bar_height").get(object).toString());
                statusHeight = context.getResources().getDimensionPixelSize(
                        height);
            } catch (Exception e) {
                e.printStackTrace();
            }
            v.setPadding(0, statusHeight, 0, 0);
        }
    }

    /**
     * 获取sdk版本
     *
     * @return
     */
    public static int getAndroidOSVersion() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }

        return osVersion;
    }

    public static String getVersionName(Context context)
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static int getVersionCode(Context context)
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int version = packInfo.versionCode;
        return version;
    }

    /**
     * 通过uri获取文件路径
     *
     * @param mUri
     * @return
     */
    public static String getFilePath(Context context, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(context,mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }


    // 获取文件路径通过url
    private static String getFilePathByUri(Context context, Uri mUri) throws FileNotFoundException {
        Cursor cursor = context.getContentResolver()
                .query(mUri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(1);
    }

    //MD5加密
    public static String getMD5Str(Map params) {
            Map treeMap = new TreeMap(params);// treeMap默认会以key值升序排序
            StringBuilder sb = new StringBuilder();
            for (Object key : treeMap.keySet()) {// 排序后的字典，将所有参数按"key=value"格式拼接在一起
                if(sb.length()==0){
                    sb.append(key).append("=").append(treeMap.get(key));
                }else{
                    sb.append("&"+key).append("=").append(treeMap.get(key));
                }
            }

            MessageDigest md5;
            byte[] bytes = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                bytes = md5.digest(sb.toString().getBytes("UTF-8"));// md5加密
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // 将MD5输出的二进制结果转换为小写的十六进制
            StringBuilder sign = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex);
            }
            return sign.toString();
        }

    /**
     *
     * 计算两个日期相差的月份数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @param pattern  日期1和日期2的日期格式
     * @return  相差的月份数
     * @throws ParseException
     */
    public static int countMonths(String date1,String date2,String pattern) {
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);

        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);

        //开始日期若小月结束日期
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }

        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH)+1;
    }

    public static String css(String html){
        String css = "<style type=\"text/css\"> img {" +
                "width:100%;" +//限定图片宽度填充屏幕
                "height:auto;" +//限定图片高度自动
                "}" +
                "body {" +
                "margin-top:10px;" +//限定网页中的文字上边距为15px(可根据实际需要进行行管屏幕适配操作)
                "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
                "}" +
                "</style>";
        html = "<html><header>" + css + "</header>" + html + "<html>";

        return html;
    }
}

