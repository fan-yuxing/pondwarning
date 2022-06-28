package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.PlantsHis;
import edu.cau.cn.pondwarning.entity.localdb.Vdevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VdeviceMapper {
    //查找用户的所有视频设备
    List<DevList> selectVdevices(String useruuid, int offset, int limit);

    //查找视频设备数量
    int selectTotalVdevice(String useruuid);

    //查找某个用户视频设备简单的信息
    List<Vdevice>  selectVdevicesFew(String useruuid);

    //查找某个视频设备信息
    Vdevice selectOneVdevice(String vdevid);

    //查找某个池塘视频设备信息
    List<Vdevice> selectVdevicesFewByPond(String ponduuid);

    //设置摄像机监测区域和监测时间间隔
    int updateVdeviceSet(Vdevice vdevice);

    //插入水草估算数据
    int insertPlantsHis(PlantsHis plantsHis);

    //查找历史水草估算数据
    List<PlantsHis> selectPlantsHis(String vdevid);

    //查找全部摄像头设备
    List<Vdevice> selectAllVdevices();

    //添加摄像机设备
    int insertVdevice(Vdevice vdevice);

    //删除某个设备的信息
    int deleteOneVdevice(String vdevid);
}
