package com.guhecloud.cube.biz.vmonitor.server.controller;

import com.guhecloud.cube.biz.mdata.client.VideoPtzClient;
import com.guhecloud.cube.biz.mdata.client.dto.VideoPhotographDTO;
import com.guhecloud.cube.biz.mdata.client.param.VideoPhotographModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPhotographParam;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPtzPhotographVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 摄像机云台控制接口
 *
 * @author lanchun
 * @date 2022/8/8 11:55
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPtzController {

    private final VideoPtzClient videoPtzClient;

    /**
     * 摄像机抓拍
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/camera-ptz/photograph")
    public CameraPtzPhotographVO videoPhotograph(@RequestBody @Valid CameraPhotographParam param) {
        LocalDateTime executeTime = DateUtils.now();
        VideoPhotographModifyParam videoPhotographRequest = new VideoPhotographModifyParam();
        videoPhotographRequest.setToken(param.getToken());
        videoPhotographRequest.setFilename(String.valueOf(RedisUtils.generateIdentity()));
        videoPhotographRequest.setStart1(DateUtils.formatDate(executeTime));
        videoPhotographRequest.setStart2(DateUtils.format(executeTime, "HH-mm-ss"));
        VideoPhotographDTO videoPhotographDTO = videoPtzClient.videoPhotograph(videoPhotographRequest);
        //执行抓拍动作
        CameraPtzPhotographVO cameraPtzPhotographVO = new CameraPtzPhotographVO();
        cameraPtzPhotographVO.setExtranetFilePath(videoPhotographDTO.getExtranetFilePath());
        cameraPtzPhotographVO.setIntranetFilePath(videoPhotographDTO.getIntranetFilePath());
        return cameraPtzPhotographVO;
    }
}
