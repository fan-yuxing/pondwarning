package edu.cau.cn.pondwarning.controller;

import edu.cau.cn.pondwarning.entity.localdb.Pond;
import edu.cau.cn.pondwarning.service.PondService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pond")
public class PondController {
    private static final Logger logger = LoggerFactory.getLogger(PondController.class);

    @Autowired
    private PondService pondService;

    @RequestMapping(value = "/info",method = RequestMethod.GET )
    public Map<String,Object> pondsList(int current,int limit){
        logger.info(">>>访问查询用户所有池塘接口<<<");
        return pondService.selectPonds(current,limit);
    }

    @RequestMapping(value = "/fewinfo",method = RequestMethod.GET )
    public Map<String,Object> pondsFewList(){
        logger.info(">>>访问查询用户所有池塘少量信息接口<<<");
        return pondService.selectPondsFew();
    }

    @RequestMapping(value = "/info/{ponduuid}",method = RequestMethod.GET )
    public Pond pondInfo(@PathVariable String ponduuid){
        logger.info(">>>访问查询"+ponduuid+"池塘信息接口<<<");
        return pondService.selectOnePond(ponduuid);
    }

    @RequestMapping(value = "/info",method = RequestMethod.PUT )
    public Map<String,Object> updatePondInfo(@RequestBody Pond pond){
        logger.info(">>>访问更新池塘信息接口<<<");
        return pondService.updatePondInfo(pond);
    }

    @RequestMapping(value = "/info",method = RequestMethod.POST )
    public Map<String,Object> insertPondInfo(@RequestBody Pond pond){
        logger.info(">>>访问添加池塘信息接口<<<");
        return pondService.insertOnePond(pond);
    }

    @RequestMapping(value = "/info",method = RequestMethod.DELETE )
    public Map<String,Object> deletePondInfo(@RequestBody String[] ponduuids){
        logger.info(">>>访问删除"+"池塘信息接口<<<");
        return pondService.deletePonds(ponduuids);
    }

}
