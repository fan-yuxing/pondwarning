package edu.cau.cn.pondwarning;

//import com.alibaba.fastjson.JSON;
//import edu.cau.cn.pondwarning.dao.UserMapper;
//import edu.cau.cn.pondwarning.entity.User;
//
//import edu.cau.cn.pondwarning.util.CommonUtil;
//import edu.cau.cn.pondwarning.primary.dao.PriUserMapper;
//import edu.cau.cn.pondwarning.primary.entity.PriUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.*;
import edu.cau.cn.pondwarning.dao.qxdb.IotQxdevHisDataMapper;
import edu.cau.cn.pondwarning.dao.wqdb.IotDevHisDataMapper;
//import edu.cau.cn.pondwarning.secondary.dao.SecUserMapper;
//import edu.cau.cn.pondwarning.secondary.entity.IotDevHisData;
//import edu.cau.cn.pondwarning.secondary.entity.SecUser;
import edu.cau.cn.pondwarning.entity.localdb.*;
import edu.cau.cn.pondwarning.entity.qxdb.IotQxdevHisData;
import edu.cau.cn.pondwarning.entity.wqdb.IotDevHisData;
import edu.cau.cn.pondwarning.service.ModelService;
import edu.cau.cn.pondwarning.service.VdeviceService;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.HlsDownloader;
import edu.cau.cn.pondwarning.util.HttpRequestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = PondwarningApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VdeviceMapper vdeviceMapper;

//    @Autowired
//    private PriUserMapper priUserMapper;

//    @Autowired
//    private SecUserMapper secUserMapper;

    @Autowired
    private IotDevHisDataMapper iotDevHisDataMapper;

    @Autowired
    private DevHisDataMapper devHisDataMapper;

    @Autowired
    private QxdevHisDataMapper qxdevHisDataMapper;
//
//    @Autowired
//    private IotQxdevHisDataMapper iotQxdevHisDataMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private QxdeviceMapper qxdeviceMapper;

    @Autowired
    private IotQxdevHisDataMapper iotQxdevHisDataMapper;

    @Autowired
    private PondMapper pondMapper;

    @Autowired
    private ModelService modelService;

    @Autowired
    private VdeviceService vdeviceService;


//    @Test
//    public void testInsertUser() {
//        User user =new User();
//        user.setUseruuid(CommonUtil.generateUUID());
//        user.setUsername("ttt");
//        String salt=CommonUtil.generateUUID().replace("-","");
//        user.setPassword(CommonUtil.md5("12345"+salt));
//        user.setSalt(salt);
//        user.setUsertype(1);
//        user.setCreateTime(new Date());
//        int rows=userMapper.insertUser(user);
//        System.out.println(rows);
//    }
//
//    @Test
//    public void testDate() {
//        Long a=new Date().getTime();
//        Long b=new Date().getTime()+60000;
//        System.out.println(new Date().getTime());
//        System.out.println(b-a);
//    }
//    @Test
//    public void testMap() {
//        User user =new User();
//        user.setUseruuid(CommonUtil.generateUUID());
//        user.setUsername("ttt");
//        // 将 实体类 转换为 Map
//        Map<String,Object> map = JSON.parseObject(JSON.toJSONString(user), Map.class);
//        map.put("dsa","da");
//        System.out.println(map);
//    }

