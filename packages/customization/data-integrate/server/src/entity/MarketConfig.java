package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采集市场配置
 * @date 2023-09-09 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MarketConfig extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //市场名称
    @Unique(composites = "deleted", message = "市场名称已存在")
    private String marketName;
    //市场编号
    @Unique(composites = "deleted", message = "市场编码已存在")
    private String marketCode;
    //市场方租户号
    @Unique(composites = "deleted", message = "租户号已存在")
    private Integer marketTenant;
    //请求地址
    private String url;
    //登录账号
    private String account;
    //登录密码
    private String password;
    //市场经纬度坐标
    private String coordinate;
    //地址
    private String address;
    //创建时间
    private String foundTime;
    //市场图片
    private String fileIds;
    /**
     * 市场面积
     */
    private Double marketArea;
    /**
     * 是否开启：影响统计，不影响采集
     */
    private Boolean status;

}