package com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration;

import lombok.Getter;

@Getter
public enum ConferenceBookStatusEnum {
    NOAPPOINTMENT("已约满"),
    AVALIABLE("可预约");
    /**
     * 描述信息
     */
    private final String description;

    ConferenceBookStatusEnum(String description) {
        this.description = description;
    }
}
