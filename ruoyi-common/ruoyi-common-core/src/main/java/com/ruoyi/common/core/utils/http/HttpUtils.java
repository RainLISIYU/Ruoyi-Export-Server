package com.ruoyi.common.core.utils.http;

import com.ruoyi.common.core.utils.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description http请求工具类
 * @date 2025/2/24
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * JsonPost get请求，获取相应头和返回值信息
     * @param url 请求地址
     * @param headers 请求头
     * @return response 响应头信息，result 返回值信息
     */
    public static Map<String, Object> doJsonGet(String url, Map<String, String> headers) {
        Map<String, Object> backMap = new HashMap<>();
        HttpGet httpGet = null;
        String result = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            httpGet = new HttpGet(url);
            // 请求头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = client.execute(httpGet);
            // 响应头信息
            backMap.put("response", response);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    // 响应值信息
                    backMap.put("result", result);
                }
            }
        } catch (Exception ex) {
            log.error("发送到接口出错", ex);
        }
        return backMap;
    }

    /**
     * JsonPost post请求，获取相应头和返回值信息
     * @param url 请求地址
     * @param jsonParam 请求参数
     * @param headers 请求头
     * @return response 响应头信息，result 返回值信息
     */
    public static Map<String, Object> doJsonPost(String url, String jsonParam, Map<String, String> headers) {
        Map<String, Object> backMap = new HashMap<>();
        HttpPost httpPost = null;
        String result = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            // 请求头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 请求参数
            if (StringUtils.isNotEmpty(jsonParam)) {
                StringEntity se = new StringEntity(jsonParam, "utf-8");
                httpPost.setEntity(se); // post方法中，加入json数据
            }
            HttpResponse response = client.execute(httpPost);
            // 响应头信息
            backMap.put("response", response);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    // 响应值信息
                    backMap.put("result", result);
                }
            }
        } catch (Exception ex) {
            log.error("发送到接口出错", ex);
        }
        return backMap;
    }

    /**
     * JsonDelete delete请求，获取相应头和返回值信息
     * @param url 请求地址
     * @param headers 请求头
     * @return response 响应头信息，result 返回值信息
     */
    public static Map<String, Object> doJsonDelete(String url, Map<String, String> headers) {
        Map<String, Object> backMap = new HashMap<>();
        HttpDelete httpDelete = null;
        String result = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            httpDelete = new HttpDelete(url);
            // 请求头
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpDelete.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = client.execute(httpDelete);
            // 响应头信息
            backMap.put("response", response);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                    // 响应值信息
                    backMap.put("result", result);
                }
            }
        } catch (Exception ex) {
            log.error("发送到接口出错", ex);
        }
        return backMap;
    }

}
