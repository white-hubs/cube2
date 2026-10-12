package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result.AttHolidayRuleListResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 假期规则
 */
@Mapper
public interface AttHolidayRuleMapper extends BaseMapper<AttHolidayRulePO> {

    /**
     * 假期规则逻辑删除
     *
     * @param id   id
     * @param time 时间戳
     */
    @Update("update att_holiday_rule set deleted = #{time} where id = #{id}")
    void removeHolidayRuleById(@Param("id") Long id, @Param("time") Long time);

    /**
     * 查询假期规则下拉列表(假期类型
     *
     * @return 列表
     */
    @Select("select id,name,not_limit from att_holiday_rule order by create_time ")
    List<AttHolidayRuleListResult> listHolidayRuleList();

}
