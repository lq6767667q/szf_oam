package com.haiyu.manager.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.haiyu.manager.dao.mng.MngMapper;
import com.haiyu.manager.dao.sys.BaseAdminRoleMapper;
import com.haiyu.manager.service.MngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MngServiceImpl implements MngService {

    @Autowired
    private MngMapper mngMapper;

    @Override
    public List<JSONObject> getBgtDept() {
        return mngMapper.getBgtDept();
    }
    @Override
    public List<JSONObject> getExDept(String deptid) {
        return mngMapper.getExDept(deptid);
    }
    @Override
    public List<JSONObject> getBgtUser(String deptid) {
        return mngMapper.getBgtUser(deptid);
    }
    @Override
    public List<JSONObject> getExUser(String deptid) {
        return mngMapper.getExUser(deptid);
    }
}
