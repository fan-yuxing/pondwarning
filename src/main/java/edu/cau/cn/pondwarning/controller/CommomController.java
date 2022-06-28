package edu.cau.cn.pondwarning.controller;

import edu.cau.cn.pondwarning.dao.localdb.CommomMapper;
import edu.cau.cn.pondwarning.entity.localdb.Image;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/commom")
public class CommomController {
    private static final Logger logger = LoggerFactory.getLogger(CommomController.class);

    @Autowired
    private CommomMapper commomMapper;

    @RequestMapping(value = "/img/{imguuid}",method = RequestMethod.GET )
    public Image selectImage(@PathVariable String imguuid){
        logger.info(">>>访问查询图片接口<<<");
        return commomMapper.selectImg(imguuid);
    }

    @RequestMapping(value = "/img",method = RequestMethod.PUT )
    public Map<String,Object> updateImage(@RequestBody Image img){
        logger.info(">>>访问更新图片接口<<<");
        Map<String, Object> map = new HashMap<>();
        img.setCreateTime(new Date());
        int rows= commomMapper.updateImg(img);
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_IMG_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_IMG_SUCCESS);
        return map;
    }

    @RequestMapping(value = "/img",method = RequestMethod.POST )
    public Map<String,Object> insertImage(@RequestBody Image img){
        logger.info(">>>访问添加图片接口<<<");
        Map<String, Object> map = new HashMap<>();
        img.setImguuid(CommonUtil.generateUUID());
        img.setCreateTime(new Date());
        int rows= commomMapper.insertImg(img);
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_IMG_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_IMG_SUCCESS);
        return map;
    }
}
