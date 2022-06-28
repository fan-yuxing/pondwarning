package edu.cau.cn.pondwarning.dao.qxdb;

import edu.cau.cn.pondwarning.entity.qxdb.IotQxdevHisData;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface IotQxdevHisDataMapper {
    IotQxdevHisData selectById(String id);

    int selectQxdevHisDataCount(String devId);

    List<IotQxdevHisData> selectQxdevHisDataByIdAndTime(String devId, Date lastCommTime);
}
