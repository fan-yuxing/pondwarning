package edu.cau.cn.pondwarning.util;

public class CommonConstant {

//    /**
//     * 激活成功
//     */
//    int ACTIVATION_SUCCESS = 0;
//
//    /**
//     * 重复激活
//     */
//    int ACTIVATION_REPEAT = 1;
//
//    /**
//     * 激活失败
//     */
//    int ACTIVATION_FAILURE = 2;
//
//    /**
//     * 默认状态的登录凭证的超时时间
//     */
//    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
//
//    /**
//     * 记住状态的登录凭证超时时间
//     */
//    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;



    /**
     *  状态码标识key
     */
    public static String STATUS_KEY = "status";

    /**
     *  Token标识key
     */
    public static String TOKEN_KEY = "token";

    /**
     *  用户类型-管理员
     */
    public static int USER_TYPE_ADMIN = 0;

    /**
     *  用户类型-普通用户
     */
    public static int USER_TYPE_ORIDINARY = 1;

    /**
     *  注册失败
     */
    public static int REGISTER_FAIL = 400;

    /**
     *  注册成功
     */
    public static int REGISTER_SUCCESS = 200;

    /**
     *  登录成功
     */
    public static int LOGIN_SUCCESS = 201;

    /**
     *  登录失败
     */
    public static int LOGIN_FAIL = 401;

    /**
     *  重新登录
     */
    public static int RE_LOGIN = 300;

    /**
     *  修改用户信息成功
     */
    public static int UPDATE_USER_SUCCESS = 202;

    /**
     *  修改用户信息失败
     */
    public static int UPDATE_USER_FAIL = 402;

    /**
     *  修改池塘信息成功
     */
    public static int UPDATE_POND_SUCCESS=203;

    /**
     *  修改池塘信息失败
     */
    public static int UPDATE_POND_FAIL=403;

    /**
     *  添加池塘信息成功
     */
    public static int INSERT_POND_SUCCESS=204;

    /**
     *  添加池塘信息失败
     */
    public static int INSERT_POND_FAIL=404;

    /**
     *  删除池塘信息成功
     */
    public static int DELETE_POND_SUCCESS=205;

    /**
     *  删除池塘信息失败
     */
    public static int DELETE_POND_FAIL=405;

    /**
     *  修改图片信息成功
     */
    public static int UPDATE_IMG_SUCCESS=206;

    /**
     *  修改图片信息失败
     */
    public static int UPDATE_IMG_FAIL=406;

    /**
     *  添加图片信息成功
     */
    public static int INSERT_IMG_SUCCESS=207;

    /**
     *  添加图片信息失败
     */
    public static int INSERT_IMG_FAIL=407;

    /**
     *  添加水质设备信息成功
     */
    public static int INSERT_DEVICE_SUCCESS=208;

    /**
     *  添加水质设备信息失败
     */
    public static int INSERT_DEVICE_FAIL=408;

    /**
     *  查询视频设备画面成功
     */
    public static int GET_VDEVICE_SUCCESS=209;

    /**
     *  查询视频设备画面失败
     */
    public static int GET_VDEVICE_FAIL=409;

    /**
     *  设置视频设备配置成功
     */
    public static int SET_VDEVICE_SUCCESS=210;

    /**
     *  设置视频设备配置失败
     */
    public static int SET_VDEVICE_FAIL=410;

    /**
     *  获取水草面积成功
     */
    public static int GET_AREA_SUCCESS=211;

    /**
     *  获取水草面积失败
     */
    public static int GET_AREA_FAIL=411;

    /**
     *  模型训练成功
     */
    public static int TRAIN_SUCCESS=212;

    /**
     *  时序预测模型
     */
    public static int TIME_PRE_MODEL=0;

    /**
     *  空间预测模型
     */
    public static int SPACE_PRE_MODEL=1;

    /**
     *  水草模型
     */
    public static int PLANT_MODEL=2;

    /**
     *  预警模型
     */
    public static int WARN_MODEL=3;

    /**
     *  未完成的模型
     */
    public static int UN_FINISHED_MODEL=301;

    /**
     *  设备不能监测所有预警指标
     */
    public static int UN_MONITOR=302;

    /**
     *  模型输入数据长度不够
     */
    public static int UN_LENGTH=303;

    /**
     *  数据添加成功
     */
    public static int UPLOAD_DATA_SUCCESS=213;

    /**
     *  数据添加失败
     */
    public static int UPLOAD_DATA_FAIL=413;



}
