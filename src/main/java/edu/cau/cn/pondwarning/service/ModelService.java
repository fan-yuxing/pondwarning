package edu.cau.cn.pondwarning.service;

import java.util.Map;

public interface ModelService {

    Map<String,Object> trainTimeSeriesPreModel(String jsonData);

    Map<String,Object> timeSeriesPre(String wdevid);

    Map<String, Object> trainSpacePreModel(String jsonData);

    Map<String,Object> spacePre(String ponduuid,float depth);

    Map<String, Object> getArea(String vdevid);

    Map<String, Object> trainWarnModel(String jsonData);

    Map<String, Object> waterWarn(String ponduuid,String wdevid);
}
