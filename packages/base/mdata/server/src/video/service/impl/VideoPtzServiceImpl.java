package com.guhecloud.cube.biz.mdata.server.video.service.impl;

import com.guhecloud.cube.biz.mdata.server.config.entity.StreamMediaConfig;
import com.guhecloud.cube.biz.mdata.server.config.service.StreamMediaConfigService;
import com.guhecloud.cube.biz.mdata.server.video.param.VideoPhotographModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.param.VideotapeModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.service.VideoPtzService;
import com.guhecloud.cube.biz.mdata.server.video.util.VideoApi;
import com.guhecloud.cube.biz.mdata.server.video.util.request.VideoPhotographRequest;
import com.guhecloud.cube.biz.mdata.server.video.util.request.VideotapeRequest;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoPhotographVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoSessionVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideotapeVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoPtzServiceImpl implements VideoPtzService {

    private final VideoApi videoApi;

    private final RedisTemplate<String, Object> redisTemplate;

    private final StreamMediaConfigService streamMediaConfigService;

    private static final String H5S_STREAM_LOGIN_SESSION_REDIS_KEY = "h5s_stream_login_session";

    @Override
    public VideoSessionVO keepAliveSession() {
        boolean keeAlive = false;
        Object object = redisTemplate.opsForValue()
                .get(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant());
        if (Objects.nonNull(object)) {
            StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getOne();
            Assert.isNotFound(streamMediaConfig, "流媒体配置不存在");
            keeAlive = this.videoApi.sessionKeepAlive(streamMediaConfig.getIntranetAddress(), object.toString());
        }
        if (!keeAlive) {
            //保活失败,移除session
            this.removeSession();
            //重新获取session
            return this.getLoginSession();
        }
        return VideoSessionVO.builder().session(object.toString()).expireTimes(0l).build();
    }

    @Override
    public VideoSessionVO getLoginSession() {
        StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getOne();
        if (Objects.isNull(streamMediaConfig)) {
            return null;
        }
        Assert.isNotFound(streamMediaConfig, "流媒体配置不存在");
        return this.getLoginSession(streamMediaConfig.getIntranetAddress(), streamMediaConfig.getUsername(), streamMediaConfig.getEncryptPassword());
    }

    @Override
    public VideoSessionVO getLoginSession(@NonNull String intranetAddress, @NonNull String username, @NonNull String password) {
        Requester requester = SessionContextHolder.getRequester();
        Object object = redisTemplate.opsForValue().get(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + requester.getTenant());
        String loginSession = "";
        if (Objects.isNull(object)) {
            loginSession = this.videoApi.videoLoginSession(intranetAddress, username, password);
            redisTemplate.opsForValue().set(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + requester.getTenant(), loginSession);
        } else {
            loginSession = object.toString();
        }
        return VideoSessionVO.builder().session(loginSession).expireTimes(0l).build();
    }

    @Override
    public VideoPhotographVO videoPhotograph(VideoPhotographModifyParam param) {
        StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getOne();
        Assert.isNotFound(streamMediaConfig, "流媒体配置不存在");
        VideoPhotographRequest videoPhotographRequest = ObjectUtils.copying(param, VideoPhotographRequest.class);
        videoPhotographRequest.setSession(this.keepAliveSession().getSession());
        videoPhotographRequest.setBasePath(streamMediaConfig.getIntranetAddress());
        String path = videoApi.videoPhotograph(videoPhotographRequest);
        return new VideoPhotographVO(streamMediaConfig.getIpAddress() + path, streamMediaConfig.getIntranetAddress() + path);
    }

    @Override
    public VideotapeVO videotape(VideotapeModifyParam param) {
        StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getOne();
        Assert.isNotFound(streamMediaConfig, "流媒体配置不存在");
        VideotapeRequest videotapeRequest = ObjectUtils.copying(param, VideotapeRequest.class);
        videotapeRequest.setSession(this.keepAliveSession().getSession());
        videotapeRequest.setBasePath(streamMediaConfig.getIntranetAddress());
        String path = videoApi.videotape(videotapeRequest);
        return new VideotapeVO(streamMediaConfig.getIpAddress() + path, streamMediaConfig.getIntranetAddress() + path);
    }

    @Override
    public void removeSession() {
        redisTemplate.delete(H5S_STREAM_LOGIN_SESSION_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant());
    }
}
