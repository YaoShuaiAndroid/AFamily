package com.family.afamily.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hp2015-7 on 2018/1/27.
 */

public class SignUtils {

    /**
     * 处理请求参数
     *
     * @param params 请求参数
     * @return 返回处理好的参数
     */
    public static String getParams(Map<String, String> params) {
        //添加加密串
        params.put("key", "e10adc3949ba59abbe56e057f20f883e");
        //排序参数
        params = sortMapByKey(params);
        StringBuffer param = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            param.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        //去掉结尾的&符号，得到加密串
        String key = param.deleteCharAt(param.length() - 1).toString();
        // L.e("tag", "签名字符串:" + key);
        //md5加密，得到签名值
        return MyMD5Util.MD5(key);

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
        Map<String, String> sortMap = new TreeMap<String, String>(new Utils.MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }


}