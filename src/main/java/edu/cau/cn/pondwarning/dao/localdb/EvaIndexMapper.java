package edu.cau.cn.pondwarning.dao.localdb;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EvaIndexMapper {
    String selectIndexName(String indexabb);
}
