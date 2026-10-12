package com.guhecloud.cube.biz.emergency.server.rule.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionPO;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionExpMapper;
import com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper.EmergencyRuleConditionMapper;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleConditionExpCollectionVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleConditionQueryService {

    private final EmergencyRuleConditionMapper emergencyRuleConditionMapper;

    private final EmergencyRuleConditionExpMapper emergencyRuleConditionExpMapper;

    public List<EmergencyRuleConditionCollectionVO> getRuleConditions(Long ruleId) {
        //查询关联的规则条件
        List<EmergencyRuleConditionPO> emergencyRuleConditionPOS = this.emergencyRuleConditionMapper.selectList(Wrappers
                                                                                                                        .<EmergencyRuleConditionPO>lambdaQuery()
                                                                                                                        .eq(EmergencyRuleConditionPO::getEmergencyRuleId, ruleId));
        List<EmergencyRuleConditionCollectionVO> emergencyRuleConditionCollectionVOS = ObjectUtils.copying(emergencyRuleConditionPOS, EmergencyRuleConditionCollectionVO.class);
        //查询关联的规则条件表达式
        Map<Long, List<EmergencyRuleConditionExpPO>> emergencyRuleConditionExpPOS = this.emergencyRuleConditionExpMapper
                .selectList(Wrappers.<EmergencyRuleConditionExpPO>lambdaQuery()
                                    .eq(EmergencyRuleConditionExpPO::getEmergencyRuleId, ruleId)).stream()
                .collect(Collectors.groupingBy(EmergencyRuleConditionExpPO::getEmergencyRuleConditionId));
        emergencyRuleConditionCollectionVOS.forEach(emergencyRuleConditionCollectionVO -> {
            if (emergencyRuleConditionExpPOS.containsKey(emergencyRuleConditionCollectionVO.getId())) {
                emergencyRuleConditionCollectionVO.setEmergencyRuleConditionExps(ObjectUtils.copying(emergencyRuleConditionExpPOS.get(emergencyRuleConditionCollectionVO.getId()), EmergencyRuleConditionExpCollectionVO.class));
            }
        });
        return emergencyRuleConditionCollectionVOS;
    }
}
