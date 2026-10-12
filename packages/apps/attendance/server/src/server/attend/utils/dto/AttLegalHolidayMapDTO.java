package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AttLegalHolidayMapDTO {
    private Map<String, String> workDateMap;
    private Map<String, String> restDateMap;
}
