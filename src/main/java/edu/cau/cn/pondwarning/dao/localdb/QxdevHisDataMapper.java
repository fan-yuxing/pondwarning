package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.DevHisData;
import edu.cau.cn.pondwarning.entity.localdb.QxdevHisData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QxdevHisDataMapper {
    int insertQxdevHisData(QxdevHisData qxdevHisData);

    //查找气象设备的所有数据
    List<QxdevHisData> selectQxdevHisDatas(String qdevid, int offset, int limit);

    //查找气象设备的历史数据数量
    int selectTotalData(String qdevid);

    //执行拼接SQL的方法
    List<QxdevHisData> selectQxdevHisDatasLimitByDynamic(@Param(value = "sqlStr") String sqlStr);
}
