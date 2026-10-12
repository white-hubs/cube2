package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/22
 * @description 招聘表
 */
@Data
public class RecruitPO {

    @FormField(label = "招聘人数")
    private Integer recruitNum;

    @FormField(label = "招聘岗位")
    private Long postId;

}