package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.DevHisData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DevHisDataMapper {
    int insertDevHisData(DevHisData devHisData);

    //查找设备的所有数据
    List<DevHisData> selectDevHisDatas(String wdevid, int offset, int limit);

    //查找设备的历史数据数量
    int selectTotalData(String wdevid);

    //按照时间正序查找设备的近期数据
    List<DevHisData> selectDevHisDatasLimit(String wdevid);

    //执行拼接SQL的方法
    List<DevHisData> selectDevHisDatasLimitByDynamic(@Param(value = "sqlStr") String sqlStr);

    //按时间倒序查找过去一天历史数据
    List<DevHisData> selectDevHisDatasDesc(String wdevid);
}
