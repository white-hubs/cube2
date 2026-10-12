package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeviceQueryDTO {

    @NotNull
    @NotBlank
    private String projectId;

    private String search;

    private String typeId;

    private String typeCode;

    private List<String> typeCodes;

    private String areaId;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    private String areaLevel;

    private String deptId;

    private String iotDeviceType;

    private String status;

    private Boolean online;

    private String timeLimitStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isAuth;

    private Boolean bimInfo;

    /*        private String myFavoriteBusinessType;*/

    private Boolean myFavorite;

    private List<String> typeIds;

    private String monitoringObject;

}