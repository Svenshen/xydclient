/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.util.EntityUtils;
/**
 * 利用HttpClient进行post请求的工具类
 * @ClassName: HttpClientUtil 
 * @Description: TODO
 * @author Devin <xxx> 
 * @date 2017年2月7日 下午1:43:38 
 *  
 */
public class HttpClientUtil {
//    @SuppressWarnings("resource")
//    public static String doPost(HttpClient httpClient,HttpPost httpPost,String jsonstr,String charset){
//        
//        String result = null;
//        try{
//            
//            httpPost.addHeader("Content-Type", "application/json");
//            StringEntity se = new StringEntity(jsonstr);
//            se.setContentType("text/json");
//            se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
//            httpPost.setEntity(se);
//            HttpResponse response = httpClient.execute(httpPost);
//            if(response != null){
//                HttpEntity resEntity = response.getEntity();
//                if(resEntity != null){
//                    result = EntityUtils.toString(resEntity,charset);
//                }
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return result;
//    }
    
//    public static String doPostLocale(HttpClient httpClient,HttpPost httpPost,String charset){
//        
//        String result = null;
//        try{
//            HttpResponse response = httpClient.execute(httpPost);
//            result = response.getFirstHeader("Location").getValue();
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        return result;
//    }
    
    public static String doPost(HttpAsyncClient httpClient,HttpPost httpPost,String charset){
        
        String result = null;
        try{
            Future<HttpResponse> future = httpClient.execute(httpPost, null);
            //HttpResponse response = httpClient.execute(httpPost);
            //result = response.getFirstHeader("Location").getValue();
            HttpResponse response = future.get();
            if(response != null){
                if(response.getStatusLine().getStatusCode() == 302){
                    String url  = response.getFirstHeader("Location").getValue();
                    System.out.println("302"+url);
                    HttpGet get = new HttpGet(url);
                    result = doGet(httpClient, get, charset);
                }else{
                    HttpEntity resEntity = response.getEntity();
                
                    if(resEntity != null){
                        result = EntityUtils.toString(resEntity,charset);
                        //System.out.println(result);
                    }
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    
    @SuppressWarnings("resource")
    public static String doGet(HttpAsyncClient httpClient,HttpGet httpGet,String charset){
        String result = null;
        try{
            //StringEntity se = new StringEntity(jsonstr);
            //se.setContentType("text/json");
            //se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            //httpGet.setEntity(se);
            Future<HttpResponse> future = httpClient.execute(httpGet, null);
            HttpResponse response = future.get();
            //HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                if(response.getStatusLine().getStatusCode() == 302){
                    String url  = response.getFirstHeader("Location").getValue();
                    System.out.println("302"+url);
                    HttpGet get = new HttpGet(url);
                    result = doGet(httpClient, get, charset);
                }else{
                    HttpEntity resEntity = response.getEntity();
                    if(resEntity != null){
                        result = EntityUtils.toString(resEntity,charset);
                    }
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    
    
    @SuppressWarnings("resource")
    public static List<String> doGetAsync(HttpAsyncClient httpClient,List<HttpGet> httpGets,String charset){
        List<String> results = new ArrayList<>();
        String result = null;
        try{
            //StringEntity se = new StringEntity(jsonstr);
            //se.setContentType("text/json");
            //se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
            //httpGet.setEntity(se);
            List<Future<HttpResponse>> respList = new LinkedList<>();
            for(HttpGet httpGet :httpGets ){
                respList.add(httpClient.execute(httpGet, null));
            }
            //Future<HttpResponse> future = httpClient.execute(httpGet, null);
            //HttpResponse response = future.get();
            //HttpResponse response = httpClient.execute(httpGet);
            for (Future<HttpResponse> future : respList) {
                HttpResponse response = future.get();
                    if(response != null){
                        if(response.getStatusLine().getStatusCode() == 302){
                            String url  = response.getFirstHeader("Location").getValue();
                            System.out.println("302"+url);
                            HttpGet get = new HttpGet(url);
                            result = doGet(httpClient, get, charset);
                            }else{
                                HttpEntity resEntity = response.getEntity();
                                if(resEntity != null){
                                    result = EntityUtils.toString(resEntity,charset);
                                    results.add(result);
                                }
                            }

                    }
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return results;
    }
}
