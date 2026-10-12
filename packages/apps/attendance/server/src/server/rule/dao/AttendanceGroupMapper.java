package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author bq
 * @date 2022/11/14
 */
@Mapper
public interface AttendanceGroupMapper extends BaseMapper<AttendanceGroupPO> {

    /**
     * 获取用户的考勤组
     *
     * @param userId
     *
     * @return
     */
    AttendanceGroupPO getAttendanceGroupByUserId(@Param("userId") Long userId);
}
