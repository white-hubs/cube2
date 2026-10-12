package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NoWorkDayDTO {

    private String projectId;

    @NotNull
    private Integer year;

    @NotNull
    @Size(min = 1)
    private List<LocalDateTime> dateList;
}
