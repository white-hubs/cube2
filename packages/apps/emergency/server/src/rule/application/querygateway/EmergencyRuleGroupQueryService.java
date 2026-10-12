package com.guhecloud.cube.biz.emergency.server.rule.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleTimeMapper;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionGroupVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleTimeVO;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleGroupQueryService {

    private final EmergencyRuleConditionQueryService emergencyRuleConditionQueryService;

    private final EmergencyRuleTimeMapper emergencyRuleTimeMapper;

    public List<EmergencyRuleConditionGroupVO> getRuleGroups(Long ruleId) {
        List<EmergencyRuleConditionCollectionVO> emergencyRuleConditionCollectionVOS = emergencyRuleConditionQueryService.getRuleConditions(ruleId);
        List<EmergencyRuleConditionGroupVO> emergencyRuleConditionGroupVOS = new ArrayList<>();
        Map<Long, List<EmergencyRuleConditionCollectionVO>> ruleConditionGroupMap = emergencyRuleConditionCollectionVOS.stream()
                .filter(emergencyRuleConditionCollectionVO -> Objects.nonNull(emergencyRuleConditionCollectionVO.getGroupId()))
                .collect(Collectors.groupingBy(EmergencyRuleConditionCollectionVO::getGroupId));
        if (!MapUtils.isEmpty(ruleConditionGroupMap)) {
            Set<Long> groupIds = ruleConditionGroupMap.keySet();
            //获取时间段配置
            Map<Long, EmergencyRuleTimePO> ruleGroupTimeMap = this.emergencyRuleTimeMapper
                    .selectList(Wrappers.<EmergencyRuleTimePO>lambdaQuery()
                                        .in(EmergencyRuleTimePO::getGroupId, groupIds))
                    .stream()
                    .collect(Collectors.toMap(EmergencyRuleTimePO::getGroupId, s -> s));
            groupIds.forEach(groupId -> {
                EmergencyRuleTimePO emergencyRuleTimePO = ruleGroupTimeMap.get(groupId);
                EmergencyRuleTimeVO emergencyRuleTimeVO = ObjectUtils.copying(emergencyRuleTimePO, EmergencyRuleTimeVO.class);
                emergencyRuleConditionGroupVOS.add(new EmergencyRuleConditionGroupVO(groupId, ruleConditionGroupMap.get(groupId), emergencyRuleTimeVO));
            });
        } else {
            emergencyRuleConditionGroupVOS.add(new EmergencyRuleConditionGroupVO(IdWorker.getId(), emergencyRuleConditionCollectionVOS, null));
        }
        return emergencyRuleConditionGroupVOS;
    }
}

