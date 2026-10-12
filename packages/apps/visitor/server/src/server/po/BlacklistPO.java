package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 黑名单
 */
@Data
@TableName("blacklist")
@EqualsAndHashCode(callSuper = true)
public class BlacklistPO extends BasicEnhancedTenantEntity<Long> {
    private static final long serialVersionUID = 1224326710008596036L;
    private Long id;
    /**
     * 视频盒子的人员code
     */
    private Integer thirdUserCode;
    private String phone;
    private String deviceCode;
    private String name;
    private String reason;
    private String sex;
    private String pictures;
}
