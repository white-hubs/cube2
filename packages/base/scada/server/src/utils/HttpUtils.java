package com.guhecloud.kaihe.scada.server.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 通用http发送方法
 *
 * @author guheyun
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(com.guhecloud.kaihe.scada.server.utils.HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, Map<String, String> authMap) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("appKey", authMap.get("appKey"));
            connection.setRequestProperty("appSecret", authMap.get("appSecret"));
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     *
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            String urlNameString = url + "?" + param;
            log.info("sendPost - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("appKey", "YWRtaW4=");
            conn.setRequestProperty("appSecret", "561bda082d5fe7397c36668289cfb53ac91d0b0b");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            log.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null) {
                if (ret != null && !ret.trim().equals("")) {
                    result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
                }
            }
            log.info("recv - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    /**
     * RestTemplate 发送post请求
     *
     * @param params
     * @param url
     *
     * @return
     */
    public static JSONObject restPost(String url, Object params, Map<String, String> authMap) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && authMap.size() > 0) {
            authMap.forEach((x, y) -> {
                headers.set(x, y);
            });
        }
        String jsonStr = JSON.toJSONString(params);
        org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(jsonStr, headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);//设置超时时间
        requestFactory.setReadTimeout(180000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> postForEntity = null;
        try {
            postForEntity = restTemplate.postForEntity(url, request, String.class);
            if (postForEntity.getStatusCodeValue() != 200) {
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            log.error("网络请求出错，请求地址为：" + url, e);
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }
        String body = postForEntity.getBody();
        return JSONObject.parseObject(body);
    }

    /**
     * RestTemplate get请求
     *
     * @param url
     * @param params
     *
     * @return
     */
    public static JSONObject restGet(String url, MultiValueMap<String, String> params, Map<String, String> authMap) {
        if (!StringUtils.isNotBlank(url)) {
            throw new RuntimeException("请求的路径不能为空");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (null != authMap && authMap.size() > 0) {
            authMap.forEach((x, y) -> {
                headers.set(x, y);
            });
        }
        org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(null, headers);
        //get请求拼接的参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.queryParams(params).build().toUri();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);//设置超时时间
        requestFactory.setReadTimeout(180000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                throw new RuntimeException("请求失败");
            }
        } catch (Exception e) {
            log.error("网络请求出错，请求地址为：" + url, e);
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }
        return JSONObject.parseObject(responseEntity.getBody());
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}