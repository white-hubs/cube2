package com.guhecloud.cube.base.bracing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 通知方式
 * @date 2023/9/1 17:30
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
