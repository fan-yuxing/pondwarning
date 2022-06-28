package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.Qxdevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface QxdeviceMapper {
    List<Qxdevice> selectQxdevice();

    int updateQxdevice(String qdevid, Date lastCommTime);

    //查找用户的所有气象设备
    List<DevList> selectQxdevices(String useruuid, int offset, int limit);

    //查找气象设备数量
    int selectTotalQxdevice(String useruuid);

    //查找某个用户气象设备简单的信息
    List<Qxdevice>  selectQxdevicesFew(String useruuid);

    //查找池塘对应的气象设备，（一个池塘只能对应一个气象设备,涉及到iot_qxdevice、tb_ponddev两个表）
    Qxdevice selectQxdevicesByPond(String ponduuid);

    //插入气象设备
    int insertQxdevice(Qxdevice qxdevice);

    //插入池塘-气象设备对应关系，因为一个气象设备可以对应多个池塘
    int insertPondQxdevice(List<DevList> list);

    //查找气象设备信息
    Qxdevice selectOneQxdevice(String qdevid);

    //删除某个设备的信息
    int deleteOneQxdevice(String qdevid);

    //删除某个设备管理池塘
    int deleteQxdeviceAndPond(String qdevid);

}
