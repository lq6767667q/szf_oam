package com.haiyu.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiyu.manager.utils.HttpUtils;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.IOUtils;

import java.io.IOException;
import java.net.ConnectException;

@Controller
public class InterfacesController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("classpath:static/json/interfaces.json")
    private Resource resource;


    @RequestMapping("getInterfaces")
    @ResponseBody
    public JSONObject getInterfaces(){
        logger.info("getInterfaces");
        try {
            JSONObject res = new JSONObject();
            String areaData = new String(IOUtils.readFully(resource.getInputStream(), -1, true));
            JSONObject indexParmObject = JSONObject.parseObject(areaData);

            //借口情况
            JSONArray servicesStatusParm = (JSONArray)indexParmObject.get("servicesStatus");
            JSONArray servicesStatusRes = new JSONArray();
            for(Object o : servicesStatusParm){
                JSONObject service = (JSONObject) o;
                JSONObject serviceres = new JSONObject();
                serviceres.put("name", service.getString("name"));
                serviceres.put("desc", service.getString("desc"));
                String type = service.getString("type");
                if(type.equals("http")){
                    String url = "http://" + service.getString("ip");
                    if(HttpUtils.testHtml(url)){
                        serviceres.put("res","yes");
                    }
                    else{
                        serviceres.put("res","no");
                    }
                }
                else if(type.equals("net")){
                    String ip = service.getString("ip");
                    String port = service.getString("port");
                    if(HttpUtils.testsocket(ip, port)){
                        serviceres.put("res","yes");
                    }
                    else{
                        serviceres.put("res","no");
                    }
                }
                servicesStatusRes.add(serviceres);
            }
            res.put("servicesStatus", servicesStatusRes);

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
