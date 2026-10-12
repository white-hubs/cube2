package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.service;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.BroadcastService;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.entity.BaoLunConfig;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.enumeration.BaoLunRetEnums;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.LoginEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.PlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.PlayTextStateEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextStateRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextResponse;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextStateResponse;
import com.guhecloud.cube.biz.adapter.broadcast.server.exception.BroadcastPlayBusyException;
import com.guhecloud.cube.biz.adapter.broadcast.server.exception.BroadcastPlayFailException;
import com.guhecloud.cube.biz.adapter.broadcast.server.param.DevicePlayTextParam;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaoLunBroadcastService implements BroadcastService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RedisTemplate<String, Object> redisTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    public String getJsessionid() {
        SubsystemAdapterConfig subsystemAdapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        Object jsessionid = redisTemplate.opsForValue().get("baolun_jsessionid" + ":" + subsystemAdapterConfig.getTenant());
        if (Objects.nonNull(jsessionid)) {
            return jsessionid.toString();
        }
        BaoLunConfig baoLunConfig = JsonUtils
                .deserialize(JsonUtils.serialize(subsystemAdapterConfig.getManufacturerJson()), BaoLunConfig.class);
        if (Objects.isNull(baoLunConfig)) {
            throw new ServiceException("广播配置为空");
        }
        LoginEvent loginEvent = new LoginEvent(this, baoLunConfig.getUsername(), baoLunConfig.getPassword());
        this.applicationEventPublisher.publishEvent(loginEvent);
        redisTemplate.opsForValue()
                .set("baolun_jsessionid" + ":" + subsystemAdapterConfig.getTenant(), loginEvent.getJsessionid(), baoLunConfig
                        .getExpireTime(), TimeUnit.SECONDS);
        return loginEvent.getJsessionid();
    }

    public PlayTextRequestAbstract getPlayRequest() {
        SubsystemAdapterConfig subsystemAdapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        BaoLunConfig baoLunConfig = JsonUtils
                .deserialize(JsonUtils.serialize(subsystemAdapterConfig.getManufacturerJson()), BaoLunConfig.class);
        if (Objects.isNull(baoLunConfig)) {
            throw new ServiceException("广播配置为空");
        }
        return PlayTextRequestAbstract.builder()
                .jsessionid(getJsessionid())
                .TTSName(baoLunConfig.getTtsName())
                .Volume(Objects.nonNull(baoLunConfig.getVolume()) ? baoLunConfig.getVolume() : 50)
                //终端
                .TargetType(1)
                .Time(DateUtils.format(LocalDateTime.now()))
                .TextCode(2)
                .build();
    }

    @Override
    public String devicePlayText(DevicePlayTextParam playTextParam) {
        Set<Integer> targetIds;
        try {
            targetIds = playTextParam.getDeviceIds().stream().map(Integer::valueOf).collect(Collectors.toSet());
        } catch (NumberFormatException e) {
            throw new ServiceException("设备参数不正确");
        }
        PlayTextRequestAbstract playTextRequest = getPlayRequest();
        playTextRequest.setContent(playTextParam.getPlayContent());
        playTextRequest.setTargetIds(targetIds);
        playTextRequest.setPlaytime(playTextParam.getPlayTime());
        playTextRequest.setPlayPrior(playTextParam.getPlayOrder());
        PlayTextEvent playTextEvent = new PlayTextEvent(this, playTextRequest);
        this.applicationEventPublisher.publishEvent(playTextEvent);
        PlayTextResponse playTextResponse = playTextEvent.getPlayTextResponse();
        if (BaoLunRetEnums.SUCCESS.getCode() == playTextResponse.getRet()) {
            return playTextResponse.getPlayTextTaskId();
        }
        if (BaoLunRetEnums.TERMINAL_BUSY.getCode() == playTextResponse.getRet()) {
            throw new BroadcastPlayBusyException("广播喊话正忙，请稍后再试");
        } else {
            throw new BroadcastPlayFailException("广播喊话执行失败");
        }
    }

    @Override
    public Integer devicePlayTextState(String playTextId) {
        PlayTextStateRequestAbstract playTextStateRequest = new PlayTextStateRequestAbstract();
        playTextStateRequest.setJsessionid(getJsessionid());
        playTextStateRequest.setTTSUUID(playTextId);
        PlayTextStateEvent playTextEvent = new PlayTextStateEvent(this, playTextStateRequest);
        this.applicationEventPublisher.publishEvent(playTextEvent);
        PlayTextStateResponse stateResponse = playTextEvent.getPlayTextStateResponse();
        if (BaoLunRetEnums.SUCCESS.getCode() == stateResponse.getRet()) {
            if (4 == stateResponse.getState()) {
                //播放成功2
                return 2;
            } else if (3 == stateResponse.getState()) {
                //播放失败0
                return 0;
            } else {
                //播放中为1
                return 1;
            }
        }
        if (BaoLunRetEnums.TERMINAL_BUSY.getCode() == stateResponse.getRet()) {
            log.error("获取广播执行状态失败,广播任务Id{}", playTextId);
        }
        return 0;
    }
}
