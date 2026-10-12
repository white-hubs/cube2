package com.guhecloud.cube.srv.gen.securitymanagement.server.meeting.enums;

/**
 * 会见人员类型
 */
public enum MeetingPersonTypeEnum {
    MEETING_PERSON("0", "会见人员"),
    ACCOMPANYING_PERSON("1", "随行人员"),
    VISITED_PERSON("2", "被会见人员"),
    ;
    private String code;
    private String name;

    MeetingPersonTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        for (MeetingPersonTypeEnum meetingPersonTypeEnum : values()) {
            if (meetingPersonTypeEnum.getCode().equals(code)) {
                return meetingPersonTypeEnum.getName();
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        for (MeetingPersonTypeEnum meetingPersonTypeEnum : values()) {
            if (meetingPersonTypeEnum.getName().equals(name)) {
                return meetingPersonTypeEnum.getCode();
            }
        }
        return null;
    }
}
