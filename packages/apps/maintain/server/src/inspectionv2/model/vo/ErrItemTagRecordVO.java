package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrItemTagRecordVO {
    private Long id;
    private String targetName;
    private String taskName;
    private LocalDateTime time;
    private String nickname;
    private String result;
}
