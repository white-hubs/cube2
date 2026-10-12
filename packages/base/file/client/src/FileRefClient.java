package com.guhecloud.cube.base.file.client;

import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.param.FileRefRemoveParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "file")
public interface FileRefClient {
    /**
     * 文件关联新增
     *
     * @param businessKey 业务主键
     * @param fileId      文件主键
     */
    @GetMapping("/file-ref")
    void save(@RequestParam("businessKey") Long businessKey, @RequestParam("fileId") Long fileId);

    /**
     * 文件关联批量上传
     *
     * @param fileRefs 文件关联参数
     */
    @PostMapping("/file-ref/batch")
    public void saveBatch(@RequestBody List<FileRefCreateParam> fileRefs);

    /**
     * 文件关联根据ID删除
     *
     * @param id 文件关联主键
     */
    @DeleteMapping("/file-ref/{id}")
    void remove(@PathVariable("id") Long id);

    /**
     * 根据业务主键删除文件关联信息
     *
     * @param businessKey 业务主键
     */
    @DeleteMapping("/file-ref/business-key")
    void removeByBusinessKey(@RequestParam("businessKey") Long businessKey);

    /**
     * 根据业务主键和文件ID删除文件关联信息
     *
     * @param fileRefRemoveParam 业务主键
     */
    @DeleteMapping("/remove/file-ref/business-key/ids")
    void removeByBusinessKeyAndIds(@RequestBody FileRefRemoveParam fileRefRemoveParam);

    /**
     * 根据业务主键删除文件关联信息
     *
     * @param businessKey 业务主键
     */
    @DeleteMapping("/file-ref/business-key/scene-code")
    void removeByBusinessKeyAndScene(@RequestParam("businessKey") Long businessKey, @RequestParam("sceneCode") String sceneCode);

    /**
     * 批量关联
     *
     * @param businessKey
     * @param imageIdList
     */
    @PostMapping("/file-ref/businessKey/batch")
    @Deprecated
    void ref(@RequestParam("businessKey") Long businessKey, @RequestParam("imageIdList") Set<Long> imageIdList);

    /**
     * 单个文件关联并持久化
     */

    @PostMapping("/file-ref/businessKey/single/induce")
    void refInduceSingle(@RequestParam("sceneCode") String sceneCode, @RequestParam(value = "businessKey") Long businessKey,
                         @RequestParam(value = "fileId") Long fileId, @RequestParam(value = "tag", required = false) String tag
                        );

    /**
     * 单个场景关联并持久化
     *
     * @param fileRefCreateParam
     */
    @PostMapping("/file-ref/businessKey/induce")
    void refInduce(@RequestBody FileRefCreateParam fileRefCreateParam);

    /**
     * 多个场景批量关联并持久化文件
     *
     * @param fileRefs 文件关联参数
     */
    @PostMapping("/file-ref/businessKey/batch/induce")
    void refInduceBatch(@RequestBody List<FileRefCreateParam> fileRefs);
}
