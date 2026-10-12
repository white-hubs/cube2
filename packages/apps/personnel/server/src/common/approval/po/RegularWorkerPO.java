package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2023/02/27
 * @description 转正审批表
 */
@Data
public class RegularWorkerPO {

    @FormField(label = "转正人员")
    private List<String> staffIds;

    @FormField(label = "转正时间")
    private String regularWorkerTime;

    @FormField(label = "试用部门")
    private Long organizationId;

    @FormField(label = "试用岗位")
    private String postId;

}