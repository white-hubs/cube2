package com.guhecloud.cube.srv.electron.guide.param;

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
     * 类型主键
     */
    private Long typeId;
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
    /**
     * 点位附件
     */
    private String filePath;
}
