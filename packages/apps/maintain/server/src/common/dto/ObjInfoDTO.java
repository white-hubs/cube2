package com.guhecloud.cube.srv.gen.maintain.server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 对象信息
 * @date 2023/11/8 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ObjInfoDTO {
    /**
     * 对象id
     */
    private Long id;
    /**
     * 对象名称
     */
    private String objName;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
}
