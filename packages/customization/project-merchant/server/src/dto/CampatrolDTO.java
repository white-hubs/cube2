package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class CampatrolDTO {

    private String id;

    private String projectId;

    private String code;

    private String name;

    private String cycleType;

    private String selection;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private LocalDateTime hour;

    private String dutyType;

    private String dutyId;

    private String dutyName;

    private Integer pointNum;

    private String description;

    private Boolean enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
