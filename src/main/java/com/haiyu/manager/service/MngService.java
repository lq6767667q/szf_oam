package com.haiyu.manager.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface MngService {

    List<JSONObject> getBgtDept();
    List<JSONObject> getExDept(String deptid);
    List<JSONObject> getBgtUser(String deptid);
    List<JSONObject> getExUser(String deptid);
}
