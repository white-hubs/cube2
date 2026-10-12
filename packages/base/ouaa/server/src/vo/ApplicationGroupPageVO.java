package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 应用组分页对象
 *
 * @author lanchun
 * @date 2022/5/9 11:26
 */
@Data
public class ApplicationGroupPageVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 应用组名称
     */
    private String name;

    /**
     * 应用组备注
     */
    private String remark;

    /**
     * 应用列表
     */
    private List<ApplicationPageVO> applicationPageVOS;
}
