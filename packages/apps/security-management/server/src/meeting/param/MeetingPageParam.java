package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class MeetingPageParam extends Paging {

    //会见日期
    private Period meetingPeriod;

    /**
     * 被见人名称
     */
    private String visitedPerson;
}
