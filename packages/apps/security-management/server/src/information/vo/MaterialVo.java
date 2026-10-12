package com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下载素材的vo类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialVo {
    /**
     * 素材id
     */
    private String materialId;
    /**
     * 素材名称
     */
    private String materialName;
    /**
     * 数据源素材URL地址
     */
    private String address;
}
