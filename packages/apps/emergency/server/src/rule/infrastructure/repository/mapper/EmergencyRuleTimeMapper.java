package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleConditionExpPO;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRuleTimePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmergencyRuleTimeMapper extends BaseMapper<EmergencyRuleTimePO> {

    Integer selectEventEmergencyTimeCount(@Param("emergencyId") Long emergencyId, @Param("modelIds") List<Long> modelIds,
                                          @Param("eventCodes") List<String> eventCodes, @Param("deviceIds") List<Long> deviceIds,
                                          @Param("emergencyRuleTimePOS") List<EmergencyRuleTimePO> emergencyRuleTimePOS
                                         );

    Integer selectPropertyEmergencyTimeCount(@Param("emergencyId") Long emergencyId, @Param("modelIds") List<Long> modelIds,
                                             @Param("deviceIds") List<Long> deviceIds, @Param("emergencyRuleConditionExpPOS")
                                             List<EmergencyRuleConditionExpPO> emergencyRuleConditionExpPOS,
                                             @Param("emergencyRuleTimePOS") List<EmergencyRuleTimePO> emergencyRuleTimePOS
                                            );
}
