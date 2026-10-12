package com.guhecloud.cube.srv.electron.guide.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class GuideAreaParam {
    private Long id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 父级主键
     */
    private Long parentId;
    /**
     * 区域坐标
     */
    private String coordinate;
    /**
     * 图层
     */
    private String zoom;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 状态
     */
    private String state;
}
