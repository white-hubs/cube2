package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingApplyPersonParam {

    private Long businessKey;

    private List<Long> businessKeys;

    /**
     * 会见人类型 0 会见 1 随行 2 被见
     */
    private String meetingPersonType;

    /**
     * 关联业务code 1:会见申请记录、2:会见记录
     */
    private Integer businessType;
}
