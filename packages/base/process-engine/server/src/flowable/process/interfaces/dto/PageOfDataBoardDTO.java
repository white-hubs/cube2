package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class PageOfDataBoardDTO extends Paging {
    @NotBlank
    private String type;

    @NotBlank
    private String applicationCode;

    private String status;

    private Period period;

    private String businessType;
}
