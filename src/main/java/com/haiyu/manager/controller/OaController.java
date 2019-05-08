package com.haiyu.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiyu.manager.service.MngService;
import com.haiyu.manager.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.IOUtils;

import java.io.IOException;
import java.util.List;

@Controller
public class OaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("classpath:static/json/interfaces.json")
    private Resource resource;

    @Autowired
    private MngService mngService;

    @Value("${mng_ip}")
    private String mng_ip;

    @Value("${ex_ip}")
    private String ex_ip;

    @RequestMapping("getBgtDept")
    @ResponseBody
    public List<JSONObject> getBgtDept(){
        logger.info("getBgtDept");

        List<JSONObject> depts = mngService.getBgtDept();
        return depts;
    }

    @RequestMapping("getExDept")
    @ResponseBody
    public List<JSONObject> getExDept(@RequestParam String deptid){
        logger.info("getExDept");
        if(deptid == null || deptid.equals("")){
            deptid = "1";
        }
        List<JSONObject> depts = mngService.getExDept(deptid);
        return depts;
    }

    @RequestMapping("getBgtUser")
    @ResponseBody
    public List<JSONObject> getBgtUser(@RequestParam String deptid){
        logger.info("getBgtUser");
        List<JSONObject> users = mngService.getBgtUser(deptid);
        return users;
    }

    @RequestMapping("getExUser")
    @ResponseBody
    public List<JSONObject> getExUser(@RequestParam String deptid){
        logger.info("getExUser");
        List<JSONObject> users = mngService.getExUser(deptid);
        return users;
    }

    @RequestMapping("getMngIp")
    @ResponseBody
    public String getMngIp(){
        logger.info("getMngIp");
        return mng_ip;
    }

    @RequestMapping("getExIp")
    @ResponseBody
    public String getExIp(){
        logger.info("getExIp");
        return ex_ip;
    }
}
