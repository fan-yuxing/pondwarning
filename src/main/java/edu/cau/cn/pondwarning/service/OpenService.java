package edu.cau.cn.pondwarning.service;

import java.util.Map;

public interface OpenService {

    Map<String, Object> getPondList();

    Map<String, Object> getDevList(String ponduuid);

    Map<String, Object> getDataList(String wdevid);

    Map<String, Object> getVideoImg(String vdevid);
}
