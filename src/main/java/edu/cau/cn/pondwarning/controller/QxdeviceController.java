package edu.cau.cn.pondwarning.controller;


import edu.cau.cn.pondwarning.entity.localdb.Qxdevice;
import edu.cau.cn.pondwarning.service.QxdeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/qxdevice")
public class QxdeviceController {
    private static final Logger logger = LoggerFactory.getLogger(QxdeviceController.class);

    @Autowired
    private QxdeviceService qxdeviceService;

    @RequestMapping(value = "/info",method = RequestMethod.GET )
    public Map<String,Object> qxdevicesList(int current, int limit){
        logger.info(">>>访问查询用户所有气象设备接口<<<");
        return qxdeviceService.selectQxdevices(current,limit);
    }

    @RequestMapping(value = "/hisdata",method = RequestMethod.GET )
    public Map<String,Object> selectQxdevHisDatas(String qdevid, int current, int limit){
        logger.info(">>>访问查询气象设备历史数据信息接口<<<");
        return qxdeviceService.selectQxdevHisDatas(qdevid,current,limit);
    }

    @RequestMapping(value = "/fewinfo",method = RequestMethod.GET )
        public Map<String,Object> qxdevicesFewList(){
            logger.info(">>>访问查询用户所有气象设备少量信息接口<<<");
            return qxdeviceService.selectQxdevicesFew();
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST )
    public Map<String,Object> insertQxdeviceInfo(@RequestBody String jsonData){
        logger.info(">>>访问添加气象设备信息接口<<<");
        return qxdeviceService.insertQxdevice(jsonData);
    }


    @RequestMapping(value = "/info/{qdevid}",method = RequestMethod.GET )
    public Qxdevice qxdeviceInfo(@PathVariable String qdevid){
        logger.info(">>>访问查询"+qdevid+"气象设备信息接口<<<");
        return qxdeviceService.selectOneQxdevice(qdevid);
    }

    @RequestMapping(value = "/info",method = RequestMethod.DELETE )
    public Map<String,Object> deleteQxdeviceInfo(@RequestBody String[] qdevids){
        logger.info(">>>访问删除"+"气象设备信息接口<<<");
        return qxdeviceService.deleteQxdevices(qdevids);
    }

    @RequestMapping(value = "/upload/{qdevid}",method = RequestMethod.POST)
    public Map<String,Object> uploadQxdeviceData(@PathVariable String qdevid,
                                               @RequestBody String jsonData){
        logger.info(">>>访问添加"+"气象设备数据接口<<<");
        return qxdeviceService.uploadQxdeviceData(qdevid,jsonData);
    }
}
