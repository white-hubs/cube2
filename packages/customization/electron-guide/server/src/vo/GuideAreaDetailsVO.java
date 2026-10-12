package com.guhecloud.cube.srv.electron.guide.vo;

import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime createTime;
    /**
     * 图层
     */
    private String zoom;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 状态
     */
    private String state;
    /**
     * 父级主键
     */
    private Long parentId;
    /**
     * 序列号
     */
    private String sequence;
}
