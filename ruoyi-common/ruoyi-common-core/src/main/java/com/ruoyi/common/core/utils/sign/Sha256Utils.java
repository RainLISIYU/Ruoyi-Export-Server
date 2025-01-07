package com.ruoyi.common.core.utils.sign;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author lsy
 * @description SHA256加密算法
 * @date 2025/1/7
 */
public class Sha256Utils {

    private static final Logger logger = LoggerFactory.getLogger(Sha256Utils.class);

    /**
     * HMAC SHA256加密
     * @param key 密钥
     * @param data 数据
     * @return 加密后的数据
     */
    public static String hmacSHA256(String key, String data) {

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKey secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());
            return Hex.encodeHexString(bytes);
        } catch (Exception e) {
            logger.error("SHA256加密失败", e);
            return null;
        }

    }

}
