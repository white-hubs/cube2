package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryaramParam;
import com.guhecloud.cube.base.file.server.client.FileTenantClient;
import com.guhecloud.cube.base.file.server.client.dto.FileTenantDTO;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.SceneInfo;
import com.guhecloud.cube.base.file.server.enumeration.FileStoreType;
import com.guhecloud.cube.base.file.server.minio.domain.MinioConfig;
import com.guhecloud.cube.base.file.server.minio.entity.FileStoreSignUp;
import com.guhecloud.cube.base.file.server.param.FileConfigModifyParam;
import com.guhecloud.cube.base.file.server.service.FileConfigService;
import com.guhecloud.cube.base.file.server.service.FileInitialisationTenantService;
import com.guhecloud.cube.base.file.server.service.FileStoreRouteStrategy;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-07  14:36
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileInitialisationTenantServiceImpl implements FileInitialisationTenantService {
    private final SysDictionaryClient sysDictionaryClient;
    private final FileConfigService fileConfigService;
    private final FileTenantClient tenantClient;
    private final SceneInfoService sceneInfoService;
    private final FileStoreRouteStrategy fileStoreRouteStrategy;
    private static final String ITEM_CODE = "FILE_SCENE_CODE";
    private final MinioConfig minioConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initialisationTenant(Integer tenant) {
        log.info("file:{}开始初始化文件配置", tenant);
        SysDictionaryaramParam param = new SysDictionaryaramParam();
        param.setItemCode(ITEM_CODE);
        List<SysDictionaryDTO> sysDictionary = sysDictionaryClient.getSysDictionaryByItem(param);
        if (ObjectUtils.isNotEmpty(sysDictionary)) {
            List<SceneInfo> sceneInfoList = new ArrayList<>();
            List<FileConfig> fileConfigList = new ArrayList<>();
            List<FileStoreSignUp> fileStoreSignUpList = new ArrayList<>();
            sysDictionary.stream().forEach(ex -> {
                FileConfig fileConfig = new FileConfig();
                fileConfig.setTenant(tenant);
                fileConfig.setId(IdWorker.getId());
                FileStoreSignUp fileStoreSignUp = new FileStoreSignUp();
                if (minioConfig.getType().equals("MINIO")) {
                    fileConfig.setAccessKeyId(minioConfig.getAccessKeyId());
                    fileConfig.setEndpoint(minioConfig.getEndpoint());
                    fileConfig.setAccessPrefix(minioConfig.getAccessPrefix());
                    fileConfig.setAccessKeySecret(minioConfig.getAccessKeySecret());
                    fileConfig.setBucket(ex.getVariable());
                    fileStoreSignUp.setTag("minio");
                    fileStoreSignUp.setType(FileStoreType.MINIO);
                }
                fileConfigList.add(fileConfig);
                fileStoreSignUp.setFileStore(fileConfig.getId());
                fileStoreSignUpList.add(fileStoreSignUp);
                SceneInfo sceneInfo = new SceneInfo();
                sceneInfo.setConfigId(fileConfig.getId());
                sceneInfo.setSceneCode(ex.getCode());
                sceneInfo.setSceneName(ex.getName());
                sceneInfoList.add(sceneInfo);
            });
            // 新增场景配置
            sceneInfoService.saveBatch(sceneInfoList);
            // 新增上传配置
            fileConfigService.saveBatch(fileConfigList);
            // 新增文件配置
            fileStoreRouteStrategy.saveBatch(fileStoreSignUpList);
            log.info("file:{}初始化文件配置成功", tenant);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConfigAllTenant(String sceneName, String sceneCode, String bucket) {
        List<FileTenantDTO> tenantList = tenantClient.listTenantsByCategory("MARKET");
        tenantList.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant.getCode());
            this.extendedData(sceneName, sceneCode, bucket, tenant.getCode());
        });
    }

    @Override
    public void updateConfigAllTenant(String sceneName, String sceneCode, String bucket) {
        log.info("file:更新场景{}配置", sceneCode);
        List<FileTenantDTO> tenantList = tenantClient.listTenantsByCategory("MARKET");
        tenantList.stream().forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant.getCode());
            try {
                List<SceneInfo> sceneInfoList = sceneInfoService.list(new LambdaQueryWrapper<SceneInfo>()
                                                                              .eq(SceneInfo::getSceneCode, sceneCode));
                if (ObjectUtils.isNotEmpty(sceneInfoList)) {
                    Set<Long> ids = sceneInfoList.stream().map(SceneInfo::getConfigId).collect(Collectors.toSet());
                    fileConfigService.update().set("bucket", bucket)
                            .in("id", ids).update();
                } else {
                    this.extendedData(sceneName, sceneCode, bucket, tenant.getCode());
                }
            } catch (Exception e) {
                log.error("file:更新场景{}配置失败", sceneCode, e);
            }
        });

    }

    private void extendedData(String sceneName, String sceneCode, String bucket, Integer tenant) {
        try {
            SceneInfo sceneInfOne = sceneInfoService.getOne(new LambdaQueryWrapper<SceneInfo>()
                                                                    .eq(SceneInfo::getSceneCode, sceneCode)
                                                                    .last("limit 1"));
            if (ObjectUtils.isNotEmpty(sceneInfOne)) {
                return;
            }
            FileConfigModifyParam fileConfig = new FileConfigModifyParam();
            fileConfig.setId(IdWorker.getId());
            fileConfig.setTenant(tenant);
            if (minioConfig.getType().equals("MINIO")) {
                fileConfig.setAccessKeyId(minioConfig.getAccessKeyId());
                fileConfig.setEndpoint(minioConfig.getEndpoint());
                fileConfig.setAccessPrefix(minioConfig.getAccessPrefix());
                fileConfig.setAccessKeySecret(minioConfig.getAccessKeySecret());
                fileConfig.setBucket(bucket);
                fileConfig.setTag("minio");
                fileConfig.setStorageObjectType(FileStoreType.MINIO.name());
            }
            SceneInfo sceneInfo = new SceneInfo();
            sceneInfo.setConfigId(fileConfig.getId());
            sceneInfo.setSceneCode(sceneCode);
            sceneInfo.setSceneName(sceneName);
            fileConfigService.addFileConfig(fileConfig);
            sceneInfoService.save(sceneInfo);
            log.info("file:{}初始化场景{}成功", tenant, sceneCode);
        } catch (Exception e) {
            log.error("file:{}初始化场景{}失败", tenant, sceneCode, e);
        }
    }
}
