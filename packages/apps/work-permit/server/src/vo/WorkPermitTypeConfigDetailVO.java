package com.guhecloud.cube.srv.gen.work.permit.server.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业类型配置详情。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkPermitTypeConfigDetailVO extends WorkPermitTypeConfigPageVO {
    /**
     * 作业风险检查项。
     */
    private List<WorkPermitAssociatedItemVO> riskCheckItems = new ArrayList<>();
    /**
     * 作业验收项。
     */
    private List<WorkPermitAssociatedItemVO> acceptanceItems = new ArrayList<>();
}
