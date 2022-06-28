package edu.cau.cn.pondwarning.controller;


import edu.cau.cn.pondwarning.service.OpenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
* 对外开放的接口
* */

@RestController
@RequestMapping("/open")
public class OpenController {
    private static final Logger logger = LoggerFactory.getLogger(OpenController.class);

    @Autowired
    private OpenService openService;

    @RequestMapping(value = "/pond",method = RequestMethod.GET )
    public Map<String,Object> pond(){
        logger.info(">>>开放接口--访问获取池塘列表接口<<<");
        Map<String, Object> map = openService.getPondList();
        return map;
    }

    @RequestMapping(value = "/dev",method = RequestMethod.GET )
    public Map<String,Object> device(String ponduuid){
        logger.info(">>>开放接口--访问获取池塘对应水质设备列表接口<<<");
        Map<String, Object> map = openService.getDevList(ponduuid);
        return map;
    }

    @RequestMapping(value = "/data",method = RequestMethod.GET )
    public Map<String,Object> data(String wdevid){
        logger.info(">>>开放接口--访问获取水质设备历史一天数据接口<<<");
        Map<String, Object> map = openService.getDataList(wdevid);
        return map;
    }

    @RequestMapping(value = "/video",method = RequestMethod.GET )
    public Map<String,Object> video(String vdevid){
        logger.info(">>>开放接口--访问获取视频设备实时图像接口<<<");
        Map<String, Object> map = openService.getVideoImg(vdevid);
        return map;
    }
}
