package edu.cau.cn.pondwarning.controller;

import edu.cau.cn.pondwarning.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/warn")
public class WarnController {

    private static final Logger logger = LoggerFactory.getLogger(WarnController.class);

    @Autowired
    private ModelService modelService;

    @RequestMapping(value = "/trainwarn",method = RequestMethod.POST)
    public Map<String,Object> trainWarnModel(@RequestBody String jsonData){
        logger.info(">>>访问训练预警模型接口<<<");
        Map<String,Object> map = modelService.trainWarnModel(jsonData);
        return map;
    }

    @RequestMapping(value = "/warn/{ponduuid}/{wdevid}",method = RequestMethod.GET)
    public Map<String,Object> waterWarn(@PathVariable String ponduuid, @PathVariable String wdevid){
        logger.info(">>>访问预警接口<<<");
        Map<String,Object> map = modelService.waterWarn(ponduuid,wdevid);
        return map;
    }

}
