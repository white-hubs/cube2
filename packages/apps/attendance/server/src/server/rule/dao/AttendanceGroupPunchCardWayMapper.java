package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.dto.PunchCardWayDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPunchCardWayPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 */
@Mapper
public interface AttendanceGroupPunchCardWayMapper extends BaseMapper<AttendanceGroupPunchCardWayPO> {

    /**
     * 通过设备code查询考勤机打卡方式
     *
     * @param deviceCode
     *
     * @return
     */
    List<PunchCardWayDTO> getMachineWayByCode(@Param("deviceCode") String deviceCode);

}
