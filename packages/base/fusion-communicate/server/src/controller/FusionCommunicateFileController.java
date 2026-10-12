package com.guhecloud.cube.biz.fusion.communicate.server.controller;

import com.guhecloud.cube.biz.fusion.communicate.server.service.FusionCommunicateFileService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.param.FileRecordQueryParam;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.FusionFileVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 通讯文件管理接口
 *
 * @author lanchun
 * @date 2024/7/29 14:09
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FusionCommunicateFileController {

    private final FusionCommunicateFileService fusionCommunicateFileService;

    /**
     * 通讯文件分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/fusion-communicate-file/page")
    public Pagination<FusionFileVO> getThirdFusionFilePage(@Valid FileRecordQueryParam query) {
        return fusionCommunicateFileService.getFilePages(query);
    }

    /**
     * 根据唯一id保存文件
     *
     * @param id
     *
     * @return
     */
    @PutMapping("/fusion-communicate-file/{id}")
    public FusionFileVO saveFileById(@PathVariable("id") String id) {
        return this.fusionCommunicateFileService.saveFileById(id);
    }
}