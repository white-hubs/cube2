package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.EnvmodelConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 环境模型配置
 * </p>
 *
 * @author bq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnvmodelConfigDTO extends ConvertionDTO<EnvmodelConfigDTO, EnvmodelConfig> {

    public EnvmodelConfigDTO() {
    }

    public EnvmodelConfigDTO(EnvmodelConfig envmodelConfig) {
        super();
        map(envmodelConfig);
    }

    public EnvmodelConfigDTO(List<EnvmodelConfig> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    @NotBlank
    @NotNull
    private String projectId;

    private String x;

    private String y;

    private String z;

    /**
     * 旋转角度
     */
    private String angle;


}
