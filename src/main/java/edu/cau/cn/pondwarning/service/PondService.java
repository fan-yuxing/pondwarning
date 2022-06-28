package edu.cau.cn.pondwarning.service;

import edu.cau.cn.pondwarning.entity.localdb.Pond;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface PondService {
    Map<String,Object> selectPonds(int current,int limit);

    Map<String,Object> selectPondsFew();

    Pond selectOnePond(String ponduuid);

    Map<String,Object> updatePondInfo(Pond pond);

    Map<String,Object>  insertOnePond(Pond pond);

    Map<String,Object>  deletePonds(String[] ponduuids);
}
