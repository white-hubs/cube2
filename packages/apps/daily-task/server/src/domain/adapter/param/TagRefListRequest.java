package com.guhecloud.cube.biz.daily.task.domain.adapter.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/8 17:03
 * @Description:
 */
@Data
public class TagRefListRequest {

    private List<Long> tagIds;

    private List<Long> targetIds;
}
