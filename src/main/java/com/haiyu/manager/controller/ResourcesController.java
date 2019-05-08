package com.haiyu.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiyu.manager.utils.HttpUtils;
import okhttp3.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import java.sql.*;

@Controller
public class ResourcesController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("classpath:static/json/resources.json")
    private Resource resource;


    @RequestMapping("getResourcesTop")
    @ResponseBody
    public JSONObject getResourcesTop(){
        logger.info("getResourcesTop");
        try {
            JSONObject res = new JSONObject();
            String areaData = new String(IOUtils.readFully(resource.getInputStream(), -1, true));
            JSONObject indexParmObject = JSONObject.parseObject(areaData);

            //服务器情况
            JSONArray pcStatusParm = (JSONArray)indexParmObject.get("pcStatus");
            JSONArray pcStatusRes = new JSONArray();
            for(Object o : pcStatusParm) {
                try{
                    JSONObject pc = (JSONObject) o;
                    String name = pc.getString("name");
                    String desc = pc.getString("desc");
                    String ip = pc.getString("ip");
                    String storage = pc.getString("storage");
                    Response response = HttpUtils.get("http://"+ip+":8103/oampc/v1/info?storage="+storage);
                    String StringTemp = response.body().string();
                    JSONObject pcstatus = (JSONObject) JSONObject.parse(StringTemp);
                    pcstatus.put("name",name);
                    pcstatus.put("desc",desc);
                    pcStatusRes.add(pcstatus);
                }
                catch (ConnectException e){
                    continue;
                }
            }
            res.put("pcStatus", pcStatusRes);

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("getResourcesBottom")
    @ResponseBody
    public JSONObject getResourcesBottom(){
        logger.info("getResourcesBottom");
        try {
            JSONObject res = new JSONObject();
            String areaData = new String(IOUtils.readFully(resource.getInputStream(), -1, true));
            JSONObject indexParmObject = JSONObject.parseObject(areaData);

            //服务器情况
            JSONArray dbStatusParm = (JSONArray)indexParmObject.get("dbStatus");
            JSONArray dbStatusRes = new JSONArray();
            for(Object o : dbStatusParm) {
                try{
                    JSONObject db = (JSONObject) o;
                    String name = db.getString("name");
                    String ip = db.getString("ip");
                    String user = db.getString("user");
                    String password = db.getString("password");
                    String tablespace = db.getString("tablespace");

                    String driver = "oracle.jdbc.driver.OracleDriver";    //驱动标识符
                    String url = "jdbc:oracle:thin:@"+ip+":1521:orcl"; //链接字符串
                    Connection con = null;
                    PreparedStatement pstm = null;
                    ResultSet rs = null;

                    String used = "";
                    String usedsize = "";
                    String totalsize = "";
                    try {
                        Class.forName(driver);
                        con = DriverManager.getConnection(url,user, password);
                        String sql = "select a.tablespace_name,a.bytes/1024/1024||'M' total,b.bytes/1024/1024||'M' used,c.bytes/1024/1024||'M' free ,\n" +
                                "trunc(b.bytes/a.bytes,2) useded, trunc(c.bytes/a.bytes,2) freeed\n" +
                                "from sys.sm$ts_avail a,sys.sm$ts_used b,sys.sm$ts_free c\n" +
                                "where a.tablespace_name = b.tablespace_name\n" +
                                "      and a.tablespace_name = c.tablespace_name";
                        pstm =con.prepareStatement(sql);
                        rs = pstm.executeQuery();

                        while(rs.next()) {
                            double useded = rs.getDouble("useded");
                            String tmpusedsize = rs.getString("used");
                            String tmptotalsize = rs.getString("total");
                            String tablespace_name = rs.getString("tablespace_name");
                            if(tablespace.toLowerCase().equals(tablespace_name.toLowerCase())){
                                used = useded * 100 +"%";
                                usedsize = tmpusedsize;
                                totalsize = tmptotalsize;
                                break;
                            }
                        }

                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch(SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if(rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        // 关闭执行通道
                        if(pstm !=null) {
                            try {
                                pstm.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        // 关闭连接通道
                        try {
                            if(con != null &&(!con.isClosed())) {
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONObject dbStatus = new JSONObject();
                    dbStatus.put("name",name);
                    dbStatus.put("used",used);
                    dbStatus.put("ip",ip);
                    dbStatus.put("usedsize",usedsize);
                    dbStatus.put("totalsize",totalsize);
                    dbStatusRes.add(dbStatus);
                }
                catch (Exception e){
                    continue;
                }
            }
            res.put("dbStatus", dbStatusRes);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
