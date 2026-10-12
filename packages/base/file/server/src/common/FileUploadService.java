package com.guhecloud.cube.base.file.server.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.minio.domain.BucketName;
import com.guhecloud.cube.base.file.server.param.FileModifyParam;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.service.impl.SingleFileStore;
import com.guhecloud.cube.base.file.server.util.FileConversionUtil;
import com.guhecloud.cube.base.file.server.util.WatermarkUtil;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/7/19 11:25
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FileUploadService {
    private final SingleFileStore fileStoreRoute;
    private final SysDictionaryClient sysDictionaryClient;
    private final FileConfigMapper fileConfigMapper;
    private final FileService fileService;
    private static final String ITEM_CODE = "file-minio-bucket";

    @Deprecated
    public synchronized FileVO upload(FileModifyParam param) {
        if (0 < param.getTenant()) {
            SessionContextHolder.requestTenant(param.getTenant());
        }
        var fileStore = fileStoreRoute.getFileStore();
        var newBucket = false;
        if (ObjectUtils.isNotEmpty(param.getApplicationCode())) {
            // 判断桶是否合法
            var dictionary = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(ITEM_CODE, param.getApplicationCode());
            Assert.isNotFound(ObjectUtils.isEmpty(dictionary.getName()), "未找到桶信息");
            param.setBusinessKey(dictionary.getName());
            newBucket = true;
        } else {
            SysDictionaryDTO dictionary = null;
            try {
                dictionary = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(ITEM_CODE, param.getBusinessKey());
            } catch (Exception e) {
                log.error("file:获取字典信息失败", e);
            }
            newBucket = ObjectUtils.isNotEmpty(dictionary);
        }
        // 添加水印
        if (ObjectUtils.isNotEmpty(param.getWatermark())) {
            param.setFile(WatermarkUtil.addWatermark(param.getFile(), 1, param.getWatermark()));
        }
        var f = fileStore.upload(param.getFile(), param.getBusinessKey(),
                                 Objects.isNull(param.getTenant()) ? 0 : param.getTenant(), newBucket, param.getFileId());
        var fileVO = ObjectUtils.copying(f, FileVO.class);
        fileVO.setEndpointPath(fileStore.getEndpoint() + f.getPath());
        fileVO.setPath(fileStore.getAccessPrefix() + f.getPath());
        return fileVO;
    }

    public byte[] getFileBytes(File file) {
        var fileStore = fileStoreRoute.getFileStore();
        return fileStore.getFileBytes(file);
    }

    public String getFileBase64(Long id) {
        var file = this.fileService.getById(id);
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            var downloadFile = SpringFileUtils.downloadFile(file.getEndpointPath());
            var encoder = Base64.getEncoder();
            return encoder.encodeToString(Objects.requireNonNull(downloadFile).fileBytes());
        } catch (Exception e) {
            log.info("{}:下载失败，错误原因:", file.getEndpointPath(), e);
            return null;
        }
    }

    /**
     * 服务器资源绑定业务
     */
    public FileVO resourceBundle(String fileName, String bucket, String path) {
        var fileConfig = fileConfigMapper.selectOne(new LambdaQueryWrapper<FileConfig>()
                                                            .eq(FileConfig::getTenant, SessionContextHolder.getRequester()
                                                                    .getTenant())
                                                            .last("limit 1"));
        var bucketNane = new BucketName(fileConfig.getBucket(), null, bucket).toString();
        var url = fileConfig.getEndpoint() + "/" + bucketNane + "/" + path + "/" + fileName;
        try {
            var downloadFile = SpringFileUtils.downloadFile(url);
            var fileModifyParam = new FileModifyParam();
            fileModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
            fileModifyParam.setBusinessKey(bucket);
            fileModifyParam.setFile(FileConversionUtil.convertByteToMultipartFile(Objects.requireNonNull(downloadFile)
                                                                                          .fileBytes(), fileName, MediaType.MULTIPART_FORM_DATA_VALUE));
            return this.upload(fileModifyParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
