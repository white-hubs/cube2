package com.guhecloud.cube.base.file.server.minio.service;

import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;

import java.util.List;

/**
 * @author lijie
 * @date 2023/5/17
 */
public interface MinioFileExpirationPolicyService {

    /**
     * minio根据桶批量删除
     *
     * @param fileList   文件信息
     * @param fileConfig nimio配置信息
     */
    List<Long> bucketListDelete(List<File> fileList, FileConfig fileConfig);

    /**
     * minio根据桶删除单个文件
     *
     * @param fileName   文件名称
     * @param bucketName 桶名称
     * @param fileConfig nimio配置信息
     */
    void bucketDelete(String fileName, String bucketName, FileConfig fileConfig);

    /**
     * minio根据桶+前缀删除文件
     *
     * @param object     前缀
     * @param bucketName 桶名称
     * @param fileConfig nimio配置信息
     */
    void bucketDeleteByobject(String object, String bucketName, FileConfig fileConfig);
}
