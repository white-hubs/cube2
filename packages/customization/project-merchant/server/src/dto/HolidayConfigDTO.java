package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.HolidayConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayConfigDTO extends ConvertionDTO<HolidayConfigDTO, HolidayConfig> {

    public HolidayConfigDTO() {
    }

    public HolidayConfigDTO(HolidayConfig holidayConfig) {
        super();
        map(holidayConfig);
    }

    public HolidayConfigDTO(List<HolidayConfig> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String name;

    private String projectId;

    private String remark;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @NotNull
    @Size(min = 1)
    private List<LocalDateTime> dateList;
}
