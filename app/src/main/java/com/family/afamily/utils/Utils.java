package com.family.afamily.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.family.afamily.R;
import com.family.afamily.entity.SignData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by hp2015-7 on 2017/3/16.
 */
public class Utils {

    static Toast toast = null;

    public static void showMToast(Context context, CharSequence msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//        System.out.println("w"+bitmap.getWidth());
//        System.out.println("h"+bitmap.getHeight());
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        Utils.showMToast(context, context.getString(R.string.network_error));
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 保存数据
     *
     * @param obj
     * @param path
     */
    public static void save(Object obj, String path) {
        try {
            File f = new File(path);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            //  L.e("保存成功----  》", obj + " --- -- " + path);
        } catch (IOException e) {
            //  L.e("保存失败----  》", obj + " --- -- " + path);
        }
    }

    /**
     * 读取保存数据
     *
     * @param path
     * @return
     */
    public static Object load(String path) {
        Object obj = null;
        File file = new File(path);
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    obj = ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ois.close();
            }
            // L.e("读取数据---ok------》", obj + "");
        } catch (IOException e) {
            e.printStackTrace();
            // L.e("读取数据--error-----》", obj + "");
        }
        return obj;
    }

    /**
     * 判断是否是身份证
     *
     * @param id
     * @return
     */
    public static boolean isIDCar(String id) {
        String reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|(X|x))$";
        // if (TextUtils.isEmpty(id))
        //     return false;
        return id.matches(reg);
    }

    public static boolean isBankCar(String bank) {
        String reg = "^\\d{16,21}$";
//        if (TextUtils.isEmpty(bank))
//            return false;
        return bank.matches(reg);
    }

    /**
     * 判断字符串是否为合法手机号 11位 13 14 15 17 18 19开头
     *
     * @param str
     * @return boolean
     */
    public static boolean isMobile(String str) {
//        if (TextUtils.isEmpty(str))
//            return false;
        return str.matches("^(13|14|15|17|18|19)\\d{9}$");
    }

    static String pws = "^(?![^a-zA-Z]+$)(?!\\D+$)[a-zA-Z0-9]{6,20}$";//必须字母数字8-20位
    private final static Pattern password = Pattern.compile(pws);

    /**
     * 判断是否是区号
     *
     * @param qh
     * @return
     */
    public static boolean isQuHao(String qh) {
        String reg = "\\(\\d{3,4}\\)|\\d{3,4}";
        return qh.matches(reg);
    }

    /**
     * 判断是否是固话
     *
     * @param gh
     * @return
     */
    public static boolean isTelNumber(String gh) {
        String reg = "\\d{7,14}";
        return gh.matches(reg);
    }

    /**
     * 密码是否规范
     *
     * @param pw
     * @return
     */
    public static boolean isPassWord(CharSequence pw) {
        return password.matcher(pw).matches();
    }

    /**
     * 给控件边距加上状态栏高度
     *
     * @param view
     * @param activity
     */
    public static void setImmerseLayout(View view, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusHeight(activity);
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    /**
     * 图片大小设置和压缩
     *
     * @param is
     * @return
     */
    public static byte[] InputStream2Bytes(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);

        bitmap = zoomImage(bitmap, 1080, 1280);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    public static byte[] bmpToByteArray(final Bitmap bbmp, final boolean needRecycle) {
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bbmp, 150, 150, true);
        bbmp.recycle();

        int i;
        int j;
        if (thumbBmp.getHeight() > thumbBmp.getWidth()) {
            i = thumbBmp.getWidth();
            j = thumbBmp.getWidth();
        } else {
            i = thumbBmp.getHeight();
            j = thumbBmp.getHeight();
        }
        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(thumbBmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                thumbBmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = thumbBmp.getHeight();
            j = thumbBmp.getHeight();
        }

    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /*
    * 判断设备 是否使用代理上网
    * */
    private boolean isWifiProxy(Context context) {

        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

        String proxyAddress;

        int proxyPort;

        if (IS_ICS_OR_LATER) {

            proxyAddress = System.getProperty("http.proxyHost");

            String portStr = System.getProperty("http.proxyPort");

            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));

        } else {

            proxyAddress = android.net.Proxy.getHost(context);

            proxyPort = android.net.Proxy.getPort(context);

        }

        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);

    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 文件转base64
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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


    /**
     * 处理请求参数
     *
     * @param params 请求参数
     * @return 返回处理好的参数
     */
    public static Map<String, String> getParams(Map<String, String> params) {
        //添加加密串
        params.put("key", "e10adc3949ba59abbe56e057f20f883e");
        //排序参数
        params = sortMapByKey(params);
        StringBuffer param = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue();
            param.append(entry.getKey() + "=" + value + "&");
        }
        //去掉结尾的&符号，得到加密串
        String key = param.deleteCharAt(param.length() - 1).toString();
        // L.e("tag", "签名字符串:" + key);
        //md5加密，得到签名值
        String sign = MyMD5Util.MD5(key);
        //移除加密key
        params.remove("key");
        //加上签名参数
        params.put("sign", sign);
        return params;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty())
            return null;
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }


    public static List<SignData> getSignData() {
        List<SignData> list = new ArrayList<>();
        Calendar data = Calendar.getInstance();
        int toDay = data.get(Calendar.DAY_OF_MONTH);
        int day = data.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = data.get(Calendar.MONTH) + 1;

        System.out.println(toDay + "--" + day + "-----" + month);

        for (int i = 1; i <= day; i++) {
            SignData sign = new SignData();
            if (toDay == i) {
                sign.setClick(true);
            } else {
                sign.setClick(false);
            }
            sign.setDay(i);
            String str = i < 10 ? "0" + i : i + "";
            String strMonth = month < 10 ? "0" + month : month + "";
            sign.setDayStr(data.get(Calendar.YEAR) + "-" + strMonth + "-" + str);
            System.out.println(sign.toString());
            list.add(sign);
        }
        return list;
    }


    public static List<Map<String, String>> getMonthTime() {
        List<Map<String, String>> list = new ArrayList<>();
        Calendar data = Calendar.getInstance();
        int td = data.get(Calendar.DAY_OF_MONTH);
        int year = data.get(Calendar.YEAR);
        int month = data.get(Calendar.MONTH);
        for (int i = 0; i < 30; i++) {
            data.set(year, month, td + i);
            int day = data.get(Calendar.DAY_OF_MONTH);
            int week = data.get(Calendar.DAY_OF_WEEK) - 1;
            Map<String, String> map = new HashMap<>();
            String weekStr = week == 0 ? "周末" : week == 1 ? "周一" : week == 2 ? "周二" : week == 3 ? "周三" : week == 4 ? "周四" : week == 5 ? "周五" : "周六";
            map.put("date", data.get(Calendar.YEAR) + "-" + (data.get(Calendar.MONTH) + 1) + "-" + day);
            map.put("day", day + "");
            map.put("week", weekStr);
            list.add(map);
            //System.out.println("今天是"+day+"号,周"+ week +" , 月="+(data.get(Calendar.MONTH)+1)+" ,年="+data.get(Calendar.YEAR));
        }
        return list;
    }

    private static Toast mToast;

    public static void myToas(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toas_coupon_ok, null);
        TextView textView = v.findViewById(R.id.my_toas_tv);
        textView.setText(text);
        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setDuration(duration);
        mToast.setView(v);
        mToast.show();
    }


    /**
     * 获取指定文件大小(单位：兆)
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static float getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        float size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                if (fis.available() > 0) {
                    size = (float) fis.available() / 1024 / 1024;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }


    public static int getFileSizeByte(File file) {
        if (file == null) {
            return 0;
        }
        int size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                if (fis.available() > 0) {
                    size = fis.available();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 格式化float，保留两位小数
     *
     * @param flt
     * @return
     */
    public static String floatFormat(float flt) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        //构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(flt);
        return p;
    }

}
