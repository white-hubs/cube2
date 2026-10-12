package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttLegalDayCountDTO {
    private List<LocalDateTime> workList;
    private List<LocalDateTime> restList;
}
