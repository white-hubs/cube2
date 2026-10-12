package com.guhecloud.cube.biz.mdata.server.video.controller;

import com.guhecloud.cube.biz.mdata.server.video.param.VideoPhotographModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.param.VideotapeModifyParam;
import com.guhecloud.cube.biz.mdata.server.video.service.VideoPtzService;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoPhotographVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideoSessionVO;
import com.guhecloud.cube.biz.mdata.server.video.vo.VideotapeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 视频云台控制管理接口
 *
 * @author lanchun
 * @date 2022/8/30 14:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoPtzController {

    private final VideoPtzService videoPtzService;

    /**
     * 获取登录会话信息
     *
     * @return
     */
    @GetMapping("/video-ptz/login-session")
    public VideoSessionVO getLoginSession() {
        return this.videoPtzService.getLoginSession();
    }

    /**
     * 抓拍
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/video-ptz/photograph")
    public VideoPhotographVO videoPhotograph(@Valid @RequestBody VideoPhotographModifyParam param) {
        return this.videoPtzService.videoPhotograph(param);
    }

    /**
     * 录像
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/video-ptz/videotape")
    public VideotapeVO videotape(@Valid @RequestBody VideotapeModifyParam param) {
        return this.videoPtzService.videotape(param);
    }
}
