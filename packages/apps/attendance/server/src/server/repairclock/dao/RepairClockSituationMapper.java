package com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.interfaces.vo.RepairClockNumVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.po.RepairClockSituationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 */
@Mapper
public interface RepairClockSituationMapper extends BaseMapper<RepairClockSituationPO> {

    @Select("select rc.`exp_type` as `exp`,count(rc.`id`) as `num` from `repair_clock_situation` rc\n" +
            "where rc.`applicant_id` = #{userId} and rc.`date` between #{startTime} and #{endTime}\n" +
            "group by rc.`exp_type`"
    )
    List<RepairClockNumVO> getRepairClockNumByPeriod(@Param("userId") Long userId,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime
                                                    );
}
