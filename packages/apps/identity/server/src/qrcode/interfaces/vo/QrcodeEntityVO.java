package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2022/7/22
 * @description 二维码实体信息
 */
@Data
public class QrcodeEntityVO {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 实体ID
     */
    private String entityId;

    /**
     * 实体名称
     */
    private String entityName;

}