package edu.cau.cn.pondwarning.dao.wqdb;

import edu.cau.cn.pondwarning.entity.wqdb.IotDevHisData;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface IotDevHisDataMapper {
    IotDevHisData selectById(String id);

    int selectDevHisDataCount(String devId);

    List<IotDevHisData> selectDevHisDataByIdAndTime(String devId, Date lastCommTime);
}
