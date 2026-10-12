package com.guhecloud.cube.srv.tj.electronic.guide.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class PointManageParam {
    private Long id;
    /**
     * 点位名称
     */
    private String name;
    /**
     * 区域
     */
    private Long areaId;
    /**
     * 点位地址
     */
    private String address;
    /**
     * 点位坐标
     */
    private String coordinate;
    /**
     * 点位描述
     */
    private String remarks;
    /**
     * 序号
     */
    private Integer level;
    /**
     * 状态
     */
    private String state;
}
