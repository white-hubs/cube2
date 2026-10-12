package com.guhecloud.cube.base.file.server.service;

import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pengjian
 * @date 2022/7/18 16:58
 * @description 将每个实例定义为一个File Store
 */
public interface FileStore {
    String getAccessPrefix();

    String getEndpoint();

    /**
     * 上传文件
     *
     * @param file
     * @param businessKey
     * @param tenant
     *
     * @return
     */
    @Deprecated
    File upload(@NonNull MultipartFile file,
                @NonNull String businessKey,
                @NonNull Integer tenant,
                Boolean newBucket,
                Long fileId
               );

    String upload(@NonNull MultipartFile file, @NonNull FileConfig fileConfig, String processedName);

    byte[] getFileBytes(File file);

}
