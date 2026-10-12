package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 预案文件分类
 * @date 2023/10/9 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmergencyFileClassify {
    /**
     * 事件分类名称
     */
    private String classifyName;
    /**
     * 事件分类编号
     */
    private String classifyCode;
    /**
     * 事件分类数量
     */
    private Long classifyNum;
    /**
     * 事件分类占比
     */
    private Double classifyRatio;
}
