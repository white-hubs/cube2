package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 故障区域分析
 * @date 2023/11/9 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FaultAreaAnalyseVO {
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 总数
     */
    private Integer objAreaTotal;
    /**
     * 待确认
     */
    private Long objAreaConfirm;
    /**
     * 待处理
     */
    private Long objAreaHandle;
    /**
     * 待关闭
     */
    private Long objAreaClosed;
    /**
     * 结束
     */
    private Long objAreaFinished;
}
