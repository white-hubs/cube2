package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.param.SurgicalDemonstrationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.service.SurgicalDemonstrationService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.OverviewVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.SurgicalDemonstrationVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurgicalDemonstrationController {

    private final SurgicalDemonstrationService surgicalDemonstrationService;

    /**
     * @Description示教情况总览
     * @Author lrj
     */
    @GetMapping("/overview")
    public List<OverviewVo> getOverview(SurgicalDemonstrationParam param) {
        return surgicalDemonstrationService.getOverview(param);
    }

    /**
     * @Description示教直播场次
     * @Author lrj
     */
    @GetMapping("/teaching-live-broadcast-sessions")
    public Map<String, Object> teachingLiveBroadcastSessions(SurgicalDemonstrationParam param) {
        return surgicalDemonstrationService.teachingLiveBroadcastSessions(param);
    }

    /**
     * @Description最受欢迎视频
     * @Author lrj
     */
    @GetMapping("/most-popular-videos")
    public List<OverviewVo> mostPopularVideos(SurgicalDemonstrationParam param) {
        return surgicalDemonstrationService.mostPopularVideos(param);
    }

    /**
     * @Description 示教分页
     * @Author lrj
     */
    @GetMapping("/surgical-demonstration-page")
    public Pagination<SurgicalDemonstrationVo> SurgicalDemonstrationPage(SurgicalDemonstrationParam param) {
        return surgicalDemonstrationService.SurgicalDemonstrationPage(param);
    }

}
