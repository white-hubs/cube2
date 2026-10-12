package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRuleClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleClassesResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 排班规则mapper
 */
@Mapper
public interface AttSchedulingRuleClassesMapper extends BaseMapper<AttSchedulingRuleClassesPO> {

    /**
     * 根据规则id逻辑删除
     *
     * @param time   时间戳
     * @param ruleId 规则id
     */
    @Update("update att_scheduling_rule_classes set deleted = #{time} where rule_id = #{ruleId}")
    void removeByRuleId(@Param("time") Long time, @Param("ruleId") Long ruleId);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     *
     * @return 数量
     */
    @Update("update att_scheduling_rule_classes set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据排班规则id查询规则的班次
     *
     * @param ruleId 规则id
     *
     * @return 班次
     */
    @Select("select asrc.id,ac.name,ac.nick_name,ac.classes_type,asrc.rule_id,asrc.`week` from att_scheduling_rule_classes asrc inner join att_classes ac on(asrc.id=ac.id) where rule_id = #{ruleId}")
    List<AttSchedulingRuleClassesResult> selectRuleClassesByRuleId(@Param("ruleId") Long ruleId);
}
