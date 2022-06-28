package edu.cau.cn.pondwarning.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /***
     * 比较两个时间
     * @now Data 当前时间
     * @validity Date 有效期
     * @thresh 时间阈值
     * @return 1：当前时间大于有效期；2：当前时间小于有效期
     * */
    public static int compareDate(Date now, Date validity,Long thresh) {
        if (validity != null) {
            try {
                Long time =validity.getTime() - now.getTime();
                if (time>thresh)
                    return 1;
                else
                    return 2;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return 2;
    }

    /**
     *  加减对应时间后的日期
     * @param date  需要加减时间的日期
     * @param amount    加减的时间(毫秒)
     * @return  加减对应时间后的日期
     */
    private Date subtractTime1(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() + amount);
            Date time = sdf.parse(strTime);
            return time;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  加减对应时间后的日期
     * @param date  需要加减时间的日期
     * @param amount    加减的时间(毫秒)
     * @return  加减对应时间后的日期
     */
    public static String subtractTime2(Date date, int amount) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strTime = sdf.format(date.getTime() + amount);
            return strTime;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  String字符串Date转Date
     */
    public static Date strToDateLong(String strDate) {
           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           ParsePosition pos = new ParsePosition(0);
           Date strtodate = formatter.parse(strDate, pos);
           return strtodate;
    }

//    /**
//     *  Date转String字符串Date
//     */
//    public static Date strToDateLong(String strDate) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ParsePosition pos = new ParsePosition(0);
//        Date strtodate = formatter.parse(strDate, pos);
//        return strtodate;
//    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 将本地图片转换为base64格式
     */
    public static String ImageToBase64(String imgPath) {
        InputStream in = null;
        byte[] data = null;// 读取图片字节数组　　
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码　　
        BASE64Encoder encoder = new BASE64Encoder();// 返回Base64编码过的字节数组字符串　　
        return encoder.encode(data);
    }


    /**
     * 获取本地图像的宽度和高度
     */
    public static Map<String,Integer> ImageWidthAndHeight(String imgPath) {
        Map<String,Integer> map = new HashMap<>();
        InputStream in = null;
        File picture=new File(imgPath);
        try {
            in = new FileInputStream(picture);
            BufferedImage sourceImg = ImageIO.read(in);
            int width=sourceImg.getWidth();
            int height=sourceImg.getHeight();
            map.put("width",width);
            map.put("height",height);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串的首字母转大写
     * @param str 需要转换的字符串
     * @return
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 时间转换成时间戳,参数和返回值都是字符串
     * @param  s
     * @return res
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws Exception {
        String res;
        //设置时间模版
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

}
