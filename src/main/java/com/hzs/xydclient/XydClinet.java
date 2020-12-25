/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzs.xydclient.data.Fengfabagchaxun;
import com.hzs.xydclient.data.Fenjianfangan;
import com.hzs.xydclient.data.Guiji;
import com.hzs.xydclient.data.Luojigekou;
import com.hzs.xydclient.data.Shoujimingxi;
import com.hzs.xydclient.data.Shoujishuju;
import com.hzs.xydclient.data.ziti.Baoguogui;
import com.hzs.xydclient.data.ziti.Tuotouzitiwangdian;
import com.hzs.xydclient.data.ziti.Tuotouzitizitidian;
import com.hzs.xydclient.http.HttpClientUtil;
import com.hzs.xydclient.http.SkipHttpsUtil;
import com.hzs.xydclient.util.AES;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author  szh
 * QQ:873689
 * @date 2020-3-11 16:41:22
 */
@Data
public class XydClinet {

    private HttpAsyncClient client ;
    
    String username = "";
    String key = "B+oQ52IuAt9wbMxw";
    String password = "";
    
    String ip = "";
    
    CookieStore cookieStore = new BasicCookieStore();
    public XydClinet(String username,String password,String ip) throws Exception{
        client = SkipHttpsUtil.wraphttpclientAsyncClient(cookieStore);
        
        //client = new SSLClient();
        this.username =username;
        this.password = AES.Encrypt(password, key);
        this.ip = ip;
        
    }
    
