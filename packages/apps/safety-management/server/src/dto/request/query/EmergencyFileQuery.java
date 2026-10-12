package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 预案文件列表
 * @date 2023/10/8 19:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyFileQuery extends BaseQuery {
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 事件分类
     */
    private String classifyCode;

    /**
     * 级别
     */
    private String levelCode;

    /**
     * 状态
     */
    private String status;
}
