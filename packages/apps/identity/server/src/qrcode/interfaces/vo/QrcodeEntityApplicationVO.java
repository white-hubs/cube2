package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 实体与应用绑定
 * @date 2023/10/23 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QrcodeEntityApplicationVO {
    private Long id;
    /**
     * 实体id
     */
    private Long entityId;
    /**
     * 应用id
     */
    private Long applicationId;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 应用Code
     */
    private String applicationCode;
    /**
     * 是否选中
     */
    private Boolean selected;

}
