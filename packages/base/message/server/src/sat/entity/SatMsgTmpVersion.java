package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 模板版本
 *
 * @author lanchun
 * @date 2023/5/10 9:32
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgTmpVersion extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 场景编码
     */
    private String msgSceneCode;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 厂商编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 厂商模板配置
     */
    private String manufacturerTmpConfig;

    /**
     * 厂商模板校验后的配置
     */
    private String manufacturerTmpSchema;

    /**
     * 版本号
     */
    private String version;
}
