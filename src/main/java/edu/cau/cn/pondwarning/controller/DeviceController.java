package edu.cau.cn.pondwarning.controller;


import edu.cau.cn.pondwarning.entity.localdb.Device;
import edu.cau.cn.pondwarning.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/info",method = RequestMethod.GET )
    public Map<String,Object> devicesList(int current, int limit){
        logger.info(">>>访问查询用户所有水质设备接口<<<");
        return deviceService.selectDevices(current,limit);
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST )
    public Map<String,Object> insertDeviceInfo(@RequestBody Device device){
        logger.info(">>>访问添加水质设备信息接口<<<");
        return deviceService.insertDevice(device);
    }

    @RequestMapping(value = "/hisdata",method = RequestMethod.GET )
    public Map<String,Object> selectDevHisDatas(String wdevid,int current, int limit){
        logger.info(">>>访问查询水质设备历史数据信息接口<<<");
        return deviceService.selectDevHisDatas(wdevid,current,limit);
    }

    @RequestMapping(value = "/fewinfo",method = RequestMethod.GET )
    public Map<String,Object> devicesFewList(){
        logger.info(">>>访问查询用户所有水质设备少量信息接口<<<");
            return deviceService.selectDevicesFew();
    }

    @RequestMapping(value = "/fewinfo/{ponduuid}",method = RequestMethod.GET )
    public Map<String,Object> devicesFewListOrderPond(@PathVariable String ponduuid){
        logger.info(">>>访问查询用户对应池塘所有水质设备少量信息接口<<<");
        return deviceService.selectDevicesFewByPond(ponduuid);
    }

    @RequestMapping(value = "/info/{wdevid}",method = RequestMethod.GET )
    public Device deviceInfo(@PathVariable String wdevid){
        logger.info(">>>访问查询"+wdevid+"水质设备信息接口<<<");
        return deviceService.selectOneDevice(wdevid);
    }

    @RequestMapping(value = "/info",method = RequestMethod.DELETE )
    public Map<String,Object> deleteDeviceInfo(@RequestBody String[] wdevids){
        logger.info(">>>访问删除"+"设备信息接口<<<");
        return deviceService.deleteDevices(wdevids);
    }

    @RequestMapping(value = "/upload/{ponduuid}/{wdevid}",method = RequestMethod.POST)
    public Map<String,Object> uploadDeviceData(@PathVariable String ponduuid, @PathVariable String wdevid,
                                               @RequestBody String jsonData){
        logger.info(">>>访问添加"+"设备数据接口<<<");
        return deviceService.uploadDeviceData(ponduuid,wdevid,jsonData);
    }

    @RequestMapping(value = "/info",method = RequestMethod.PUT )
    public Map<String,Object> updateDeviceInfo(@RequestBody Device device){
        logger.info(">>>访问修改"+"设备信息接口<<<");
        return deviceService.updateDevice(device);
    }

}
