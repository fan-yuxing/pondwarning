package edu.cau.cn.pondwarning.controller;

import edu.cau.cn.pondwarning.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stpre")
public class SpatioTemporalController {

    private static final Logger logger = LoggerFactory.getLogger(SpatioTemporalController.class);

    @Autowired
    private ModelService modelService;

    @RequestMapping(value = "/traintspre",method = RequestMethod.POST)
    public Map<String,Object> trainTimeSeriesPreModel(@RequestBody String jsonData){
        logger.info(">>>访问训练时序预测模型接口<<<");
        Map<String,Object> map = modelService.trainTimeSeriesPreModel(jsonData);
        return map;
    }

    @RequestMapping(value = "/tspre/{wdevid}",method = RequestMethod.GET)
    public Map<String,Object> timeSeriesPre(@PathVariable String wdevid){
        logger.info(">>>访问时序预测接口<<<");
        Map<String,Object> map = modelService.timeSeriesPre(wdevid);
        return map;
    }

    @RequestMapping(value = "/trainspacepre",method = RequestMethod.POST)
    public Map<String,Object> trainSpacePreModel(@RequestBody String jsonData){
        logger.info(">>>访问训练时空预测模型接口<<<");
        Map<String,Object> map = modelService.trainSpacePreModel(jsonData);
        return map;
    }

    @RequestMapping(value = "/spre/{ponduuid}/{depth}",method = RequestMethod.GET)
    public Map<String,Object> spacePre(@PathVariable String ponduuid, @PathVariable float depth){
        logger.info(">>>访问空间预测接口<<<");
        Map<String,Object> map = modelService.spacePre(ponduuid,depth);
        return map;
    }
}
