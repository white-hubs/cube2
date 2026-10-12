package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.ConferenceUserTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.DepartureTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

;

@Getter
public class ConferenceUser {

    /**
     * 用户主键
     */
    private final Long id;

    /**
     * 会议用户类型
     */
    private final ConferenceUserTypeEnum conferenceUserType;

    /**
     * 入会时间
     */
    @Setter
    private LocalDateTime membershipTime;

    /**
     * 离会时间
     */
    @Setter
    private LocalDateTime departureTime;

    /**
     * 离会方式
     */
    @Setter
    private DepartureTypeEnum departureType;

    public ConferenceUser(Long id, ConferenceUserTypeEnum conferenceUserType) {
        this.id = id;
        this.conferenceUserType = conferenceUserType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConferenceUser that = (ConferenceUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
