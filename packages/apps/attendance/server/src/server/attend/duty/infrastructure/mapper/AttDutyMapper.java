package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po.AttDutyPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.result.AttDutyStaffIdNameResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 排班mapper
 */
@Mapper
public interface AttDutyMapper extends BaseMapper<AttDutyPO> {

    /**
     * 获取某月所有的值班人员
     *
     * @param yearMonth 年月
     *
     * @return 值班人员集合
     */
    @Select("select distinct staff_id as `id`,staff_name as `name` from att_duty where duty_month = #{yearMonth}")
    List<AttDutyStaffIdNameResult> getDutyAllStaffIdAndName(@Param("yearMonth") String yearMonth);

    /**
     * 获取某月所有的值班
     *
     * @param yearMonth 年月
     * @param keyword   人员关键字
     *
     * @return 值班
     */
    @Select({
            "<script> select ads.staff_id ,ads.staff_name ,ad.* from att_duty ad right join att_duty_staff ads on (ad.staff_id=ads.staff_id) where ads.duty_month = #{yearMonth} <when test='keyword!=null'> and ads.staff_name = #{keyword} </when> order by ads.staff_id </script>"
    }
    )
    List<AttDutyPO> getAllDutyByWhere(@Param("yearMonth") String yearMonth, @Param("keyword") String keyword);

    /**
     * 逻辑删除
     *
     * @param id   主键id
     * @param time 时间戳
     */
    @Update("update att_duty set deleted = #{time} where id = #{id} ")
    void removeById(@Param("time") Long time, @Param("id") Long id);

    /**
     * 逻辑删除
     *
     * @param time    时间戳
     * @param wrapper 条件
     *
     * @return 数量
     */
    @Update("update att_duty set deleted = #{time} ${ew.customSqlSegment} ")
    int remove(@Param("time") Long time, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}