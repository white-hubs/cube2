package com.guhecloud.cube.biz.daily.task.domain.adapter.dto;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/8 17:03
 * @Description:
 */
@Data
public class BaseTargetInfoResponse {
    private Long targetId;
    private String targetName;
    private String targetType;
    private String typeId;
    private String tagIds;
    private String tagNames;
}
