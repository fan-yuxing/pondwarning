package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.DevHisDataMapper;
import edu.cau.cn.pondwarning.dao.localdb.DeviceMapper;
import edu.cau.cn.pondwarning.entity.localdb.DevHisData;
import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.Device;
import edu.cau.cn.pondwarning.entity.localdb.Page;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DevHisDataMapper devHisDataMapper;

    @Override
    public Map<String, Object> selectDevices(int current, int limit) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<DevList> devicesList = deviceMapper.selectDevices(useruuid,page.getOffset(),page.getLimit());
        page.setRows(deviceMapper.selectTotalDevice(useruuid));
        page.setPath("api/device/info");
        map.put("datalist",devicesList);
        map.put("page",page);
        return map;
    }

    @Override
    public Map<String, Object> insertDevice(Device device) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        device.setUseruuid(useruuid);
        device.setCreateTime(new Date());
        int rows=0;
        try{
            rows = deviceMapper.insertDevice(device);
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
    public Map<String, Object> selectDevHisDatas(String wdevid, int current, int limit) {
        Map<String,Object> map = new HashMap<>();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<DevHisData> dataList = devHisDataMapper.selectDevHisDatas(wdevid,page.getOffset(),page.getLimit());
        page.setRows(devHisDataMapper.selectTotalData(wdevid));
        page.setPath("api/device/hisdata");
        map.put("datalist",dataList);
        map.put("page",page);
        return map;
    }

    @Override
    public Map<String, Object> selectDevicesFew() {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        List<Device> devicesList = deviceMapper.selectDevicesFew(useruuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    public Map<String, Object> selectDevicesFewByPond(String ponduuid) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        List<Device> devicesList = deviceMapper.selectDevicesFewByPond(useruuid,ponduuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    public Device selectOneDevice(String wdevid) {
        return deviceMapper.selectOneDevice(wdevid);
    }

    @Override
    public Map<String, Object> deleteDevices(String[] wdevids) {
        System.out.println(Arrays.toString(wdevids));
        Map<String,Object> map = new HashMap<>();
        if(wdevids!=null){
            for(String wdevid :wdevids){
                System.out.println(">>>访问删除"+wdevid+"设备信息接口<<<");
                int rows=deviceMapper.deleteOneDevice(wdevid);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> uploadDeviceData(String ponduuid,String wdevid,String jsonData) {
        Map<String,Object> map = new HashMap<>();
        //解析json字符串
        JSONArray dataArray = JSONObject.parseArray(jsonData);
        System.out.println(dataArray.size());
        JSONObject jsonObject=null;
        for(int i=0;i<dataArray.size();i++){
            jsonObject=dataArray.getJSONObject(i);
            //插入数据
            try {
                String hisid = wdevid+CommonUtil.dateToStamp(jsonObject.getString("Time"));
                DevHisData devHisData = new DevHisData(hisid,wdevid,CommonUtil.strToDateLong(jsonObject.getString("Time")),jsonObject.getBigDecimal("dox"),
                        jsonObject.getBigDecimal("thw"),jsonObject.getBigDecimal("ec"),jsonObject.getBigDecimal("ph"),
                        jsonObject.getBigDecimal("ad"),jsonObject.getBigDecimal("zd"),jsonObject.getBigDecimal("yw"),jsonObject.getBigDecimal("yls"));
                devHisDataMapper.insertDevHisData(devHisData);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚事务
                System.out.println("回滚事务");
                e.printStackTrace();
                map.put(CommonConstant.STATUS_KEY,CommonConstant.UPLOAD_DATA_FAIL);
                return map;
            }
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.UPLOAD_DATA_SUCCESS);
        return map;
    }

    @Override
    public Map<String, Object> updateDevice(Device device) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        device.setUseruuid(useruuid);
        device.setCreateTime(new Date());
        int rows=0;
        try{
            rows = deviceMapper.updateOneDevice(device);
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
}
