package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageOfHandledDTO extends Paging {
    @NotBlank
    private String applicationCode;
    private Period taskCompletedTime;
    private String status;

    private String businessType;

    public PageOfHandledDTO(String applicationCode, String businessType, Period taskCompletedTime, String status, Integer pageNumber,
                            Integer pageSize
                           ) {
        super(pageNumber, pageSize);
        this.applicationCode = applicationCode;
        this.taskCompletedTime = taskCompletedTime;
        this.businessType = businessType;
        this.status = status;

    }

}
