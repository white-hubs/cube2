package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 隐患单列表详情
 * @date 2023/9/11 17:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerVO {
    /**
     * 隐患单名称
     */
    private String hiddenName;
    /**
     * 市场编号
     */
    private Integer marketTenant;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 隐患分类
     */
    private String category;
    /**
     * 隐患级别
     */
    private String levelName;
    /**
     * 隐患状态
     */
    private String status;
    /**
     * 隐患描述
     */
    private String remark;
    /**
     * 隐患创建时间
     */
    private LocalDateTime hiddenCreateTime;

}
