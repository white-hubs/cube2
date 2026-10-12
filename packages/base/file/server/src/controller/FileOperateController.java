package com.guhecloud.cube.base.file.server.controller;

import com.guhecloud.cube.base.file.server.common.FileOperateService;
import com.guhecloud.cube.base.file.server.enumeration.FileStorageStrategyEnum;
import com.guhecloud.cube.base.file.server.param.FileGenerateParam;
import com.guhecloud.cube.base.file.server.param.FileInduceParam;
import com.guhecloud.cube.base.file.server.param.FileOperateDbParam;
import com.guhecloud.cube.base.file.server.param.FileParam;
import com.guhecloud.cube.base.file.server.param.FileUploadParam;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 文件操作接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileOperateController {
    private final FileService fileService;
    private final FileOperateService fileOperateService;

    /**
     * 预生成文件信息
     *
     * @return 文件信息
     */
    @GetMapping("/upload/details")
    public FileVO getUploadDetails(@Valid @SpringQueryMap FileGenerateParam param) {
        return fileService.getUploadDetails(param);
    }

    /**
     * 文件上传(客户端应该都调用这个接口)
     * * @param param 上传文件信息
     *
     * @return 文件信息
     */
    @PostMapping("/upload/temporary")
    public FileVO uploadTemporary(@Valid FileUploadParam param) {
        FileOperateDbParam fileOperateDbParam = ConvertUtils.convert(FileOperateDbParam.class, param);
        fileOperateDbParam.setStorageStrategy(FileStorageStrategyEnum.TEMPORARY.name());
        return fileOperateService.upload(fileOperateDbParam);
    }

    /**
     * 文件上传并持久化
     * * @param param 上传文件信息
     *
     * @return 文件信息
     */
    @PostMapping("/upload/sustain")
    public FileVO uploadSustain(@Valid FileUploadParam param) {
        FileOperateDbParam fileOperateDbParam = ConvertUtils.convert(FileOperateDbParam.class, param);
        fileOperateDbParam.setStorageStrategy(FileStorageStrategyEnum.SUSTAIN.name());
        return fileOperateService.upload(fileOperateDbParam);
    }

    /**
     * 单个文件持久化
     *
     * @param sceneCode   场景编码
     * @param businessKey 业务主键
     * @param fileId      文件id
     *
     * @return
     */
    @PostMapping("/induce/single")
    public Boolean fileInduceSingle(@RequestParam("sceneCode") String sceneCode,
                                    @RequestParam(value = "businessKey", required = false) Long businessKey,
                                    @RequestParam(value = "fileId", required = false) Long fileId
                                   ) {

        Assert.isParameterError(ObjectUtils.isEmpty(sceneCode), "场景不能为空");
        Assert.isParameterError(ObjectUtils.isEmpty(businessKey) && ObjectUtils.isEmpty(fileId), "业务key和文件不能同时为空!");
        FileInduceParam fileInduceParam = new FileInduceParam();
        fileInduceParam.setFileIdStr(fileId.toString());
        fileInduceParam.setBusinessKey(businessKey);
        fileInduceParam.setSceneCode(sceneCode);
        return fileOperateService.fileInduceBatch(Collections.singletonList(fileInduceParam));
    }

    @PostMapping("/induce/single/body")
    public Boolean fileInduceSingleBody(@Valid @RequestBody FileInduceParam param) {
        return fileOperateService.fileInduceBatch(Collections.singletonList(param));
    }

    /**
     * 归纳到场景下
     */
    @PostMapping("/induce/batch")
    public Boolean fileInduceBatch(@Valid @RequestBody List<FileInduceParam> param) {
        return fileOperateService.fileInduceBatch(param);
    }

    /**
     * 文件删除
     *
     * @param ids 文件id
     *
     * @return 删除结果
     */
    @DeleteMapping("/deleteByIds")
    public Boolean physicalDeleteByIds(@RequestBody FileParam ids) {
        return this.fileService.physicalDeleteByIds(ids);
    }
}
