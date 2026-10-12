package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SpareType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SpareTypeDTO extends ConvertionDTO<SpareTypeDTO, SpareType> {

    public SpareTypeDTO() {
    }

    public SpareTypeDTO(SpareType spareType) {
        super();
        map(spareType);
    }

    public SpareTypeDTO(List<SpareType> spareTypes) {
        super();
        map(spareTypes);
    }

    private String id;

    private String pid;

    private String projectId;

    private String code;

    private String name;

    private String description;

    private Integer sort;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private List<SpareTypeDTO> children;
}
