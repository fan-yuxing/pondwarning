package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.*;
import edu.cau.cn.pondwarning.entity.localdb.*;
import edu.cau.cn.pondwarning.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

@Service
public class ModelServiceImpl implements ModelService{

    @Value("${python.traintspre}")
    private String url1;

    @Value("${python.tspre}")
    private String url2;

    @Value("${python.trainspacepre}")
    private String url3;

    @Value("${python.spre}")
    private String url4;

    @Value("${python.plant}")
    private String url5;

    @Value("${python.trainwarn}")
    private String url6;

    @Value("${python.warn}")
    private String url7;

    @Value("${user.root}")
    private String root;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DevHisDataMapper devHisDataMapper;

    @Autowired
    private QxdevHisDataMapper qxdevHisDataMapper;

    @Autowired
    private PondMapper pondMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private QxdeviceMapper qxdeviceMapper;

    @Autowired
    private VdeviceMapper vdeviceMapper;

    @Autowired
    private EvaIndexMapper evaIndexMapper;


    /**
     * 训练溶解氧时序预测模型
     * @param jsonData
     * @return
     */
    @Override
    public Map<String, Object> trainTimeSeriesPreModel(String jsonData) {
        // 每个用户的模型存在自己的目录中，所以要先创建用户目录
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String, Object> map = new HashMap<>();
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        int pretime=jsonObject.getInteger("pretime");
        JSONArray jsonArray = jsonObject.getJSONArray("data1d");
        Double[][] train=new Double[jsonArray.size()][1];
        for (int i = 0; i < jsonArray.size(); i++) {
            train[i][0]=jsonArray.getJSONObject(i).getDouble("Index");
        }

        //创建模型类
        Model model=new Model();
        model.setUseruuid(useruuid);
        model.setModeltype(CommonConstant.TIME_PRE_MODEL);
        model.setMpath("TimeSeriesPreModel.h5");
        model.setDescription("溶解氧时序预测");
        model.setCreateTime(new Date());
        //检查是否已经存在与数据库中
        int count=modelMapper.selectExistModel(model);
        if(count==0){//数据库中未存在则插入
            modelMapper.insertModel(model);
        }

        String path=rootPath+modelMapper.selectModelPath(model);
        //将数据转换为json格式
        JSONObject object = new JSONObject();
        object.put("path",path);
        object.put("traindata",train);
        object.put("pretime",pretime);
        object.put("epochs",20);
        String data=JSON.toJSONString(object);
        System.out.println(data);

        map.put(CommonConstant.STATUS_KEY,CommonConstant.TRAIN_SUCCESS);

        //创建线程，异步方式，通过http请求访问Python后端，训练模型
        new TrainModelThread(modelMapper,model,data,url1).start();

//        //通过http请求访问Python后端，获取预测结果
//        String result= HttpRequestUtil.doPost(url1,data);
//        System.out.println(result);
//        //json字符串转map
//        map = JSON.parseObject(result);
        return map;
    }

    /**
     * 溶解氧时序预测
     * @param wdevid
     * @return
     */
    @Override
    public Map<String, Object> timeSeriesPre(String wdevid) {
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String, Object> map = new HashMap<>();
        List<DevHisData> dataList = devHisDataMapper.selectDevHisDatasLimit(wdevid);
        Date lastTime = dataList.get(dataList.size()-1).getCollectTime();
        Double[][] test=new Double[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            test[i][0]=dataList.get(i).getDox().doubleValue();
        }

        Model model=new Model();
        model.setUseruuid(useruuid);
        model.setModeltype(CommonConstant.TIME_PRE_MODEL);
        String path=rootPath+modelMapper.selectModelPath(model);

        //将数据转换为json格式
        JSONObject object = new JSONObject();
        object.put("path",path);
        object.put("testdata",test);
        String data=JSON.toJSONString(object);
        System.out.println(data);
        //通过http请求访问Python后端，获取预测结果
        String result= HttpRequestUtil.doPost(url2,data);
        System.out.println(result);
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(result);
        int code = jsonObject.getInteger("code");
        //json字符串转map
        map = JSON.parseObject(result);
        if(code==202){
            JSONArray jarray = jsonObject.getJSONArray("result");
            System.out.println(jarray);
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map1 =null;
            for (int i = 0; i < jarray.size(); i++) {
                map1 = new HashMap<>();
                String date=CommonUtil.subtractTime2(lastTime,600000);
                map1.put("日期",date);
                map1.put("溶解氧",jarray.getJSONArray(i).getDouble(0));
                lastTime=CommonUtil.strToDateLong(date);
                list.add(map1);
            }
            map.put("code",code);
            map.put("datalist",list);
        }else{
            map.put("code",code);
        }
        return map;
    }

