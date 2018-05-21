package com.family.afamily.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by hp2015-7 on 2017/5/9.
 */
public class MyMD5Util {
    private static final String HEX_NUMS_STR = "0123456789ABCDEF";
    private static final Integer SALT_LENGTH = 12;

    /**
     * 将16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4
                    | HEX_NUMS_STR.indexOf(hexChars[pos + 1]));
        }
        return result;
    }


    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 验证口令是否合法
     *
     * @param password
     * @param passwordInDb
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static boolean validPassword(String password, String passwordInDb)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //将16进制字符串格式口令转换成字节数组
        byte[] pwdInDb = hexStringToByte(passwordInDb);
        //声明盐变量
        byte[] salt = new byte[SALT_LENGTH];
        //将盐从数据库中保存的口令字节数组中提取出来
        System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH);
        //创建消息摘要对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        //将盐数据传入消息摘要对象
        md.update(salt);
        //将口令的数据传给消息摘要对象
        md.update(password.getBytes("UTF-8"));
        //生成输入口令的消息摘要
        byte[] digest = md.digest();
        //声明一个保存数据库中口令消息摘要的变量
        byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH];
        //取得数据库中口令的消息摘要
        System.arraycopy(pwdInDb, SALT_LENGTH, digestInDb, 0, digestInDb.length);
        //比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
        if (Arrays.equals(digest, digestInDb)) {
            //口令正确返回口令匹配消息
            return true;
        } else {
            //口令不正确返回口令不匹配消息
            return false;
        }
    }


    /**
     * 获得加密后的16进制形式口令
     *
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getEncryptedPwd(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //声明加密后的口令数组变量
        byte[] pwd = null;
        //随机数生成器
        SecureRandom random = new SecureRandom();
        //声明盐数组变量
        byte[] salt = new byte[SALT_LENGTH];
        //将随机数放入盐变量中
        random.nextBytes(salt);

        //声明消息摘要对象
        MessageDigest md = null;
        //创建消息摘要
        md = MessageDigest.getInstance("MD5");
        //将盐数据传入消息摘要对象
        md.update(salt);
        //将口令的数据传给消息摘要对象
        md.update(password.getBytes("UTF-8"));
        //获得消息摘要的字节数组
        byte[] digest = md.digest();

        //因为要在口令的字节数组中存放盐，所以加上盐的字节长度
        pwd = new byte[digest.length + SALT_LENGTH];
        //将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
        //将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
        //将字节数组格式加密后的口令转化为16进制字符串格式的口令
        return byteToHexString(pwd);
    }

    /**
     * 返回经过加密后的字符串
     *
     * @param pwd
     * @return
     */
    public final static String MD5(String pwd) {
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = pwd.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] arg) {
        // System.out.println(MD5("key=e10adc3949ba59abbe56e057f20f883e&phone=13725562154&token=ba40f9d218baa58639a92c6fc204ab18"));
//        String ids = "0496FF29AC07276A5A1ACCE90F1664C91600000000000001,04380C633913766A206341BA40F68F197A00000000000001,04374F093935260F479A902F0636B098E600000000000001,048DA0D9BB4F7740E631D0053C4E9602C300000000000001,04DB3CD84DC3081A8007FA304960E2338B00000000000001,0446CE7EB74A8B8C8C65237CFB9446644F00000000000001,0429A04CBF7DB22C0A844464A3342CD2EA00000000000001,04603AD27CD77717D69DC6BE4C9D34A3F500000000000001,044C3C37B41F552A9EE581FB1D536BF25E00000000000001,04CEA1F37EE9F48581A89AD5E89975DF8A00000000000001,0491094A68FBBBE1D639B512D138446E7900000000000001,047BA2D270DA7561A9BBEA4BE05505B74D00000000000001,04C0992A8E7AD78C21707C6F31BEE5D8B400000000000001,045C3D4224AB6C642C2E49F7F16BD6722400000000000001,0496CA9A4DFE0C6CF4739CCA8F6FADA98600000000000001,04158E424291B363BBFCEEE4CD9033E41B00000000000001,049C685C5ED18BF8A124B881B8749E70FE00000000000001,044AB412128B24DBE9F1F509EF17EB3CB700000000000001,049937B6C3ED09DE000605A9F59A98731700000000000001,04051828A8BDBCEDCB36BA9AFA04508F7E00000000000001,04A3B0DB8F940FCCCDAFA63329B29D8CD100000000000001,041E6B1B4E71B86E1D42ABCDF4AB07C03700000000000006";
//        String []sinaIds = ids.split(",");
//        System.out.println(sinaIds.length);
//        String qh = "11dsffd1";
//        System.out.println(Utils.isPassWord(qh));

//        Map<String,String> map = new HashMap<>();
//        map.put("phone","13725562154");
//        map.put("password","a12345678");
//        Map<String,String> map1 = Utils.getParams(map);
//        for (Map.Entry<String, String> entry: map1.entrySet()) {
//           // map1.append(entry.getKey() +"=" +entry.getValue() +"&");
//            System.out.println(entry.getKey() +"=" +entry.getValue());
//        }

//        String json = "{\"ret_code\":1,\"msg\":\"\",\"data\":[{\"id_\":\"9\",\"username\":\"\\u738b\\u5b87\",\"mobile\":\"\",\"waiter\":[]}]}";
//        Gson g = new Gson();
//        ResponseListBean<Frag3ListData> data = g.fromJson(json,new TypeToken<ResponseListBean<Frag3ListData>>(){}.getType());
//        System.out.println(data.getData().get(0).toString());
//        System.out.println(data.getData().get(0).getWaiter() == null);
//        System.out.println(data.getData().get(0).getWaiter().size());

//        List<SignData> list = Utils.getSignData();
//        for (int i = 0; i <list.size() ; i++) {
//            System.out.println(list.get(i).toString());
//        }
        Utils.getSignData();

    }
}
