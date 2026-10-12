package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 隐患分类统计
 * @date 2024/1/24 13:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerTypeStatistics {
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类数量
     */
    private Long num;
    /**
     * 占比
     */
    private float proportion;
}
