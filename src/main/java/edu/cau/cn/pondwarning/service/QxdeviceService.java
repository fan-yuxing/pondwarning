package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.entity.localdb.Qxdevice;

import java.util.Map;

public interface QxdeviceService {
    Map<String,Object> selectQxdevices(int current, int limit);

    Map<String, Object> selectQxdevHisDatas(String qdevid, int current, int limit);

    Map<String,Object> selectQxdevicesFew();

    Map<String, Object> insertQxdevice(String jsonData);

    Qxdevice selectOneQxdevice(String qdevid);

    Map<String, Object> deleteQxdevices(String[] qdevids);

    Map<String, Object> uploadQxdeviceData(String qdevid, String jsonData);
}
