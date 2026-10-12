package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FirmManage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 公司管理-用于安全月报
 * </p>
 *
 * @author bq
 * @since 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FirmManageDTO extends ConvertionDTO<FirmManageDTO, FirmManage> {

    public FirmManageDTO() {
    }

    public FirmManageDTO(FirmManage firmManage) {
        super();
        map(firmManage);
    }

    public FirmManageDTO(List<FirmManage> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String projectName;

    @NotNull
    @NotBlank
    @Length(max = 50)
    private String name;

    private String url;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

}
