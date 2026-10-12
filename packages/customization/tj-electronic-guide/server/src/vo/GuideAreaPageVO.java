package com.guhecloud.cube.srv.tj.electronic.guide.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class GuideAreaPageVO {
    private Long id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 区域坐标
     */
    private String coordinate;
    /**
     * 图层
     */
    private String zoom;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 序号
     */
    private Integer level;
    /**
     * 状态
     */
    private String state;
}
