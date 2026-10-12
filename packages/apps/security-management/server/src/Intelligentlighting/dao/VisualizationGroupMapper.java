package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao;

import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GetGroupControlVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VisualizationGroupMapper {
    List<GetGroupControlVO> getGroupControl(@Param("ids") List<Long> ids);

    GetGroupControlVO getGroupControl2(@Param("ids") List<Long> ids);

    Integer getActualTimeControl(@Param("date") LocalDateTime date, @Param("timeCycle") Integer timeCycle);

    Integer getActualTimePlanControl(@Param("date") LocalDateTime date, @Param("timeCycle") Integer timeCycle);
}
