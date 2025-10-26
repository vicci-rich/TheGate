package org.tron.core.services.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.tron.core.Constant.CONTENT_TYPE_KAFKA;

@Slf4j
public class HttpUtil {
    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public static String postJsonContent(String url, String json) throws Exception {
        logger.info("request json" + json);
        PostMethod method = new PostMethod(url);
        HttpClient httpClient = new HttpClient();
        try {
            String data = "{\"records\":[{\"value\":" + json + "}]}";
            RequestEntity entity = new StringRequestEntity(data,CONTENT_TYPE_KAFKA,"UTF-8");
            method.setRequestEntity(entity);
            httpClient.executeMethod(method);
            logger.info("request url：" + method.getURI().toString());
            InputStream in = method.getResponseBodyAsStream();
            StringBuffer sb = new StringBuffer();
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            char[] b = new char[4096];
            for(int n; (n = isr.read(b)) != -1;) {
                sb.append(new String(b, 0, n));
            }
            String returnStr = sb.toString();
            return returnStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            method.releaseConnection();
        }
    }
}
