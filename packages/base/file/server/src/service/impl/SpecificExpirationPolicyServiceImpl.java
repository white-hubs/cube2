package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.server.dao.FileMapper;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.FileExpirationPolicy;
import com.guhecloud.cube.base.file.server.enumeration.FileStoreType;
import com.guhecloud.cube.base.file.server.minio.dao.FileStoreSignUpMapper;
import com.guhecloud.cube.base.file.server.minio.entity.FileStoreSignUp;
import com.guhecloud.cube.base.file.server.minio.service.MinioFileExpirationPolicyService;
import com.guhecloud.cube.base.file.server.minio.tool.MinioClientHelper;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import com.guhecloud.cube.base.file.server.service.SpecificExpirationPolicyService;
import com.guhecloud.cube.base.file.server.vo.FileExpiratioPolicyTimeVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpecificExpirationPolicyServiceImpl implements SpecificExpirationPolicyService {
    private final FileMapper fileMapper;
    private final MinioFileExpirationPolicyService minioFileExpirationPolicyService;
    private final FileStoreSignUpMapper fileStoreSignUpMapper;
    private final MinioClientHelper minioClientHelper;
    private final SceneInfoService sceneInfoService;
    private final FileService fileService;

    @Override
    public void basedTime(FileExpirationPolicy policy) {
        log.info("基于时间过期,场景：{}", policy.getSceneCode());
        FileExpiratioPolicyTimeVO fileExpiratioPolicyTimeVO = JsonUtils.deserialize(policy.getConfigInfo(), FileExpiratioPolicyTimeVO.class);
        LocalDateTime delayDays = DateUtils.offsetDay(LocalDateTime.now(), -fileExpiratioPolicyTimeVO.getDelayDays());
        log.info("{}:删除：{}，之前的文件", policy.getSceneCode(), delayDays);
        FileConfig fileConfig = sceneInfoService.getConfig(policy.getSceneCode(), policy.getTenant());
        MinioClient minioClient = minioClientHelper.getMinioClient(fileConfig);
        try {
            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(fileConfig.getBucket()).build());
            if (found) {
                FileStoreSignUp fileStoreSignUp = fileStoreSignUpMapper.selectOne(new LambdaQueryWrapper<FileStoreSignUp>()
                                                                                          .eq(FileStoreSignUp::getFileStore, fileConfig.getId())
                                                                                          .last("limit 1"));
                if (Objects.isNull(fileStoreSignUp)) {
                    return;
                }
                // 获取已过期文件
                Page<?> page = new Page<>(1, 1000);
                Page<File> filePage = fileMapper.listAll(page, delayDays, policy.getSceneCode(), fileConfig.getId());
                log.info("过期文件共：{}个", filePage.getTotal());
                if (ObjectUtils.isNotEmpty(filePage)) {
                    this.fileHandle(filePage.getRecords(), fileStoreSignUp.getType(), fileConfig);
                    if (filePage.getPages() > 1) {
                        for (int i = 2; i <= filePage.getPages(); i++) {
                            page.setCurrent(i);
                            Page<File> fileList = fileMapper.listAll(page, delayDays, policy.getSceneCode(), fileConfig.getId());
                            this.fileHandle(fileList.getRecords(), fileStoreSignUp.getType(), fileConfig);
                        }
                    }
                }
            } else {
                log.info("未找到桶：{}", fileConfig.getBucket());
            }
        } catch (Exception e) {
            log.info("{}:桶不存在", fileConfig.getBucket());
        }
    }

    @Override
    public void basedTimeTag(FileExpirationPolicy policy) {
        log.info("场景：{}：开始标记删除", policy.getSceneCode());
        FileExpiratioPolicyTimeVO fileExpiratioPolicyTimeVO = JsonUtils.deserialize(policy.getConfigInfo(), FileExpiratioPolicyTimeVO.class);
        LocalDateTime expirationTime = DateUtils.offsetDay(LocalDateTime.now(), -fileExpiratioPolicyTimeVO.getMultipleDays());
        log.info("{}:标记删除：{}，之前的文件", policy.getSceneCode(), expirationTime);
        LocalDateTime date = LocalDateTime.now();
        // 根据时间进行标记更新
        Page<?> page = new Page<>(1, 1000);
        Page<File> needLogicDeletePage = fileMapper.selectListBySceneCodeAndExpirationTime(page, policy.getSceneCode(), expirationTime);
        if (ObjectUtils.isNotEmpty(needLogicDeletePage.getRecords())) {
            logicDeleteFile(policy, needLogicDeletePage.getRecords(), date);
            if (needLogicDeletePage.getPages() > 1) {
                for (int i = 2; i <= needLogicDeletePage.getPages(); i++) {
                    page.setCurrent(i);
                    Page<File> filePage = fileMapper.selectListBySceneCodeAndExpirationTime(page, policy.getSceneCode(), expirationTime);
                    logicDeleteFile(policy, filePage.getRecords(), date);
                }
            }
        }
    }

    private void logicDeleteFile(FileExpirationPolicy policy, List<File> needLogicDeleteList, LocalDateTime date) {
        List<Long> fileIdList = new ArrayList<>();
        //更新文件存在多个场景时移除当前场景
        List<File> updateFiles = new ArrayList<>();
        needLogicDeleteList.forEach(file -> {
            if (!file.getSceneCode().equals(policy.getSceneCode())) {
                String[] sceneCodes = file.getSceneCode().split(",");
                String sceneCode = Arrays.stream(sceneCodes)
                        .filter(e -> !e.equals(policy.getSceneCode()))
                        .collect(Collectors.joining(","));
                file.setSceneCode(sceneCode);
                updateFiles.add(file);
            } else {
                fileIdList.add(file.getId());
            }
        });
        // 需要删除的逻辑删除
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            fileService.lambdaUpdate()
                    .in(File::getId, fileIdList)
                    .set(File::getCleaningTime, date)
                    .set(File::getDeleted, System.currentTimeMillis())
                    .set(File::getUpdateTime, date)
                    .update();
        }
        //多场景文件更新
        if (CollectionUtils.isNotEmpty(updateFiles)) {
            fileService.updateBatchById(updateFiles);
        }
        fileIdList.clear();
        updateFiles.clear();
    }

    /**
     * 根据配置调用不同的实现
     */
    public void fileHandle(List<File> fileList, FileStoreType fileStoreType, FileConfig fileConfig) {
        List<Long> ids = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(fileList) && ObjectUtils.isNotEmpty(fileStoreType)) {
            // 后续可以改为工厂模式获取实现
            if (FileStoreType.MINIO.name().equals(fileStoreType.name())) {
                log.info("调用MINIO删除文件");
                ids = minioFileExpirationPolicyService.bucketListDelete(fileList, fileConfig);
                log.info("MINIO删除文件,共：{}条", ids.size());

            }
        }
        if (CollectionUtils.isNotEmpty(ids)) {
            Integer num = fileMapper.deleteByIds(ids);
            log.info("删除数据库文件成功,共：{}条", num);
        }
    }

    @Override
    public void basedSize(FileExpirationPolicy policy) {

    }

    @Override
    public void basedFrequency(FileExpirationPolicy policy) {

    }

}
