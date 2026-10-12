package com.guhecloud.cube.biz.mdata.server.video.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.mdata.server.video.util.request.VideoPhotographRequest;
import com.guhecloud.cube.biz.mdata.server.video.util.request.VideotapeRequest;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.standard.http.HttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class VideoApi {

    /**
     * 视频录象
     */
    private static final String VIDEO_LOGIN = "/api/v1/Login";

    /**
     * 视频快照
     */
    private static final String VIDEO_MONITOR_SHOT_URL = "/api/v1/PathSnapshot";

    /**
     * 视频录象
     */
    private static final String VIDEO_MONITOR_RECORD_URL = "/api/v1/PathRecord";

    /**
     * 会话保活
     */
    private static final String VIDEO_SESSION_KEEP_ALIVE = "/api/v1/Keepalive";

    private static RestTemplate REST_TEMPLATE;

    private static RestTemplate getRestTemplate() {
        try {
            if (REST_TEMPLATE == null) {
                REST_TEMPLATE = HttpClientFactory.createRestTemplate(ApplicationContextHolder.getBean(ObjectMapper.class));
            }
            return REST_TEMPLATE;
        } catch (GeneralSecurityException e) {
            log.error("create rest template error.", e);
        }
        return null;
    }

    public boolean sessionKeepAlive(String basePath, String session) {
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            String stringBuilder = "?" + "session=" +
                                   session;
            response = Objects.requireNonNull(VideoApi.getRestTemplate())
                    .exchange(basePath + VIDEO_SESSION_KEEP_ALIVE + stringBuilder, HttpMethod.GET, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map videoResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                return Boolean.parseBoolean(videoResponse.get("bStatus").toString());
            }
        } catch (Exception e) {
            log.error("摄像机保活异常:{}", e.getMessage());
        }
        return false;
    }

    /**
     * 摄像机抓拍
     *
     * @param request
     *
     * @return
     */
    public String videoPhotograph(VideoPhotographRequest request) {
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            StringBuilder stringBuilder = new StringBuilder("?");
            if (StringUtils.isNotEmpty(request.getFilename())) {
                stringBuilder.append("filename=").append(request.getFilename());
            }
            if (StringUtils.isNotEmpty(request.getToken())) {
                stringBuilder.append("&token=").append(request.getToken());
            }
            if (StringUtils.isNotEmpty(request.getSession())) {
                stringBuilder.append("&session=").append(request.getSession());
            }
            if (StringUtils.isNotEmpty(request.getStart2())) {
                stringBuilder.append("&start2=").append(request.getStart2());
            }
            if (StringUtils.isNotEmpty(request.getStart1())) {
                stringBuilder.append("&start1=").append(request.getStart1());
            }
            response = Objects.requireNonNull(VideoApi.getRestTemplate()).exchange(
                    request.getBasePath() + VIDEO_MONITOR_SHOT_URL + stringBuilder.toString(), HttpMethod.GET, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map videoResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                boolean bStatus = Boolean.parseBoolean(videoResponse.get("bStatus").toString());
                if (!bStatus) {
                    throw new ServiceException("抓拍失败" + response.getBody());
                }
                if (Objects.nonNull(videoResponse.get("strUrl"))) {
                    return videoResponse.get("strUrl").toString();
                } else {
                    return "/mediastore/snapshot/" + request.getToken() + "/" + request.getStart1() + "TZ08/" + request.getStart2() + "/" +
                           request.getFilename() + ".jpg";
                }
            } else {
                //解析返回对象
                Map jgResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                throw new ServiceException("响应状态码" + response.getStatusCodeValue() + ",错误详细:" + jgResponse.get("error"));
            }
        } catch (Exception e) {
            log.error("抓拍失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 摄像机录像
     *
     * @param request
     *
     * @return
     */
    public String videotape(VideotapeRequest request) {
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            StringBuilder stringBuilder = new StringBuilder("?");
            if (StringUtils.isNotEmpty(request.getAlwayscreate())) {
                stringBuilder.append("alwayscreate=").append(request.getAlwayscreate());
            }
            if (StringUtils.isNotEmpty(request.getFilename())) {
                stringBuilder.append("&filename=").append(request.getFilename());
            }
            if (StringUtils.isNotEmpty(request.getTz())) {
                stringBuilder.append("&tz=").append(request.getTz());
            }
            if (StringUtils.isNotEmpty(request.getDuration())) {
                stringBuilder.append("&duration=").append(request.getDuration());
            }
            if (StringUtils.isNotEmpty(request.getStart2())) {
                stringBuilder.append("&start2=").append(request.getStart2());
            }
            if (StringUtils.isNotEmpty(request.getStart1())) {
                stringBuilder.append("&start1=").append(request.getStart1());
            }
            if (StringUtils.isNotEmpty(request.getToken())) {
                stringBuilder.append("&token=").append(request.getToken());
            }
            if (StringUtils.isNotEmpty(request.getSession())) {
                stringBuilder.append("&session=").append(request.getSession());
            }
            response = Objects.requireNonNull(VideoApi.getRestTemplate()).exchange(
                    request.getBasePath() + VIDEO_MONITOR_RECORD_URL + stringBuilder, HttpMethod.GET, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map videoResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                boolean bStatus = Boolean.parseBoolean(videoResponse.get("bStatus").toString());
                if (!bStatus) {
                    throw new ServiceException("录像失败" + response.getBody());
                }
                if (Objects.nonNull(videoResponse.get("strUrl"))) {
                    return videoResponse.get("strUrl").toString();
                } else {
                    return "/mediastore/record/" + request.getToken() + "/" + request.getStart1() + "TZ08/" + request.getStart2() + "/" +
                           request.getFilename() + ".mp4";
                }
            } else {
                //解析返回对象
                Map jgResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                throw new ServiceException("响应状态码" + response.getStatusCodeValue() + ",错误详细:" + jgResponse.get("error"));
            }
        } catch (Exception e) {
            log.error("录像失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 流媒体登录
     *
     * @param basePath
     * @param username
     * @param password
     *
     * @return
     */
    public String videoLoginSession(String basePath, String username, String password) {
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getHeaders();
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            String stringBuilder = "?" +
                                   "user=" + username +
                                   "&password=" + password;
            response = Objects.requireNonNull(VideoApi.getRestTemplate())
                    .exchange(basePath + VIDEO_LOGIN + stringBuilder, HttpMethod.GET, entity, String.class);
            if (HttpStatus.OK == response.getStatusCode()) {
                //解析返回对象
                Map videoResponse = JsonUtils.deserialize(response.getBody(), Map.class);
                boolean bStatus = Boolean.parseBoolean(videoResponse.get("bStatus").toString());
                if (!bStatus) {
                    throw new ServiceException("登录失败" + response.getBody());
                }
                return videoResponse.get("strSession").toString();
            } else {
                throw new ServiceException("响应状态码" + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("登录异常:{}", e.getMessage());
        }
        return null;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
