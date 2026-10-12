package com.guhecloud.cube.srv.gen.appraisals.server.integration;

import com.guhecloud.cube.base.flow.client.FlowGroupClient;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.dto.FormGroupPathSnapshot;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.FormFlowGroupTreeVO;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程表单分组集成服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormFlowGroupIntegrationService {
    private final FlowGroupClient flowGroupClient;

    /**
     * 获取完整分组路径快照
     *
     * @return key 为分组 ID，value 为路径快照
     */
    public Map<Long, FormGroupPathSnapshot> getGroupPathSnapshotMap() {
        List<FormFlowGroupTreeVO> trees = flowGroupClient.tree(null);
        if (trees == null || trees.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, FormGroupPathSnapshot> snapshotMap = new LinkedHashMap<>();
        for (FormFlowGroupTreeVO tree : trees) {
            collectSnapshot(tree, null, snapshotMap);
        }
        return snapshotMap;
    }

    /**
     * 校验分组存在且为末级分组
     *
     * @param formGroupId 分组 ID
     */
    public void assertLeafGroup(Long formGroupId) {
        if (Objects.isNull(formGroupId)) {
            return;
        }
        FormGroupPathSnapshot snapshot = getGroupPathSnapshotMap().get(formGroupId);
        Assert.isParameterError(snapshot == null, "所属分组不存在");
        Assert.isParameterError(!Boolean.TRUE.equals(snapshot.getLeaf()), "所属分组必须选择末级分组");
    }

    private void collectSnapshot(FormFlowGroupTreeVO node, FormFlowGroupTreeVO parent, Map<Long, FormGroupPathSnapshot> snapshotMap) {
        if (node == null || node.getId() == null) {
            return;
        }
        boolean leaf = node.getChildren() == null || node.getChildren().isEmpty();
        snapshotMap.put(node.getId(), FormGroupPathSnapshot.builder()
                .formGroupId(node.getId())
                .formGroupName(node.getName())
                .leaf(leaf)
                .firstLevelFormGroupId(parent == null ? null : parent.getId())
                .firstLevelFormGroupName(parent == null ? null : parent.getName())
                .secondLevelFormGroupId(node.getId())
                .secondLevelFormGroupName(node.getName())
                .build());
        if (!leaf) {
            for (FormFlowGroupTreeVO child : node.getChildren()) {
                collectSnapshot(child, node, snapshotMap);
            }
        }
    }
}
