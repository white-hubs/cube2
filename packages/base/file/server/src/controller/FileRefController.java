package com.guhecloud.cube.base.file.server.controller;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.cube.base.file.server.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.server.param.FileRefRemoveParam;
import com.guhecloud.cube.base.file.server.service.FileRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 文件关联接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileRefController {

    private final FileRefService fileRefService;

    /**
     * 文件关联新增
     *
     * @param businessKey 业务主键
     * @param fileId      文件主键
     */
    @GetMapping("/file-ref")
    public void save(@RequestParam("businessKey") Long businessKey, @RequestParam("fileId") Long fileId) {
        this.fileRefService.save(businessKey, fileId);
    }

    /**
     * 文件关联批量上传
     *
     * @param fileRefs 文件关联参数
     */
    @PostMapping("/file-ref/batch")
    public void saveBatch(@RequestBody List<FileRefCreateParam> fileRefs) {
        this.fileRefService.saveBatch(fileRefs);
    }

    /**
     * 文件关联根据ID删除
     *
     * @param id 文件关联主键
     */
    @DeleteMapping("/file-ref/{id}")
    public void remove(@PathVariable("id") Long id) {
        fileRefService.remove(id);
    }

    /**
     * 根据业务主键删除文件关联信息
     *
     * @param businessKey 业务主键
     */
    @DeleteMapping("/file-ref/business-key")
    public void removeByBusinessKey(@RequestParam("businessKey") Long businessKey) {
        this.fileRefService.removeByBusinessKey(businessKey);
    }

    /**
     * 根据业务主键和文件ID删除文件关联信息
     *
     * @param fileRefRemoveParam 业务主键
     */
    @DeleteMapping("/remove/file-ref/business-key/ids")
    public void removeByBusinessKeyAndIds(@RequestBody FileRefRemoveParam fileRefRemoveParam) {
        this.fileRefService.removeByBusinessKeyAndIds(fileRefRemoveParam);
    }

    /**
     * 根据业务主键删除文件关联信息
     *
     * @param businessKey 业务主键
     */
    @DeleteMapping("/file-ref/business-key/scene-code")
    public void removeByBusinessKeyAndScene(@RequestParam("businessKey") Long businessKey, @RequestParam("sceneCode") String sceneCode) {

        this.fileRefService.removeByBusinessKeyAndScene(businessKey, sceneCode);
    }

    /**
     * 批量关联
     *
     * @param businessKey
     * @param imageIdList
     */
    @PostMapping("/file-ref/businessKey/batch")
    public void ref(@RequestParam("businessKey") Long businessKey, @RequestParam("imageIdList") Set<Long> imageIdList) {
        // 处理附件
        if (CollectionUtils.isNotEmpty(imageIdList)) {
            // 配置文件关联关系
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(businessKey);
            fileRefCreateParam.setFileIdList(new ArrayList<>(imageIdList));
            this.saveBatch(Collections.singletonList(fileRefCreateParam));
        }
    }

    /**
     * 单个文件关联并持久化
     *
     * @param sceneCode   场景code
     * @param businessKey 业务主键
     * @param fileId      文件id
     * @param tag         标记
     */

    @PostMapping("/file-ref/businessKey/single/induce")
    public void refInduceSingle(@RequestParam("sceneCode") String sceneCode, @RequestParam(value = "businessKey") Long businessKey,
                                @RequestParam(value = "fileId") Long fileId, @RequestParam(value = "tag", required = false) String tag
                               ) {
        FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
        fileRefCreateParam.setSceneCode(sceneCode);
        fileRefCreateParam.setBusinessKey(businessKey);
        fileRefCreateParam.setFileIdList(Collections.singletonList(fileId));
        fileRefCreateParam.setTag(tag);
        fileRefService.refInduceBatch(Collections.singletonList(fileRefCreateParam));
    }

    /**
     * 单个场景关联并持久化
     */

    @PostMapping("/file-ref/businessKey/induce")
    public void refInduce(@RequestBody FileRefCreateParam fileRefCreateParam) {
        fileRefService.refInduceBatch(Collections.singletonList(fileRefCreateParam));
    }

    /**
     * 多个场景批量关联并持久化文件
     *
     * @param fileRefs 文件关联参数
     */
    @PostMapping("/file-ref/businessKey/batch/induce")
    public void refInduceBatch(@RequestBody List<FileRefCreateParam> fileRefs) {
        fileRefService.refInduceBatch(fileRefs);
    }
}
