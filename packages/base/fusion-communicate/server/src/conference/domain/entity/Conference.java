package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.CallingTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.MediaTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 会议信息
 *
 * @author lanchun
 * @date 2025/2/11 17:44
 */
@Getter
public class Conference {

    /**
     * 编号
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    /**
     * 会议室类型
     */
    private final String conferenceType;

    /**
     * 媒体类型
     */
    @Setter
    private MediaTypeEnum mediaType;

    /**
     * 通话类型
     */
    @Setter
    private CallingTypeEnum callingType;

    /**
     * 会议结束时间
     */
    private final LocalDateTime closingTime;

    public Conference(String code, String name, String conferenceType, MediaTypeEnum mediaType, CallingTypeEnum callingType,
                      LocalDateTime closingTime
                     ) {
        this.code = code;
        this.name = name;
        this.conferenceType = conferenceType;
        this.mediaType = mediaType;
        this.callingType = callingType;
        this.closingTime = closingTime;
    }

}
