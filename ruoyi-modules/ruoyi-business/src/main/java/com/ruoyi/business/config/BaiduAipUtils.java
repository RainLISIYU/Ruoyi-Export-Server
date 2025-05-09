package com.ruoyi.business.config;

import com.ruoyi.business.config.properties.BaiduAipProperties;
import com.ruoyi.common.core.utils.sign.Sha256Utils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.util.UriEncoder;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author lsy
 * @description 百度ai配置类
 * @date 2025/1/6
 */
@Component
public class BaiduAipUtils {

    @Resource
    private BaiduAipProperties baiduAipProperties;

    /**
     * 生成认证签名
     *
     * @param method 请求方式
     * @param path 路径
     * @param headers 请求头
     * @return 认证签名
     */
    public String generateSignKey(String method, String path, Map<String, String> headers) {
        String authStringPrefix = generateAuthPrefix();
        String signedHeaders = getSignedHeaders(headers);
        String canonicalRequest = generationCanonicalRequest(method, path, headers);
        String signature = generateSignature(generateSigningKey(authStringPrefix), canonicalRequest);
        return authStringPrefix + "/" + signedHeaders + "/" + signature;
    }

    /**
     * 生成授权前缀
     *
     * @return 前缀
     */
    public String generateAuthPrefix() {
        return baiduAipProperties.getAuthPrefix() + "/" +
                baiduAipProperties.getAccessKey() + "/" +
                LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))
                        .replace(" ", "T") + "Z" + "/" +
                baiduAipProperties.getExpirationPeriodInSeconds();
    }

    /**
     *获取signingKey
     *
     * @param authStringPrefix 认证前缀
     * @return 加密数据
     */
    public String generateSigningKey(String authStringPrefix) {
        String sign = baiduAipProperties.getSecretKey();
        return Sha256Utils.hmacSHA256(sign, authStringPrefix);
    }

    /**
     * 获取signature
     *
     * @return 密钥
     */
    public String generateSignature(String signingKey, String canonicalRequest) {
        return Sha256Utils.hmacSHA256(signingKey, canonicalRequest);
    }

    /**
     * 获取规范请求
     *
     * @param method 请求方式 GET、POST、PUT、DELETE
     * @param path 请求url
     * @param headers 请求头map
     * @return 规范请求 CanonicalRequest = HTTP Method + "\n" + CanonicalURI + "\n" + CanonicalQueryString + "\n" + CanonicalHeaders
     */
    public String generationCanonicalRequest(String method, String path, Map<String, String> headers) {
        // 获取CanonicalURI
        String canonicalURI = getCanonicalURI(path);
        // 获取queryString
        String queryString = getQueryString(path);
        // 获取CanonicalHeaders
        String canonicalHeaders = getCanonicalHeaders(headers);
        // 获取规范请求 CanonicalRequest
        return method + "\n" + canonicalURI + "\n" + queryString + "\n" + canonicalHeaders;
    }

    /**
     * 获取规范URI
     *
     * @param path 完整url
     * @return 规范URI
     */
    private String getCanonicalURI(String path) {
        // 获取url中?前面的字符串
        String[] pathArr = path.split("\\?");
        String pathStr = pathArr[0];
        // 获取CanonicalURI
        return pathStr.substring(pathStr.indexOf("") + "".length());
    }

    /**
     * 获取查询参数
     *
     * @param path 路径
     * @return 结果
     */
    private String getQueryString(String path) {
        StringBuilder result = new StringBuilder();
        // 获取查询参数
        String[] queryStringArr = path.split("\\?");
        if (queryStringArr.length < 2) {
            return result.toString();
        }
        // 排序map
        SortedMap<String, String> sortedMap = new TreeMap<>();
        // 获取url中?后面的字符串
        String queryString = queryStringArr[1];
        String[] queryArr = queryString.split("&");
        for (String query : queryArr) {
            String[] querySplitArr = query.split("=");
            if (querySplitArr.length == 1) {
                String key = UriEncoder.encode(querySplitArr[0]) + "=";
                sortedMap.put(key, "");
            } else if (querySplitArr.length == 2) {
                String key = UriEncoder.encode(querySplitArr[0]) + "=" + UriEncoder.encode(querySplitArr[1]);
                sortedMap.put(key, "");
            }
        }
        // 返回排序后的字符串
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            result.append(entry.getKey()).append("&");
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 获取规范请求
     *
     * @param headers 请求头
     * @return 排序处理后的数据
     */
    private String getCanonicalHeaders(Map<String, String> headers) {
        // 排序map
        SortedMap<String, String> sortedMap = new TreeMap<>();
        // 按字典序排序请求头数据
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sortedMap.put(uriEncode(entry.getKey().toLowerCase(), true) + ":" +uriEncode(entry.getValue(), true), "");
        }
        // 获取规范请求
        StringBuilder result = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            result.append(key).append("\n");
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 获取签名头域
     *
     * @param headers 请求头map
     * @return 签名头域
     */
    private String getSignedHeaders(Map<String, String> headers) {
        // 排序map
        SortedMap<String, String> sortedMap = new TreeMap<>();
        // 获取签名头域
        for (String key : headers.keySet()) {
            sortedMap.put(key, "");
        }
        // 签名头域
        StringBuilder result = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            result.append(key).append(";");
        }
        return result.substring(0, result.length() - 1);
    }

    public static String uriEncode(CharSequence input, boolean encodeSlash) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '_' || ch == '-' || ch == '~' || ch == '.') {
                result.append(ch);
            } else if (ch == '/') {
                result.append(encodeSlash ? "%2F" : ch);
            } else {
                result.append(toHexUTF8(ch));
            }
        }
        return result.toString();
    }

    private static String toHexUTF8(char character) {
        String characterStr = String.valueOf(character);
        byte[] bytes = characterStr.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexStr = new StringBuilder();
        for (byte b : bytes) {
            hexStr.append(String.format("%02X", b & 0xFF));
        }
        return "%" + hexStr;
    }

    public static void main(String[] args) {
        String method = "PUT";
        String path = "/v1/test/myfolder/readme.txt?partNumber=9&uploadId=a44cc9bab11cbd156984767aad637851";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "bj.bcebos.com");
        headers.put("Date", "Mon, 27 Apr 2015 16:23:49 +0800");
        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", "8");
        headers.put("Content-Md5", "NFzcPqhviddjRNnSOGo4rw==");
        headers.put("x-bce-date", "2015-04-27T08:23:49Z");
        BaiduAipUtils baiduAipUtils = new BaiduAipUtils();
        System.out.println(baiduAipUtils.generationCanonicalRequest(method, path, headers));
    }

}
