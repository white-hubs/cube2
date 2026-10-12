package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-14  11:16
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
public class InspectionIObjDetailsVO {
    /**
     * 正常数量
     */
    private Long totalNum;
    /**
     * 异常数量
     */
    private Long abnormalNum;
}
