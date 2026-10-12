package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 会议房间人员
 *
 * @author lanchun
 * @date 2025/2/11 17:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "conference_room_user")
public class ConferenceRoomUserPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = 4304977898073111223L;

    private Long id;

    /**
     * 会议房间主键
     */
    private Long conferenceRoomId;

    /**
     * 房间号
     */
    private String conferenceRoomNumber;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 会议用户类型
     */
    private String conferenceUserType;

    /**
     * 入会时间
     */
    private LocalDateTime membershipTime;

    /**
     * 离会时间
     */
    private LocalDateTime departureTime;

    /**
     * 离会方式
     */
    private String departureType;
}
