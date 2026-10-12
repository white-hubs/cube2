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
public class GuideAreaDetailsVO {
    private Long id;
    private String name;
    private String coordinate;
    /**
     * 上传时间
     */
    private Date createTime;
    /**
     * 图层
     */
    private String zoom;
    /**
     * 序号
     */
    private Integer level;
    /**
     * 状态
     */
    private String state;
}
