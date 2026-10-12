package com.guhecloud.cube.base.file.client;

import com.guhecloud.cube.base.file.client.param.FileGenerateParam;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  15:10
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "file")
public interface FileOperateClient {
    /**
     * 预生成文件信息
     *
     * @return 文件信息
     */
    @GetMapping("/upload/details")
    FileVO getUploadDetails(@Valid @SpringQueryMap FileGenerateParam param);

    /**
     * 文件上传(客户端应该都调用这个接口)
     * * @param param 上传文件信息
     *
     * @return 文件信息
     */
    @PostMapping(value = "/upload/temporary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileVO uploadTemporary(@RequestBody FileUploadParam param);

    @PostMapping(value = "/upload/sustain", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileVO uploadSustain(@RequestBody FileUploadParam param);

    /**
     * 单个文件持久化
     */
    @PostMapping("/induce/single")
    Boolean fileInduceSingle(@RequestParam("sceneCode") String sceneCode,
                             @RequestParam(value = "businessKey", required = false) Long businessKey,
                             @RequestParam(value = "fileId", required = false) Long fileId
                            );

    /**
     * 单个场景持久化
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/induce/single/body")
    Boolean fileInduceSingleBody(@Valid @RequestBody FileInduceParam param);

    /**
     * 多个场景批量持久化
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/induce/batch")
    Boolean fileInduceBatch(@Valid @RequestBody List<FileInduceParam> param);

    /**
     * 文件删除（物理删除）
     *
     * @param ids 文件id
     *
     * @return 删除结果
     */
    @DeleteMapping("/deleteByIds")
    Boolean physicalDeleteByIds(@RequestBody FileParam ids);
}
