package com.guhecloud.cube.base.file.server.minio.service.impl;

import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.minio.service.MinioFileExpirationPolicyService;
import com.guhecloud.cube.base.file.server.minio.tool.MinioClientHelper;
import com.guhecloud.platform.base.util.ObjectUtils;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lijie
 * @date 2023/5/17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MinioFileExpirationPolicyServiceImpl implements MinioFileExpirationPolicyService {
    private final MinioClientHelper minioClientHelper;

    /**
     * minio根据桶批量删除
     *
     * @param fileList   文件信息
     * @param fileConfig nimio配置信息
     */
    public List<Long> bucketListDelete(List<File> fileList, FileConfig fileConfig) {
        log.info("MINIO开始批量删除过期文件");
        MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
        List<Long> ids = new ArrayList<>();
        List<DeleteObject> objects = new LinkedList<>();
        for (File ex : fileList) {
            if (ObjectUtils.isNotEmpty(ex.getPath()) && ObjectUtils.isNotEmpty(ex.getBucketFileName())) {
                objects.add(new DeleteObject(ex.getBucketFileName()));
            }
        }
        try {
            Iterable<Result<DeleteError>> results =
                    minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(fileConfig.getBucket()).objects(objects).build());
            List<String> errorList = new ArrayList<>();
            for (Result<DeleteError> result : results) {
                DeleteError deleteError = result.get();
                String objectName = result.get().objectName();
                if (ObjectUtils.isNotEmpty(objectName)) {
                    log.error("删除文件失败：{},原因{}", objectName, deleteError.message());
                }
                errorList.add(objectName);
            }
            fileList.stream().filter(ex -> !errorList.contains(ex.getBucketFileName())).forEach(ex -> ids.add(ex.getId()));
        } catch (Exception e) {
            log.error("批量删除minio文件失败", e);
        }

        return ids;
    }

    /**
     * minio根据桶删除单个文件
     *
     * @param fileName   文件名称
     * @param bucketName 桶名称
     * @param fileConfig nimio配置信息
     */
    @Override
    public void bucketDelete(String fileName, String bucketName, FileConfig fileConfig) {
        MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                                             .bucket(bucketName)
                                             .object(fileName)
                                             .build());
        } catch (Exception e) {
            log.error("删除文件失败：{}", fileName);
        }
    }

    /**
     * minio根据桶+前缀删除文件
     *
     * @param object     前缀
     * @param bucketName 桶名称
     * @param fileConfig nimio配置信息
     */
    @Override
    public void bucketDeleteByobject(String object, String bucketName, FileConfig fileConfig) {
        MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
        // 列出指定文件夹下的所有文件
        Iterable<Result<Item>> iterable = minioClient.listObjects(ListObjectsArgs.builder()
                                                                          .bucket(bucketName)
                                                                          .prefix(object)
                                                                          .recursive(true)
                                                                          .build());
        // 遍历列表并删除文件
        Iterator<Result<Item>> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            try {
                Result<Item> res = iterator.next();
                String objectName = res.get().objectName();
                minioClient.removeObject(RemoveObjectArgs.builder()
                                                 .bucket(bucketName)
                                                 .object(objectName)
                                                 .build());
            } catch (Exception e) {
                log.error("获取文件信息异常：{}" + e);
            }
        }
    }
}
