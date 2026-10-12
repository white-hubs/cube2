package com.guhecloud.cube.srv.gen.conferencemanage.client.param;

import lombok.Data;

import java.time.LocalDate;

/**
 * 会议履职记录查询参数。
 */
@Data
public class ConferenceResumeQueryParam {

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 查询开始日期。
     */
    private LocalDate startDate;

    /**
     * 查询结束日期。
     */
    private LocalDate endDate;
}
