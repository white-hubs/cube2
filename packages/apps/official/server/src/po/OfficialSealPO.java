package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 印章
 */
@Data
@TableName("official_seal")
public class OfficialSealPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    @Unique(message = "印章名称不能重复")
    private String sealName;//印章名称

    private String sealDesc;//印章描述

}