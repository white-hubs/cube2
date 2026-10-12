package com.guhecloud.cube.base.file.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.server.common.FileOperateService;
import com.guhecloud.cube.base.file.server.dao.FileMapper;
import com.guhecloud.cube.base.file.server.dao.FileRefMapper;
import com.guhecloud.cube.base.file.server.entity.FileRef;
import com.guhecloud.cube.base.file.server.param.FileInduceParam;
import com.guhecloud.cube.base.file.server.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.server.param.FileRefRemoveParam;
import com.guhecloud.cube.base.file.server.service.FileRefService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileRefServiceImpl extends ServiceImpl<FileRefMapper, FileRef> implements FileRefService {

    private final FileMapper fileMapper;
    private final FileRefMapper fileRefMapper;
    private final FileOperateService fileOperateService;
    private final static Long fileSize = 16L;

    @Override
    public List<FileRef> getByBusinessKey(@NonNull Long businessKey) {
        long index = businessKey % fileSize;
        return this.lambdaQuery().eq(FileRef::getBusinessKey, businessKey).list();
    }

    @Override
    public List<FileRef> getByBusinessKeys(@NonNull List<Long> businessKeys) {
        return CollectionUtils.isEmpty(businessKeys) ? Collections.emptyList() :
               this.lambdaQuery().in(FileRef::getBusinessKey, businessKeys).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull Long businessKey, @NonNull Long fileId) {
        FileRef fileRef = new FileRef();
        fileRef.setId(IdWorker.getId());
        fileRef.setBusinessKey(businessKey);
        fileRef.setFileId(fileId);
        fileRef.setDeleted(false);
        EntityAuditor.populateAudit(fileRef);
        long index = businessKey % fileSize;
        this.baseMapper.addFileRef(fileRef, index);

        // file表中ref_count加1
        this.fileMapper.updateRefCount(true, fileId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<FileRefCreateParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        Map<Long, List<FileRef>> fileRefMap = new HashMap<>();
        Set<Long> allFileIds = new HashSet<>();
        for (FileRefCreateParam param : params) {
            Set<Long> fileIds = new HashSet<>();
            if (!CollectionUtils.isEmpty(param.getFileIdList())) {
                fileIds.addAll(param.getFileIdList());
            }
            if (StringUtils.isNotBlank(param.getFileIdStr())) {
                fileIds.addAll(Arrays.stream(param.getFileIdStr().split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            if (Objects.nonNull(param.getFileId())) {
                fileIds.add(param.getFileId());
            }
            if (CollectionUtils.isEmpty(fileIds)) {
                continue;
            }
            allFileIds.addAll(fileIds);
            List<FileRef> fileRefs = fileIds.stream()
                    .map(fileId -> {
                        FileRef fileRef = new FileRef();
                        fileRef.setId(IdWorker.getId());
                        fileRef.setBusinessKey(param.getBusinessKey());
                        fileRef.setFileId(fileId);
                        fileRef.setTag(param.getTag());
                        return fileRef;
                    }).collect(Collectors.toList());
            List<FileRef> fileRefList = fileRefMap.get(param.getBusinessKey());
            if (CollectionUtils.isEmpty(fileRefList)) {
                fileRefList = new ArrayList<>(fileRefs);
            } else {
                fileRefList.addAll(fileRefs);
            }
            fileRefMap.putIfAbsent(param.getBusinessKey(), fileRefList);
        }
        List<FileRef> fileRefs = new ArrayList<>();
        fileRefMap.forEach((k, v) -> {
            v.forEach(fileRef -> {
                fileRef.setId(IdWorker.getId());
                fileRef.setDeleted(false);
                EntityAuditor.populateAudit(fileRef);
                fileRefs.add(fileRef);
            });
        });
        this.saveBatch(fileRefs);
        // file表中ref_count加1

        fileRefMap.forEach((k, v) -> {
            long index = k % fileSize;
            List<Long> fileIds = v.stream().map(FileRef::getFileId).collect(Collectors.toList());
            this.fileMapper.updateBatchRefCount(true, fileIds, index);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        FileRef fileRef = fileRefMapper.selectById(id);
        if (fileRef != null) {
            fileRefMapper.deleteById(id);

            // file表中ref_count减1
            fileMapper.updateRefCount(false, fileRef.getFileId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBusinessKey(@NonNull Long businessKey) {

        long index = businessKey % fileSize;
        this.baseMapper.deleteByBusinessKey(businessKey, index, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBusinessKeyAndIds(@NonNull FileRefRemoveParam fileRefRemoveParam) {
        Long businessKey = fileRefRemoveParam.getBusinessKey();
        List<Long> fileIds = fileRefRemoveParam.getFileIdList().stream().distinct().collect(Collectors.toList());
        long index = businessKey % fileSize;
        this.baseMapper.deleteByBusinessKey(businessKey, index, fileIds);
        fileOperateService.updateFileScene(fileRefRemoveParam.getSceneCode(), fileIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refInduceBatch(List<FileRefCreateParam> fileRefs) {
        //保存关联关系
        this.saveBatch(fileRefs);
        //文件根据场景进行持久化
        List<FileInduceParam> fileInduceParamList = fileRefs.stream().map(refParam -> {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setBusinessKey(refParam.getBusinessKey());
            fileInduceParam.setFileIdList(refParam.getFileIdList());
            fileInduceParam.setFileIdStr(refParam.getFileIdStr());
            fileInduceParam.setSceneCode(refParam.getSceneCode());
            setFileId(refParam, fileInduceParam);
            return fileInduceParam;
        }).collect(Collectors.toList());
        fileOperateService.fileInduceBatch(fileInduceParamList);
    }

    /**
     * 兼容之前的fileId
     *
     * @param refParam
     * @param fileInduceParam
     */
    private static void setFileId(FileRefCreateParam refParam, FileInduceParam fileInduceParam) {
        if (Objects.nonNull(refParam.getFileId())) {
            List<Long> fileIdList = fileInduceParam.getFileIdList();
            if (!CollectionUtils.isEmpty(fileIdList)) {
                fileIdList.add(refParam.getFileId());
            } else {
                fileIdList = new ArrayList<>();
                fileIdList.add(refParam.getFileId());
                fileInduceParam.setFileIdList(fileIdList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByBusinessKeyAndScene(Long businessKey, String sceneCode) {
        this.removeByBusinessKey(businessKey);
        List<FileRef> fileRefList = this.lambdaQuery()
                .select(FileRef::getFileId)
                .eq(FileRef::getBusinessKey, businessKey)
                .list();
        if (CollectionUtils.isEmpty(fileRefList)) {
            return;
        }
        List<Long> fileIds = fileRefList.stream().map(FileRef::getFileId).distinct().collect(Collectors.toList());
        fileOperateService.updateFileScene(sceneCode, fileIds);
    }

}
