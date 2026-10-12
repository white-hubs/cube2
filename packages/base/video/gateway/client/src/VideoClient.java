package com.guhecloud.cube.base.videogw.client;

import com.guhecloud.cube.base.videogw.client.command.VideoCaptureCommand;
import com.guhecloud.cube.base.videogw.client.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.client.dto.VideoCaptureDTO;
import com.guhecloud.cube.base.videogw.client.dto.VideoFileDTO;
import com.guhecloud.cube.base.videogw.client.dto.VideoSnapCaptureDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "videoGateWay")
public interface VideoClient {

    /**
     * 视频截图抓拍
     *
     * @param deviceId
     */
    @PostMapping("/video/{deviceId}/snap-capture")
    VideoSnapCaptureDTO videoSnapCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoSnapCaptureCommand command);

    /**
     * 视频定时录像
     *
     * @param deviceId
     * @param request
     *
     * @return
     */
    @PostMapping("/video/{deviceId}/video-capture")
    VideoCaptureDTO videoCapture(@PathVariable("deviceId") String deviceId, @RequestBody VideoCaptureCommand request);

    /**
     * 获取文件信息
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/video/file/{id}")
    VideoFileDTO getVideoFileById(@PathVariable("id") Long id);

    /**
     * 获取文件信息
     *
     * @param ids
     *
     * @return
     */
    @PostMapping("/video/files")
    List<VideoFileDTO> getVideoFilesByIds(@RequestBody Set<Long> ids);
}
