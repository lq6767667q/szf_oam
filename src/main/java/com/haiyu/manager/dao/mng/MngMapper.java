package com.haiyu.manager.dao.mng;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MngMapper {

    List<JSONObject> getBgtDept();
    List<JSONObject> getExDept(@Param("deptid") String deptid);
    List<JSONObject> getBgtUser(@Param("deptid") String deptid);
    List<JSONObject> getExUser(@Param("deptid") String deptid);
}
