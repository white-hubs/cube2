package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.param.ConferenceResumeQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceResumeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会议履职记录接口。
 */
@RestController
@RequiredArgsConstructor
public class ConferenceResumeController {

    private final ConferenceResumeQueryService conferenceResumeQueryService;

    /**
     * 按OUAA用户和日期范围查询会议履职记录。
     *
     * @param param 查询参数
     *
     * @return 会议履职记录列表
     */
    @PostMapping("/resume/meeting-records")
    public List<ConferenceResumeRecordDTO> listMeetingRecords(@RequestBody ConferenceResumeQueryParam param) {
        return conferenceResumeQueryService.listMeetingRecords(param);
    }
}
