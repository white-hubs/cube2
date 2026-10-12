package com.guhecloud.cube.biz.fusion.communicate.server.conference.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 会议房间信息
 *
 * @author lanchun
 * @date 2025/2/19 14:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConferenceRoomVO {

    private Long id;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 发起人
     */
    private Long proposeUserId;

    /**
     * 发起时间
     */
    private LocalDateTime proposeTime;

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

    /**
     * 会议成员列表
     */
    private Set<Long> members;
}
