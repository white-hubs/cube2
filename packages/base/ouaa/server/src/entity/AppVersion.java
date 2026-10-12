package com.guhecloud.cube.biz.ouaa.server.entity;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.AppTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AppVersion extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 终端编号
     */
    private String terminal;

    /**
     * 类型
     */
    private AppTypeEnum type;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 文件密钥
     */
    private String apkSecret;

    /**
     * 版本序号
     */
    private Integer sort;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdated;

    /**
     * 版本备注
     */
    private String remark;
}
