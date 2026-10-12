package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import lombok.Data;

/**
 * 主题返回参数
 *
 * @author lanchun
 * @date 2023/4/18 11:24
 */
@Data
public class ThemeVO {

    private Long id;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 备注说明
     */
    private String remark;
}
