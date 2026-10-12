package com.guhecloud.cube.biz.ouaa.client.param;

import lombok.Data;

import java.util.Set;

@Data
public class WorkStatusQueryParam {
    private Integer tenant;
    private Set<Long> userIdList;
}
