package com.guhecloud.cube.deliver.project.merchant.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Project;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO extends ConvertionDTO<ProjectDTO, Project> {

    public ProjectDTO() {
    }

    public ProjectDTO(Project project) {
        super();
        map(project);
    }

    public ProjectDTO(List<Project> list) {
        super();
        map(list);
    }

    private String id;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String code;

    @NotNull
    @NotBlank
    @Length(max = 30)
    private String name;

    @NotNull
    @NotBlank
    @Length(max = 28)
    private String abbr;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String longitude;

    @NotNull
    @NotBlank
    @Length(max = 20)
    private String latitude;

    @NotNull
    private Boolean enabled;

    @Length(max = 100)
    private String remark;

    private String provinceCode;

    private String cityCode;

    private String address;

    @NotNull
    @NotBlank
    private String url;

    @NotNull
    @NotBlank
    @Length(max = 100)
    private String api;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime launchDate;

    private Boolean launched;

    private Integer launchDay;

    @Length(max = 5000)
    private String introduction;

    private String figures;

    private int sort;

    private int acreage;

    private String copyright;

    private String model;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private List<UserDTO> managers;

    private List<String> fileIds;

    private List<FileVO> fileDTOS;

    private Boolean accessible;
    private Integer tenant;

}
