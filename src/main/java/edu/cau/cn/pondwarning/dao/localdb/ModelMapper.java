package edu.cau.cn.pondwarning.dao.localdb;

import edu.cau.cn.pondwarning.entity.localdb.Model;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelMapper {
    String selectModelPath(Model model);

    int updateModelTrainState(Model model);

    int selectExistModel(Model model);

    int insertModel(Model model);

    int updateModel(Model model);

    Model selectUserModel(String useruuid, int modeltype);
}
