package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 场景类型返回参数
 *
 * @author lanchun
 * @date 2021/12/2 17:43
 */
@Data
public class SceneTypeDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 场景类型名称
     */
    private String name;

    /**
     * 场景类型编号
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 序号
     */
    private Integer sort;
}
