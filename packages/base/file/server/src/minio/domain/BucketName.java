package com.guhecloud.cube.base.file.server.minio.domain;

/**
 * @author pengjian
 * @date 2022/7/18 17:26
 * @description 桶名称
 */
public class BucketName {
    private String bucketName;

    public BucketName(String bucket, String active, String businessKey) {
        this.bucketName = bucket + "-prod-" + businessKey;
    }

    public BucketName(String bucket, String businessKey) {
        this.bucketName = bucket + "-prod-" + businessKey;
    }

    @Override
    public String toString() {
        return this.bucketName;
    }
}
