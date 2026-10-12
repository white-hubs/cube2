package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query.EmergencyRuleApplicationQuery;
import com.guhecloud.cube.biz.emergency.server.rule.domain.po.EmergencyRulePO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleDetailVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleSafetyReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmergencyRuleMapper extends BaseMapper<EmergencyRulePO> {

    List<EmergencyRuleDetailVO> selectRuleByEventCode(@Param("eventCode") String eventCode, @Param("modelId") Long modelId,
                                                      @Param("enable") Boolean enable
                                                     );

    Integer selectEventEmergencyTenant(@Param("eventCode") String eventCode, @Param("deviceId") Long deviceId);

    /**
     * 忽略租户插件的查询
     *
     * @param ruleId
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from emergency_rule where id=#{ruleId}")
    EmergencyRulePO selectOneByIgnoreTenant(@Param("ruleId") Long ruleId);

    List<EmergencyRuleSafetyReportVO> selectEmergencyRuleSetInfo(@Param("param") EmergencyRuleApplicationQuery param);
}
