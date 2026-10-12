package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/24 13:42
 * @Description:
 */
@Data
public class KeyPlacesQueryParam {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 数据类型ID
     */
    private Long dataTypeId;

    /**
     * 名称
     */
    private String keyword;

    /**
     * 区域
     */
    private String area;
}
