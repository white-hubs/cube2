package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 实体对象信息
 * @date 2023/11/20 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EntityInfoCommand {
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 实体名称
     */
    private String entityName;
    /**
     * 编码
     */
    private String serialNumber;

    /**
     * 实体业务信息
     */
    private Object businessEntity;
}
