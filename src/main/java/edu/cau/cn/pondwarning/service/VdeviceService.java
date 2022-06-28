package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.entity.localdb.Vdevice;

import java.util.List;
import java.util.Map;

public interface VdeviceService {
    Map<String,Object> selectVdevices(int current, int limit);

    Map<String, Object> selectVdevicesFew();

    Vdevice selectOneVdevice(String vdevid);

    Map<String, Object> selectVdevicesFewByPond(String ponduuid);

    Map<String, Object> getVdeiceImg(String vdevid) throws Exception;

    Map<String, Object> setVdeiceImg(String jsonData);

    List<Map<String, Object>> getHisArea(String vdevid);

    Map<String, Object> insertVdevice(Vdevice vdevice);

    Map<String, Object> deleteVdevices(String[] vdevices);

}