    /**
     * 训练空间预测模型
     * @param jsonData
     * @return
     */
    @Override
    public Map<String, Object> trainSpacePreModel(String jsonData) {
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String, Object> map = new HashMap<>();
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        int pretime=jsonObject.getInteger("pretime");//冗余
        JSONArray dataArray = jsonObject.getJSONArray("data3d");
        JSONArray localArray = jsonObject.getJSONArray("local");
        System.out.println(jsonData);
        int indexlen1=dataArray.getJSONObject(0).size()-1;
        Double[][] train=new Double[dataArray.size()][indexlen1];
        for (int i = 0; i < dataArray.size(); i++) {
            for(int j=1;j<=indexlen1;j++){
                train[i][j-1]=dataArray.getJSONObject(i).getDouble("Local"+j);
            }
        }
        Double[][] local=new Double[localArray.size()][3];
        for (int i = 0; i < localArray.size(); i++) {
            local[i][0]=localArray.getJSONObject(i).getDouble("X");
            local[i][1]=localArray.getJSONObject(i).getDouble("Y");
            local[i][2]=localArray.getJSONObject(i).getDouble("Z");
        }

        //创建模型类
        Model model=new Model();
        model.setUseruuid(useruuid);
        model.setModeltype(CommonConstant.SPACE_PRE_MODEL);
        model.setMpath("SpacePreModel.pkl");
        model.setDescription("溶解氧空间预测");
        model.setCreateTime(new Date());
        //检查是否已经存在与数据库中
        int count=modelMapper.selectExistModel(model);
        if(count==0){//数据库中未存在则插入
            modelMapper.insertModel(model);
        }

        String path=rootPath+modelMapper.selectModelPath(model);

        //将数据转换为json格式
        JSONObject object = new JSONObject();
        object.put("path",path);
        object.put("traindata",train);
        object.put("local",local);
        object.put("epochs",20);
        String data=JSON.toJSONString(object);
        System.out.println(data);

        map.put(CommonConstant.STATUS_KEY,CommonConstant.TRAIN_SUCCESS);

        //创建线程，异步方式，通过http请求访问Python后端，训练模型
        new TrainModelThread(modelMapper,model,data,url3).start();

//        //通过http请求访问Python后端，获取预测结果
//        String result= HttpRequestUtil.doPost(url3,data);
//        System.out.println(result);
//        //json字符串转map
//        map = JSON.parseObject(result);
        return map;
    }

    /**
     * 溶解氧时空预测
     * @param ponduuid
     * @param depth
     * @return
     */
    @Override
    public Map<String, Object> spacePre(String ponduuid,float depth) {
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String, Object> map = new HashMap<>();
        //path1 时序预测模型
        Model model=new Model();
        model.setUseruuid(useruuid);
        model.setModeltype(CommonConstant.TIME_PRE_MODEL);
        String path1=rootPath+modelMapper.selectModelPath(model);
        //path2 空间预测模型
        model.setModeltype(CommonConstant.SPACE_PRE_MODEL);
        String path2=rootPath+modelMapper.selectModelPath(model);
        //池塘X、Y、Z
        Pond pond = pondMapper.selectOnePond(ponduuid);
        int X = pond.getPondlength().intValue();
        int Y = pond.getPondwidth().intValue();
        float Z = pond.getPonddepth().floatValue();
        if(depth<Z){
            Z=depth;
        }
        //testdata:中心监测点设备历史时序数据，一个池塘只能有一个中心监测点
        Device device = deviceMapper.selectCenterDevice(ponduuid);
        List<DevHisData> dataList = devHisDataMapper.selectDevHisDatasLimit(device.getWdevid());
        Date lastTime = dataList.get(dataList.size()-1).getCollectTime();

//        try{
//            lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-04-06 11:10:54");
//        }catch (Exception e){
//
//        }
        Double[][] test=new Double[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            test[i][0]=dataList.get(i).getDox().doubleValue();
        }
        //将数据转换为json格式
        JSONObject object = new JSONObject();
        object.put("path1",path1);
        object.put("path2",path2);
        object.put("testdata",test);
        object.put("X",X);
        object.put("Y",Y);
        object.put("Z",Z);
        String data=JSON.toJSONString(object);
        System.out.println(data);
        //通过http请求访问Python后端，获取预测结果
        String result= HttpRequestUtil.doPost(url4,data);
        System.out.println(result);
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(result);
        int code = jsonObject.getInteger("code");
        //json字符串转map
        map = JSON.parseObject(result);
        if(code==202){
            JSONArray jarray = jsonObject.getJSONArray("result");
            System.out.println(jarray);
            System.out.println(jarray.size());
            List<Map<String, Object>> list = new ArrayList<>();
            List timeList= new ArrayList();
            for (int i = 0; i < jarray.size(); i++) {
                String date=CommonUtil.subtractTime2(lastTime,600000);
                timeList.add(date);
                lastTime=CommonUtil.strToDateLong(date);
            }
            map.put("status",code);
            map.put("datalist",result);
            map.put("timelist",timeList);
        }else{
            map.put("status",code);
        }
        return map;
    }

