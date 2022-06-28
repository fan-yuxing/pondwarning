package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.Pond;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PondMapper {

    //查找用户的所有池塘
    List<Pond> selectPonds(String useruuid, int offset, int limit);

    //查找某个池塘简单的信息
    List<Pond> selectPondsFew(String useruuid);

    //查找池塘数量
    int selectTotalPond(String useruuid);

    //查找池塘总面积
    BigDecimal selectTotalAcreage(String useruuid);

    //查找某个池塘的信息
    Pond selectOnePond(String ponduuid);


    //修改某个池塘的信息
    int updateOnePond(Pond pond);

    //添加某个池塘的信息
    int insertOnePond(Pond pond);

    //删除某个池塘的信息
    int deleteOnePond(String ponduuid);
}
