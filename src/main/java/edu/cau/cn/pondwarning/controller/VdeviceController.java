package edu.cau.cn.pondwarning.controller;

import com.alibaba.fastjson.JSONObject;
import edu.cau.cn.pondwarning.entity.localdb.Vdevice;
import edu.cau.cn.pondwarning.service.ModelService;
import edu.cau.cn.pondwarning.service.VdeviceService;
import edu.cau.cn.pondwarning.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vdevice")
public class VdeviceController {

    private static final Logger logger = LoggerFactory.getLogger(VdeviceController.class);

    @Autowired
    private VdeviceService vdeviceService;

    @Autowired
    private ModelService modelService;

    @RequestMapping(value = "/info",method = RequestMethod.GET )
    public Map<String,Object> vdevicesList(int current, int limit){
        logger.info(">>>访问查询用户所有视频设备接口<<<");
        return vdeviceService.selectVdevices(current,limit);
    }

    @RequestMapping(value = "/fewinfo",method = RequestMethod.GET )
    public Map<String,Object> vdevicesFewList(){
        logger.info(">>>访问查询用户所有视频设备少量信息接口<<<");
        return vdeviceService.selectVdevicesFew();
    }

    @RequestMapping(value = "/fewinfo/{ponduuid}",method = RequestMethod.GET )
    public Map<String,Object> vdevicesFewListByPond(@PathVariable String ponduuid){
        logger.info(">>>访问查询池塘对应视频设备少量信息接口<<<");
        return vdeviceService.selectVdevicesFewByPond(ponduuid);
    }

    @RequestMapping(value = "/info/{vdevid}",method = RequestMethod.GET )
    public Vdevice vdeviceInfo(@PathVariable String vdevid){
        logger.info(">>>访问查询"+vdevid+"摄像机信息接口<<<");
        return vdeviceService.selectOneVdevice(vdevid);
    }

    @RequestMapping(value = "/img/{vdevid}",method = RequestMethod.GET )
    public Map<String,Object> getVdevImg(@PathVariable String vdevid) throws Exception {
        logger.info(">>>访问查询"+vdevid+"摄像机实时池塘水面图像接口<<<");
        return vdeviceService.getVdeiceImg(vdevid);
    }

    @RequestMapping(value = "/img",method = RequestMethod.POST )
    public Map<String,Object> setVdevImg(@RequestBody String jsonData){
        //传到python后端的为imgpath，setarea两个字段
        //{"vdevid":"63b2af47-5bf1-49fd-b1b0-87a9c395d367","setarea":{"shapes":[{"label":"water","group_id":1,"points":[[618,272],[1098,305],[456,476]]}]},"settime":3}
        logger.info(">>>访问设置摄像机配置接口<<<");
        return vdeviceService.setVdeiceImg(jsonData);
    }

    @RequestMapping(value = "/area/{vdevid}",method = RequestMethod.GET )
    public Map<String,Object> getArea(@PathVariable String vdevid){
        logger.info(">>>访问水草面积估算接口<<<");
        return modelService.getArea(vdevid);
    }

    @RequestMapping(value = "/hisarea/{vdevid}",method = RequestMethod.GET )
    public List<Map<String,Object>> getHisArea(@PathVariable String vdevid){
        logger.info(">>>访问"+vdevid+"历史水草面积接口<<<");
        return vdeviceService.getHisArea(vdevid);
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST )
    public Map<String,Object> insertVdeviceInfo(@RequestBody Vdevice vdevice){
        logger.info(">>>访问添加摄像机设备信息接口<<<");
        return vdeviceService.insertVdevice(vdevice);
    }

    @RequestMapping(value = "/info",method = RequestMethod.DELETE )
    public Map<String,Object> deleteVdeviceInfo(@RequestBody String[] vdevids){
        logger.info(">>>访问删除"+"视频设备信息接口<<<");
        return vdeviceService.deleteVdevices(vdevids);
    }
}
