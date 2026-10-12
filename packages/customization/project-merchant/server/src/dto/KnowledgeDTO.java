package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Knowledge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class KnowledgeDTO extends ConvertionDTO<KnowledgeDTO, Knowledge> {

    public KnowledgeDTO() {
    }

    public KnowledgeDTO(Knowledge knowledge) {
        super();
        map(knowledge);
    }

    public KnowledgeDTO(List<Knowledge> knowledges) {
        super();
        map(knowledges);
    }

    private String id;

    @NotNull
    @NotBlank
    private String projectId;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    private String code;

    @NotNull
    @NotBlank
    private String type;

    private String deviceTypeId;

    private String deviceIds;

    private String description;

    @Length(max = 100)
    private String remark;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private String createByName;

    private List<String> fileIds;

    private List<FileVO> FileVOS;

    private List<DeviceDTO> deviceDTOS;

    private String deviceTypeName;

}
