package com.guhecloud.cube.srv.electron.guide.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:36
 * @Description:
 * @Version: 1.0
 */
@Data
public class LayerManageDetailsVO {
    private Long id;
    /**
     * 图层名称
     */
    private String layerName;
    /**
     * 图层附件
     */
    private String layerFile;
    /**
     * 上传时间
     */
    private LocalDateTime createTime;
    /**
     * 使用状态
     */
    private String state;
    /**
     * 西南角
     */
    private String southwest;
    /**
     * 东北角
     */
    private String northeast;
    /**
     * 中心点
     */
    private String center;
    /**
     * 区域信息
     */
    public GuideAreaDetailsVO area;
}
