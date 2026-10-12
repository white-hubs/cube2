package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2023/02/28
 * @description 离职手续审批表
 */
@Data
public class LeaveOfficeProceduresPO {

    @FormField(label = "离职员工")
    private List<String> staffIds;

}