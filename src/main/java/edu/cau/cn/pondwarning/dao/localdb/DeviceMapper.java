package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.DevList;
import edu.cau.cn.pondwarning.entity.localdb.Device;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface DeviceMapper {

    List<Device> selectDevice();
    int updateDevice(String wdevid, Date lastCommTime);

    //查找用户的所有水质设备
    List<DevList> selectDevices(String useruuid, int offset, int limit);

    //查找水质设备数量
    int selectTotalDevice(String useruuid);

    int insertDevice(Device Ddvice);

    //查找某个用户设备简单的信息
    List<Device> selectDevicesFew(String useruuid);

    //查找某个池塘设备简单的信息
    List<Device>  selectDevicesFewByPond(String useruuid,String ponduuid);

    //查找某个池塘中心监测点设备信息
    Device selectCenterDevice(String ponduuid);

    //查找池塘设备中能够同时监测所需预警指标的设备
    Device selectNeedDevice(Device Ddvice);

    //查找设备信息
    Device selectOneDevice(String wdevid);

    //删除某个设备的信息
    int deleteOneDevice(String wdevid);

    //修改某个设备的信息
    int updateOneDevice(Device Ddvice);


}
