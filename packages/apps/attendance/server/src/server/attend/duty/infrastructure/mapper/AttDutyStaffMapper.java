package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyStaffIdNameResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 值班人员
 */
@Mapper
public interface AttDutyStaffMapper extends BaseMapper<AttDutyStaffPO> {

    /**
     * 获取某月所有的值班人员id
     *
     * @param yearMonth 年月
     * @param keyword   人员名称
     *
     * @return 值班人员id集合
     */
    @Select({
            "<script> select distinct staff_id as `id`,staff_name as `name` from att_duty_staff where duty_month = #{yearMonth} <when test='keyword!=null'> and staff_name = #{keyword} </when> order by `staff_id` </script>"
    }
    )
    List<AttDutyStaffIdNameResult> getDutyAllStaffByWhere(@Param("yearMonth") String yearMonth, @Param("keyword") String keyword);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_duty_staff set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_duty_staff set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
