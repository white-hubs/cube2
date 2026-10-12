package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.service;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.BroadcastService;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.entity.BaoLunVSevenSevenConfig;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.BaoLunV7700ApiService;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request.TTsPlayTextRequest;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.response.TTsPlayTextResponse;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.enumeration.BaoLunV7700ResultEnums;
import com.guhecloud.cube.biz.adapter.broadcast.server.exception.BroadcastPlayBusyException;
import com.guhecloud.cube.biz.adapter.broadcast.server.param.DevicePlayTextParam;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 保伦itc V7700版本
 *
 * @author lanchun
 * @date 2024/11/7 11:20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaoLunV7700Service implements BroadcastService {

    private final BaoLunV7700ApiService baoLunV7700ApiService;

    @Override
    public String devicePlayText(DevicePlayTextParam playTextParam) {
        String targetIds = null;
        try {
            targetIds = String.join(",", playTextParam.getDeviceIds());
        } catch (NumberFormatException e) {
            throw new ServiceException("设备参数不正确");
        }
        String taskId = UUID.randomUUID().toString();
        TTsPlayTextRequest playTextRequest = new TTsPlayTextRequest();
        playTextRequest.setToken(playTextRequest.getAuthToken());
        BaoLunVSevenSevenConfig baoLunVSevenSevenConfig = playTextRequest.getRequestConfig();
        TTsPlayTextRequest.PlayTextData playTextData = new TTsPlayTextRequest.PlayTextData();
        playTextData.setTextContent(playTextParam.getPlayContent());
        playTextData.setTaskID(taskId);
        playTextData.setEndPointsList(targetIds);
        playTextData.setTTSEngineName(baoLunVSevenSevenConfig.getTtsName());
        playTextData.setTTSSpeed(5);
        playTextData.setRepeatTime(playTextParam.getPlayTime());
        playTextData.setTaskName(taskId);
        playTextData.setTaskPriority(playTextParam.getPlayOrder());
        playTextData.setVolume(baoLunVSevenSevenConfig.getVolume());
        playTextRequest.setData(playTextData);
        TTsPlayTextResponse tTsPlayTextResponse = baoLunV7700ApiService.ttsPlayText(playTextRequest);
        if (BaoLunV7700ResultEnums.SUCCESS.getCode() == tTsPlayTextResponse.getResult()) {
            return taskId;
        }
        log.error("保伦itc V7700播放失败,失败原因{}", tTsPlayTextResponse.getResult() + tTsPlayTextResponse.getReturn_message());
        throw new BroadcastPlayBusyException("广播失败");
    }

    @Override
    public Integer devicePlayTextState(String playTextId) {
        return 0;
    }
}
