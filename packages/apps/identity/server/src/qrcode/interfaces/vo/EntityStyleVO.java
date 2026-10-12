package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 实体类型二维码样式信息
 * @date 2024/1/30 10:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntityStyleVO extends QrcodeStyleVO {
    /**
     * 是否选中
     */
    private Boolean optFor;
}
