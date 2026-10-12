package com.guhecloud.cube.base.file.server.enumeration;

/**
 * @author pengjian
 * @date 2022/7/18 17:02
 * @description
 */
public enum FileStoreType {
    OSS,
    MINIO;

    public static FileStoreType getFileStoreType(String type) {
        return MINIO.name().equals(type) ? MINIO : OSS;
    }
}