    public  String login() throws UnsupportedEncodingException, URISyntaxException  {
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/cas/login?service=https://10.4.188.1/portal/a/cas");
        uRIBuilder.setHost(ip);
        HttpGet get = new HttpGet(uRIBuilder.build());
        String html = HttpClientUtil.doGet(client, get, "utf8");
        String lt = html.split("name=\"lt\" value=\"")[1].split("\"")[0];
        String execution = html.split("name=\"execution\" value=\"")[1].split("\"")[0];
        String _eventId = html.split("name=\"_eventId\" value=\"")[1].split("\"")[0];
        String loginIdentifier = html.split("name=\"loginIdentifier\" value=\"")[1].split("\"")[0];
        String loginCode = html.split("name=\"loginCode\" value=\"")[1].split("\"")[0];
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("lt", lt);
        BasicNameValuePair param2 = new BasicNameValuePair("execution",execution);
        BasicNameValuePair param3 = new BasicNameValuePair("username", username);
        BasicNameValuePair param4 = new BasicNameValuePair("password", password);
        BasicNameValuePair param5 = new BasicNameValuePair("_eventId",_eventId);
        BasicNameValuePair param6 = new BasicNameValuePair("loginIdentifier", loginIdentifier);
        BasicNameValuePair param7 = new BasicNameValuePair("loginCode", loginCode);
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        list.add(param6);
        list.add(param7);
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        //https://10.4.188.1/cas/login?service=https://10.4.188.1/pickup-web/a/cas
        //https://10.4.188.1/portal/a/cas?ticket=ST%24TGT-219769-RWNLwhrnNDztiUf7KOq5S0pZSSJXq5CzDJdtl4ADdNMbEv7NjR-cpsd%24-533861-w1V3IjIkYzBjJZSRWZj4-cpsd
        URIBuilder uRIBuilder2 = new URIBuilder("https://10.4.188.1/cas/login?service=https://10.4.188.1/portal/a/cas");
        uRIBuilder2.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder2.build());
        post.setEntity(entityParam);
        return HttpClientUtil.doPost(client,post, "utf8");
    }

    
    public  String  xuanzetiban( String jigouid) throws URISyntaxException {
        ///portal/a/substitutionorg/11944
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/portal/a/substitutionorg/"+jigouid);
        uRIBuilder.setHost(ip);
        HttpGet get = new HttpGet(uRIBuilder.build());
        return HttpClientUtil.doGet(client, get, "utf8");
    }
    
    public String xuanzetaixi( String rs) throws UnsupportedEncodingException, URISyntaxException  {
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/portal/a/basic/saveOrgShopTeamSeat");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        List list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("rs", rs);
        list.add(param1);
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        return HttpClientUtil.doPost(client,post, "utf8");
    }

    public String shouye() throws URISyntaxException{
        //https://10.4.188.1/pickup-web/a/cas
        //https://10.4.188.1/portal/a
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/cas/login?service=https://10.4.188.1/pickup-web/a/cas");
        uRIBuilder.setHost(ip);
        HttpGet get = new HttpGet(uRIBuilder.build());
        return HttpClientUtil.doGet(client, get, "utf8");
    }
    
    public  List<Fengfabagchaxun> getFengfaMailbagchaxunid( String begindate, String enddate, int yema) throws UnsupportedEncodingException, URISyntaxException  {
        
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/packqueryprint/list?pageNo=1&pageSize=20&createUserName=&createUserCode=&mailbagNumber=&destinationOrgName=&destinationOrgCode=&mailbagClassCode=&mailbagClassName=--%E8%AF%B7+%E9%80%89+%E6%8B%A9--&logicGridNo=&status=1&printFlag=3&shiftNo=&handlePropertyCode=&handlePropertyName=&handlePropertyCode1=&gmtCreatedBegin=2020-03-29+00%3A00%3A00&gmtCreatedEnd=2020-03-29+23%3A59%3A59");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate)
                .setParameter("pageNo", yema+"");
        HttpGet httpget = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpget, "utf8"));
        List<Fengfabagchaxun> lists = new ArrayList();
        
        for(Element e :doc.select("table#contentTable > tbody > tr")){
            Fengfabagchaxun f = new Fengfabagchaxun();
            f.setId(e.selectFirst("td > input[name=ids]").val());
            f.setShijian(e.select("td").get(8).text());
            f.setRongqitiaoma(e.select("td").get(9).text());
            f.setCaozuorenyuan(e.select("td").get(7).text());
            f.setJidaju(e.select("td").get(2).text());
            f.setQingdanhaoma(e.select("td").get(1).text());
            f.setZongbaozhonglei(e.select("td").get(4).text());
            f.setYoujianshuliang(e.select("td").get(5).text());
            f.setGekouhao(e.select("td").get(3).text());
            //zongbaozhonglei
            
            lists.add(f);
        }
        
        return lists;
        
    }

    public  int getFengfaMailbagchaxunid( String begindate, String enddate) throws UnsupportedEncodingException, URISyntaxException   {
       URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/packqueryprint/list?pageNo=1&pageSize=20&createUserName=&createUserCode=&mailbagNumber=&destinationOrgName=&destinationOrgCode=&mailbagClassCode=&mailbagClassName=--%E8%AF%B7+%E9%80%89+%E6%8B%A9--&logicGridNo=&status=1&printFlag=3&shiftNo=&handlePropertyCode=&handlePropertyName=&handlePropertyCode1=&gmtCreatedBegin=2020-03-29+00%3A00%3A00&gmtCreatedEnd=2020-03-29+23%3A59%3A59");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate);
        HttpGet httpget = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpget, "utf8"));
        if(doc.selectFirst("span.pagination_note")==null){
            return -1;
        }
        return Integer.valueOf(doc.selectFirst("span.pagination_note").text().split("共")[1].split("页")[0].trim());
        
    }
    
    
    public String getFengfaMailbagid(String chaxunid,String begindate, String enddate) throws UnsupportedEncodingException, URISyntaxException {
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/packqueryprint/detailList?id=1029335347&createUserCode=&mailbagNumber=&destinationOrgCode=&mailbagClassCode=&logicGridNo=&statusDetail=1&printFlag=3&statusFh=1&gmtCreatedBegin=2020-03-29%2010:32:46&gmtCreatedEnd=2020-03-30%2010:32:46&shiftNo=&handlePropertyCode=&pageNoReturn=1&pageSizeReturn=20");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate)
                .setParameter("id", chaxunid);
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        if(doc.selectFirst("input#mailbagNo")==null){
            return null;
        }
        return doc.selectFirst("input#mailbagNo").val();
    //
    }
    
    

    public int getFayunMailbagchaxunid( String begindate, String enddate) throws UnsupportedEncodingException, URISyntaxException  {
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/list?pageNo=1&pageSize=20&businessUnit=B&handoverObjectName=&billName=&interFlag=1&gmtCreatedBegin=2020-03-30+00%3A00%3A00&gmtCreatedEnd=2020-03-30+23%3A59%3A59&routeLevel=&billNo=&truckingNo=&status=1&statusJ=3&vehicleNo=");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate);
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        if(doc.selectFirst("span.pagination_note")==null){
            return -1;
        }
        return Integer.valueOf(doc.selectFirst("span.pagination_note").text().split("共")[1].split("页")[0].trim());
    }
    
    public List<Map<String,String>> getFayunMailbagchaxunid( String begindate, String enddate,int yema) throws UnsupportedEncodingException,URISyntaxException  {
        List<Map<String,String>> lists  = new ArrayList();
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/list?pageNo=1&pageSize=20&businessUnit=B&handoverObjectName=&billName=&interFlag=1&gmtCreatedBegin=2020-03-30+00%3A00%3A00&gmtCreatedEnd=2020-03-30+23%3A59%3A59&routeLevel=&billNo=&truckingNo=&status=1&statusJ=3&vehicleNo=");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate)
                .setParameter("pageNo", yema+"");
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        for(Element e :doc.select("table#contentTable > tbody > tr")){
            String s = e.selectFirst("td > a").attr("onclick");
            s = s.replaceAll("gotoLinksGroupLd", "");
            s = s.replaceAll("[(]", "");
            s = s.replaceAll("[)]", "");
            String ss[] = s.split(",");
            for(int i = 0;i < ss.length;i++){
                ss[i] = ss[i].replaceAll("[']", "");
            }
            Map<String,String> map = new HashMap();
            map.put("handoverObjectNoGroup", ss[0]);//
            map.put("truckingNoGroup", ss[1]);//
            map.put("vehicleNoGroup", ss[2]);//
            map.put("throwVehicleNoGroup", ss[3]);//
            map.put("carriageNoGroup", ss[4]);//
            map.put("billNoGroup", ss[5]);//
            map.put("createUserCodeGroup", ss[6]);//
            map.put("statusGroup", ss[7]);//
            map.put("departIdGroup", ss[8]);//
            map.put("loadTime", ss[11]);//
            map.put("interFlag", ss[9]);
            map.put("djType", ss[10]);
            map.put("gmtCreatedBegin", doc.selectFirst("#gmtCreatedBegin2").val());
            map.put("gmtCreatedEnd", doc.selectFirst("#gmtCreatedEnd2").val());
            map.put("handoverObjectName", doc.selectFirst("#handoverObjectName").val());
            map.put("status", doc.selectFirst("#status2").val());
            map.put("billName", doc.selectFirst("#billName").val());
            map.put("vehicleNo", doc.selectFirst("#vehicleNo").val());
            map.put("routeLevel", doc.selectFirst("#routeLevel").val());
            
            map.putAll(getFayunMailbagchaxunid(map));
            lists.add(map);
        }
        return lists;
    }
    
    public Map<String,String> getFayunMailbagchaxunid(Map<String,String> map) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/detailListGroup?departIdGroup=46473744&interFlag=1&gmtCreatedBegin=2020-03-29%2000:00:00&gmtCreatedEnd=2020-03-29%2023:59:59&handoverObjectName=&billName=&vehicleNo=&routeLevel=&djType=0&status=1&handoverObjectNoGroup=2321529001&truckingNoGroup=W005847342&vehicleNoGroup=%E8%8B%8FE2A562&throwVehicleNoGroup=&carriageNoGroup=1&createUserCodeGroup=03739084&statusGroup=1&billNoGroup=03290001&loadTime=2020/03/29%2020:03:03");
        
        uRIBuilder.setHost(ip).setParameters(zhuanhuan(map));
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        map.put("ldlshReturn",doc.selectFirst("#ldlshReturn").val());
        map.put("nextOrgCode",doc.selectFirst("#nextOrgCode").val());
        map.put("billNameSj",doc.selectFirst("#billNameSj").val());
        map.put("statusJ",doc.selectFirst("#statusJ").val());
        map.put("dwxx",doc.selectFirst("#dwxx").val());
        map.put("ids",doc.selectFirst("#ids").val());
        map.put("id",doc.selectFirst("#id").val());
        map.put("arriveId",doc.selectFirst("#arriveId").val());
        map.put("areaOrOrgTypeData",doc.selectFirst("#areaOrOrgTypeData").val());
        map.put("billName",doc.selectFirst("#billName").val());
        map.put("handoverObjectName",doc.selectFirst("#handoverObjectName").val());
        return map;
    }
    
  
    public int getFayunMailbagid(Map<String,String> map) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/detailListGroup?departIdGroup=46473744&interFlag=1&gmtCreatedBegin=2020-03-29%2000:00:00&gmtCreatedEnd=2020-03-29%2023:59:59&handoverObjectName=&billName=&vehicleNo=&routeLevel=&djType=0&status=1&handoverObjectNoGroup=2321529001&truckingNoGroup=W005847342&vehicleNoGroup=%E8%8B%8FE2A562&throwVehicleNoGroup=&carriageNoGroup=1&createUserCodeGroup=03739084&statusGroup=1&billNoGroup=03290001&loadTime=2020/03/29%2020:03:03");
        uRIBuilder.setHost(ip).setParameters(zhuanhuan(map));
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        if(doc.selectFirst("span.pagination_note")==null){
            return -1;
        }
        return Integer.valueOf(doc.selectFirst("span.pagination_note").text().split("共")[1].split("页")[0].trim());
    }
    
    public List<String> getFayunMailbagid(Map<String,String> map,int page) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/detailListGroup?handoverObjectNoGroup=3121524400&truckingNoGroup=&vehicleNoGroup=&throwVehicleNoGroup=&carriageNoGroup=1&billNoGroup=03300065&createUserCodeGroup=03739084&statusGroup=1&departIdGroup=46773792&ldlshReturn=&loadTime=2020-03-30+09%3A12%3A15&nextOrgCode=&billNameSj=&status=1&statusJ=&dwxx=0&ids=67364855&id=&arriveId=&areaOrOrgTypeData=&billName=&interFlag=1&vehicleNo=&routeLevel=&djType=0&handoverObjectName=&gmtCreatedBegin=2020-03-30+00%3A00%3A00&gmtCreatedEnd=2020-03-30+23%3A59%3A59&pageNum=20&pageNo=1&pageSize=20");
        uRIBuilder.setHost(ip).setParameters(zhuanhuan(map)).setParameter("pageNo",page+"");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        List<String> lists = new ArrayList();
        for(Element e : doc.select("table#contentTable > tbody > tr")){
            
            lists.add(e.select("td > a").text());
        }
        return lists;
    }
    
    public int getFayunMailbagid(String departIdGroup,String interFlag,String begindate, String enddate,
            String  handoverObjectName,String billName,String vehicleNo,String routeLevel,String djType,String status,String handoverObjectNoGroup,
            String truckingNoGroup,String vehicleNoGroup,String throwVehicleNoGroup,String carriageNoGroup,String createUserCodeGroup,String statusGroup,String billNoGroup,String loadTime) throws UnsupportedEncodingException, URISyntaxException{

        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/detailListGroup?departIdGroup=46473744&interFlag=1&gmtCreatedBegin=2020-03-29%2000:00:00&gmtCreatedEnd=2020-03-29%2023:59:59&handoverObjectName=&billName=&vehicleNo=&routeLevel=&djType=0&status=1&handoverObjectNoGroup=2321529001&truckingNoGroup=W005847342&vehicleNoGroup=%E8%8B%8FE2A562&throwVehicleNoGroup=&carriageNoGroup=1&createUserCodeGroup=03739084&statusGroup=1&billNoGroup=03290001&loadTime=2020/03/29%2020:03:03");
        
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate)
                .setParameter("departIdGroup", departIdGroup)
                .setParameter("interFlag", interFlag)
                .setParameter("handoverObjectName", handoverObjectName)
                .setParameter("billName", billName)
                .setParameter("vehicleNo", vehicleNo)
                .setParameter("routeLevel", routeLevel)
                .setParameter("djType", djType)
                .setParameter("status", status)
                .setParameter("handoverObjectNoGroup", handoverObjectNoGroup)
                .setParameter("truckingNoGroup", truckingNoGroup)
                .setParameter("vehicleNoGroup", vehicleNoGroup)
                .setParameter("throwVehicleNoGroup", throwVehicleNoGroup)
                .setParameter("carriageNoGroup", carriageNoGroup)
                .setParameter("createUserCodeGroup", createUserCodeGroup)
                .setParameter("statusGroup", statusGroup)
                .setParameter("billNoGroup", billNoGroup)
                .setParameter("loadTime", loadTime);
        HttpGet httpGet = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,httpGet, "utf8"));
        if(doc.selectFirst("span.pagination_note")==null){
            return -1;
        }
        return Integer.valueOf(doc.selectFirst("span.pagination_note").text().split("共")[1].split("页")[0].trim());
    }
    
    public List<String> getFayunMailbagid(String departIdGroup,String interFlag,
            String begindate, String enddate,
            String  handoverObjectName,String billName,String vehicleNo,
            String routeLevel,String djType,String status,String handoverObjectNoGroup,
            String truckingNoGroup,String vehicleNoGroup,String throwVehicleNoGroup,String carriageNoGroup,
            String createUserCodeGroup,String statusGroup,String billNoGroup,String loadTime,int page) throws UnsupportedEncodingException, URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/queryandprint/waybillquerymaint/detailListGroup?handoverObjectNoGroup=3121524400&truckingNoGroup=&vehicleNoGroup=&throwVehicleNoGroup=&carriageNoGroup=1&billNoGroup=03300065&createUserCodeGroup=03739084&statusGroup=1&departIdGroup=46773792&ldlshReturn=&loadTime=2020-03-30+09%3A12%3A15&nextOrgCode=&billNameSj=&status=1&statusJ=&dwxx=0&ids=67364855&id=&arriveId=&areaOrOrgTypeData=&billName=&interFlag=1&vehicleNo=&routeLevel=&djType=0&handoverObjectName=&gmtCreatedBegin=2020-03-30+00%3A00%3A00&gmtCreatedEnd=2020-03-30+23%3A59%3A59&pageNum=20&pageNo=2&pageSize=20");
        uRIBuilder.setHost(ip).setParameter("gmtCreatedBegin", begindate)
                .setParameter("gmtCreatedEnd", enddate)
                .setParameter("departIdGroup", departIdGroup)
                .setParameter("interFlag", interFlag)
                .setParameter("handoverObjectName", handoverObjectName)
                .setParameter("billName", billName)
                .setParameter("vehicleNo", vehicleNo)
                .setParameter("routeLevel", routeLevel)
                .setParameter("djType", djType)
                .setParameter("status", status)
                .setParameter("handoverObjectNoGroup", handoverObjectNoGroup)
                .setParameter("truckingNoGroup", truckingNoGroup)
                .setParameter("vehicleNoGroup", vehicleNoGroup)
                .setParameter("throwVehicleNoGroup", throwVehicleNoGroup)
                .setParameter("carriageNoGroup", carriageNoGroup)
                .setParameter("createUserCodeGroup", createUserCodeGroup)
                .setParameter("statusGroup", statusGroup)
                .setParameter("billNoGroup", billNoGroup)
                .setParameter("loadTime", loadTime)
                .setParameter("pageNo", page+"");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        List<String> lists = new ArrayList();
        for(Element e : doc.select("table#contentTable > tbody > tr")){
            
            lists.add(e.select("td > a").text());
        }
        return lists;
    }

    public Luojigekou getMonirugeluoji(String mailno) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sortercfgpost/simulationgrid/list?waybillNo=9505852605502&opOrgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&opOrgCode=21520017&shopCode=SD21520017&shopName=++%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4+&sorterCode=21502103&sorterName=++%E5%90%B4%E6%B1%9F%E7%AE%80%E6%98%93%E5%88%86%E6%8B%A3%E6%9C%BA%3A21502103+");
        uRIBuilder.setHost(ip).setParameter("waybillNo", mailno)
                .setParameter("opOrgName", "吴江中心")
                .setParameter("opOrgCode", "21520017")
                .setParameter("shopCode", "SD21520017")
                .setParameter("shopName", "  默认车间")
                .setParameter("sorterCode", "21502103")
                .setParameter("sorterName", "  吴江简易分拣机:21502103 ");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        String body = doc.select("body").text();
        
        if(body.contains("\"name\":")){
            try{
                Luojigekou l = new Luojigekou();
                l.setName(body.split("\"name\":")[1].split(",")[0].replaceAll("\"", ""));
                l.setCode(body.split("\"code\":")[1].split(",")[0].replaceAll("\"", ""));
                l.setMailno(mailno);
                return l;
            }catch(Exception e){
                System.out.println(mailno);
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    public String getMoniruge(String mailno) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sortercfgpost/simulationgrid/list?waybillNo=9505852605502&opOrgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&opOrgCode=21520017&shopCode=SD21520017&shopName=++%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4+&sorterCode=21502103&sorterName=++%E5%90%B4%E6%B1%9F%E7%AE%80%E6%98%93%E5%88%86%E6%8B%A3%E6%9C%BA%3A21502103+");
        uRIBuilder.setHost(ip).setParameter("waybillNo", mailno)
                .setParameter("opOrgName", "吴江中心")
                .setParameter("opOrgCode", "21520017")
                .setParameter("shopCode", "SD21520017")
                .setParameter("shopName", "  默认车间")
                .setParameter("sorterCode", "21502103")
                .setParameter("sorterName", "  吴江简易分拣机:21502103 ");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        String body = doc.select("body").text();
        //System.out.println(body);
        if(body.contains("返回结果")){
            try{
                return body.split("返回结果:#")[1].split("#")[0];
            }catch(Exception e){
                System.out.println(mailno);
                e.printStackTrace();
                return null;
            }
            
        }
        return null;
    }
    
    public List<Luojigekou> getMonirugeluojiAsync(List<String> mailnos) throws URISyntaxException{
        
        List<HttpGet> gets = new ArrayList();
        List<Luojigekou> luojigekous = new ArrayList<>();
            for(String mailno:mailnos){
                URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sortercfgpost/simulationgrid/list?waybillNo=9505852605502&opOrgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&opOrgCode=21520017&shopCode=SD21520017&shopName=++%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4+&sorterCode=21502103&sorterName=++%E5%90%B4%E6%B1%9F%E7%AE%80%E6%98%93%E5%88%86%E6%8B%A3%E6%9C%BA%3A21502103+");
            uRIBuilder.setHost(ip).setParameter("waybillNo", mailno)
                    .setParameter("opOrgName", "吴江中心")
                    .setParameter("opOrgCode", "21520017")
                    .setParameter("shopCode", "SD21520017")
                    .setParameter("shopName", "  默认车间")
                    .setParameter("sorterCode", "21502103")
                    .setParameter("sorterName", "  吴江简易分拣机:21502103 ");
            HttpGet get = new HttpGet(uRIBuilder.build());
            gets.add(get);
        }
        List<String> mystrs = HttpClientUtil.doGetAsync(client,gets, "utf8");
        for(int i=0;i<mystrs.size();i++){
            //String str ="";
            Document doc = Jsoup.parse(mystrs.get(i));
            String body = doc.select("body").text();
            if(body.contains("\"name\":")){
            try{
                Luojigekou l = new Luojigekou();
                l.setName(body.split("\"name\":")[1].split(",")[0].replaceAll("\"", ""));
                l.setCode(body.split("\"code\":")[1].split(",")[0].replaceAll("\"", ""));
                l.setMailno(mailnos.get(i));
                luojigekous.add(l);
                //return l;
            }catch(Exception e){
                System.out.println(mailnos.get(i));
                e.printStackTrace();
                //return null;
            }
        }
        
        
        }
        return luojigekous;
    }
    
    public List<Guiji> getGuiji(List<String> mailnos) throws UnsupportedEncodingException, URISyntaxException{
        List<HttpGet> httpgets = new ArrayList<>();
        for(String mailno : mailnos){
            URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/querypush-web/a/qps/qpswaybilltraceinternal/queryTraceByTrace_no?trace_no=9561295816102");
            uRIBuilder.setHost(ip).setParameter("trace_no", mailno);
            HttpGet get = new HttpGet(uRIBuilder.build());
            httpgets.add(get);
        }
        
        
        List<String> strs = HttpClientUtil.doGetAsync(client,httpgets, "utf8");
        
        List<Guiji> guijis = new ArrayList<>();
        for(String str:strs){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
            //System.out.println(str);
            guijis.addAll(gson.fromJson(str, type));
        }
        
        
        
        return guijis;
    }
    
    public List<Guiji> getGuiji(String mailno) throws UnsupportedEncodingException, URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/querypush-web/a/qps/qpswaybilltraceinternal/queryTraceByTrace_no?trace_no=9561295816102");
        uRIBuilder.setHost(ip).setParameter("trace_no", mailno);
        HttpGet get = new HttpGet(uRIBuilder.build());
        String str = HttpClientUtil.doGet(client,get, "utf8");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
        List<Guiji> guijis = gson.fromJson(str, type);
        return guijis;
    }
    
    public List<Guiji> getGeijuGuiji(List<String> mailnos) throws UnsupportedEncodingException, URISyntaxException{
        List<HttpGet> httpgets = new ArrayList<>();
        for(String mailno : mailnos){
            URIBuilder uRIBuilder = new URIBuilder("http://10.4.181.166/querypush-traln/qps/qpswaybilltraceinternal/queryTraceByTrace_no?trace_no=9830135547790");
            uRIBuilder.setParameter("trace_no", mailno);
            HttpGet get = new HttpGet(uRIBuilder.build());
            httpgets.add(get);
        }
        //System.out.println("test");
        
        List<String> strs = HttpClientUtil.doGetAsync(client,httpgets, "utf8");
        
        List<Guiji> guijis = new ArrayList<>();
        for(String str:strs){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
            //System.out.println(str);
            guijis.addAll(gson.fromJson(str, type));
        }
        
        
        
        return guijis;
    }
    
    public List<Guiji> getGeijuGuiji(String mailno) throws UnsupportedEncodingException, URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("http://10.4.181.166/querypush-traln/qps/qpswaybilltraceinternal/queryTraceByTrace_no?trace_no=9830135547790");
        uRIBuilder.setParameter("trace_no", mailno);
        HttpGet get = new HttpGet(uRIBuilder.build());
        String str = HttpClientUtil.doGet(client,get, "utf8");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
        List<Guiji> guijis = gson.fromJson(str, type);
        return guijis;
    }
    
    
    public int getshoujixinxiPagesize(String kshijian,String jshijian,int pagesize) throws URISyntaxException, UnsupportedEncodingException{
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("bizOccurDateStart", kshijian);
        BasicNameValuePair param2 = new BasicNameValuePair("bizOccurDateEnd",jshijian);
        BasicNameValuePair param3 = new BasicNameValuePair("pageNo", "1");
        BasicNameValuePair param4 = new BasicNameValuePair("pageSize", pagesize+"");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        //https://10.4.188.1/pickup-web/a/pickup/waybillquery/querybase??postOrgNo=&orgDrdsCode=&wayBillNo=&postState=&bizOccurDateStart=2020-06-27+00%3A00%3A00&bizOccurDateEnd=2020-06-28+23%3A59%3A59+&senderNo=&sender=&senderWarehouseName=&postPersonNo=&settlementMode=&ioType=&bizProductNo=&allowSealingFlag=&isFeedFlag=&codFlag=&feeDateStart=&feeDateEnd=&oneBillFlag=&insuranceFlag=&packaging=&pickupPersonNo=&receiverProvinceNo=&receiverProvinceName=&senderLinker=&senderMobile=&receiverLinker=&receiverMobile=&transferType=&pageNo=1&pageSize=10
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pickup-web/a/pickup/waybillquery/querybase");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        Gson gson = new Gson();
        //Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
        String str = HttpClientUtil.doPost(client, post, "utf8");
        if(str.contains("没有符合条件的查询结果")){
            return 0;
        }
        //System.out.println(str);
        Shoujishuju ss = gson.fromJson(str, Shoujishuju.class);
        //System.out.println("ss"+ss.getInnerHtml());
        return  Integer.valueOf(ss.getInnerHtml().split("<input type=\\\"text\\\" value=\\\"")[2].split("\\\"")[0]);
    }
    
    public List<Shoujimingxi> getShoujixinxi(String kshijian,String jshijian,int pageNo,int pagesize) throws URISyntaxException, UnsupportedEncodingException{
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("bizOccurDateStart", kshijian);
        BasicNameValuePair param2 = new BasicNameValuePair("bizOccurDateEnd",jshijian);
        BasicNameValuePair param3 = new BasicNameValuePair("pageNo", String.valueOf(pageNo));
        BasicNameValuePair param4 = new BasicNameValuePair("pageSize", pagesize+"");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        //https://10.4.188.1/pickup-web/a/pickup/waybillquery/querybase??postOrgNo=&orgDrdsCode=&wayBillNo=&postState=&bizOccurDateStart=2020-06-27+00%3A00%3A00&bizOccurDateEnd=2020-06-28+23%3A59%3A59+&senderNo=&sender=&senderWarehouseName=&postPersonNo=&settlementMode=&ioType=&bizProductNo=&allowSealingFlag=&isFeedFlag=&codFlag=&feeDateStart=&feeDateEnd=&oneBillFlag=&insuranceFlag=&packaging=&pickupPersonNo=&receiverProvinceNo=&receiverProvinceName=&senderLinker=&senderMobile=&receiverLinker=&receiverMobile=&transferType=&pageNo=1&pageSize=10
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pickup-web/a/pickup/waybillquery/querybase");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        Gson gson = new Gson();
        //Type type = new TypeToken<ArrayList<Guiji>>(){}.getType();
        String str = HttpClientUtil.doPost(client, post, "utf8");
        Shoujishuju ss = gson.fromJson(str, Shoujishuju.class);
        return ss.getDetail();
    }
    
    public String getfenjianfanganid() throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sorterschemechose/list?orgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&orgCode=21520017&shopName=%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4&shopCode=SD21520017");
        uRIBuilder.setHost(ip).setParameter("orgName", "吴江中心")
                .setParameter("orgCode", "21520017")
                .setParameter("shopName", "默认车间")
                .setParameter("shopCode", "SD21520017");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        return  doc.select("input#sorterPlanId").first().val();
    }
    
    public int getfenjianfanganPagesize(String fanganid) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sorterschemecfgnew/list?orgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&shopName=%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4&shopCode=SD21520017&name=%E5%90%B4%E6%B1%9F%E6%96%B9%E6%A1%881&routeName=&orgCode=21520017&directGridFlag=&physicalGridNo=&mailbagClassCode=&logicGridCode=&mailbagClassName=&logicGridName=&planCode=2&sorterPlanId=9881000030&areaCode=21520000");
        uRIBuilder.setHost(ip).setParameter("sorterPlanId", fanganid)
                .setParameter("pageNo", "1")
                .setParameter("pageSize","20");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        return Integer.valueOf(doc.select("span.pagination_note").text().split("共")[1].split("页")[0].trim());
        
    }
    
    public List<Fenjianfangan> getfenjianfangan(String fanganid,int pageNo) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/pcs-tc-web/a/pcs/sorterschemecfgnew/list?orgName=%E5%90%B4%E6%B1%9F%E4%B8%AD%E5%BF%83&shopName=%E9%BB%98%E8%AE%A4%E8%BD%A6%E9%97%B4&shopCode=SD21520017&name=%E5%90%B4%E6%B1%9F%E6%96%B9%E6%A1%881&routeName=&orgCode=21520017&directGridFlag=&physicalGridNo=&mailbagClassCode=&logicGridCode=&mailbagClassName=&logicGridName=&planCode=2&sorterPlanId=9881000030&areaCode=21520000");
        uRIBuilder.setHost(ip).setParameter("sorterPlanId", fanganid)
                .setParameter("pageNo", String.valueOf(pageNo))
                .setParameter("pageSize","20");
        HttpGet get = new HttpGet(uRIBuilder.build());
        Document doc = Jsoup.parse(HttpClientUtil.doGet(client,get, "utf8"));
        List<Fenjianfangan> lists = new ArrayList();
        for(Element e :doc.select("table#contentTable > tbody > tr#trr")){
            Fenjianfangan fa = new Fenjianfangan();
            fa.setLuojigekou(e.select("td").get(11).text());
            fa.setLuojigekouname(e.select("td").get(12).text());
            fa.setWuligekou1(Integer.valueOf(e.select("td").get(14).text()));
            fa.setWuligekou2(Integer.valueOf(e.select("td").get(15).text()));
            fa.setWuligekou3(Integer.valueOf(e.select("td").get(16).text()));
            fa.setWuligekou4(Integer.valueOf(e.select("td").get(17).text()));
            
            lists.add(fa);
        }
        return lists;
    }
    
    public List<Baoguogui> getbaoguogui(String kshijian,String jshijian) throws URISyntaxException, UnsupportedEncodingException{
        //https://10.4.188.1/report-web/a/report/tdgl/allBrandParcelCabinetStatistics/result
        
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("buOrgCode", "00003500");
        BasicNameValuePair param2 = new BasicNameValuePair("orgLevel","04");
        BasicNameValuePair param3 = new BasicNameValuePair("provinceOrgCode", "320000");
        BasicNameValuePair param4 = new BasicNameValuePair("cityOrgCode", "320500");
        BasicNameValuePair param5 = new BasicNameValuePair("districtOrgCode", "320584");
        BasicNameValuePair param6 = new BasicNameValuePair("code", "");
        BasicNameValuePair param7 = new BasicNameValuePair("statisticsDateStart", kshijian.replaceAll("-", ""));
        BasicNameValuePair param8 = new BasicNameValuePair("statisticsDateEnd", jshijian.replaceAll("-", ""));
        BasicNameValuePair param9 = new BasicNameValuePair("tjrqStart", kshijian);
        BasicNameValuePair param10 = new BasicNameValuePair("tjrqEnd", jshijian);
        BasicNameValuePair param11 = new BasicNameValuePair("dateStart", kshijian);
        BasicNameValuePair param12 = new BasicNameValuePair("dateEnd", jshijian);
        BasicNameValuePair param13 = new BasicNameValuePair("monthdateStart", "2020-08");
        BasicNameValuePair param14 = new BasicNameValuePair("monthdateEnd", "2020-08");
        BasicNameValuePair param15 = new BasicNameValuePair("yeardateStart", "2019");
        BasicNameValuePair param16 = new BasicNameValuePair("yeardateEnd", "2019");
        BasicNameValuePair param17 = new BasicNameValuePair("empBusinessUnit", "");
        BasicNameValuePair param18 = new BasicNameValuePair("statisticsLevel", "06");
        BasicNameValuePair param19 = new BasicNameValuePair("businessUnit", "");
        BasicNameValuePair param20 = new BasicNameValuePair("statisticsType", "2");
        BasicNameValuePair param21 = new BasicNameValuePair("core", "");
        BasicNameValuePair param22 = new BasicNameValuePair("isDate", "0");
        BasicNameValuePair param23 = new BasicNameValuePair("reportType", "1");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        list.add(param6);
        list.add(param7);
        list.add(param8);
        list.add(param9);
        list.add(param10);
        list.add(param11);
        list.add(param12);
        list.add(param13);
        list.add(param14);
        list.add(param15);
        list.add(param16);
        list.add(param17);
        list.add(param18);
        list.add(param19);
        list.add(param20);
        list.add(param21);
        list.add(param22);
        list.add(param23);
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/report-web/a/report/tdgl/allBrandParcelCabinetStatistics/result");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        String str = HttpClientUtil.doPost(client, post, "utf8");
        //System.out.println(str);
        Document doc = Jsoup.parse(str, "utf8");
        List<Baoguogui> baoguiList = new ArrayList<>();
        for(Element e :doc.select("table#contentTable > tbody > tr")){
            Baoguogui b = new Baoguogui();
            List<Element> l = e.select("td");
            b.setSheng(l.get(1).text());
            b.setShi(l.get(2).text());
            b.setQu(l.get(3).text());
            b.setJigoudaima(l.get(4).text());
            b.setJigoumingcheng(l.get(5).text());
            b.setHejitouguiliang(Integer.valueOf(l.get(7).text()));
            baoguiList.add(b);
        }
        return baoguiList;
    }
    
    
    public List<Tuotouzitiwangdian> getTuotouzitiwangdians(String kshijian,String jshijian) throws URISyntaxException, UnsupportedEncodingException{
        String str = gettuotouziti(kshijian,jshijian,"06");
        Document doc = Jsoup.parse(str, "utf8");
        List<Tuotouzitiwangdian> baoguiList = new ArrayList<>();
        for(Element e :doc.select("table#contentTable > tbody > tr")){
            Tuotouzitiwangdian b = new Tuotouzitiwangdian();
            List<Element> l = e.select("td");
            b.setSheng(l.get(1).text());
            b.setShi(l.get(2).text());
            b.setQu(l.get(3).text());
            b.setJigoudaima(l.get(4).text());
            b.setJigoumingcheng(l.get(5).text());
            b.setTuotouliang(Integer.valueOf(l.get(7).text()));
            b.setYingyewangdianzhuanziti(Integer.valueOf(l.get(12).text()));
            b.setDaxuexiaoyuanzhuanziti(Integer.valueOf(l.get(14).text()));
            b.setToudiwangdianzhuanziti(Integer.valueOf(l.get(16).text()));
            b.setShehuiwangdianzhuangziti(Integer.valueOf(l.get(18).text()));
            b.setBaokantingzhuanziti(Integer.valueOf(l.get(22).text()));
            baoguiList.add(b);
        }
        return baoguiList;
    }
    
    public List<Tuotouzitizitidian> getTuotouzitizitidians(String kshijian,String jshijian) throws URISyntaxException, UnsupportedEncodingException{
        String str = gettuotouziti(kshijian,jshijian,"11");
        Document doc = Jsoup.parse(str, "utf8");
        List<Tuotouzitizitidian> baoguiList = new ArrayList<>();
        for(Element e :doc.select("table#contentTable > tbody > tr")){
            Tuotouzitizitidian b = new Tuotouzitizitidian();
            List<Element> l = e.select("td");
            b.setSheng(l.get(1).text());
            b.setShi(l.get(2).text());
            b.setQu(l.get(3).text());
            b.setJigoudaima(l.get(4).text());
            b.setJigoumingcheng(l.get(5).text());
            b.setZitidianbianhao(l.get(6).text());
            b.setZitidianmingcheng(l.get(7).text());
            b.setZhuanzitishu(Integer.valueOf(l.get(8).text()));
            baoguiList.add(b);
        }
        return baoguiList;
    }
    
    /**
     * 
     * @param kshijian
     * @param jshijian
     * @param statisticsType 
     * 06 获取网点
     * 11 获取带头点
     * @return
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException 
     */
    private String gettuotouziti(String kshijian,String jshijian,String statisticsType) throws URISyntaxException, UnsupportedEncodingException{
        //https://10.4.188.1/report-web/a/report/tdgl/allBrandParcelCabinetStatistics/result
        
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("provinceOrgCode", "320000");
        BasicNameValuePair param2 = new BasicNameValuePair("cityOrgCode","320500");
        BasicNameValuePair param3 = new BasicNameValuePair("districtOrgCode", "320584");
        BasicNameValuePair param4 = new BasicNameValuePair("code", "");
        BasicNameValuePair param5 = new BasicNameValuePair("statisticsDateStart", kshijian.replaceAll("-", ""));
        BasicNameValuePair param6 = new BasicNameValuePair("statisticsDateEnd", jshijian.replaceAll("-", ""));
        BasicNameValuePair param7 = new BasicNameValuePair("tjrqStart", kshijian);
        BasicNameValuePair param8 = new BasicNameValuePair("tjrqEnd", jshijian);
        BasicNameValuePair param9 = new BasicNameValuePair("dateStart", kshijian);
        BasicNameValuePair param10 = new BasicNameValuePair("dateEnd", jshijian);
        BasicNameValuePair param11 = new BasicNameValuePair("monthdateStart", "2020-08");
        BasicNameValuePair param12 = new BasicNameValuePair("monthdateEnd", "2020-08");
        BasicNameValuePair param13 = new BasicNameValuePair("yeardateStart", "2019");
        BasicNameValuePair param14 = new BasicNameValuePair("yeardateEnd", "2019");
        BasicNameValuePair param15 = new BasicNameValuePair("statisticsLevel", statisticsType);
        BasicNameValuePair param16 = new BasicNameValuePair("businessUnit", "");
        BasicNameValuePair param17 = new BasicNameValuePair("statisticsType", "2");
        BasicNameValuePair param18 = new BasicNameValuePair("yjzl", "");
        BasicNameValuePair param19 = new BasicNameValuePair("bs", "");
        BasicNameValuePair param20 = new BasicNameValuePair("selfPickupNetworkBrandCode", "");
        BasicNameValuePair param21 = new BasicNameValuePair("core", "");
        BasicNameValuePair param22 = new BasicNameValuePair("isDate", "0");
        BasicNameValuePair param23 = new BasicNameValuePair("reportType", "1");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        list.add(param6);
        list.add(param7);
        list.add(param8);
        list.add(param9);
        list.add(param10);
        list.add(param11);
        list.add(param12);
        list.add(param13);
        list.add(param14);
        list.add(param15);
        list.add(param16);
        list.add(param17);
        list.add(param18);
        list.add(param19);
        list.add(param20);
        list.add(param21);
        list.add(param22);
        list.add(param23);
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/report-web/a/report/tdgl/submitStatisticalReport/result");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        String str = HttpClientUtil.doPost(client, post, "utf8");
        return str;
    }
    
    private List zhuanhuan(Map<String,String> map){
        List<NameValuePair> list = new LinkedList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            list.add(new BasicNameValuePair(key, val));
        }
        return list;
    }
    
    public String getmingzhidizhi(String mailno) throws URISyntaxException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/addrcntr-web/a/addrcntr/amcmatchgis/getAddr?waybillNo=");
        uRIBuilder.setHost(ip).setParameter("waybillNo", mailno);
        
        HttpGet get = new HttpGet(uRIBuilder.build());
        
        String str = HttpClientUtil.doGet(client, get, "utf8");
        return str;
    }
    
    public String getpipeiduodao(String mailno,String addr,String pipeijigou) throws URISyntaxException, UnsupportedEncodingException{
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/addrcntr-web/a/addrcntr/amcmatchgis/txmatchAll");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("waybillNo", mailno);
        BasicNameValuePair param2 = new BasicNameValuePair("address",addr);
        BasicNameValuePair param3 = new BasicNameValuePair("deliverOrgCode", pipeijigou);
        BasicNameValuePair param4 = new BasicNameValuePair("busiType", "8");
        BasicNameValuePair param5 = new BasicNameValuePair("layer","0");
        BasicNameValuePair param6 = new BasicNameValuePair("businessUnit", "B");
        BasicNameValuePair param7 = new BasicNameValuePair("matchDataSource", "1");
        BasicNameValuePair param8 = new BasicNameValuePair("gisType", "0");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        list.add(param6);
        list.add(param7);
        list.add(param8);
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        post.setEntity(entityParam);
        String str = HttpClientUtil.doPost(client, post, "utf8");
        return  str;
    }
    
    public void getxiaduanmingxi(String kshijian,String jshijian,int pageNo) throws URISyntaxException{
        //https://10.4.188.1/delivery-web/a/delivery/deliveryfeedbackquery/querydeliveryfeedbackquery
        URIBuilder uRIBuilder = new URIBuilder("https://10.4.188.1/addrcntr-web/a/addrcntr/amcmatchgis/txmatchAll");
        uRIBuilder.setHost(ip);
        HttpPost post = new HttpPost(uRIBuilder.build());
        List<NameValuePair> list = new LinkedList<>();
        BasicNameValuePair param1 = new BasicNameValuePair("netWork", "");
        BasicNameValuePair param2 = new BasicNameValuePair("pageSize","20");
        BasicNameValuePair param3 = new BasicNameValuePair("pageNo", pageNo+"");
        BasicNameValuePair param4 = new BasicNameValuePair("waybillNo", "");
        BasicNameValuePair param5 = new BasicNameValuePair("waybillState","");
        BasicNameValuePair param6 = new BasicNameValuePair("beginDate", "");
        BasicNameValuePair param7 = new BasicNameValuePair("endDate", "");
        BasicNameValuePair param8 = new BasicNameValuePair("startDate", kshijian);
        BasicNameValuePair param9 = new BasicNameValuePair("endingDate", jshijian);
        BasicNameValuePair param10 = new BasicNameValuePair("baseProductCode", "");
        BasicNameValuePair param11 = new BasicNameValuePair("bizProductCode", "");
        BasicNameValuePair param12 = new BasicNameValuePair("beginTime", "0");
        BasicNameValuePair param24 = new BasicNameValuePair("endTime", "0");
        BasicNameValuePair param13 = new BasicNameValuePair("dlvRoadseg", "0");
        BasicNameValuePair param14 = new BasicNameValuePair("dlvPersonNo", "0");
        BasicNameValuePair param15 = new BasicNameValuePair("dlvState", "0");
        BasicNameValuePair param16 = new BasicNameValuePair("dlvNextActione", "0");
        BasicNameValuePair param17 = new BasicNameValuePair("receiverAddr", "0");
        BasicNameValuePair param18 = new BasicNameValuePair("receiverName", "0");
        BasicNameValuePair param19 = new BasicNameValuePair("receiverMobile", "0");
        BasicNameValuePair param20 = new BasicNameValuePair("returnworkState", "0");
        BasicNameValuePair param21 = new BasicNameValuePair("businessProdductCode", "0");
        BasicNameValuePair param22 = new BasicNameValuePair("codFlag", "0");
        BasicNameValuePair param23 = new BasicNameValuePair("paymentMode", "0");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);
        list.add(param5);
        list.add(param6);
        list.add(param7);
        list.add(param8);
        list.add(param9);
        list.add(param10);
        list.add(param11);
        list.add(param12);
        list.add(param13);
        list.add(param14);
        list.add(param15);
        list.add(param16);
        list.add(param17);
        list.add(param18);
        list.add(param19);
        list.add(param20);
        list.add(param21);
        list.add(param22);
        list.add(param23);
        list.add(param24);
        
        
    }
    
    public static void main(String[] args) throws Exception {
        //Fjjclient f = new Fjjclient();
        //System.out.println(f.getGKCX("123455678"));
        //https://10.4.188.1/portal/a/substitutionorg/11692
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String jshijian = dateFormat.format(calendar.getTime());
//        calendar.add(Calendar.MINUTE, -30);
//        String kshijian =dateFormat.format(calendar.getTime());
        XydClinet xydClinet = new XydClinet("2152660099999","xyd1234567","10.4.188.1");
        xydClinet.login();
        xydClinet.xuanzetiban("11692");
        xydClinet.shouye();
//       xydClinet.getshoujixinxiPagesize(kshijian, jshijian);
        
//        String s = "{\"resCode\":\"\",\"resMess\":\"\",\"comment\":\"5045.6\",\"detail\":[{\"count\":377,\"pkpWaybillId\":15057546701,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1126437329734\",\"bizOccurDate\":\"2020-06-27 08:54:59.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"1100036775190\",\"sender\":\"苏州嗨喽科技有限公司\",\"senderLinker\":\"陈先生\",\"senderMobile\":\"13656250618\",\"senderIdType\":\"00\",\"senderIdNo\":\"91320509MA1WURPJX2\",\"senderAddr\":\"江苏省苏州市吴江市开平路3333号德尔广场A座\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"恩生\",\"receiverMobile\":\"18012731732\",\"receiverAddr\":\"广东省广州市白云区三元里沙涌北和盛苑\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"440000\",\"receiverProvinceName\":\"广东省\",\"receiverCityName\":\"广州市\",\"receiverCountyName\":\"白云区\",\"receiverPostcode\":\"510400\",\"realWeight\":750.0,\"feeWeight\":750.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"3\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593219299000,\"postageTotal\":17.0,\"postageStandard\":21.0,\"postagePaid\":17.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593219299000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"51000000-广州市|51010000-广州穗本\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"广州穗本\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15058728859,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1126481784934\",\"bizOccurDate\":\"2020-06-27 09:56:31.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"徐炎\",\"senderMobile\":\"18252222381\",\"senderIdType\":\"01\",\"senderIdNo\":\"320928199910275312\",\"senderAddr\":\"江苏省苏州市吴江市松陵镇吾悦广场吾悦一期51202\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"徐炎\",\"receiverMobile\":\"18252222381\",\"receiverAddr\":\"江苏省盐城市盐都区大冈镇万利达厨具\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"盐城市\",\"receiverCountyName\":\"盐都区\",\"receiverPostcode\":\"224000\",\"realWeight\":12670.0,\"feeWeight\":12670.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593222991000,\"postageTotal\":31.0,\"postageStandard\":36.0,\"postagePaid\":31.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593222991000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"22400000-盐城市|22400000-盐城市\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"盐城市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15059704156,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1195862969578\",\"bizOccurDate\":\"2020-06-27 09:59:15.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"徐炎\",\"senderMobile\":\"18252222381\",\"senderIdType\":\"01\",\"senderIdNo\":\"320928199910275312\",\"senderAddr\":\"江苏省苏州市吴江市开平路2188号新城吾悦广场吾悦一期51202\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"徐炎\",\"receiverMobile\":\"18252222381\",\"receiverAddr\":\"江苏省盐城市盐都区江苏盐城市盐都区卧龙路南50米万利达厨具大冈店\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"盐城市\",\"receiverCountyName\":\"盐都区\",\"receiverPostcode\":\"224000\",\"realWeight\":11330.0,\"feeWeight\":11330.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593223155000,\"postageTotal\":33.0,\"postageStandard\":33.0,\"postagePaid\":33.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593223155000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"22400000-盐城市|22400000-盐城市\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"盐城市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15057276265,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1195862970478\",\"bizOccurDate\":\"2020-06-27 10:00:27.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"徐炎\",\"senderMobile\":\"18252222381\",\"senderIdType\":\"01\",\"senderIdNo\":\"320928199910275312\",\"senderAddr\":\"江苏省苏州市吴江市松陵镇吾悦广场吾悦一期51202\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"徐炎\",\"receiverMobile\":\"18252222381\",\"receiverAddr\":\"江苏省盐城市盐都区江苏盐城市盐都区大冈镇万利达厨具\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"盐城市\",\"receiverCountyName\":\"盐都区\",\"receiverPostcode\":\"224000\",\"realWeight\":4000.0,\"feeWeight\":4000.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593223227000,\"postageTotal\":18.0,\"postageStandard\":18.0,\"postagePaid\":18.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593223226000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"22400000-盐城市|22400000-盐城市\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"盐城市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15059121829,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1157909955576\",\"bizOccurDate\":\"2020-06-27 10:02:51.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"朱焘焘\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"杨春科\",\"senderMobile\":\"18362382674\",\"senderIdType\":\"01\",\"senderIdNo\":\"622429199610130439\",\"senderAddr\":\"江苏省苏州市吴江市江苏省苏州市吴江区群光电子有限公司\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"杨春科\",\"receiverMobile\":\"18362382674\",\"receiverAddr\":\"甘肃省定西市岷县甘肃省定西市岷县茶埠镇谈\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"620000\",\"receiverProvinceName\":\"甘肃省\",\"receiverCityName\":\"定西市\",\"receiverCountyName\":\"岷县\",\"receiverPostcode\":\"748400\",\"realWeight\":16000.0,\"feeWeight\":16000.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593223371000,\"postageTotal\":201.0,\"postageStandard\":201.0,\"postagePaid\":201.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593223371000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"73000000-兰州|73000000-兰州\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"岷县\",\"isJinguan\":\"0\",\"pickupPersonName\":\"朱焘焘\"},{\"count\":0,\"pkpWaybillId\":15057877784,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1195862971878\",\"bizOccurDate\":\"2020-06-27 10:35:44.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"叶明甲\",\"senderMobile\":\"13451651900\",\"senderIdType\":\"01\",\"senderIdNo\":\"34052119840515001x\",\"senderAddr\":\"江苏省苏州市吴江市东太湖大道绿地太湖城143栋2706\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"刘欢\",\"receiverMobile\":\"13581329065\",\"receiverAddr\":\"浙江省湖州市南浔区浙江湖州市南浔区经济开发区强园东路浙江联大科技有限责任公司\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"330000\",\"receiverProvinceName\":\"浙江省\",\"receiverCityName\":\"湖州市\",\"receiverCountyName\":\"南浔区\",\"receiverPostcode\":\"313000\",\"realWeight\":800.0,\"feeWeight\":800.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593225344000,\"postageTotal\":8.0,\"postageStandard\":12.0,\"postagePaid\":8.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593225344000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"31300000-湖州|31300000-湖州\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"湖州\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15060419818,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1195800383578\",\"bizOccurDate\":\"2020-06-27 11:47:50.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"陈荣明\",\"senderNo\":\"1100043810187\",\"sender\":\"德尔未来科技控股集团股份有限公司(松陵）\",\"senderLinker\":\"佘金鑫\",\"senderMobile\":\"13179780231\",\"senderIdType\":\"00\",\"senderIdNo\":\"91320500767387634T\",\"senderAddr\":\"江苏省苏州市吴江市德尔广场B座\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"李旭楠\",\"receiverMobile\":\"18684315845\",\"receiverAddr\":\"辽宁省大连市甘井子区前革李老汤拉面\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"210000\",\"receiverProvinceName\":\"辽宁省\",\"receiverCityName\":\"大连市\",\"receiverCountyName\":\"甘井子区\",\"receiverPostcode\":\"116000\",\"realWeight\":170.0,\"feeWeight\":170.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593229670000,\"postageTotal\":16.0,\"postageStandard\":21.0,\"postagePaid\":16.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593229670000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"11600000-大连|11600000-大连\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"大连\",\"isJinguan\":\"0\",\"pickupPersonName\":\"陈荣明\"},{\"count\":0,\"pkpWaybillId\":15059658007,\"bizProductName\":\"国内EMS促销物品\",\"ioType\":\"30\",\"ecommerceNo\":\"CLOUD\",\"waybillNo\":\"1102467624677\",\"bizOccurDate\":\"2020-06-27 11:59:44.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"顾智晓\",\"senderNo\":\"\",\"sender\":\"\",\"senderLinker\":\"申先生\",\"senderMobile\":\"18556880719\",\"senderIdType\":\"01\",\"senderIdNo\":\"410222198403153530\",\"senderAddr\":\"江苏省苏州市吴江市松岭镇西湖花苑641号麻辣烫\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"杨女士\",\"receiverMobile\":\"15701678561\",\"receiverAddr\":\"北京市北京市房山区窦店于庄豪庭公寓\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"110000\",\"receiverProvinceName\":\"北京市\",\"receiverCityName\":\"北京市\",\"receiverCountyName\":\"房山区\",\"receiverPostcode\":\"102400\",\"realWeight\":200.0,\"feeWeight\":200.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptWaybillNo\":\"\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230384000,\"postageTotal\":21.0,\"postageStandard\":21.0,\"postagePaid\":21.0,\"postageOther\":0.0,\"settlementMode\":\"1\",\"gmtModified\":1593237868000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"10000000-北京|10000000-北京\",\"oneBillFlag\":\"0\",\"receiverArriveOrgName\":\"房山\",\"isJinguan\":\"0\",\"pickupPersonName\":\"顾智晓\"},{\"count\":0,\"pkpWaybillId\":15061006458,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529406074\",\"bizOccurDate\":\"2020-06-27 12:02:50.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"陈琪铭\",\"receiverMobile\":\"13912763918\",\"receiverAddr\":\"江苏省苏州市吴江市盛泽镇吴江区盛泽镇敦煌路388号汇赢金融大厦101室201室202室\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":4920.0,\"feeWeight\":4920.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230570000,\"postageTotal\":22.0,\"postageStandard\":20.0,\"postagePaid\":22.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230570000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15058286787,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529622074\",\"bizOccurDate\":\"2020-06-27 12:02:56.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"陆金清\",\"receiverMobile\":\"13701558507\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区平望镇黄秧墩路12号107室之信会计公司\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":200.0,\"feeWeight\":200.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230576000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230575000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15061302872,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529621674\",\"bizOccurDate\":\"2020-06-27 12:02:57.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"刘玉文\",\"receiverMobile\":\"13405656900\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区盛泽镇西环路德尔金色摩纺号17幢101室\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":150.0,\"feeWeight\":150.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230577000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230577000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15059659414,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529620274\",\"bizOccurDate\":\"2020-06-27 12:02:58.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"沈洁芳\",\"receiverMobile\":\"13584416903\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区平望镇学才路路29号锦绣家园\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":150.0,\"feeWeight\":150.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230578000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230578000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15058581755,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529491874\",\"bizOccurDate\":\"2020-06-27 12:03:00.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"王剑文\",\"receiverMobile\":\"13912720904\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区盛泽镇祥盛路426号盛泽镇祥盛路426号锦绣天地商铺7号\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":220.0,\"feeWeight\":220.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230580000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230580000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15060226212,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529489574\",\"bizOccurDate\":\"2020-06-27 12:03:01.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"陆建明\",\"receiverMobile\":\"13912763166\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区同里镇屯村西路86号屯村西86号\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":180.0,\"feeWeight\":180.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230581000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230581000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15059756061,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529472874\",\"bizOccurDate\":\"2020-06-27 12:03:03.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"屠晟珣\",\"receiverMobile\":\"13511618343\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区盛泽镇舜湖西路0号东茂商业广场1幢907室\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":200.0,\"feeWeight\":200.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230583000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230583000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15060716723,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529480274\",\"bizOccurDate\":\"2020-06-27 12:03:08.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"顾一鸰\",\"receiverMobile\":\"15358814663\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区黎里镇吴江区北厍镇梅墩村八组南港路61号佳怡安财务公司\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":350.0,\"feeWeight\":350.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230588000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230588000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15058678953,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529488174\",\"bizOccurDate\":\"2020-06-27 12:03:22.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"涂毛毛\",\"receiverMobile\":\"18012700217\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区盛泽镇临盛路路2号百典纺织\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":220.0,\"feeWeight\":220.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230602000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230602000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15058678960,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529481674\",\"bizOccurDate\":\"2020-06-27 12:03:24.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"顾雪凤\",\"receiverMobile\":\"13913085950\",\"receiverAddr\":\"江苏省苏州市吴江市盛泽镇吴江区盛泽镇轻纺商城三分场六区14幢5号\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":470.0,\"feeWeight\":470.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230604000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230604000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15059466277,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529487874\",\"bizOccurDate\":\"2020-06-27 12:03:25.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"陆建明\",\"receiverMobile\":\"13912763166\",\"receiverAddr\":\"江苏省苏州市吴江市吴江区同里镇屯村西路86号屯村西86号\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":200.0,\"feeWeight\":200.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230605000,\"postageTotal\":14.0,\"postageStandard\":12.0,\"postagePaid\":14.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230605000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"},{\"count\":0,\"pkpWaybillId\":15061106554,\"bizProductName\":\"国内EMS促销文件\",\"ioType\":\"30\",\"ecommerceNo\":\"EMSRM\",\"waybillNo\":\"1133529486474\",\"bizOccurDate\":\"2020-06-27 12:03:26.0\",\"postOrgNo\":\"21520004\",\"postOrgName\":\"中国邮政集团有限公司苏州市吴江区寄递事业部松陵营业部\",\"postPersonName\":\"王平\",\"senderNo\":\"90000009842597\",\"sender\":\"吴江国税发票寄递\",\"senderLinker\":\"苏州市吴江区税务局\",\"senderMobile\":\"13375168130\",\"senderIdType\":\"00\",\"senderIdNo\":\"11320584014204178A\",\"senderAddr\":\"江苏省苏州市吴江市松陵街道体育路579号\",\"senderProvinceName\":\"江苏省\",\"senderCityName\":\"苏州市\",\"senderCountyName\":\"吴江市\",\"senderDistrictNo\":\"320584\",\"senderPostcode\":\"215299\",\"receiverLinker\":\"吴玉妹\",\"receiverMobile\":\"13912720228\",\"receiverAddr\":\"江苏省苏州市吴江市震泽镇吴江区震泽镇林港村\",\"receiverCountryName\":\"中华人民共和国\",\"receiverProvinceNo\":\"320000\",\"receiverProvinceName\":\"江苏省\",\"receiverCityName\":\"苏州市\",\"receiverCountyName\":\"吴江市\",\"receiverPostcode\":\"215200\",\"realWeight\":1570.0,\"feeWeight\":1570.0,\"volume\":0.0,\"length\":0.0,\"width\":0.0,\"height\":0.0,\"codAmount\":0.0,\"receiptFlag\":\"1\",\"receiptFeeAmount\":0.0,\"insuranceFlag\":\"1\",\"insuranceAmount\":0.0,\"insurancePremiumAmount\":0.0,\"transferType\":\"1\",\"allowFeeFlag\":\"1\",\"isFeedFlag\":\"1\",\"feeDate\":1593230606000,\"postageTotal\":16.0,\"postageStandard\":14.0,\"postagePaid\":16.0,\"postageOther\":0.0,\"settlementMode\":\"2\",\"gmtModified\":1593230606000,\"theOrgGridNo\":\"1\",\"sortingCode\":\"21500000-苏州市|21500000-苏州市\",\"oneBillFlag\":\"0\",\"oneBillFeeType\":\"0\",\"receiverArriveOrgName\":\"吴江市\",\"isJinguan\":\"0\",\"pickupPersonName\":\"王平\"}],\"innerHtml\":\"<ul>\\n<li class=\\\"disabled\\\"><a href=\\\"javascript:\\\">? 上一页</a></li>\\n<li class=\\\"active\\\"><a href=\\\"javascript:\\\">1</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(2,20);\\\">2</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(3,20);\\\">3</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(4,20);\\\">4</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(5,20);\\\">5</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(6,20);\\\">6</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(7,20);\\\">7</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(8,20);\\\">8</a></li>\\n<li class=\\\"disabled\\\"><a href=\\\"javascript:\\\">...</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(19,20);\\\">19</a></li>\\n<li><a href=\\\"javascript:\\\" onclick=\\\"page(2,20);\\\">下一页 ?</a></li>\\n<li class=\\\"disabled controls\\\"><a href=\\\"javascript:\\\">当前 <input type=\\\"text\\\" value=\\\"1\\\" onkeypress=\\\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)page(this.value,20);\\\" onclick=\\\"this.select();\\\"  id='pageNo1' /> / <input type=\\\"text\\\" value=\\\"19\\\" onkeypress=\\\"var e=window.event||event;var c=e.keyCode||e.which;if(c==13)page(1,this.value,');\\\" onclick=\\\"this.select();\\\" style='width:35px'/> 页，共 377 条</a></li>\\n<li class=\\\"disabled controls\\\"><a href=\\\"javascript:\\\"> </ul>\\n<div style=\\\"clear:both;\\\"></div>\"}";
//        Gson gson = new Gson();
//        Type type = new TypeToken<Shoujishuju>() {}.getType();
//        //Type type2 = new TypeToken<List<Map<String, Object>>>() {}.getType();
//        Shoujishuju ss = gson.fromJson(s, Shoujishuju.class);
//        //System.out.println(String.valueOf(m.get("detail")).replace("[", "").replace("]", ""));
//        //List<Map<String,Object>> lm = gson.fromJson(String.valueOf(m.get("detail")),type2);
//        System.out.println(Integer.valueOf(ss.getInnerHtml().split("/ <input type=\\\"text\\\" value=\\\"")[1].split("\\\"")[0]));
        //9uCSErHaqqc8yoamaIj0jg==
        
//        FileWriter fw = new FileWriter(new File("c:\\1.txt"));
//        BufferedReader br = new BufferedReader(new FileReader("c:\\2.txt"));
//        FileWriter fw3 = new FileWriter(new File("c:\\3.txt"));
//        
//        String mima =  "wK63038952";
//        XydClinet xydClinet = new XydClinet("2152660099999","xyd1234567","10.4.188.1");
//        fw.write(xydClinet.shouye());
//        xydClinet.login();
//        //xydClinet.xuanzetiban("7648");
//        //xydClinet.xuanzetaixi("rs:{\"workShopCode\":\"SD21520017\",\"workShopName\":\"默认车间\",\"workShopGroupCode\":\"001\",\"workShopGroupName\":\"早班\",\"seatCode\":\"DEFAULT_CONFIGURE\",\"seatName\":\"默认台席配置\"}");
//        xydClinet.shouye();
//        fw3.write(xydClinet.shouye());
        //System.out.println(xydClinet.getfenjianfanganid());
        //System.out.println(xydClinet.getfenjianfangan("9881000030",1));
//        String str  = br.readLine();
//        while (str != null) {
//            System.out.println(str);
//            for(Guiji g :xydClinet.getGuiji(str)){
//                if(g.getOpCode().equals("203")){
//                    fw.write(g.getDesc()+"\r\n");
//                }
//            }
//            
//            str = br.readLine();
//        }
//        fw.close();
//        br.close();
    }
}
