package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * 应用组信息
 *
 * @author lanchun
 * @date 2022/5/9 11:26
 */
@Data
public class ApplicationGroupVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

}