//    @Test
//    public void testMultiDb() {
//        User user = userMapper.selectByName("fan");
//        System.out.println("user:"+user.toString());
//        System.out.println("++++++++++++++++++++++++++++");
//
////        PriUser priuser =priUserMapper.selectByName("fan");
////        System.out.println("priuser:"+priuser.toString());
////        System.out.println("++++++++++++++++++++++++++++");
//
////        SecUser secuser =secUserMapper.selectByName("ttt");
////        System.out.println("secuser:"+secuser.toString());
////        System.out.println("++++++++++++++++++++++++++++");
//
//
////        IotDevHisData iotDevHisData =iotDevHisDataMapper.selectById("11202003007211220100008");
////        System.out.println("iotDevHisData:"+iotDevHisData.toString());
////        System.out.println("++++++++++++++++++++++++++++");
//
//        int count =iotDevHisDataMapper.selectDevHisDataCount("11202003007");
//        System.out.println("++++++++++++++++++++++++++++");
//        System.out.println("iotDevHisData:"+count);
//        System.out.println("++++++++++++++++++++++++++++");
//
//        int count2 =iotQxdevHisDataMapper.selectQxdevHisDataCount("64720111568");
//        System.out.println("++++++++++++++++++++++++++++");
//        System.out.println("iotDevHisData:"+count2);
//        System.out.println("++++++++++++++++++++++++++++");
//
//    }

    @Test
    public void testDevice() {
//        List<Device> list=deviceMapper.selectDevice();
//        for(Device device:list){
//            System.out.println(device.toString());
//        }
//
//        List<Qxdevice> qlist=qxdeviceMapper.selectQxdevice();
//        for(Qxdevice qxdevice:qlist){
//            System.out.println(qxdevice.toString());
//        }
//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            List<IotDevHisData> list = iotDevHisDataMapper.selectDevHisDataByIdAndTime("11202003004",simpleDateFormat.parse("2021-12-19 10:50:08"));
//            for(IotDevHisData iotDevHisData:list){
//                System.out.println(iotDevHisData.toString());
//            }
//        }catch (Exception e){
//
//        }

//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            int rows=devHisDataMapper.insertDevHisData(new DevHisData("11202003001200102091931","11202003001",simpleDateFormat.parse("2022-01-23 17:07:53"), new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12)));
//            System.out.println(rows);
//        }catch (Exception e){
//
//        }
//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            int rows=deviceMapper.updateDevice("11202003003",simpleDateFormat.parse("2022-01-22 17:07:53"));
//            System.out.println(rows);
//        }catch (Exception e){
//
//        }

//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            List<IotQxdevHisData> iotqxdhList = iotQxdevHisDataMapper.selectQxdevHisDataByIdAndTime("64720111568",simpleDateFormat.parse("2022-01-30 18:50:15"));
//            IotQxdevHisData sa = iotqxdhList.get(0);
//            System.out.println(sa.toString());
//            for(IotQxdevHisData iotQxdevHisData:iotqxdhList){
//                System.out.println(iotQxdevHisData.toString());
//            }
//        }catch (Exception e){
//
//        }

//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            int rows=qxdeviceMapper.updateQxdevice("64720111568",simpleDateFormat.parse("2022-01-21 17:07:53"));
//            System.out.println(rows);
//        }catch (Exception e){
//
//        }

//        try{
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            int rows=qxdevHisDataMapper.insertQxdevHisData(new QxdevHisData("1642928928805","64720111568",simpleDateFormat.parse("2022-01-23 17:07:53"), new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12),new BigDecimal(12)));
//            System.out.println(rows);
//        }catch (Exception e){
//
//        }
        List<DevList> list=deviceMapper.selectDevices("17c960e0-71ee-47c8-97a5-a116f3edb52d",0,1);
        for(DevList device: list){
            System.out.println(device.toString());
        }

        System.out.println(deviceMapper.selectTotalDevice("17c960e0-71ee-47c8-97a5-a116f3edb52d"));

    }

    @Test
    public void testPond() {
        List<Pond> list=pondMapper.selectPonds("17c960e0-71ee-47c8-97a5-a116f3edb52d",0,1);
        for(Pond pond: list){
            System.out.println(pond.toString());
        }

    }
    @Test
    public void time(){
//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date a = sdf.parse("2022-01-21 17:07:53");
//            String strTime = sdf.format(a.getTime() + 600000);
//            System.out.println(strTime);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println(CommonUtil.strToDateLong("2022-01-21 17:07:53"));

    }

    @Test
    public void testpre(){
        modelService.timeSeriesPre("11202003004");

    }

    @Test
    public void testhls() throws Exception {
        long startTime=System.currentTimeMillis();   //获取开始时间
        //测试类
        String rootPath = "F:\\workspace\\sysfile\\MP4";//存放视频和截图的地址，先放在本地
        String originUrlpath = "https://cmgw-vpc.lechange.com:8890/LCO/7F05E7CPAZEA975/4/0/20220113T053102/523e0031c9731d81a1fe48aab541b38d.m3u8?proto=https";//这个是m3u8直播地址
        String preUrlPath ="";
        String fileName = "";
        File targetFile = new File(rootPath);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        HlsDownloader downLoader = new HlsDownloader(originUrlpath, preUrlPath, rootPath);
        //downLoader.setThreadQuantity(10);
        fileName = downLoader.download(false);//true用多线程下载，false不用多线程

        if(fileName.isEmpty()){
            System.out.println("下载失败");

        }else{
            System.err.println("下载成功");

            String name = HlsDownloader.getTempPath(rootPath, fileName);//这个是视频截图方法
            System.out.println("name:"+name);

            // 删除无用文件夹
            boolean flag1 = HlsDownloader.DeleteFolder(rootPath+"\\"+name);
            System.out.println(rootPath+"\\"+name);
            // 删除无用文件
            boolean flag2 = HlsDownloader.DeleteFolder(rootPath+"\\"+fileName);
            System.out.println(rootPath+"\\"+fileName);
            if(flag1 && flag2){
                System.out.println("删除成功");
            }else{
                System.out.println("删除失败");
            }
            long endTime=System.currentTimeMillis(); //获取结束时间

            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        }

    }

    @Test
    public void testbase64(){
        String path = "F:\\workspace\\sysfile\\MP4\\45235d76fbbb473bb7e2c6dbc14c1ffc_img.jpg";
        String s = CommonUtil.ImageToBase64(path);
        System.out.println(s);
        System.out.println("le:"+s.length());
    }


    @Test
    public void plant(){
        String vdevid = "63b2af47-5bf1-49fd-b1b0-87a9c395d367";
//        Vdevice vdevice = vdeviceMapper.selectOneVdevice(vdevid);
//        //将数据转换为json格式
//        JSONObject object = new JSONObject();
//        object.put("path","F:/workspace/sysfile/images/100910_2.jpg");
//        object.put("setarea",vdevice.getSetarea());
//        String data= JSON.toJSONString(object);
//        System.out.println(data);
//        //通过http请求访问Python后端，获取预测结果
//        String result= HttpRequestUtil.doPost("http://localhost:8777/api/plant",data);
//        System.out.println(result);

//        Map<String, Object> map=modelService.getArea(vdevid);
//        System.out.println(map.toString());


//        PlantsHis plantsHis =new PlantsHis();
//        plantsHis.setHisid("1");
//        plantsHis.setVdevid(vdevid);
//        plantsHis.setArea(new BigDecimal(12.3));
//        plantsHis.setCollectTime("2");
//        int row = vdeviceMapper.insertPlantsHis(plantsHis);
//        System.out.println(row);

//        List<PlantsHis> a=vdeviceMapper.selectPlantsHis(vdevid);
//        System.out.println(a.toString());
        String format = "HH:mm:ss";
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        String now = sf.format(new Date());
        boolean runFlag=false;
        try {
            Date nowTime = new SimpleDateFormat(format).parse(now);
            Date startTime = new SimpleDateFormat(format).parse("09:00:00");
            Date endTime = new SimpleDateFormat(format).parse("21:00:00");
            if (CommonUtil.isEffectiveDate(nowTime, startTime, endTime)) {
                runFlag = true;
                System.out.println("系统时间在早上9点到下午17点之间.");
            } else {
                runFlag = false;
                System.out.println("系统时间不在早上9点到下午17点之间.");
            }
        } catch (java.text.ParseException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void gitTest(){
        System.out.println("gitTest1");
        System.out.println("gitTest2");
        System.out.println("gitTest3");
        System.out.println("hot-fix test");
    }

}
