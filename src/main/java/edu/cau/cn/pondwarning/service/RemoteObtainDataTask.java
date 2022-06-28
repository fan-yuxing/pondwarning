package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.*;
import edu.cau.cn.pondwarning.dao.qxdb.IotQxdevHisDataMapper;
import edu.cau.cn.pondwarning.dao.wqdb.IotDevHisDataMapper;
import edu.cau.cn.pondwarning.entity.localdb.*;
import edu.cau.cn.pondwarning.entity.qxdb.IotQxdevHisData;
import edu.cau.cn.pondwarning.entity.wqdb.IotDevHisData;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.HlsDownloader;
import edu.cau.cn.pondwarning.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RemoteObtainDataTask {

    private static final Logger logger = LoggerFactory.getLogger(RemoteObtainDataTask.class);
    private int i;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private QxdeviceMapper qxdeviceMapper;
    @Autowired
    private IotDevHisDataMapper iotDevHisDataMapper;
    @Autowired
    private DevHisDataMapper devHisDataMapper;
    @Autowired
    private IotQxdevHisDataMapper iotQxdevHisDataMapper;
    @Autowired
    private QxdevHisDataMapper qxdevHisDataMapper;
    @Autowired
    private VdeviceMapper vdeviceMapper;

    @Value("${user.root}")
    private String root;
    @Value("${python.plant}")
    private String url5;



    //每隔20分钟执行一次
    @Scheduled(cron = "0 */20 * * * ?")
    @Transactional  //事物管理的方法，如果这个方法抛出异常，事务就会回滚
    public void execute() {
        logger.info("thread id:{},RemoteObtainDataTask execute times:{}", Thread.currentThread().getId(), ++i);

        //获得水质设备信息
        List<Device> wlist=deviceMapper.selectDevice();

        //获得气象设备信息
        List<Qxdevice> qxlist=qxdeviceMapper.selectQxdevice();

        //查找水质设备的历史数据
        if(wlist!=null && wlist.size()!=0){
            logger.info("Begin Obtain Water History Data");
            for(Device device:wlist){
                List<IotDevHisData> iotdhList = iotDevHisDataMapper.selectDevHisDataByIdAndTime(device.getWdevid(),device.getLastCommTime());
                if(iotdhList!=null && iotdhList.size()!=0){
                    //记录这个设备最新数据的时间
                    deviceMapper.updateDevice(iotdhList.get(0).getDevId(),iotdhList.get(0).getCollectTime());
                    //插入这个设备的历史数据
                    for(IotDevHisData iotDevHisData:iotdhList){
                        DevHisData devHisData = new DevHisData(iotDevHisData.getId(),iotDevHisData.getDevId(),iotDevHisData.getCollectTime(),iotDevHisData.getDox(),
                                iotDevHisData.getThw(),iotDevHisData.getEc(),iotDevHisData.getPh(),iotDevHisData.getAd(),iotDevHisData.getZd(),iotDevHisData.getYw(),iotDevHisData.getYls());
                        devHisDataMapper.insertDevHisData(devHisData);
                    }
                }
                logger.info("Water devid:"+device.getWdevid()+">>>time:"+new Date().toString()+ ">>>insert data rows:"+iotdhList.size());
            }
        }

        //查找气象设备的历史数据
        if(qxlist!=null && qxlist.size()!=0){
            logger.info("Begin Obtain Qixiang History Data");
            for(Qxdevice qxdevice:qxlist){
                List<IotQxdevHisData> iotqxdhList = iotQxdevHisDataMapper.selectQxdevHisDataByIdAndTime(qxdevice.getQdevid(),qxdevice.getLastCommTime());
                if(iotqxdhList!=null && iotqxdhList.size()!=0){
                    //记录这个设备最新数据的时间
                    qxdeviceMapper.updateQxdevice(iotqxdhList.get(0).getDevId(),iotqxdhList.get(0).getCollectTime());
                    //插入这个设备的历史数据
                    for(IotQxdevHisData iotQxdevHisData:iotqxdhList){
                        QxdevHisData qxdevHisData = new QxdevHisData(iotQxdevHisData.getId(),iotQxdevHisData.getDevId(),iotQxdevHisData.getCollectTime(),
                                iotQxdevHisData.getDqsd(),iotQxdevHisData.getDqwd(),iotQxdevHisData.getDqyl(),iotQxdevHisData.getTyfs(),
                                iotQxdevHisData.getFx(),iotQxdevHisData.getTyfs(),iotQxdevHisData.getYl());
                        qxdevHisDataMapper.insertQxdevHisData(qxdevHisData);
                    }

                }
                logger.info("Qixiang devid:"+qxdevice.getQdevid()+">>>time:"+new Date().toString()+ ">>>insert data rows:"+iotqxdhList.size());
            }
        }
    }

    //每隔2小时执行一次，定时对水草进行监测
//    @Scheduled(cron = "0 */3 * * * ?")
    @Scheduled(cron = "0 0 */2 * * ?")
    @Transactional  //事物管理的方法，如果这个方法抛出异常，事务就会回滚
    public void executeVedio() {
        logger.info("thread id:{},RemoteObtainDataTask executeVedio times:{}", Thread.currentThread().getId(), ++i);
        // 首先判断当前时间是否在7点到18点之间，即在白天，夜间无法识别
        String format = "HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        String now = sf.format(new Date());
        boolean runFlag=false;
        try {
            Date nowTime = new SimpleDateFormat(format).parse(now);
            Date startTime = new SimpleDateFormat(format).parse("07:00:00");
            Date endTime = new SimpleDateFormat(format).parse("18:00:00");
            if (CommonUtil.isEffectiveDate(nowTime, startTime, endTime)) {
                runFlag = true;
                logger.info("系统时间在早上7点到下午18点之间.");
            } else {
                runFlag = false;
                System.out.println("系统时间不在早上7点到下午18点之间.");
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //系统时间在早上7点到下午18点之间时进行下一步处理
        if(runFlag){
            //获得视频设备信息
            List<Vdevice> vlist = vdeviceMapper.selectAllVdevices();
            //遍历
            if(vlist!=null && vlist.size()!=0){
                for(Vdevice vdevice : vlist){
                    // 必须设置监控区域
                    if(vdevice.getSetarea()!=null || vdevice.getSetarea()!=""){
                        String useruuid = vdevice.getUseruuid();
                        String vdevid = vdevice.getVdevid();
                        String rootPath=root+useruuid.replace("-","")+"/mp4";//存放视频和截图的地址，先放在本地
                        File dir = new File(rootPath);
                        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
                            dir.mkdir();
                        }

                        String originUrlpath = vdevice.getUrl();//这个是m3u8直播地址
                        String preUrlPath ="";
                        String fileName = "";
                        try{
                            // 获取时间信息
                            Date now1 = new Date();
                            DateFormat d8 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); //显示日期，时间（精确到分）
                            String realTime = d8.format(now1);//与SHORT风格相比，这种方式最好用

                            // 获取实时图像
                            HlsDownloader downLoader = new HlsDownloader(originUrlpath, preUrlPath, rootPath);
                            //downLoader.setThreadQuantity(10);
                            fileName = downLoader.download(false);//true用多线程下载，false不用多线程
                            if(fileName.isEmpty()){
                                logger.info(vdevid+">>>视频下载失败");
                                continue;
                            }else {
                                logger.info(vdevid+">>>视频下载成功");
                                String name = HlsDownloader.getTempPath(rootPath, fileName);//这个是视频截图方法
                                String path =rootPath+"/"+ name+".jpg";
                                // 获取监控区域
                                String setarea = vdevice.getSetarea();
                                //将数据转换为json格式
                                JSONObject object = new JSONObject();
                                object.put("path",path);
                                object.put("setarea",setarea);
                                String data= JSON.toJSONString(object);
                                //通过http请求访问Python后端，获取预测结果
                                String result= HttpRequestUtil.doPost(url5,data);

                                //解析json字符串
                                JSONObject jsonObject = JSONObject.parseObject(result);
                                int code = jsonObject.getInteger("code");
                                if(code==202){
                                    Double area = jsonObject.getDouble("area");//这里需要存入数据库+时间
                                    //将结果存入数据库+时间
                                    PlantsHis plantsHis =new PlantsHis();
                                    plantsHis.setHisid(CommonUtil.generateUUID());
                                    plantsHis.setVdevid(vdevid);
                                    plantsHis.setArea(new BigDecimal(area));
                                    plantsHis.setCollectTime(CommonUtil.strToDateLong(realTime));
                                    int row = vdeviceMapper.insertPlantsHis(plantsHis);
                                }else{
                                    logger.info(vdevid+">>>水草估算失败");
                                }
                                // 删除无用文件夹
                                boolean flag1 = HlsDownloader.DeleteFolder(rootPath + "/" + name);
                                // 删除无用文件
                                boolean flag2 = HlsDownloader.DeleteFolder(rootPath + "/" + fileName);
                                boolean flag3 = HlsDownloader.DeleteFolder(path);
                                if (flag1 && flag2 && flag3) {
                                    logger.info(vdevid+">>>删除成功");
                                } else {
                                    logger.info(vdevid+">>>删除失败");
                                    CommonUtil.delAllFile(rootPath);
                                }
                            }
                        }catch (Exception e){
                            logger.info(vdevid+">>>水草估算出错");
                        }
                    }
                }
            }
        }
    }
}
