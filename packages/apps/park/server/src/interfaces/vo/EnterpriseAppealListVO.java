package com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnterpriseAppealListVO extends BasicTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 诉求类型ID
     */
    private Long appealTypeId;

    /**
     * 诉求类型名
     */
    private String appealTypeName;

    /**
     * 诉求时间
     */
    private LocalDateTime time;

    /**
     * 反馈人员名
     */
    private String username;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

}
