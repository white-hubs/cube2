package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.base.file.client.param.FileRefRemoveParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.SafetyPersonnelConstants;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 安全人员服务文件关联门面。
 */
@Service("safetyPersonnelFileFacadeService")
@RequiredArgsConstructor
public class SafetyPersonnelFileFacadeService {

    public static final String SCENE_COMMITTEE_ATTACHMENT = SafetyPersonnelConstants.SCENE_COMMITTEE_ATTACHMENT;
    public static final String SCENE_CERT_ATTACHMENT = SafetyPersonnelConstants.SCENE_CERT_ATTACHMENT;

    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    /**
     * 校验单个附件并返回文件信息。
     *
     * @param fileId 文件ID，可为空
     *
     * @return 文件信息，fileId为空时返回null
     */
    public FileVO requireFile(Long fileId) {
        if (fileId == null) {
            return null;
        }
        FileVO file = fileClient.getById(fileId);
        Assert.isParameterError(file == null || file.getId() == null, "附件不存在或已被删除");
        return file;
    }

    /**
     * 按文件ID查询文件信息。
     *
     * @param fileId 文件ID，可为空
     *
     * @return 文件信息，fileId为空时返回null
     */
    public FileVO getFile(Long fileId) {
        return fileId == null ? null : fileClient.getById(fileId);
    }

    /**
     * 按业务主键查询已关联文件列表。
     *
     * @param businessKey 业务主键
     *
     * @return 文件列表
     */
    public List<FileVO> listByBusinessKey(Long businessKey) {
        if (businessKey == null) {
            return Collections.emptyList();
        }
        List<FileVO> files = fileClient.getByBusinessKey(businessKey);
        return files == null ? Collections.emptyList() : files;
    }

    /**
     * 批量查询文件信息。
     *
     * @param fileIds 文件ID集合
     *
     * @return 文件ID到文件信息的映射
     */
    public Map<Long, FileVO> mapFiles(List<Long> fileIds) {
        List<Long> normalized = normalize(fileIds);
        if (CollectionUtils.isEmpty(normalized)) {
            return Collections.emptyMap();
        }
        List<FileVO> files = fileClient.getByIds(normalized);
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyMap();
        }
        return files.stream()
                .filter(file -> file.getId() != null)
                .collect(Collectors.toMap(FileVO::getId, Function.identity(), (left, right) -> left));
    }

    /**
     * 按业务主键批量查询首个文件信息。
     *
     * @param businessKeys 业务主键集合
     *
     * @return 业务主键到首个文件信息的映射
     */
    public Map<Long, FileVO> mapFirstByBusinessKeys(List<Long> businessKeys) {
        List<Long> normalized = normalize(businessKeys);
        if (CollectionUtils.isEmpty(normalized)) {
            return Collections.emptyMap();
        }
        Map<Long, List<FileVO>> filesMap = fileClient.getByBusinessKeys(BusinessKeysParam.builder()
                                                                                         .businessKeys(normalized)
                                                                                         .build());
        if (MapUtils.isEmpty(filesMap)) {
            return Collections.emptyMap();
        }
        return filesMap.entrySet().stream()
                .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getFirst(), (left, right) -> left));
    }

    /**
     * 关联并持久化单个业务附件。
     *
     * @param sceneCode   文件场景码
     * @param businessKey 业务主键
     * @param fileId      文件ID，可为空
     */
    public void induceSingle(String sceneCode, Long businessKey, Long fileId) {
        if (fileId == null || businessKey == null) {
            return;
        }
        FileRefCreateParam param = new FileRefCreateParam();
        param.setSceneCode(sceneCode);
        param.setBusinessKey(businessKey);
        param.setFileIdList(Collections.singletonList(fileId));
        fileRefClient.refInduceBatch(Collections.singletonList(param));
    }

    /**
     * 替换业务主键在指定场景下的全部文件关联。
     *
     * @param sceneCode   文件场景码
     * @param businessKey 业务主键
     * @param fileIds     文件ID集合，可为空
     */
    public void replaceAssociations(String sceneCode, Long businessKey, List<Long> fileIds) {
        if (businessKey == null || sceneCode == null) {
            return;
        }
        if (!fileIds.isEmpty()) {
            Map<Long, FileVO> fileMap = mapFiles(fileIds);
            Assert.isParameterError(fileMap.size() != fileIds.size(), "附件不存在或已被删除");
        }
        removeAllByBusinessKeyAndScene(businessKey, sceneCode);
        if (fileIds.isEmpty()) {
            return;
        }
        FileRefCreateParam param = new FileRefCreateParam();
        param.setSceneCode(sceneCode);
        param.setBusinessKey(businessKey);
        param.setFileIdList(fileIds);
        fileRefClient.refInduceBatch(Collections.singletonList(param));
    }

    /**
     * 解除业务与文件的关联。
     *
     * @param businessKey 业务主键
     * @param fileId      文件ID，可为空
     */
    public void removeAssociation(Long businessKey, Long fileId) {
        if (businessKey == null || fileId == null) {
            return;
        }
        FileRefRemoveParam param = new FileRefRemoveParam();
        param.setBusinessKey(businessKey);
        param.setFileIdList(Collections.singletonList(fileId));
        fileRefClient.removeByBusinessKeyAndIds(param);
    }

    /**
     * 解除业务主键在指定场景下的全部文件关联。
     *
     * @param businessKey 业务主键
     * @param sceneCode   文件场景码
     */
    public void removeAllByBusinessKeyAndScene(Long businessKey, String sceneCode) {
        if (businessKey == null || sceneCode == null) {
            return;
        }
        fileRefClient.removeByBusinessKeyAndScene(businessKey, sceneCode);
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
