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
 * 会议邀请记录
 *
 * @author lanchun
 * @date 2025/2/11 17:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "conference_invite_record")
public class ConferenceInviteRecordPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = -750812270073924161L;

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
     * 邀请时间
     */
    private LocalDateTime inviteTime;
}
