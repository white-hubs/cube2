package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wql
 * @describe 二维码实体信息
 * @date 2024/1/30 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrcodeEntityInfoVO {
    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 实体ID
     */
    private Long entityId;

    /**
     * 实体名称
     */
    private String entityName;
    /**
     * 二维码包含的应用信息
     */
    private List<QrcodeApplicationVO> qrcodeApplicationVOS;
    /**
     * 租户
     */
    private Integer tenant;
}
