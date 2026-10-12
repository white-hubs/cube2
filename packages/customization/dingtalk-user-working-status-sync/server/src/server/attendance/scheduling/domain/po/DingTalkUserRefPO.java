package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ding_talk_user_ref")
public class DingTalkUserRefPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 钉钉用户id
     */
    private String dingTalkUserId;

    /**
     * 系统用户id
     */
    private Long systemUserId;
}
