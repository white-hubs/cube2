package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 二维码对象信息
 * @date 2024/1/16 10:32
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QrcodeEntityInfo {
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 实体名称
     */
    private String entityName;
    /**
     * 实体类型
     */
    private String entityType;
    /**
     * 样式id
     */
    private Long styleId;
}
