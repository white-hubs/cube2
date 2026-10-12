package com.guhecloud.subsystem.requester.sdk.vo.information;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节目详情
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
