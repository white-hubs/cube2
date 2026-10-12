package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 应用卡片返回参数
 *
 * @author lanchun
 * @date 2022/5/24 15:45
 */
@Data
public class ApplicationCardVO {

    /**
     * 应用主键
     */
    private Long id;

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 卡片集合
     */
    private List<CardVO> cardVOS;
}
