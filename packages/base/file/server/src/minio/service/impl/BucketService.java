package com.guhecloud.cube.base.file.server.minio.service.impl;

import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.minio.tool.MinioClientHelper;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.SetBucketPolicyArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2022/7/18 10:44
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BucketService {

    private final MinioClientHelper minioClientHelper;

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     *
     * @return Boolean
     */
    @Deprecated
    public Boolean makeBucket(String bucketName, Integer tenant) {
        try {
            MinioClient minioClient = minioClientHelper.getMinioClient(tenant);
            minioClient.makeBucket(MakeBucketArgs.builder()
                                           .bucket(bucketName)
                                           .build());
            String policy =
                    "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" +
                    bucketName +
                    "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" +
                    bucketName + "/*\"]}]}";
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean makeBucket(FileConfig fileConfig) {
        try {
            MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
            minioClient.makeBucket(MakeBucketArgs.builder()
                                           .bucket(fileConfig.getBucket())
                                           .build());
            String policy =
                    "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" +
                    fileConfig.getBucket() +
                    "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" +
                    fileConfig.getBucket() + "/*\"]}]}";
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(fileConfig.getBucket()).config(policy).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName, Integer tenant) {
        try {
            MinioClient minioClient = minioClientHelper.getMinioClient(tenant);
            minioClient.removeBucket(RemoveBucketArgs.builder()
                                             .bucket(bucketName)
                                             .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
