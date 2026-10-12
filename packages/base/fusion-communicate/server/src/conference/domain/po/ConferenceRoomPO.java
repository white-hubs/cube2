package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会议房间信息
 *
 * @author lanchun
 * @date 2025/2/11 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "conference_room")
public class ConferenceRoomPO extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = 2329723497127759687L;

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 房间号
     */
    private String number;

    /**
     * 名称
     */
    private String name;

    /**
     * 发起人
     */
    private Long proposeUserId;

    /**
     * 发起时间
     */
    private LocalDateTime proposeTime;

    /**
     * 会议室类型
     */
    private String conferenceType;

    /**
     * 媒体类型
     */
    private String mediaType;

    /**
     * 通话类型
     */
    private String callingType;

    /**
     * 会议结束时间
     */
    private LocalDateTime closingTime;

}
