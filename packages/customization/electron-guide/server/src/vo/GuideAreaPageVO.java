package com.guhecloud.cube.srv.electron.guide.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class GuideAreaPageVO extends Tree.Simple<Long, GuideAreaPageVO> {
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
    private LocalDateTime createTime;
    /**
     * 序列号
     */
    private String sequence;
    /**
     * 序号
     */
    private Integer sort;
    /**
     * 状态
     */
    private String state;
}
