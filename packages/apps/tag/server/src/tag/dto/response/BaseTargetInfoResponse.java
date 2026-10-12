package com.guhecloud.cube.srv.gen.tag.tag.dto.response;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/8 11:33
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
