package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/7/20
 * @description 二维码样式
 */
@Data
@TableName("qrcode_style")
@EqualsAndHashCode(callSuper = false)
public class QrcodeStylePO extends BasicEnhancedTenantEntity<Long> {

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
     * 内容配置json
     */
    private String configJson;

    /**
     * 宽度
     */
    private String width;

    /**
     * 高度
     */
    private String height;

}