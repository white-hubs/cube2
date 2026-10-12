package com.guhecloud.cube.base.visualization.server.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  11:32
 * @Description:
 * @Version: 1.0
 */
@Data
public class ModelRouteVO {
    private Long id;
    /**
     * 模型导航名称
     */
    private String name;
    /**
     * 模型导航编号
     */
    private String code;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 父类主键
     */
    private Long parentId;
    /**
     * 序号
     */
    private Integer serial;
    /**
     * 模型地址
     */
    private String url;
    /**
     * 模型导航目录
     */
    private String directory;
    /**
     * 下级数据
     */
    private List<ModelRouteVO> children;
}
