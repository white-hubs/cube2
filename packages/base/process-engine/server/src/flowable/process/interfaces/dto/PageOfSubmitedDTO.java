package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author pengjian
 * @date 2022/10/25 16:16
 * @description
 */
@Getter
@Setter
@NoArgsConstructor
public class PageOfSubmitedDTO extends Paging {
    private Period period;
    private String status;
    @NotBlank
    private String businessType;

    private String applicationCode;

    public PageOfSubmitedDTO(Period period, String status, String businessType, Integer pageNumber, Integer pageSize) {
        super(pageNumber, pageSize);
        this.period = period;
        this.status = status;
        this.businessType = businessType;
    }

    public PageOfSubmitedDTO(Period period, String status, String applicationCode, String businessType, Integer pageNumber,
                             Integer pageSize
                            ) {
        super(pageNumber, pageSize);
        this.period = period;
        this.status = status;
        this.businessType = businessType;
        this.applicationCode = applicationCode;
    }
}
