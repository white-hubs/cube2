package com.guhecloud.cube.srv.tj.electronic.guide.param;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:34
 * @Description:
 * @Version: 1.0
 */
@Data
public class LayerManageParam {
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
}
