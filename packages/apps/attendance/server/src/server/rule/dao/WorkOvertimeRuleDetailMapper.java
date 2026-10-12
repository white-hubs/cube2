package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.WorkOvertimeRuleDetailPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author bq
 * @date 2022/11/11
 */
@Mapper
public interface WorkOvertimeRuleDetailMapper extends BaseMapper<WorkOvertimeRuleDetailPO> {
    @Select("SELECT * FROM `work_overtime_rule_detail` where `rule_id`=#{ruleId} and `type`=#{type}")
    WorkOvertimeRuleDetailPO getProportionByUserId(@Param("ruleId") Long ruleId, @Param("type") String type);

}
