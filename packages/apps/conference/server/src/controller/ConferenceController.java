package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceUsageParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIdleVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIntervalLogVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentUsageVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceInfoVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceUsedRatioVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 会议室管理（CRUD）
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceController {

    private final ConferenceService conferenceService;

    /**
     * 新增会议室
     *
     * @param param
     */
    @PostMapping("/insertConference")
    public void insertConference(@RequestBody @Valid ConferenceAddOrUpdateParam param) {
        conferenceService.insertConference(param);
    }

    /**
     * 修改会议室
     *
     * @param param
     */
    @PutMapping("/updateConference")
    public void updateConference(@RequestBody ConferenceAddOrUpdateParam param) {
        conferenceService.updateConference(param);
    }

    /**
     * 会议室分页查询
     *
     * @param param
     */
    @GetMapping("/getConferenceInfo")
    public Pagination<ConferenceInfoVo> getConferenceInfo(ConferenceQueryParam param) {
        return conferenceService.selectConferenceInfo(param);
    }

    /**
     * 会议室列表
     *
     * @param keyword
     */
    @GetMapping("/get-conference-list")
    public List<Conference> getConferenceList(@RequestParam(value = "keyword", required = false) String keyword) {
        return conferenceService.getConferenceList(keyword);
    }

    /**
     * 会议室详情
     */
    @GetMapping("/getConferenceInfo/{id}")
    public ConferenceInfoVo getConferenceDetailInfo(@PathVariable Long id) {
        return conferenceService.getConferenceDetailInfo(id);
    }

    /**
     * 会议室预约使用情况统计
     *
     * @param param
     */
    @GetMapping("/getConferenceUsage")
    public List<AppointmentUsageVo> getConferenceUsage(ConferenceUsageParam param) {
        return conferenceService.selectConferenceUsage(param);
    }

    /**
     * 会议室预约使用总数统计
     *
     * @param param
     */
    @GetMapping("/getConferenceCount")
    public AppointmentUsageVo getConferenceCount(ConferenceUsageParam param) {
        return conferenceService.selectConferenceCount(param);
    }

    /**
     * 会议室预约闲置率统计
     *
     * @param param
     */
    @GetMapping("/getConferenceIdle")
    public List<AppointmentIdleVo> getConferenceIdle(ConferenceUsageParam param) {
        return conferenceService.selectConferenceIdle(param);
    }

    /**
     * 会议室预约时段分析
     *
     * @param param
     */
    @GetMapping("/getTimeIntervalLog")
    public List<AppointmentIntervalLogVo> getTimeIntervalLog(ConferenceUsageParam param) {
        return conferenceService.selectTimeIntervalLog(param);
    }

    /**
     * 会议室删除
     *
     * @param id
     */
    @DeleteMapping("/conference/{id}")
    public void deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
    }

    /**
     * 状态修改
     *
     * @param id
     * @param status
     */
    @PutMapping("/conference/{id}")
    public void updateConferenceStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        conferenceService.updateConferenceStatus(id, status);
    }

    /**
     * 会议室详情查询
     *
     * @param id
     */
    @GetMapping("/getConference-detail/{id}")
    public ConferenceInfoVo getConferenceDetail(@PathVariable("id") Long id) {
        return conferenceService.getConferenceDetail(id);
    }

    /**
     * 会议室使用率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference/used/ratio")
    public ConferenceUsedRatioVO getConferenceUsedRatio(ConferenceUsageParam param) {
        return conferenceService.getConferenceUsedRatio(param);
    }

}
