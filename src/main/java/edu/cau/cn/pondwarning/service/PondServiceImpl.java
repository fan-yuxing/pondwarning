package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.dao.localdb.PondMapper;
import edu.cau.cn.pondwarning.entity.localdb.Page;
import edu.cau.cn.pondwarning.entity.localdb.Pond;
import edu.cau.cn.pondwarning.util.CommonConstant;
import edu.cau.cn.pondwarning.util.CommonUtil;
import edu.cau.cn.pondwarning.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class PondServiceImpl implements PondService{

    private static final Logger logger = LoggerFactory.getLogger(PondServiceImpl.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PondMapper pondMapper;

    @Override
    public Map<String,Object> selectPonds(int current,int limit) {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        Page page=new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        List<Pond> pondsList = pondMapper.selectPonds(useruuid,page.getOffset(),page.getLimit());
        page.setRows(pondMapper.selectTotalPond(useruuid));
        page.setPath("api/pond/info");
        map.put("datalist",pondsList);
        map.put("page",page);
        return map;
    }


    @Override
    public Pond selectOnePond(String ponduuid) {
        return pondMapper.selectOnePond(ponduuid);
    }

    @Override
    public Map<String,Object> updatePondInfo(Pond pond){
        Map<String,Object> map = new HashMap<>();
        pond.setCreateTime(new Date());
        int rows = pondMapper.updateOnePond(pond);
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_POND_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.UPDATE_POND_SUCCESS);
        return map;
    }

    @Override
    public Map<String,Object> insertOnePond(Pond pond){
        Map<String,Object> map = new HashMap<>();
        pond.setPonduuid(CommonUtil.generateUUID());
        pond.setUseruuid(jwtUtils.getUseruuid());
        pond.setCreateTime(new Date());
        int rows = pondMapper.insertOnePond(pond);
        if(rows==0){
            map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_POND_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.INSERT_POND_SUCCESS);
        return map;
    }

    @Override
    public Map<String,Object>  deletePonds(String[] ponduuids){
        Map<String,Object> map = new HashMap<>();
        if(ponduuids!=null){
            for(String ponduuid :ponduuids){
                logger.info(">>>访问删除"+ponduuid+"池塘信息接口<<<");
                int rows=pondMapper.deleteOnePond(ponduuid);
                if(rows==0){
                    map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_FAIL);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚事务
                    logger.info("回滚事务");
                    return map;
                }
            }
        }else{
            map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_FAIL);
            return map;
        }
        map.put(CommonConstant.STATUS_KEY,CommonConstant.DELETE_POND_SUCCESS);
        return map;
    }

    @Override
    public Map<String, Object> selectPondsFew() {
        Map<String,Object> map = new HashMap<>();
        String useruuid = jwtUtils.getUseruuid();
        List<Pond> pondsList = pondMapper.selectPondsFew(useruuid);
        map.put("datalist",pondsList);
        return map;
    }
}
