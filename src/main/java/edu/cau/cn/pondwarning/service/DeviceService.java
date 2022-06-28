package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.entity.localdb.Device;

import java.util.Map;

public interface DeviceService {
    Map<String,Object> selectDevices(int current, int limit);

    Map<String, Object> insertDevice(Device device);

    Map<String,Object> selectDevHisDatas(String wdevid, int current,int limit);

    Map<String,Object> selectDevicesFew();

    Map<String,Object>  selectDevicesFewByPond(String ponduuid);

    Device selectOneDevice(String wdevid);

    Map<String,Object> deleteDevices(String[] wdevids);

    Map<String, Object> uploadDeviceData(String ponduuid,String wdevid,String jsonData);

    Map<String, Object> updateDevice(Device device);
}
