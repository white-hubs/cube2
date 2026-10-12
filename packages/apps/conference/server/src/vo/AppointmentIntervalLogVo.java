package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentIntervalLogVo {

    private String conferenceName;
    private List<ConferenceNameList> conferenceNameLists;
}
