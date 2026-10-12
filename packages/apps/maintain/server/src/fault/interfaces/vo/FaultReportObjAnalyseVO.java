package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 故障设备分析
 * @date 2023/11/9 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultReportObjAnalyseVO {
    /**
     * 设备类型名称
     */
    private String objTypeName;
    /**
     * 总数
     */
    private Integer objTypeTotal;
    /**
     * 待确认
     */
    private Long objTypeConfirm;
    /**
     * 待处理
     */
    private Long objTypeHandle;
    /**
     * 待关闭
     */
    private Long objTypeClosed;
    /**
     * 结束
     */
    private Long objTypeFinished;
}
