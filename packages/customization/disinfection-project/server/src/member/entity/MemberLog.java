package com.guhecloud.cube.srv.gen.disinfection.server.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 操作日志
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("member_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLog extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String title;
    private Long deviceId;
    private String detail;
    private MemberLogTypeEnum type;

}
