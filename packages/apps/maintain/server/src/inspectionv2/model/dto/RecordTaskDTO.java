package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTaskDTO {
    private Long typeId;
    private Long taskId;
    private Long recordId;
    private String taskName;
    private String targetName;
}
