package com.guhecloud.cube.base.file.client;

import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileGenerateParam;
import com.guhecloud.cube.base.file.client.param.FileModifyParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "file")
public interface FileClient {
    /**
     * 预生成文件信息
     *
     * @return 文件信息
     */
    @GetMapping("/file/upload/details")
    FileVO getUploadDetails(@Valid @SpringQueryMap FileGenerateParam param);

    /**
     * ID查询文件
     *
     * @param id 文件主键
     *
     * @return 文件信息
     */
    @GetMapping("/file/{id}")
    FileVO getById(@PathVariable("id") Long id);

    /**
     * ID列表查询文件
     *
     * @param ids 文件主键
     *
     * @return 文件信息
     */
    @GetMapping("/file/ids")
    List<FileVO> getByIds(@RequestParam("ids") List<Long> ids);

    /**
     * 根据业务主键查找文件
     *
     * @param businessKey 业务主键
     *
     * @return 文件信息集合
     */
    @GetMapping(value = "/file/business-key", produces = "application/json")
    List<FileVO> getByBusinessKey(@RequestParam("businessKey") Long businessKey);

    /**
     * 根据业务主键查找文件
     *
     * @param param 业务主键集合
     *
     * @return 文件信息集合
     */
    @PostMapping("/file/business-keys")
    Map<Long, List<FileVO>> getByBusinessKeys(@Valid @RequestBody BusinessKeysParam param);

    /**
     * 根据文件id 查询
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/file/list")
    List<FileVO> getFileList(@RequestBody FileParam param);

    /**
     * 带tag查询
     *
     * @param businessKey
     * @param tag
     *
     * @return
     */
    @GetMapping("/file/business-key/tag")
    List<FileVO> getByBusinessKeyWithTag(@RequestParam("businessKey") Long businessKey, @RequestParam("tag") String tag);

    /**
     * 文件上传
     *
     * @param param
     *
     * @return 文件信息
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileVO upload(@RequestBody FileModifyParam param);

    /**
     * 获取文件字节数组
     *
     * @param id 文件主键
     *
     * @return 文件字节数组
     */
    @GetMapping("/file/getByte/{id}")
    byte[] getFileByte(@PathVariable("id") Long id);

    @DeleteMapping("/file/{id}")
    void remove(@PathVariable("id") Long id);

    /**
     * 文件删除（物理删除）
     *
     * @param ids 文件id
     *
     * @return 删除结果
     */
    @DeleteMapping("/file/deleteByIds")
    Boolean physicalDeleteByIds(@RequestBody FileParam ids);

    @GetMapping("/file/getFileBase64/{id}")
    String getFileBase64(@PathVariable("id") Long id);

    /**
     * @description: 服务器资源绑定业务
     * @author: lijie
     * @date: 2024/6/25 18:25
     * @param: fileName 文件名称
     * @return:
     **/
    @GetMapping("/file/resourceBundle")
    FileVO resourceBundle(@RequestParam("fileName") String fileName, @RequestParam("bucket") String bucket,
                          @RequestParam("path") String path
                         );

}
