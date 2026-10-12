package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 10:21
 * @Description: 会议室类型
 */

@EqualsAndHashCode(callSuper = true)
@TableName("conference_room_type")
@Data
public class ConferenceRoomType extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 类型名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean status;
}
