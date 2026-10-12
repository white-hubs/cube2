package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.ouaa.server.enumeration.UserTelBindEnableEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_tel")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTel extends BasicEnhancedEntity<Long> {
    private Long id;
    private Long userId;
    private String tel;
    private UserTelBindEnableEnum isBind;
}
