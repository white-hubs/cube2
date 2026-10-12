package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.facade;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.RiskControlConstants;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("securityManagementRiskFileFacadeService")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileFacadeService {
    public static final String SCENE_RISK_WARNING_SIGN_ICON = RiskControlConstants.FILE_SCENE_WARNING_SIGN_ICON;
    public static final String SCENE_RISK_MEASURE_EXECUTION_ATTACHMENT = RiskControlConstants.FILE_SCENE_MEASURE_EXECUTION_ATTACHMENT;

    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    public void validateFiles(Collection<String> fileIds) {
        normalizeAndValidate(fileIds);
    }

    public String validateSingle(String fileId) {
        if (isBlank(fileId)) {
            return null;
        }
        List<String> normalized = normalizeAndValidate(List.of(fileId));
        return normalized.isEmpty() ? null : normalized.get(0);
    }

    public List<String> normalizeAndValidate(Collection<String> fileIds) {
        List<String> normalized = normalize(fileIds, true);
        if (CollectionUtils.isEmpty(normalized)) {
            return normalized;
        }
        List<Long> ids = normalized.stream().map(Long::valueOf).toList();
        List<FileVO> files = fileClient.getByIds(ids);
        Map<String, FileVO> fileMap = files == null ? Collections.emptyMap() : files.stream()
                .filter(Objects::nonNull)
                .filter(file -> file.getId() != null)
                .collect(Collectors.toMap(file -> String.valueOf(file.getId()), file -> file, (left, right) -> left));
        Assert.isParameterError(fileMap.size() != normalized.size(), "文件不存在或已被删除");
        return normalized;
    }

    public FileVO getFile(String fileId) {
        if (isBlank(fileId)) {
            return null;
        }
        Long id = parseLong(fileId.trim(), false);
        return id == null ? null : fileClient.getById(id);
    }

    public List<FileVO> getFiles(Collection<String> fileIds) {
        List<String> normalized = normalize(fileIds, false);
        if (CollectionUtils.isEmpty(normalized)) {
            return Collections.emptyList();
        }
        Map<String, FileVO> fileMap = batchFiles(normalized);
        return normalized.stream().map(fileMap::get).filter(Objects::nonNull).toList();
    }

    public Map<String, FileVO> batchFiles(Collection<String> fileIds) {
        List<String> normalized = normalize(fileIds, false);
        if (CollectionUtils.isEmpty(normalized)) {
            return Collections.emptyMap();
        }
        List<Long> ids = normalized.stream().map(id -> parseLong(id, false)).filter(Objects::nonNull).distinct().toList();
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        try {
            List<FileVO> files = fileClient.getByIds(ids);
            if (CollectionUtils.isEmpty(files)) {
                return Collections.emptyMap();
            }
            return files.stream()
                    .filter(Objects::nonNull)
                    .filter(file -> file.getId() != null)
                    .collect(Collectors.toMap(file -> String.valueOf(file.getId()), file -> file, (left, right) -> left));
        } catch (RuntimeException ex) {
            log.warn("Failed to resolve risk control files, fileIds={}", ids, ex);
            return Collections.emptyMap();
        }
    }

    public void induceSingle(String sceneCode, Long businessKey, String fileId) {
        if (!isBlank(fileId)) {
            induce(sceneCode, businessKey, List.of(fileId));
        }
    }

    public void induce(String sceneCode, Long businessKey, Collection<String> fileIds) {
        List<String> normalized = normalize(fileIds, true);
        if (businessKey == null || isBlank(sceneCode) || CollectionUtils.isEmpty(normalized)) {
            return;
        }
        FileRefCreateParam param = new FileRefCreateParam();
        param.setSceneCode(sceneCode);
        param.setBusinessKey(businessKey);
        param.setFileIdList(normalized.stream().map(Long::valueOf).toList());
        fileRefClient.refInduceBatch(Collections.singletonList(param));
    }

    public void replaceAssociations(String sceneCode, Long businessKey, Collection<String> fileIds) {
        if (businessKey == null || isBlank(sceneCode)) {
            return;
        }
        removeAllByBusinessKeyAndScene(businessKey, sceneCode);
        induce(sceneCode, businessKey, fileIds);
    }

    public void removeAllByBusinessKeyAndScene(Long businessKey, String sceneCode) {
        if (businessKey == null || isBlank(sceneCode)) {
            return;
        }
        fileRefClient.removeByBusinessKeyAndScene(businessKey, sceneCode);
    }

    private List<String> normalize(Collection<String> fileIds, boolean strict) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return new ArrayList<>();
        }
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String fileId : fileIds) {
            if (isBlank(fileId)) {
                continue;
            }
            String value = fileId.trim();
            Long id = parseLong(value, strict);
            if (id != null) {
                normalized.add(String.valueOf(id));
            }
        }
        return new ArrayList<>(normalized);
    }

    private Long parseLong(String fileId, boolean strict) {
        try {
            return Long.valueOf(fileId);
        } catch (NumberFormatException ex) {
            Assert.isParameterError(strict, "文件 ID 非法");
            return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
