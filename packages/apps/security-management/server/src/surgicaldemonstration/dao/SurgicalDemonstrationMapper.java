package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.entity.SurgicalDemonstration;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.param.SurgicalDemonstrationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.OverviewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SurgicalDemonstrationMapper extends BaseMapper<SurgicalDemonstration> {

    List<OverviewVo> getOverview(@Param("param") SurgicalDemonstrationParam param);

    List<OverviewVo> teachingLiveBroadcastSessions(@Param("dateFormat") String dateFormat, @Param("st") LocalDateTime st, @Param("et")
                                                   LocalDateTime et
                                                  );

    List<OverviewVo> mostPopularVideos(@Param("param") SurgicalDemonstrationParam param);
}
