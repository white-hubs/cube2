package com.guhecloud.cube.base.file.server.controller;

import com.guhecloud.cube.base.file.server.common.FileUploadService;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import com.guhecloud.cube.base.file.server.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.server.param.FileGenerateParam;
import com.guhecloud.cube.base.file.server.param.FileModifyParam;
import com.guhecloud.cube.base.file.server.param.FileParam;
import com.guhecloud.cube.base.file.server.service.FileConfigService;
import com.guhecloud.cube.base.file.server.service.FileRefService;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {
    private final FileService fileService;
    private final FileRefService fileRefService;
    private final FileUploadService fileUploadService;
    private final FileConfigService fileConfigService;

    /**
     * 预生成文件信息
     *
     * @return 文件信息
     */
    @Deprecated
    @GetMapping("/file/upload/details")
    public FileVO getUploadDetails(@Valid @SpringQueryMap FileGenerateParam param) {
        return fileService.getUploadDetails(param);
    }

    /**
     * 文件上传
     *
     * @return
     */
    @Deprecated
    @PostMapping("/file")
    public FileVO upload2OSS(FileModifyParam param) {
        return fileUploadService.upload(param);
    }

    /**
     * 文件上传
     *
     * @param file 文件上传信息
     *
     * @return 文件信息
     */
    @Deprecated
    @PostMapping("/file/upload")
    public FileVO upload(@Valid @NotNull(message = "文件上传信息不能为空") MultipartFile file) {
        return this.fileService.upload(file);
    }

    /**
     * 文件删除（物理删除）
     *
     * @param ids 文件id
     *
     * @return 删除结果
     */
    @DeleteMapping("/file/deleteByIds")
    public Boolean physicalDeleteByIds(@RequestBody FileParam ids) {
        return this.fileService.physicalDeleteByIds(ids);
    }

    @Deprecated
    @PostMapping("/public/file/upload")
    public FileVO uploadForPublic(FileModifyParam param) {
        File f = this.fileService.publicUpload2OSS(param.getFile(), param.getBusinessKey(), param.getTenant());
        return ObjectUtils.copying(f, FileVO.class);
    }

    /**
     * ID查询文件
     *
     * @param id 文件主键
     *
     * @return 文件信息
     */
    @GetMapping("/file/{id}")
    public FileVO getById(@NotNull(message = "文件主键不能为空") @Min(value = 1, message = "文件主键不能小于1")
                          @PathVariable("id") Long id
                         ) {
        return this.fileService.getById(id);
    }

    /**
     * 外部系统获取文件
     *
     * @param id 文件主键
     *
     * @return 文件信息
     */
    @GetMapping("/public/file/{id}")
    public FileVO publicGetFileById(@NotNull(message = "文件主键不能为空") @Min(value = 1, message = "文件主键不能小于1")
                                    @PathVariable("id") Long id
                                   ) {
        return this.fileService.getById(id);
    }

    /**
     * 通过ID列表 查询文件
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/file/ids")
    public List<FileVO> getByIds(@RequestParam("ids") List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<File> files = this.fileService.listByIds(ids);
        return this.getFile(files);
    }

    /**
     * 根据文件id 查询
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/file/list")
    public List<FileVO> getFileList(@RequestBody FileParam param) {
        return this.getByIds(param.getIds());
    }

    /**
     * 根据业务主键查找文件
     *
     * @param businessKey 业务主键
     *
     * @return 文件信息集合
     */
    @GetMapping("/file/business-key")
    public List<FileVO> getByBusinessKey(@RequestParam("businessKey") Long businessKey) {
        List<FileRef> fileRefs = this.fileRefService.getByBusinessKey(businessKey);
        if (CollectionUtils.isEmpty(fileRefs)) {
            return Collections.emptyList();
        }
        List<Long> fileIds = fileRefs.stream().map(FileRef::getFileId).collect(Collectors.toList());
        List<File> files = this.fileService.listByIds(fileIds);
        return this.getFile(files);
    }

    @GetMapping("/file/business-key/tag")
    public List<FileVO> getByBusinessKeyWithTag(@RequestParam("businessKey") Long businessKey, @RequestParam("tag") String tag) {
        List<FileVO> fileVOList = getByBusinessKey(businessKey);
        return fileVOList.stream().filter(fileVO -> tag.equals(fileVO.getTag())).collect(Collectors.toList());
    }

    /**
     * 根据业务主键集合查询对应文件
     *
     * @param param 业务主键信息
     *
     * @return 业务主键/文件映射关系
     */
    @PostMapping("/file/business-keys")
    public Map<Long, List<FileVO>> getByBusinessKeys(@Valid @RequestBody BusinessKeysParam param) {
        List<FileVO> files = this.fileService.listByBusinessKeys(param.getBusinessKeys());
        files.forEach(fileVO -> {
            fileVO.setPath(fileVO.getAccessPrefix() + fileVO.getPath());
        });
        return files.stream().collect(Collectors.groupingBy(FileVO::getBusinessKey));
    }

    @DeleteMapping("/file/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.fileService.remove(id);
    }

    /**
     * 获取文件字节数组
     *
     * @param id 文件主键
     *
     * @return 文件字节数组
     */
    @GetMapping("/file/getByte/{id}")
    public byte[] getFileByte(@PathVariable("id") Long id) {
        FileVO file = this.fileService.getById(id);
        if (Objects.isNull(file)) {
            return null;
        }
        return fileUploadService.getFileBytes(ConvertUtils.convert(File.class, file));
    }

    /**
     * 获取文件字节数组
     *
     * @param id 文件主键
     *
     * @return 文件字节数组
     */
    @GetMapping("/file/getFileBase64/{id}")
    public String getFileBase64(@PathVariable("id") Long id) {
        return fileUploadService.getFileBase64(id);
    }

    /**
     * 服务器资源绑定业务
     */
    @GetMapping("/file/resourceBundle")
    public FileVO resourceBundle(@RequestParam("fileName") String fileName, @RequestParam("bucket") String bucket,
                                 @RequestParam("path") String path
                                ) {
        return fileUploadService.resourceBundle(fileName, bucket, path);
    }

    private List<FileVO> getFile(List<File> files) {
        List<FileVO> fileList = new ArrayList<>();
        if (ObjectUtils.isEmpty(files)) {
            return fileList;
        }
        List<Long> ids = files.stream().map(File::getConfigId).collect(Collectors.toList());
        List<FileConfig> fileConfigs = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(ids)) {
            fileConfigs = fileConfigService.listByIds(ids);
        }
        Map<Long, String> fileConfigMap = ObjectUtils.isNotEmpty(fileConfigs) ? fileConfigs.stream()
                .collect(Collectors.toMap(FileConfig::getId, FileConfig::getEndpoint)) : new HashMap<>();
        FileConfig fileConfig = fileConfigService.getByTenant(SessionContextHolder.getRequester().getTenant());
        files.forEach(file -> {
            FileVO fileVO = ObjectUtils.copying(file, FileVO.class);
            fileVO.setPath(file.getAccessPrefix() + file.getPath());
            String endpoint = fileConfigMap.get(file.getConfigId());
            if (ObjectUtils.isEmpty(endpoint) && ObjectUtils.isNotEmpty(fileConfig)) {
                endpoint = fileConfig.getEndpoint();
            }
            fileVO.setEndpointPath(ObjectUtils.isNotEmpty(endpoint) ? endpoint + file.getPath() : file.getPath());
            fileList.add(fileVO);
        });
        return fileList;
    }
}
