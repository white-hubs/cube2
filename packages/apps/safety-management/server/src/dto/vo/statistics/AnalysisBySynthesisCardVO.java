package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 综合分析卡片
 * @date 2023/9/8 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnalysisBySynthesisCardVO {
    /**
     * 报警总数
     */
    private Integer totalNum;
    /**
     * 未响应报警数
     */
    private Integer noResponseNum;
    /**
     * 处理中报警数
     */
    private Integer inProcessNum;
    /**
     * 超时未处理报警数
     */
    private Integer overtimeUntreatedNum;
    /**
     * 未处理报警数
     */
    private Integer notHandlerNum;
    /**
     * 已处理报警数
     */
    private Integer handlerNum;
    /**
     * 危险源建档数
     */
    private Integer dangerFilingNum;
    /**
     * 消防设备维保到期数
     */
    private Integer maintenanceExpireNum;
}
