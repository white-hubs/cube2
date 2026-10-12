package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import lombok.Data;

import java.util.List;

@Data
public class AttHandStaffDateParam {

    private String date;
    List<Long> classIdList;
}
