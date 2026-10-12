package com.guhecloud.cube.biz.mdata.server.stall.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

/**
 * 档位分类信息
 */
@Data
public class StallCategoryPageVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 文件信息
     */
    private FileVO fileVO;
}
