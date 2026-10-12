package com.guhecloud.cube.srv.gen.securitymanagement.server.information.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.PlayRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.PlayRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramScheduleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.PlayRecordPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramSchedulePageVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationController {
    private final PlayRecordService playRecordService;
    private final ProgramScheduleService programScheduleService;

    /**
     * @Description 信息分页
     * @Author lrj
     */
    @GetMapping("/play-record")
    public Pagination<PlayRecordPageVo> getPlayRecordPage(PlayRecordParam param) {
        return playRecordService.getPlayRecordPage(param);
    }

    /**
     * @Description 信息总数
     * @Author lrj
     */
    @GetMapping("/play-record-count")
    public Long getPlayRecordCount(PlayRecordParam param) {
        return playRecordService.getPlayRecordCount(param);
    }

    /**
     * @Description 节目单列表分页
     * @Author xiaofeng
     */
    @GetMapping("/getProgramSchedulePage")
    public Pagination<ProgramSchedulePageVo> getProgramSchedulePage(ProgramScheduleParam param) {
        return programScheduleService.getProgramSchedulePage(param);
    }

    /**
     * @Description 节目单总数
     * @Author xiaofeng
     */
    @GetMapping("/getProgramScheduleCount")
    public Integer getProgramScheduleCount(ProgramScheduleParam param) {
        return programScheduleService.getProgramScheduleCount(param);
    }
}
