package com.guhecloud.cube.base.file.server.minio.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.dao.FileMapper;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.minio.domain.BucketName;
import com.guhecloud.cube.base.file.server.minio.domain.FileName;
import com.guhecloud.cube.base.file.server.minio.tool.MinioClientHelper;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.service.FileStore;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceNotAcceptableException;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/7/18 10:28
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MinIoFileStore implements FileStore {
    private final FileConfigMapper fileConfigMapper;
    private final MinioClientHelper minioClientHelper;
    private final BucketService bucketService;
    @Getter
    private String bucketName;
    @Getter
    private FileName fileName;
    @Getter
    private FileConfig fileConfig;
    private static final String ALLOW_TYPES = "jpg,jpeg,png,gif,txt,webp,icon,xlsx,xls,docx,doc,pptx,ppt,zip,pdf,avi,pem,.docx" +
                                              "mp4,svg,apk,m4a,mp3,glb,gltf";

    @Override
    public File upload(@NonNull MultipartFile file, @NonNull String businessKey, @NonNull Integer tenant, Boolean newBucket, Long fileId) {
        return this.getFile(file, businessKey, tenant, newBucket, fileId);
    }

    @Override
    public String upload(@NonNull MultipartFile file, @NonNull FileConfig fileConfig, @NonNull String processedName) {
        return uploadFile(file, fileConfig, processedName);
    }

    /**
     * 批量删除文件对象
     *
     * @param bucketName 存储bucket名称
     * @param objects    对象名称集合
     */
    public Iterable<Result<DeleteError>> removeObjects(String bucketName, List<String> objects, Integer tenant) {
        FileConfig fileConfig = this.fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant));
        MinioClient minioClient = MinioClient.builder().endpoint(fileConfig.getEndpoint())
                .credentials(fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret()).build();
        List<DeleteObject> dos = objects.stream().map(e -> new DeleteObject(e)).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(dos)
                                                                                  .build());
        return results;
    }

    private String uploadFile(MultipartFile file, FileConfig fileConfig, String processedName) {
        InputStream in = null;
        MinioClient minioClient;
        minioClient = minioClientHelper.getMinioClient(fileConfig);
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(fileConfig.getBucket()).build());
        } catch (Exception e) {
            log.error("minio访问失败:{}", e.getMessage());
            throw new ServiceException("minio访问失败");
        }
        if (!found) {
            found = bucketService.makeBucket(fileConfig);
            Assert.isUnknownError(!found, "创建桶失败");
        }
        try {

            in = file.getInputStream();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                                                                                    .bucket(fileConfig.getBucket())
                                                                                    .object(processedName)
                                                                                    .stream(in, in.available(), -1)
                                                                                    .contentType("application/octet-stream")
                                                                                    .build()
                                                                           );
            log.info("minio文件上传响应:{}", objectWriteResponse);
        } catch (Exception e) {
            log.error("minio文件上传失败,异常原因:{}", e.getMessage());
            throw new ServiceException("minio配置信息有误");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("minio上传文件关闭流失败，异常原因:{}", e.getMessage());
                }
            }
        }

        String url = "/" + fileConfig.getBucket() + "/";
        return url;
    }

    /**
     * 上传到minio
     */
    private String uploadFile(@NonNull MultipartFile file, @NonNull String businessKey, @NonNull Integer tenant, Boolean newBucket) {
        InputStream in = null;
        MinioClient minioClient = null;
        Boolean found = false;
        /**
         * 查看桶是否存在
         */
        try {
            minioClient = minioClientHelper.getMinioClient(tenant);
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(getBucketName()).build());
        } catch (Exception e) {
            log.error("minio文件上传失败,异常原因:{}", e.getMessage());
            throw new ServiceException("minio配置信息有误");
        }
        if (!found) {
            Assert.isParameterError(!newBucket, "桶名无效");
            bucketService.makeBucket(getBucketName(), tenant);
        }
        try {
            in = file.getInputStream();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                                                                                    .bucket(getBucketName())
                                                                                    .object(getFileName().getProcessedName())
                                                                                    .stream(in, in.available(), -1)
                                                                                    .contentType(file.getContentType())
                                                                                    .build()
                                                                           );
            log.info("minio文件上传响应:{}", objectWriteResponse);
        } catch (Exception e) {
            log.error("minio文件上传上传失败,异常原因:{}", e.getMessage());
            throw new ResourceNotAcceptableException("文件上传失败!");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("minio上传文件关闭流失败，异常原因:{}", e.getMessage());
                }
            }

        }

        return "/" + getBucketName() + "/";
    }

    @Override
    public String getAccessPrefix() {
        return getFileConfig().getAccessPrefix();
    }

    @Override
    public String getEndpoint() {
        return getFileConfig().getEndpoint();
    }

    @Override
    public byte[] getFileBytes(File file) {
        /**
         *
         */
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        FileConfig fileConfig = fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant)
                                                                   .last("limit 1"));
        MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
        String bucket = file.getPath().substring(1, file.getPath().indexOf("/", 2));

        String path = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucket).object(path).build();
        GetObjectResponse object = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            object = minioClient.getObject(objectArgs);
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = object.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File getFile(@NonNull MultipartFile file, @NonNull String businessKey, @NonNull Integer tenant, Boolean newBucket, Long fileId) {
        FileConfigMapper fileConfigMapper = ApplicationContextHolder.getBean(FileConfigMapper.class);
        FileService fileService = ApplicationContextHolder.getBean(FileService.class);
        FileMapper fileMapper = ApplicationContextHolder.getBean(FileMapper.class);
        String processedName = null;
        if (ObjectUtils.isNotEmpty(fileId)) {
            Long index = fileId % 16;
            File fileVO = fileMapper.getById(fileId);
            Assert.parameterIsNull(fileVO, "文件信息不存在");
            processedName = fileVO.getBucketFileName();
        }
        this.fileConfig = fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant)
                                                             .last(" limit 1"));
        this.bucketName = new BucketName(fileConfig.getBucket(), null, businessKey).toString();
        this.fileName = new FileName(file.getOriginalFilename(), processedName);
        Assert.isParameterError(
                StringUtils.isEmpty(fileName.getFileExtName()) ||
                !ALLOW_TYPES.contains(fileName.getFileExtName().toLowerCase()),
                "不允许的文件名后缀!");
        String url = uploadFile(file, businessKey, tenant, newBucket);
        File f = File.getFile(fileName, file.getSize(), url, getAccessPrefix(), fileConfig.getId());
        if (ObjectUtils.isNotEmpty(fileId)) {
            f.setId(fileId);
        }
        f = fileService.saveFile(f);
        return f;
    }

}
