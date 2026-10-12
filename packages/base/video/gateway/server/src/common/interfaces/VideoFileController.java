package com.guhecloud.cube.base.videogw.server.common.interfaces;

import com.guhecloud.cube.base.videogw.server.common.application.querygateway.VideoFileQueryService;
import com.guhecloud.cube.base.videogw.server.common.interfaces.vo.VideoFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoFileController {

    private final VideoFileQueryService videoFileQueryService;

    /**
     * 获取文件信息
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/video/file/{id}")
    public VideoFileVO getVideoFileById(@PathVariable("id") Long id) {
        return this.videoFileQueryService.getById(id);
    }

    /**
     * 获取文件信息
     *
     * @param ids
     *
     * @return
     */
    @PostMapping("/video/files")
    public List<VideoFileVO> getVideoFilesByIds(@RequestBody Set<Long> ids) {
        return this.videoFileQueryService.getByIds(ids);
    }
}
