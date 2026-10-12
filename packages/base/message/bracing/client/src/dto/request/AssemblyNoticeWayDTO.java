package com.guhecloud.cube.base.bracing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知渠道
 *
 * @author lanchun
 * @date 2024/11/14 19:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyNoticeWayDTO {
    /**
     * 通知方式
     */
    private String noticeWay;
    /**
     * 模板id
     */
    private Long templateId;
}
