package com.guhecloud.cube.biz.mdata.server.area.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/20 10:13
 * @Description: 存在的区域缓存模型
 */
@Data
public class AreaCacheInfo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 序列号
     */
    private String sequence;

    private String pathId;

    private String pathName;
}
