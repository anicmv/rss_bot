package com.github.anicmv.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * @author anicmv
 * @date 2024/9/18 16:00
 * @description http 工具类
 */
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static HttpURLConnection getHttpURLConnection(String url) {
        HttpURLConnection cnn;
        try {
            cnn = (HttpURLConnection) new URI(url).toURL().openConnection();
            cnn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        } catch (IOException | URISyntaxException e) {
            log.error("HttpUtil error: ", e);
            return null;
        }

        return cnn;
    }
}
