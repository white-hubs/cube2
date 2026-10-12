package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author bq
 * @date 2022/7/20
 * @description 二维码第三方链接
 */
@Data
@TableName("qrcode_application")
public class QrcodeApplicationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String applicationCode;

    /**
     * 名称
     */
    private String applicationName;

}