package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Flow;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlowDTO extends ConvertionDTO<FlowDTO, Flow> {

    public FlowDTO() {
    }

    public FlowDTO(Flow flow) {
        super();
        map(flow);
    }

    public FlowDTO(List<Flow> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    private String deptId;

    @NotNull
    @NotBlank
    private String category;

    @NotNull
    @NotBlank
    private String name;

    private String code;

    @NotNull
    @NotBlank
    private String key;

    @NotNull
    private Boolean enabled;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

}
