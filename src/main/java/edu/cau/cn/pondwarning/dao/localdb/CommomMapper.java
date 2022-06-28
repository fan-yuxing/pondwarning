package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommomMapper {
    Image selectImg(String imguuid);
    int updateImg(Image img);
    int insertImg(Image img);

}
