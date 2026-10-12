package com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 资产二维码信息
 * @date 2023/11/27 10:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PropertyQrCodeVO extends QrcodeVO {
    /**
     * @Author wql
     * @Description 资产编码
     * @Date 10:23 2023/11/27
     **/
    private String serialNumber;
}
