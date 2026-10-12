package com.guhecloud.cube.base.file.server.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.file.server.dao.FileRefMapper;
import com.guhecloud.cube.base.file.server.entity.File;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import com.guhecloud.cube.base.file.server.enumeration.FileStorageStrategyEnum;
import com.guhecloud.cube.base.file.server.param.FileInduceParam;
import com.guhecloud.cube.base.file.server.param.FileOperateDbParam;
import com.guhecloud.cube.base.file.server.service.FileService;
import com.guhecloud.cube.base.file.server.service.FileStore;
import com.guhecloud.cube.base.file.server.service.FileStoreRouteStrategy;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import com.guhecloud.cube.base.file.server.util.WatermarkUtil;
import com.guhecloud.cube.base.file.server.vo.FileVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.getFilenameExtension;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  10:20
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileOperateService {
    private final SceneInfoService sceneInfoService;
    private final FileStoreRouteStrategy fileStoreRouteStrategy;
    private final FileService fileService;
    private final FileRefMapper fileRefMapper;
    private static final String ALLOW_TYPES = "jpg,jpeg,png,gif,txt,webp,icon,xlsx,xls,docx,doc,pptx,ppt,zip,pdf,avi,pem,.docx" +
                                              "mp4,svg,apk,m4a,mp3,glb,gltf";

    @Transactional(rollbackFor = Exception.class)
    public FileVO upload(FileOperateDbParam param) {
        String fileName = param.getFile().getOriginalFilename();
        Assert.parameterIsNull(fileName, "文件名不能为空");
        String fileExtName = getFilenameExtension(fileName);
        Assert.isParameterError(
                ObjectUtils.isNull(fileExtName) || !ALLOW_TYPES.contains(fileExtName.toLowerCase()),
                "不允许的文件名后缀!");
        Integer tenant = ObjectUtils.isNotEmpty(param.getTenant()) ? param.getTenant() : SessionContextHolder.getRequester().getTenant();
        FileConfig fileConfig = sceneInfoService.getConfig(param.getSceneCode(), tenant);
        Assert.parameterIsNull(fileConfig, "请先配置文件服务信息");
        FileStore fileStore = fileStoreRouteStrategy.getFileStore(fileConfig.getId());
        Assert.parameterIsNull(fileName, "请先配置文件存储信息");
        // 添加水印
        if (ObjectUtils.isNotEmpty(param.getWatermark())) {
            param.setFile(WatermarkUtil.addWatermark(param.getFile(), 1, param.getWatermark()));
        }
        File file = param.getFile(fileConfig);
        String fileUrl = fileStore.upload(param.getFile(), fileConfig, file.getBucketFileName());
        Assert.parameterIsNull(fileUrl, "上传文件失败");
        fileService.saveFile(file);
        FileVO fileVO = ObjectUtils.copying(file, FileVO.class);
        fileVO.setEndpointPath(fileConfig.getEndpoint() + file.getPath());
        fileVO.setPath(fileConfig.getAccessPrefix() + file.getPath());
        return fileVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean fileInduceBatch(List<FileInduceParam> paramList) {
        if (CollectionUtils.isEmpty(paramList)) {
            return false;
        }
        Set<Long> allFileIds = new HashSet<>();
        Set<Long> businessKeys = new HashSet<>();
        Map<Long, String> sceneCodeMap = new HashMap<>();
        Map<Long, String> sceneCodeBusinessMap = new HashMap<>();
        for (FileInduceParam param : paramList) {
            Assert.isParameterError(ObjectUtils.isEmpty(param.getSceneCode()), "场景不能为空");
            Assert.isParameterError(ObjectUtils.isEmpty(param.getBusinessKey())
                                    && ObjectUtils.isEmpty(param.getFileIdStr()) &&
                                    CollectionUtils.isEmpty(param.getFileIdList()), "业务key和文件不能同时为空!");
            Set<Long> fileIds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(param.getFileIdList())) {
                fileIds.addAll(param.getFileIdList());
            }
            if (StringUtils.isNotEmpty(param.getFileIdStr())) {
                fileIds.addAll(Arrays.stream(param.getFileIdStr().split(",")).map(Long::parseLong).toList());
            }
            //文件id不存在则传入业务主键
            if (CollectionUtils.isEmpty(fileIds)) {
                businessKeys.add(param.getBusinessKey());
                sceneCodeBusinessMap.put(param.getBusinessKey(), param.getSceneCode());
            } else {
                fileIds.forEach(id -> {
                    sceneCodeMap.put(id, param.getSceneCode());
                });
                allFileIds.addAll(fileIds);
            }
        }
        if (!CollectionUtils.isEmpty(businessKeys)) {
            List<FileRef> fileRefList = fileRefMapper.selectList(Wrappers.<FileRef>lambdaQuery()
                                                                         .select(FileRef::getId, FileRef::getFileId)
                                                                         .in(FileRef::getBusinessKey, businessKeys));
            for (FileRef fileRef : fileRefList) {
                sceneCodeMap.put(fileRef.getFileId(), sceneCodeBusinessMap.get(fileRef.getBusinessKey()));
                allFileIds.add(fileRef.getFileId());
            }
        }
        if (CollectionUtils.isEmpty(allFileIds)) {
            return false;
        }
        //更新场景编码
        fileService.updateBatchById(buildUpdateFileSceneList(allFileIds, sceneCodeMap));
        return true;
    }

    private List<File> buildUpdateFileSceneList(Set<Long> allFileIds, Map<Long, String> sceneCodeMap) {
        List<File> files = fileService.lambdaQuery()
                .in(File::getId, allFileIds)
                .list();
        Assert.isNotFound(CollectionUtils.isEmpty(files), "文件上传失败,文件不存在");
        files.forEach(file -> {
            Set<String> sceneCodeSet = new HashSet<>();
            if (ObjectUtils.isNotEmpty(file.getSceneCode())) {
                String[] sceneCodes = file.getSceneCode().split(",");
                sceneCodeSet = Arrays.stream(sceneCodes)
                        .collect(Collectors.toSet());
            }
            sceneCodeSet.add(sceneCodeMap.get(file.getId()));
            file.setSceneCode(sceneCodeSet.stream().distinct().collect(Collectors.joining(",")));
            file.setStorageStrategy(FileStorageStrategyEnum.SUSTAIN.name());
        });
        return files;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFileScene(String sceneCode, Collection<Long> fileIds) {
        if (CollectionUtils.isEmpty(fileIds) || StringUtils.isEmpty(sceneCode)) {
            return;
        }
        List<File> files = fileService.lambdaQuery()
                .in(File::getId, fileIds)
                .list();
        files.forEach(t -> {
            if (sceneCode.equals(t.getSceneCode())) {
                t.setSceneCode(FileStorageStrategyEnum.TEMPORARY.name());
            }
            if (!StringUtils.isEmpty(t.getSceneCode())) {
                String newSceneCode = Arrays.stream(t.getSceneCode().split(",")).filter(sceneCode::equals)
                        .collect(Collectors.joining());
                t.setSceneCode(newSceneCode);
            }
        });

        fileService.updateBatchById(files);
    }
}
