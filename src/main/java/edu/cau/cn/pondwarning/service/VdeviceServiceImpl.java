package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.VdeviceMapper;
import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.Page;
import edu.cau.cn.pondwarning.entity.localdb.PlantsHis;
import edu.cau.cn.pondwarning.entity.localdb.Vdevice;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.HlsDownloader;
import edu.cau.cn.pondwarning.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.text.DateFormat;
import java.util.*;

@Service
public class VdeviceServiceImpl implements VdeviceService{
    @Value("${user.root}")
    private String root;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private VdeviceMapper vdeviceMapper;

    @Override
    public Map<String, Object> selectVdevices(int current, int limit) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<DevList> devicesList = vdeviceMapper.selectVdevices(useruuid,page.getOffset(),page.getLimit());
        page.setRows(vdeviceMapper.selectTotalVdevice(useruuid));
        page.setPath("api/vdevice/info");
        map.put("datalist",devicesList);
        map.put("page",page);
        return map;
    }

    @Override
    public Map<String, Object> selectVdevicesFew() {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        List<Vdevice> devicesList = vdeviceMapper.selectVdevicesFew(useruuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    public Map<String, Object> selectVdevicesFewByPond(String ponduuid) {
        Map<String,Object> map = new HashMap<>();
        List<Vdevice> devicesList = vdeviceMapper.selectVdevicesFewByPond(ponduuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    public Vdevice selectOneVdevice(String vdevid) {
        return vdeviceMapper.selectOneVdevice(vdevid);
    }

    @Override
    public Map<String, Object> getVdeiceImg(String vdevid){
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        String rootPath=root+useruuid.replace("-","")+"/mp4";//存放视频和截图的地址，先放在本地
        File dir = new File(rootPath);
        if (!dir.exists()) {// 判断目录是否存在，不存在则创建目录
            dir.mkdir();
        }
        Vdevice vdevice = vdeviceMapper.selectOneVdevice(vdevid);
        String originUrlpath = vdevice.getUrl();//这个是m3u8直播地址
        String preUrlPath ="";
        String fileName = "";
        try{
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
                System.out.println("name:" + name);

                String path =rootPath+"/"+ name+".jpg";
                String base64 = CommonUtil.ImageToBase64(path);
                Map<String,Integer> map1=CommonUtil.ImageWidthAndHeight(path);
                map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_SUCCESS);
                map.put("base64Img", base64);
                map.put("width", map1.get("width"));
                map.put("height", map1.get("height"));
                // 删除无用文件夹
                boolean flag1 = HlsDownloader.DeleteFolder(rootPath + "/" + name);
                // 删除无用文件
                boolean flag2 = HlsDownloader.DeleteFolder(rootPath + "/" + fileName);
                boolean flag3 = HlsDownloader.DeleteFolder(path);
                System.out.println(path);
                if (flag1 && flag2 && flag3) {
                    System.out.println("删除成功");
                } else {
                    System.out.println("删除失败");
                    CommonUtil.delAllFile(rootPath);
                }
            }
        }catch (Exception e){
            map.put(CommonConstant.STATUS_KEY, CommonConstant.GET_VDEVICE_FAIL);
            return map;
        }
        return map;
    }

    @Override
    public Map<String, Object> setVdeiceImg(String jsonData) {
        Map<String,Object> map = new HashMap<>();
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        String vdevid = jsonObject.getString("vdevid");
        String setarea = jsonObject.getString("setarea");
        System.out.println(setarea);
        int settime = jsonObject.getInteger("settime");
        Vdevice vdevice = new Vdevice();
        vdevice.setVdevid(vdevid);
        vdevice.setSetarea(setarea);
        vdevice.setSettime(settime);
        int result=vdeviceMapper.updateVdeviceSet(vdevice);
        if(result==1){
            map.put(CommonConstant.STATUS_KEY, CommonConstant.SET_VDEVICE_SUCCESS);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY, CommonConstant.SET_VDEVICE_FAIL);
        return map;
    }

    @Override
    public List<Map<String, Object>> getHisArea(String vdevid) {
        List<PlantsHis> his=vdeviceMapper.selectPlantsHis(vdevid);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map = null;
        for(PlantsHis plantsHis:his){
            map = new HashMap<>();
            DateFormat d8 = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM); //显示日期，时间（精确到分）
            map.put("日期",d8.format(plantsHis.getCollectTime()));
            map.put("水草面积",plantsHis.getArea());
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String, Object> insertVdevice(Vdevice vdevice) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        vdevice.setUseruuid(useruuid);
        vdevice.setCreateTime(new Date());
        int rows=0;
        try{
            rows = vdeviceMapper.insertVdevice(vdevice);
        }catch (Exception e){
            rows=0;
        }
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_DEVICE_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_DEVICE_SUCCESS);
        return map;
    }

    @Override
    public Map<String, Object> deleteVdevices(String[] vdevids) {
        System.out.println(Arrays.toString(vdevids));
        Map<String,Object> map = new HashMap<>();
        if(vdevids!=null){
            for(String vdevid :vdevids){
                System.out.println(">>>访问删除"+vdevid+"设备信息接口<<<");
                int rows=vdeviceMapper.deleteOneVdevice(vdevid);
                if(rows==0){
                    map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_FAIL);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚事务
                    System.out.println("回滚事务");
                    return map;
                }
            }
        }else{
            map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_SUCCESS);
        return map;
    }
}
