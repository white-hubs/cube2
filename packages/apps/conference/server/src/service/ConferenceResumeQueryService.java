package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.param.ConferenceResumeQueryParam;

import java.util.List;

/**
 * 会议履职记录查询服务。
 */
public interface ConferenceResumeQueryService {

    /**
     * 按OUAA用户和日期范围查询会议履职记录。
     *
     * @param param 查询参数
     *
     * @return 会议履职记录列表
     */
    List<ConferenceResumeRecordDTO> listMeetingRecords(ConferenceResumeQueryParam param);
}
