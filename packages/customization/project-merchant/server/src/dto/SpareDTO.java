package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Spare;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpareDTO extends ConvertionDTO<SpareDTO, Spare> {
    public SpareDTO() {
    }

    public SpareDTO(Spare spare) {
        super();
        map(spare);
    }

    public SpareDTO(List<Spare> spares) {
        super();
        map(spares);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    private String code;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String typeId;

    @NotNull
    @NotBlank
    private String typeCode;

    private String factory;

    private String model;

    private Integer num;

    private String unit;

    private Double price;

    private Integer estimateLow;

    private Integer estimateHigh;

    private String description;

    private String address;

    private LocalDateTime time;

    private Boolean hasDetails;

    private String status;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private String typeName;

    private List<String> deviceIds;

    private List<DeviceDTO> deviceDTOS;
}
