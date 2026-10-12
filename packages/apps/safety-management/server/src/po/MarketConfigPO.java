package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 市场配置
 * @date 2023/10/19 15:37
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "market_config")
public class MarketConfigPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 市场编号
     */
    private String marketCode;
    /**
     * 执行器编号
     */
    private String executorCode;
    /**
     * 执行器端口
     */
    private Integer executorPort;
    /**
     * 市场方租户号
     */
    private Integer marketTenant;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 市场经纬度坐标
     */
    private String coordinate;
    /**
     * 市场地址
     */
    private String address;
    /**
     * 市场创立时间
     */
    private String foundTime;
    /**
     * 市场图片，多个用逗号分割
     */
    private String fileIds;
    /**
     * 市场面积
     */
    private Double marketArea;
    /**
     * 状态
     */
    private Boolean status;

}
