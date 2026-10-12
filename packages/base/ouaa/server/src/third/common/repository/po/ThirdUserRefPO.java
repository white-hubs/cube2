package com.guhecloud.cube.biz.ouaa.server.third.common.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName(value = "third_user_ref")
public class ThirdUserRefPO extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 三方类型(微信等其它三方)
     */
    private String type;

    /**
     * 三方系统配置id
     */
    private Long configId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 关联的第三方账号id
     */
    private Long refId;

    /**
     * 租户编号
     */
    private Integer tenant;
}
