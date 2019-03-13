package com.superman.superman.dao;

import com.superman.superman.model.SysJhPddAll;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysJhPddAllDao extends BaseDao<SysJhPddAll> {
    List<SysJhPddAll> queryPageJd(Map<String,Object> map);
}
