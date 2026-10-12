package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.entity.SurgicalDemonstration;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.param.SurgicalDemonstrationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.OverviewVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.SurgicalDemonstrationVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface SurgicalDemonstrationService extends IService<SurgicalDemonstration> {

    List<OverviewVo> getOverview(SurgicalDemonstrationParam param);

    Map<String, Object> teachingLiveBroadcastSessions(SurgicalDemonstrationParam param);

    List<OverviewVo> mostPopularVideos(SurgicalDemonstrationParam param);

    Pagination<SurgicalDemonstrationVo> SurgicalDemonstrationPage(SurgicalDemonstrationParam param);
}
