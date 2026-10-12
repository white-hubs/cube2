package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface TimeControlLogMapper extends BaseMapper<TimeControlLog> {
    Page<TimeControlLogVo> getTimeControlLogs(Page page, @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime
                                             );

    Page<TimeControlLogVo> getTimeControlPlan(Page page, @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("timeCycle") String timeCycle
                                             );
}
