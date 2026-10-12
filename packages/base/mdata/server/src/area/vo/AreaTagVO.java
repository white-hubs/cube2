package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

@Data
public class AreaTagVO {
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 描述
     */
    private String describe;

    /**
     * 标签图标
     */
    private Long icoFileId;
    private FileVO fileVO;
}