    /**
     * 水草面积获取
     * @param vdevid
     * @return
     */
    @Override
    public Map<String, Object> getArea(String vdevid) {
        Map<String,Object> map = new HashMap<>();
        Vdevice vdevice = vdeviceMapper.selectOneVdevice(vdevid);
        if(vdevice.getSetarea()==null ||vdevice.getSetarea()==""){//没有设置监控区域则返回
            map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_AREA_FAIL);
            return map;
        }

        String useruuid = jwtUtils.getUseruuid();
//        String useruuid = "993a2291-9792-4b17-ae1c-b5212ac8a8c7";
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
            Date now = new Date();
            DateFormat d8 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); //显示日期，时间（精确到分）
            String realTime = d8.format(now);//与SHORT风格相比，这种方式最好用

            // 获取实时图像
            HlsDownloader downLoader = new HlsDownloader(originUrlpath, preUrlPath, rootPath);
            //downLoader.setThreadQuantity(10);
            fileName = downLoader.download(false);//true用多线程下载，false不用多线程
            if(fileName.isEmpty()){
                System.out.println("下载失败");
                map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_FAIL);
                return map;
            }else {
                System.err.println("下载成功");
                String name = HlsDownloader.getTempPath(rootPath, fileName);//这个是视频截图方法
//                System.out.println("name:" + name);
                // 实时图像存放路径
                String path =rootPath+"/"+ name+".jpg";
                // 获取监控区域
                String setarea = vdevice.getSetarea();
                //将数据转换为json格式
                JSONObject object = new JSONObject();
                object.put("path",path);
                object.put("setarea",setarea);
                String data= JSON.toJSONString(object);
                System.out.println(data);
                //通过http请求访问Python后端，获取预测结果
                String result= HttpRequestUtil.doPost(url5,data);
                System.out.println(result);

                //解析json字符串
                JSONObject jsonObject = JSONObject.parseObject(result);
                int code = jsonObject.getInteger("code");
                //json字符串转map
                map = JSON.parseObject(result);
                if(code==202){
                    // 将原图像转换为base64格式
                    String ori_base64 = CommonUtil.ImageToBase64(path);
                    Double area = jsonObject.getDouble("area");//这里需要存入数据库+时间
                    String seg_base64 = jsonObject.getString("seg_base64");
                    map.put("code",code);
                    map.put("area",area);
                    map.put("ori_base64",ori_base64);
                    map.put("seg_base64",seg_base64);
                    map.put("time",realTime);
                    map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_AREA_SUCCESS);

                    //将结果存入数据库+时间
                    PlantsHis plantsHis =new PlantsHis();
                    plantsHis.setHisid(CommonUtil.generateUUID());
                    plantsHis.setVdevid(vdevid);
                    plantsHis.setArea(new BigDecimal(area));
                    plantsHis.setCollectTime(CommonUtil.strToDateLong(realTime));
                    int row = vdeviceMapper.insertPlantsHis(plantsHis);
//                    System.out.println(row);
                }else{
                    map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_AREA_FAIL);
                }
                // 删除无用文件夹
                boolean flag1 = HlsDownloader.DeleteFolder(rootPath + "/" + name);
                // 删除无用文件
                boolean flag2 = HlsDownloader.DeleteFolder(rootPath + "/" + fileName);
                boolean flag3 = HlsDownloader.DeleteFolder(path);
                if (flag1 && flag2 && flag3) {
                    System.out.println("删除成功");
                } else {
                    System.out.println("删除失败");
                    CommonUtil.delAllFile(rootPath);
                }
                return map;
            }
        }catch (Exception e){
            map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_AREA_FAIL);
            return map;
        }
    }

    /**
     * 训练水质预警模型
     * @param jsonData
     * @return
     */
    @Override
    public Map<String, Object> trainWarnModel(String jsonData) {
        // 每个用户的模型存在自己的目录中，所以要先创建用户目录
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String,Object> map = new HashMap<>();
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        int pretime=jsonObject.getInteger("pretime");
        JSONArray dataArray = jsonObject.getJSONArray("datawd");
        JSONArray indexArray = jsonObject.getJSONArray("index");
//        System.out.println(jsonData);

        // 预测模型输入总列数
        int indexlen=dataArray.getJSONObject(0).size()-1;
//        System.out.println(indexlen);

        //预警指标数量
        int warnIndexNum=indexArray.size();

        //存放预警指标
        String[] windex = new String[warnIndexNum];
        for(int i=0;i<warnIndexNum;i++){
            windex[i]=indexArray.getString(i);
        }

        //存放非预警指标，即辅助预测指标
        String[] findex = new String[indexlen-warnIndexNum];
        Iterator<String> sIterator=dataArray.getJSONObject(0).keySet().iterator();
        int kk=0;
        while (sIterator.hasNext()) {
            // 获得key
            String key = sIterator.next();
            int i=0;
            for(String wkey : windex){
                if(key.equals(wkey) ||key.equals("Time")){
                    i++;
                }
            }
            if(i==0){
                findex[kk++]=key;
            }
        }

        //转换训练数据,预警指标在前，辅助指标在后
        Double[][] train=new Double[dataArray.size()][indexlen];
        for (int i = 0; i < dataArray.size(); i++) {
            for(int j=1;j<=warnIndexNum;j++){
                train[i][j-1]=dataArray.getJSONObject(i).getDouble(windex[j-1]);
            }
            for(int j=1;j<=indexlen-warnIndexNum;j++){
                train[i][j+warnIndexNum-1]=dataArray.getJSONObject(i).getDouble(findex[j-1]);
            }
        }

        //将使用的预警指标和辅助指标存入数据库：{"F":["dqsd","dqwd","dqyl"],"W":["dox","thw"],"P":12}
        JSONObject object1 = new JSONObject();
        object1.put("W",windex);
        object1.put("F",findex);
        object1.put("P",pretime);
        String data1=JSON.toJSONString(object1);
        System.out.println(data1);

        //创建模型类
        Model model=new Model();
        model.setUseruuid(useruuid);
        model.setModeltype(CommonConstant.WARN_MODEL);
        model.setMpath("WarnModel.h5");
        model.setModelpara(data1);
        model.setDescription("CNN-GRU");
        model.setCreateTime(new Date());
        //检查是否已经存在与数据库中
        int count=modelMapper.selectExistModel(model);
        if(count==0){//数据库中未存在则插入
            modelMapper.insertModel(model);
        }else{//数据库中存在则更新
            modelMapper.updateModel(model);
        }

        String path=rootPath+model.getMpath();

        //将数据转换为json格式
        JSONObject object2 = new JSONObject();
        object2.put("path",path);
        object2.put("pretime",pretime);
        object2.put("traindata",train);
        object2.put("indexnum",warnIndexNum);
        object2.put("epochs",20);
        String data2=JSON.toJSONString(object2);
        System.out.println(data2);
        map.put(CommonConstant.STATUS_KEY,CommonConstant.TRAIN_SUCCESS);

        //创建线程，异步方式，通过http请求访问Python后端，训练模型
        new TrainModelThread(modelMapper,model,data2,url6).start();
        return map;
    }

    /**
     * 水质预警
     * @param ponduuid
     * @param wdevid
     * @return
     */
    @Override
    public Map<String, Object> waterWarn(String ponduuid,String wdevid) {
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/models/";//存放用户模型
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }

        Map<String,Object> map = new HashMap<>();

        // 首先从models表中查找预警指标和辅助指标
        Model model =modelMapper.selectUserModel(useruuid,CommonConstant.WARN_MODEL);
        if(model==null || model.getTrain()!=1){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UN_FINISHED_MODEL);
            return map;
        }
        String para = model.getModelpara();
        JSONObject jsonObject = JSONObject.parseObject(para);
        JSONArray wJson = jsonObject.getJSONArray("W");
        JSONArray fJson = jsonObject.getJSONArray("F");
        int pretime =  jsonObject.getInteger("P");

        // 从数据库找到池塘设备
        Device device = new Device();
        device.setUseruuid(useruuid);
        device.setPonduuid(ponduuid);
        device.setWdevid(wdevid);
        device =deviceMapper.selectNeedDevice(device);

        // 利用反射检测设备是否能够监测所需预警指标
        boolean flag=true;//是否包含的标志
        Class dcalss = device.getClass();
        Field[] fields = dcalss.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
        for(int i=0;i<wJson.size();i++){
            for(Field f:fields){
                if((wJson.getString(i)+"Sts").equals(f.getName())){
                    try {
//                        System.out.println("set"+CommonUtil.captureName(f.getName()));
                        Method method = dcalss.getMethod("get"+CommonUtil.captureName(f.getName()));
                        //执行该方法
                        int re = (int)method.invoke(device);
//                        System.out.println(re);
                        if(re!=1){flag=false;break;}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //未找到则返回
        if(!flag){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UN_MONITOR);
            return map;
        }
        //设备满足条件，接下来查询设备数据，默认气象设备一定满足条件
        //水质数据
        String ws = "";
        for(int i=0;i<wJson.size();i++){
            if(i<wJson.size()-1){ws=ws+wJson.getString(i)+",";}
            else{ws=ws+wJson.getString(i);}
        }
        String sql1="select hisid,collectTime,"+ws+" from (select * from iot_devhisdata where wdevid="+wdevid+" order by collectTime DESC limit 0,"+(pretime+144)+") sub ORDER BY collectTime ASC";
        System.out.println(sql1);
        List<DevHisData> wdataList = devHisDataMapper.selectDevHisDatasLimitByDynamic(sql1);
//        System.out.println(wdataList.size());

        //气象数据
        //查找用户气象设备id
        Qxdevice qxdevice = qxdeviceMapper.selectQxdevicesByPond(ponduuid);
        String qdevid = qxdevice.getQdevid();
        String qs = "";
        for(int i=0;i<fJson.size();i++){
            if(i<fJson.size()-1){qs=qs+fJson.getString(i)+",";}
            else{qs=qs+fJson.getString(i);}
        }
        String sql2="select hisid,collectTime,"+qs+" from (select * from iot_qxdevhisdata where qdevid="+qdevid+" order by collectTime DESC limit 0,"+(pretime+144)*10+") sub ORDER BY collectTime ASC";
        System.out.println(sql2);
        List<QxdevHisData> qdataList = qxdevHisDataMapper.selectQxdevHisDatasLimitByDynamic(sql2);
//        System.out.println(qdataList.size());

        //检查数据量是否支撑预警,模型输入需要最短长度
        if(wdataList.size()<(pretime+144) || qdataList.size()<(pretime+144)*10){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UN_LENGTH);
            return map;
        }

        //水质转换为double数组
        Double[][] test = new Double[wdataList.size()][wJson.size()+fJson.size()];
        for(int i=0;i<wJson.size();i++){
            for (int j=0;j<wdataList.size();j++){
                DevHisData wd = wdataList.get(j);
                Class wcalss = wd.getClass();
                Field[] wfields = wcalss.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
                for(Field f:wfields){
                    if((wJson.getString(i)).equals(f.getName())){
                        try {
                            Method method = wcalss.getMethod("get"+CommonUtil.captureName(f.getName()));
                            //执行该方法
                            test[j][i] = ((BigDecimal)method.invoke(wd)).doubleValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        //气象转换为double数组,气象因子数据为1分钟采集一次，取10个数据额平均值，与水质因子对齐
        for(int i=wJson.size();i<wJson.size()+fJson.size();i++){
            for (int j=0;j<qdataList.size()/10;j++){
                double sum=0;
                for(int k=j*10;k<j*10+10;k++){
                    QxdevHisData qd = qdataList.get(j*10);
                    Class qcalss = qd.getClass();
                    Field[] qfields = qcalss.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
                    for(Field f:qfields){
                        if((fJson.getString(i-wJson.size())).equals(f.getName())){
                            try {
                                Method method = qcalss.getMethod("get"+CommonUtil.captureName(f.getName()));
                                //执行该方法
                                sum += ((BigDecimal)method.invoke(qd)).doubleValue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // 保留2为小数
                BigDecimal bg = new BigDecimal(sum/10);
                test[j][i] = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }

        for(Double[] d:test){
            System.out.println(Arrays.toString(d));
        }

        String path=rootPath+model.getMpath();
        //将数据转换为json格式
        JSONObject object = new JSONObject();
        object.put("path",path);
        object.put("testdata",test);
        object.put("indexs",wJson);
        object.put("pretime",pretime);
        String data=JSON.toJSONString(object);
        System.out.println(data);
        //通过http请求访问Python后端，获取预测结果
        String result= HttpRequestUtil.doPost(url7,data);
        System.out.println(result);

        //解析json字符串
        JSONObject jsonObject1 = JSONObject.parseObject(result);
        int code = jsonObject1.getInteger("code");
        //json字符串转map
        map = JSON.parseObject(result);

        if(code==202){
            List<Map<String, Object>> list1 = new ArrayList<>();
            List<Map<String, Object>> list2 = new ArrayList<>();
            Date lastTime = wdataList.get(wdataList.size()-1).getCollectTime();
            for (int i = 0; i < pretime; i++) {
                Map<String, Object> map1 = new HashMap<>();
                String date=CommonUtil.subtractTime2(lastTime,600000);
                lastTime=CommonUtil.strToDateLong(date);
                map1.put("日期",date);
                map1.put("综合预警等级",jsonObject1.getJSONArray("wqi").get(i));
                map1.put("预警阈值",3);
                list1.add(map1);

                Map<String, Object> map2 = new HashMap<>();
                map2.put("日期",date);
                for(int j=0;j<jsonObject1.getJSONArray("indexs").size();j++){
                    String indexabb=jsonObject1.getJSONArray("indexs").getString(j);
                    String indexname=evaIndexMapper.selectIndexName(indexabb);
                    map2.put(indexname,jsonObject1.getJSONObject("singlewqi").getJSONArray(indexabb).get(i));
                }
                list2.add(map2);
            }

            map.put(CommonConstant.STATUS_KEY,code);
            map.put("wqiline",list1);
            map.put("singleline",list2);
        }else{
            map.put(CommonConstant.STATUS_KEY,code);
        }
        return map;
    }

    /**
     * 此类的作用是异步访问python模型端进行模型训练，防止因模型训练时间过长导致前端堵死，
     * 在数据库模型表增加了一个是否正在训练的标志，训练完成后修改标志位，0正在训练，1训练完成，2训练失败
     */
    public class TrainModelThread extends Thread{

        private ModelMapper modelMapper;

        //训练的模型的类型-0时空预测-时序预测1时空预测-空间预测2水草识别3预警
        private Model model;

        //传递给python后端的数据
        private String jsonData;
        //python后端接口地址
        private String url;
        // 结束线程标志
        private boolean flag=true;

        public TrainModelThread(ModelMapper modelMapper,Model model,String jsonData,String url){
            this.modelMapper=modelMapper;
            this.model=model;
            this.jsonData=jsonData;
            this.url=url;
        }

        @Override
        public void run() {
            while(flag) {
                // 首先更新数据库中train的标志位0
                model.setTrain(0);
                int row=modelMapper.updateModelTrainState(model);
                if(row>0){//更新成功
                    //通过http请求访问Python后端，获取结果
                    String result= HttpRequestUtil.doPost(url,jsonData);
                    System.out.println(result);
                    //解析json字符串
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    int code = jsonObject.getInteger("code");
                    if(code==201){
                        // 训练成功，更新数据库中train的标志位1
                        model.setTrain(1);
                        modelMapper.updateModelTrainState(model);
                    }else{
                        // 训练失败，更新数据库中train的标志位2
                        model.setTrain(2);
                        modelMapper.updateModelTrainState(model);
                    }
                }
                // 结束线程
                flag=false;

                //首先判断类型，执行对应逻辑
//                if(model.getModeltype()==0){
//
//                }
//                if(model.getModeltype()==1){
//
//                }
//                if(model.getModeltype()==2){//保留，暂未用到
//
//                }
//                if(model.getModeltype()==CommonConstant.WARN_MODEL){
//
//                }
            }
        }
    }
}
