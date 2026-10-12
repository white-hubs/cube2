package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationVisitHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ApplicationVisitHistoryMapper extends BaseMapper<ApplicationVisitHistory> {

    List<Application> selectUserApplicationVisitHistory(@Param("userId") Long userId, @Param("terminal") String terminal,
                                                        @Param("applicationIds") Collection<Long> applicationIds
                                                       );

    List<ApplicationVisitHistory> selectUserApplicationVisitHistories(@Param("userId") Long userId, @Param("terminal") String terminal,
                                                                      @Param("applicationIds") Collection<Long> applicationIds
                                                                     );
}
