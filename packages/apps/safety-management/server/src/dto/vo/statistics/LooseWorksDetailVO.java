package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 小散工程详情列表
 * @date 2023/9/19 15:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LooseWorksDetailVO {
    private Long id;
    /**
     * 工程编号
     */
    private String projectNum;
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 完成时限
     */
    private LocalDateTime completionTime;
    /**
     * 监管负责人名称
     */
    private String superviseUserName;
    /**
     * 监管部门名称
     */
    private String superviseDepName;
    /**
     * 工程状态
     */
    private String status;
    /**
     * 完成进度
     */
    private Integer schedule;
    /**
     * 市场租户号
     */
    private Integer marketTenant;
    /**
     * 市场租户名称
     */
    private String marketName;
}
