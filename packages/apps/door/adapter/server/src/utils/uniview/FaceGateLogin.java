package com.guhecloud.cube.srv.gen.adapter.access.control.server.utils.uniview;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.ClientHttpVo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.SecureUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sW4716 on 2018/3/4.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FaceGateLogin {

    private final static String MANUFACTURER = "uniview";
    private final static String LOGIN_URL = "/fastgate/user/login";

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final RedisTemplate<String, String> redisTemplate;

    public String faceGateLogin() throws Exception {
        ClientHttpVo clientHttpVo = getConfigClient();
        // 登录的地址
        String sUrl = clientHttpVo.getHost() + LOGIN_URL;
        /*
          首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using
          java.net.URL and //java.net.URLConnection
         */
        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        /*
          然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
          通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        //设置请求头
        connection.setRequestProperty("Content-type", "application/json");
        //最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...
        //其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称
        Map<String, String> map = new HashMap<>();
        map.put("Id", clientHttpVo.getAccount());
        map.put("Pwd", SecureUtils.md5(clientHttpVo.getPwd()));
        String json = JSON.toJSONString(map);
        byte[] writeBytes = json.getBytes();
        // 设置文件长度
        connection.setRequestProperty("Content-Length", String.valueOf(writeBytes.length));
        OutputStream reactivestreams = connection.getOutputStream();
        reactivestreams.write(json.getBytes());
        reactivestreams.flush();
        reactivestreams.close();
        // 取得cookie，相当于记录了身份，供下次访问时使用
        String cookieVal = connection.getHeaderField("Set-Cookie");
        redisTemplate.opsForValue().set("uniViewCookie", cookieVal);
        log.info("宇视登录成功:{}", cookieVal);
        return cookieVal;
    }

    public String getCookie() {
        String cookie = redisTemplate.opsForValue().get("uniViewCookie");
        if (StringUtils.isEmpty(cookie)) {
            try {
                return faceGateLogin();
            } catch (Exception e) {
                log.error("宇视登录异常", e);
            }
        }
        return cookie;
    }

    public void clearCookie() {
        redisTemplate.opsForValue().set("uniViewCookie", "");
    }

    public ClientHttpVo getConfigClient() {
        String key =
                SubsystemTypeConstants.DOOR_SERVER + ":" + MANUFACTURER + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        return JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ClientHttpVo.class);
    }
}
