package com.guhecloud.cube.srv.gen.conferencemanage.client;

import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.param.ConferenceResumeQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 会议履职记录客户端。
 */
@FeignClient(ServiceRoute.TAG)
public interface ConferenceResumeClient {

    /**
     * 按OUAA用户和日期范围查询会议履职记录。
     *
     * @param param 查询参数
     *
     * @return 会议履职记录列表
     */
    @PostMapping("/resume/meeting-records")
    List<ConferenceResumeRecordDTO> listMeetingRecords(@RequestBody ConferenceResumeQueryParam param);
}
