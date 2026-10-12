package com.guhecloud.cube.biz.mdata.client;

import com.guhecloud.cube.biz.mdata.client.dto.VideoPhotographDTO;
import com.guhecloud.cube.biz.mdata.client.dto.VideotapeDTO;
import com.guhecloud.cube.biz.mdata.client.param.VideoPhotographModifyParam;
import com.guhecloud.cube.biz.mdata.client.param.VideotapeModifyParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mdata")
public interface VideoPtzClient {

    /**
     * 获取登录session
     *
     * @return
     */
    @GetMapping("/video-ptz/login-session")
    String getLoginSession();

    /**
     * 抓拍
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/video-ptz/photograph")
    VideoPhotographDTO videoPhotograph(@Valid @RequestBody VideoPhotographModifyParam param);

    /**
     * 录像
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/video-ptz/videotape")
    VideotapeDTO videotape(@Valid @RequestBody VideotapeModifyParam param);
}
