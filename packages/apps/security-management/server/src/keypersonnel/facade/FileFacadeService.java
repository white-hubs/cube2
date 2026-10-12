package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileFacadeService {
    public static final String SCENE_KEY_PERSON_PHOTO = "KEY::PERSON_SECURITY_KEY_PERSON_PHOTO";
    public static final String SCENE_KEY_PERSON_ATTACHMENT = "KEY::PERSON_SECURITY_KEY_PERSON_ATTACHMENT";
    public static final String SCENE_KEY_PERSON_REPORT_ATTACHMENT = "KEY::PERSON_SECURITY_KEY_PERSON_REPORT_ATTACHMENT";

    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;

    public List<Long> normalizeAndValidate(List<Long> fileIds) {
        List<Long> normalized = normalize(fileIds);
        if (CollectionUtils.isEmpty(normalized)) {
            return normalized;
        }
        List<FileVO> files = fileClient.getByIds(normalized);
        Set<Long> existingIds = files == null ? Collections.emptySet() :
                                files.stream().map(FileVO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        Assert.isParameterError(existingIds.size() != normalized.size(), "附件不存在或已被删除");
        return normalized;
    }

    public Long validateSingle(Long fileId) {
        if (fileId == null) {
            return null;
        }
        FileVO file = fileClient.getById(fileId);
        Assert.isParameterError(file == null || file.getId() == null, "附件不存在或已被删除");
        return fileId;
    }

    public FileVO getFile(Long fileId) {
        return fileId == null ? null : fileClient.getById(fileId);
    }

    public List<FileVO> getFiles(List<Long> fileIds) {
        List<Long> normalized = normalize(fileIds);
        return CollectionUtils.isEmpty(normalized) ? Collections.emptyList() : fileClient.getByIds(normalized);
    }

    public void induce(String sceneCode, Long businessKey, List<Long> fileIds) {
        List<Long> normalized = normalize(fileIds);
        if (CollectionUtils.isEmpty(normalized)) {
            return;
        }
        FileInduceParam param = new FileInduceParam();
        param.setSceneCode(sceneCode);
        param.setBusinessKey(businessKey);
        param.setFileIdList(normalized);
        fileOperateClient.fileInduceSingleBody(param);
    }

    public void induceSingle(String sceneCode, Long businessKey, Long fileId) {
        if (fileId != null) {
            induce(sceneCode, businessKey, Collections.singletonList(fileId));
        }
    }

    private List<Long> normalize(List<Long> fileIds) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return new ArrayList<>();
        }
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        for (Long fileId : fileIds) {
            if (fileId != null) {
                set.add(fileId);
            }
        }
        return new ArrayList<>(set);
    }
}
