package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceUsageParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIdleVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIntervalLogVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentUsageVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceInfoVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceUsedRatioVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface ConferenceService extends IService<Conference> {

    void insertConference(ConferenceAddOrUpdateParam param);

    void updateConference(ConferenceAddOrUpdateParam param);

    Pagination<ConferenceInfoVo> selectConferenceInfo(ConferenceQueryParam param);

    ConferenceInfoVo getConferenceDetailInfo(Long id);

    Pagination<AppointmentVo> getAppointmentVo(AppointmentParam param);

    List<AppointmentUsageVo> selectConferenceUsage(ConferenceUsageParam param);

    AppointmentUsageVo selectConferenceCount(ConferenceUsageParam param);

    List<AppointmentIdleVo> selectConferenceIdle(ConferenceUsageParam param);

    List<AppointmentIntervalLogVo> selectTimeIntervalLog(ConferenceUsageParam param);

    void deleteConference(Long id);

    void updateConferenceStatus(Long id, Integer status);

    ConferenceInfoVo getConferenceDetail(Long id);

    /**
     * 驾驶舱会议室统计
     *
     * @param param
     *
     * @return
     */
    ConferenceUsedRatioVO getConferenceUsedRatio(ConferenceUsageParam param);

    /**
     * 会议室列表
     *
     * @param keyword
     *
     * @return
     */
    List<Conference> getConferenceList(String keyword);

    /**
     * 构建查询参数
     */
    void buildFilterParam(AppointmentParam param);
}
