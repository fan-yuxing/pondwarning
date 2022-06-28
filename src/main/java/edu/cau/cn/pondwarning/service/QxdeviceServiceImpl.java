package edu.cau.cn.pondwarning.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.dao.localdb.QxdevHisDataMapper;
import edu.cau.cn.pondwarning.dao.localdb.QxdeviceMapper;
import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.Page;
import edu.cau.cn.pondwarning.entity.localdb.QxdevHisData;
import edu.cau.cn.pondwarning.entity.localdb.Qxdevice;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class QxdeviceServiceImpl implements QxdeviceService{

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private QxdeviceMapper qxdeviceMapper;

    @Autowired
    private QxdevHisDataMapper qxdevHisDataMapper;

    @Override
    public Map<String, Object> selectQxdevices(int current, int limit) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<DevList> devicesList = qxdeviceMapper.selectQxdevices(useruuid,page.getOffset(),page.getLimit());
        page.setRows(qxdeviceMapper.selectTotalQxdevice(useruuid));
        page.setPath("api/qxdevice/info");
        map.put("datalist",devicesList);
        map.put("page",page);
        return map;
    }

    @Override
    public Map<String, Object> selectQxdevHisDatas(String qdevid, int current, int limit) {
        Map<String,Object> map = new HashMap<>();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<QxdevHisData> dataList = qxdevHisDataMapper.selectQxdevHisDatas(qdevid,page.getOffset(),page.getLimit());
        page.setRows(qxdevHisDataMapper.selectTotalData(qdevid));
        page.setPath("api/qxdevice/hisdata");
        map.put("datalist",dataList);
        map.put("page",page);
        return map;
    }

    @Override
    public Map<String, Object> selectQxdevicesFew() {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        List<Qxdevice> devicesList = qxdeviceMapper.selectQxdevicesFew(useruuid);
        map.put("datalist",devicesList);
        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> insertQxdevice(String jsonData) {
        System.out.println(jsonData);
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        //解析json字符串
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        Qxdevice qxdevice = new Qxdevice();
        qxdevice.setQdevid(jsonObject.getString("qdevid"));
        qxdevice.setUseruuid(useruuid);
        qxdevice.setQdevname(jsonObject.getString("qdevname"));
        qxdevice.setDescription(jsonObject.getString("description"));
        qxdevice.setCreateTime(new Date());
        try{
            int row=qxdeviceMapper.insertQxdevice(qxdevice);
            if(row==1){
                List<DevList> list = new ArrayList<>();
                JSONArray dataArray = jsonObject.getJSONArray("ponduuids");
                if(dataArray.size()>0){
                    for (int i = 0; i < dataArray.size(); i++) {
                        String ponduuid=dataArray.getString(i);
                        DevList devList = new DevList();
                        devList.setPonduuid(ponduuid);
                        devList.setDevid(jsonObject.getString("qdevid"));
                        devList.setType(1);
                        list.add(devList);
                    }
                    qxdeviceMapper.insertPondQxdevice(list);
                }
                map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_DEVICE_SUCCESS);
                return map;
            }
        }catch(Exception e){
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_DEVICE_FAIL);
            return map;
        }

        map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_DEVICE_FAIL);
        return map;
    }

    @Override
    public Qxdevice selectOneQxdevice(String qdevid) {
        return qxdeviceMapper.selectOneQxdevice(qdevid);
    }

    @Override
    public Map<String, Object> deleteQxdevices(String[] qdevids) {
        System.out.println(Arrays.toString(qdevids));
        Map<String,Object> map = new HashMap<>();
        if(qdevids!=null){
            for(String qdevid :qdevids){
                System.out.println(">>>访问删除"+qdevid+"设备信息接口<<<");
                int rows1=qxdeviceMapper.deleteOneQxdevice(qdevid);
                int rows2=qxdeviceMapper.deleteQxdeviceAndPond(qdevid);
                if(rows1==0){
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
    public Map<String, Object> uploadQxdeviceData(String qdevid, String jsonData) {
        System.out.println(qdevid);
        System.out.println(jsonData);
        Map<String,Object> map = new HashMap<>();
        //解析json字符串
        JSONArray dataArray = JSONObject.parseArray(jsonData);
        System.out.println(dataArray.size());
        JSONObject jsonObject=null;
        for(int i=0;i<dataArray.size();i++){
            jsonObject=dataArray.getJSONObject(i);
            //插入数据
            try {
                String hisid = qdevid+ CommonUtil.dateToStamp(jsonObject.getString("Time"));
                QxdevHisData qxdevHisData = new QxdevHisData(hisid,qdevid,CommonUtil.strToDateLong(jsonObject.getString("Time")),
                        jsonObject.getBigDecimal("dqsd"),jsonObject.getBigDecimal("dqwd"),jsonObject.getBigDecimal("dqyl"),
                        jsonObject.getBigDecimal("fs"),jsonObject.getBigDecimal("fx"),jsonObject.getBigDecimal("tyfs"),jsonObject.getBigDecimal("yl"));
                qxdevHisDataMapper.insertQxdevHisData(qxdevHisData);
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
}
