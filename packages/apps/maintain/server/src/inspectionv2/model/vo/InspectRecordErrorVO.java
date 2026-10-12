package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InspectRecordErrorVO {
    private Long id;
    private LocalDateTime time;
    private String targetName;
    private Long userId;
    private String nickname;
    private String result;
    private Integer errNumber;
    private Long taskId;
    private String taskName;

    /**
     * 异常项id
     */
    private String recordItemId;
    /**
     * 异常项
     */
    private String errorItem;
}
