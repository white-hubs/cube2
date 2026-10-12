package com.guhecloud.cube.base.file.server.service.impl;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.guhecloud.cube.base.file.server.dao.FileConfigMapper;
import com.guhecloud.cube.base.file.server.dao.FileMapper;
import com.guhecloud.cube.base.file.server.dao.FileRefMapper;
import com.guhecloud.cube.base.file.server.dao.SceneInfoMapper;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import com.guhecloud.cube.base.file.server.entity.SceneInfo;
import com.guhecloud.cube.base.file.server.enumeration.FileStorageStrategyEnum;
import com.guhecloud.cube.base.file.server.minio.domain.FileName;
import com.guhecloud.cube.base.file.server.minio.service.MinioFileExpirationPolicyService;
import com.guhecloud.cube.base.file.server.param.FileGenerateParam;
import com.guhecloud.cube.base.file.server.param.FileParam;
import com.guhecloud.cube.base.file.server.service.FileConfigService;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.getFilenameExtension;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private static final String ALLOW_TYPES = "jpg,jpeg,png,gif,txt,webp,icon,xlsx,xls,docx,doc,pptx,ppt,zip,pdf,avi,pem,.docx" +
                                              "mp4,svg,apk,m4a,mp3,glb,gltf";
    private static final String ALLOW_TYPES_IMAGE = "jpg,jpeg,png,gif,bmp";
    private static final String ALLOW_TYPES_VIDEO = "avi,mp4";
    private static final int retainDays = 1;

    private final FileMapper fileMapper;
    private final FileRefMapper fileRefMapper;
    private final FileConfigMapper fileConfigMapper;
    @Getter
    private FileName fileName;
    @Getter
    private FileConfig fileConfig;
    @Value("${spring.profiles.active}")
    private String active;

    private final FastFileStorageClient fastFileStorageClient;
    private final MinioFileExpirationPolicyService minioFileExpirationPolicyService;
    private final FileConfigService fileConfigService;
    private final SceneInfoMapper sceneInfoMapper;

    @Override
    public FileVO upload(@NonNull MultipartFile file) {
        String path;
        Assert.parameterIsNull(file, "上传的文件为空!");

        String fileName = file.getOriginalFilename();
        if (fileName.split("\\.").length > 2) {
            String[] arr = fileName.split("\\.");
            fileName = arr[0] + "." + arr[arr.length - 1];
        }
        Assert.isOutOfRange(fileName.length() > 90, "文件名称不能大于90");
        String fileExtName = getFilenameExtension(file.getOriginalFilename());
        Assert.isParameterError(StringUtils.isEmpty(fileExtName) || !ALLOW_TYPES.contains(fileExtName.toLowerCase()), "不允许的文件后缀!");
        ByteArrayOutputStream bos = null;
        long fileSize = 0;
        try {
            StorePath storePath = null;
            if (ALLOW_TYPES_IMAGE.contains(fileExtName.toLowerCase())) {
                bos = this.compressImage(file.getInputStream(), 0.8);
                ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
                fileSize = bos.toByteArray().length;
                storePath = fastFileStorageClient.uploadFile(bais, fileSize, fileExtName, null);
            } else {
                fileSize = file.getSize();
                storePath = fastFileStorageClient.uploadFile(file.getInputStream(), fileSize, fileExtName, null);
            }
            path = storePath.getFullPath();
        } catch (Exception e) {
            throw new ServiceException("上传失败!");
        }
        File f = new File();
        f.setName(fileName);
        f.setSuffix(fileExtName);
        f.setPath(path);
        f.setSize(fileSize);
        f.setType(fileExtName);
        f.setRefCount(0);
        fileMapper.insert(f);

        return ObjectUtils.copying(f, FileVO.class);
    }

    @Override
    public Boolean physicalDeleteByIds(FileParam param) {
        if (ObjectUtils.isEmpty(param) || (CollectionUtils.isEmpty(param.getIds()) && StringUtils.isEmpty(param.getFileIdStr()))) {
            throw new ServiceException("附件id不能为空");
        }
        Set<Long> fileIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(param.getIds())) {
            fileIds.addAll(param.getIds());
        }
        if (!StringUtils.isEmpty(param.getFileIdStr())) {
            fileIds.addAll(Arrays.stream(param.getFileIdStr().split(",")).map(Long::parseLong).collect(Collectors.toSet()));
        }
        List<File> fileList = fileMapper.selectList(Wrappers.<File>lambdaQuery()
                                                            .select(File::getId, File::getConfigId, File::getPath, File::getBucketFileName)
                                                            .in(File::getId, fileIds));
        if (CollectionUtils.isEmpty(fileList)) {
            return false;
        }
        Set<Long> fileConfig = fileList.stream().filter(t -> Objects.nonNull(t.getConfigId())).map(File::getConfigId)
                .collect(Collectors.toSet());
        Map<Long, FileConfig> fileConfigMap = fileConfigService.listByIds(fileConfig)
                .stream().collect(Collectors.toMap(FileConfig::getId, obj -> obj));
        Map<Long, List<File>> groupByConfigMap = fileList.stream().collect(Collectors.groupingBy(File::getConfigId));
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        new Thread(() -> {
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            for (var entry : groupByConfigMap.entrySet()) {
                if (!fileConfigMap.containsKey(entry.getKey())) {
                    continue;
                }
                FileConfig fileConfigPO = fileConfigMap.get(entry.getKey());
                minioFileExpirationPolicyService.bucketListDelete(entry.getValue(), fileConfigPO);
            }
        }).start();
        fileMapper.deleteByIds(fileIds);
        return true;
    }

    @Override
    public File upload2OSS(@NonNull MultipartFile file, @NonNull String businessKey, @NonNull Integer tenant) {
        // 获取OSS配置
        FileConfig fileConfig = this.fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant));
        Assert.isNotFound(fileConfig, "未配置OSS配置信息!");

        // 存储空间名称
        String bucketName = fileConfig.getBucket() + "-" + active + "-" + businessKey;
        String url = "https://" + bucketName + "." + fileConfig.getEndpoint() + "/";
        OSS ossClient = null;
        String path = "";
        String filename = file.getOriginalFilename();
        String fileExtName = getFilenameExtension(filename);
        Assert.isParameterError(
                StringUtils.isEmpty(fileExtName) || !ALLOW_TYPES.contains(fileExtName.toLowerCase()),
                "不允许的文件名后缀!");
        try {
            ossClient = new OSSClientBuilder().build(fileConfig.getEndpoint(), fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret());
            path = IdWorker.getId() + "." + fileExtName;
            ossClient.putObject(bucketName, path, new ByteArrayInputStream(file.getBytes()));
        } catch (OSSException | IOException e) {
            throw new ServiceException("文件上传失败!");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 构建信息
        long id = IdWorker.getId();
        File f = File.builder()
                .id(id)
                .name(file.getOriginalFilename())
                .suffix(fileExtName)
                .size(file.getSize())
                .path(url + path)
                .type(fileExtName)
                .refCount(0)
                .build();
        f.setDeleted(false);
        EntityAuditor.populateAudit(f);
        this.baseMapper.addFile(f);
        return f;
    }

    @Override
    public File publicUpload2OSS(MultipartFile file, String businessKey, Integer tenant) {
        // 获取OSS配置
        FileConfig fileConfig = this.fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant));
        Assert.isNotFound(fileConfig, "未配置OSS配置信息!");

        // 存储空间名称
        String bucketName = fileConfig.getBucket() + "-" + active + "-" + businessKey;
        String url = "https://" + bucketName + "." + fileConfig.getEndpoint() + "/";
        OSS ossClient = null;
        String path = "";
        String filename = file.getOriginalFilename();
        String fileExtName = getFilenameExtension(filename);
        Assert.isParameterError(
                StringUtils.isEmpty(fileExtName) && !ALLOW_TYPES.contains(fileExtName.toLowerCase()),
                "不允许的文件名后缀!");
        try {
            ossClient = new OSSClientBuilder().build(fileConfig.getEndpoint(), fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret());
            path = IdWorker.getId() + "." + fileExtName;
            ossClient.putObject(bucketName, path, new ByteArrayInputStream(file.getBytes()));
        } catch (OSSException | IOException e) {
            throw new ServiceException("文件上传失败!");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 构建信息
        long id = IdWorker.getId();
        File f = File.builder()
                .id(id)
                .name(file.getOriginalFilename())
                .suffix(fileExtName)
                .size(file.getSize())
                .path(url + path)
                .type(fileExtName)
                .refCount(0)
                .build();
        f.setDeleted(false);
        EntityAuditor.populateInnerAudit(f);
        this.baseMapper.addFile(f);
        return f;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long fileId) {
        this.fileMapper.remove(fileId, System.currentTimeMillis(), LocalDateTime.now());
    }

    @Override
    public byte[] getFileBytes(File file) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取OSS配置
        FileConfig fileConfig = this.fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant));
        Assert.isNotFound(fileConfig, "未配置OSS配置信息!");

        String[] split = file.getPath().split("\\.");
        String bucketName = split[0].substring(8);
        OSS ossClient = null;
        ByteArrayOutputStream output = null;
        try {
            ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
            configuration.setConnectionTimeout(500 * 1000);
            configuration.setSocketTimeout(500 * 1000);
            configuration.setConnectionRequestTimeout(50 * 60 * 1000);
            String fileKey = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
            ossClient = new OSSClientBuilder().build(fileConfig.getEndpoint(), fileConfig.getAccessKeyId(), fileConfig.getAccessKeySecret());
            OSSObject ossObject = ossClient.getObject(bucketName, fileKey);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = ossObject.getObjectContent().read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } catch (Exception e) {
            log.error("获取文件流失败");
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    @Override
    public FileVO getById(@NonNull Long id) {
        File file = this.baseMapper.getById(id);
        if (ObjectUtils.isEmpty(file)) {
            return new FileVO();
        }
        FileConfig fileConfig = new FileConfig();
        if (ObjectUtils.isNotEmpty(file.getConfigId())) {
            fileConfig = fileConfigService.getById(file.getConfigId());
        }
        if (ObjectUtils.isEmpty(fileConfig)) {
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            fileConfig = fileConfigService.getByTenant(tenant);
        }
        FileVO fileVO = ObjectUtils.copying(file, FileVO.class);
        fileVO.setEndpointPath(fileConfig.getEndpoint() + file.getPath());
        fileVO.setPath(fileConfig.getAccessPrefix() + file.getPath());
        return fileVO;
    }

    @Override
    public List<File> getByBusinessKey(@NonNull Long businessKey) {
        List<FileRef> fileRefs = fileRefMapper.selectList(Wrappers.<FileRef>lambdaQuery()
                                                                  .eq(FileRef::getBusinessKey, businessKey));
        if (CollectionUtils.isEmpty(fileRefs)) {
            return Collections.emptyList();
        }
        List<Long> fileIds = fileRefs.stream().map(FileRef::getFileId).collect(Collectors.toList());
        return fileMapper.selectList(Wrappers.<File>lambdaQuery().in(File::getId, fileIds));
    }

    @Override
    public List<FileVO> listByBusinessKeys(@NotNull List<Long> businessKeys) {
        List<FileRef> fileRefs = fileRefMapper.selectList(Wrappers.<FileRef>lambdaQuery()
                                                                  .in(FileRef::getBusinessKey, businessKeys));
        if (CollectionUtils.isEmpty(fileRefs)) {
            return Collections.emptyList();
        }
        List<Long> fileIds = fileRefs.stream().map(FileRef::getFileId).collect(Collectors.toList());
        List<File> files = this.lambdaQuery().in(File::getId, fileIds)
                .list();
        Map<Long, List<FileRef>> fileRefMap = fileRefs.stream().collect(Collectors.groupingBy(FileRef::getFileId));

        // 构建信息
        List<FileVO> fileVOS = files.stream().map(file -> {
            FileVO fileVO = ObjectUtils.copying(file, FileVO.class);
            if (fileRefMap.containsKey(file.getId())) {
                FileRef fileRef = fileRefMap.get(file.getId()).get(0);
                fileVO.setBusinessKey(fileRef.getBusinessKey());
                return fileVO;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return fileVOS;
    }

    @Override
    public List<File> listByIds(@NonNull List<Long> fileIds) {
        return this.baseMapper.listByIds(fileIds);
    }

    @Override
    public FileVO getUploadDetails(FileGenerateParam param) {
        if (ObjectUtils.isNotEmpty(param.getBusinessKey())) {
            return this.getUploadDetails(param, null);
        }
        SceneInfo sceneInfo = sceneInfoMapper.selectOne(new LambdaQueryWrapper<SceneInfo>()
                                                                .eq(SceneInfo::getSceneCode, param.getSceneCode()));
        Assert.parameterIsNull(sceneInfo, "请先配置场景!");
        this.fileConfig = fileConfigMapper.selectById(sceneInfo.getConfigId());
        Assert.parameterIsNull(sceneInfo, "请先配置文件服务信息!");
        this.fileName = new FileName(param.getFileName(), null);
        String url = "/" + fileConfig.getBucket() + "/";
        File file = File.getFile(fileName, 0L, url, fileConfig.getAccessPrefix(), fileConfig.getId(), param.getSceneCode(), FileStorageStrategyEnum.SUSTAIN.name());
        FileVO fileVO = ConvertUtils.convert(FileVO.class, file);
        fileVO.setEndpointPath(fileConfig.getEndpoint() + fileVO.getPath());
        fileVO.setPath(fileConfig.getAccessPrefix() + fileVO.getPath());
        this.saveFile(file);
        return fileVO;
    }

    @Deprecated
    private FileVO getUploadDetails(FileGenerateParam param, String type) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        this.fileConfig = fileConfigMapper.selectOne(Wrappers.<FileConfig>lambdaQuery().eq(FileConfig::getTenant, tenant)
                                                             .last("limit 1"));
        this.fileName = new FileName(param.getFileName(), null);
        String url = "/" + fileConfig.getBucket() + "/";
        File file = File.getFile(fileName, 0L, url, fileConfig.getAccessPrefix(), fileConfig.getId());
        FileVO fileVO = ConvertUtils.convert(FileVO.class, file);
        fileVO.setEndpointPath(fileConfig.getEndpoint() + fileVO.getPath());
        fileVO.setPath(fileConfig.getAccessPrefix() + fileVO.getPath());
        this.saveFile(file);
        return fileVO;
    }

    @Override
    public File saveFile(File file) {
        File f = fileMapper.getById(file.getId());
        if (ObjectUtils.isNotEmpty(f)) {
            fileMapper.updateFile(file);
        } else {
            fileMapper.addFile(file);
        }
        return file;
    }

    private ByteArrayOutputStream compressImage(InputStream is, double quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Thumbnails.of(is).scale(1).outputQuality(quality).toOutputStream(baos);
        } catch (IOException e) {
            log.error("压缩图片异常", e);
        }
        return baos;
    }

    @Override
    public String getAccessPrefix() {
        return "";
    }

    @Override
    public String getEndpoint() {
        return "";
    }

    @Override
    public File upload(@NonNull MultipartFile file, @NonNull String businessKey, @NonNull Integer tenant, Boolean newBucket, Long fileId) {
        return this.upload2OSS(file, businessKey, tenant);
    }

    @Override
    public String upload(@NonNull MultipartFile file, @NonNull FileConfig fileConfig, @NonNull String processedName) {
        return null;
    }

    @Override
    public boolean saveBatch(Collection<File> entityList) {
        return super.saveBatch(entityList);
    }
}
