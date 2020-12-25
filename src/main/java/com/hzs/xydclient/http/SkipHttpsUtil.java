/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @autoor szh
 */

package com.hzs.xydclient.http;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

/**
 * Description: httpclient跳过https验证
 */
public class SkipHttpsUtil {
    
    public static CloseableHttpAsyncClient wraphttpclientAsyncClient(CookieStore cookieStore){
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // don't check
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // don't check
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            SSLIOSessionStrategy sslioSessionStrategy = new SSLIOSessionStrategy(sslContext, SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                    .register("http", NoopIOSessionStrategy.INSTANCE)
                    .register("https", sslioSessionStrategy)
                    .build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager 
            pool = new PoolingNHttpClientConnectionManager(ioReactor,registry);
            pool.setMaxTotal(500); // 设置最多连接数
            pool.setDefaultMaxPerRoute(200); // 设置每个host最多20个连接数
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(12000) // 设置请求响应超时时间
                    .setConnectTimeout(12000) // 设置请求连接超时时间
                    .build();
 
            CloseableHttpAsyncClient httpclient  = HttpAsyncClients.custom().setConnectionManager(pool) // 设置连接池
                    .setDefaultRequestConfig(requestConfig) // 设置请求配置
                    .build();
            httpclient.start();
            return httpclient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    
    //private static Logger logger = Logger.getLogger(SkipHttpsUtil.class);
    //绕过证书
    public static CloseableHttpAsyncClient wrapClient(CookieStore cookieStore) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
 
                public void checkClientTrusted(X509Certificate[] arg0,
                        String arg1) throws CertificateException {
                }
 
                public void checkServerTrusted(X509Certificate[] arg0,
                        String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                    .setDefaultCookieStore(cookieStore)
                    .setUserAgent("Mozilla/5.0")
                    .setSSLContext(ctx)
                    
                    //.setSSLSocketFactory(ssf)
                    .build();
            httpclient.start();
            return httpclient;
        } catch (Exception e) {
            return HttpAsyncClients.createDefault();
        }
    }

        public static CloseableHttpClient createSSLClientDefault(){
                        try {
                            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                                    //信任所有
                            public boolean isTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                                    return true;
                                }
                                    }).build();
                                    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
                                    return HttpClients.custom().setSSLSocketFactory(sslsf).build();
                                    } catch (KeyManagementException e) {
                                        e.printStackTrace();
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                        } catch (KeyStoreException e) {
                                            e.printStackTrace();
                                        }
                                    return HttpClients.createDefault();
                                }    
}
