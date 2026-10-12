package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
/**商铺*/
public class CompanyDTO extends ConvertionDTO<CompanyDTO, Company> {

    public CompanyDTO() {
    }

    public CompanyDTO(Company company) {
        super();
        map(company);
    }

    public CompanyDTO(List<Company> list) {
        super();
        map(list);
    }

    /**
     * ID
     */
    private String id;

    @NotNull
    @NotBlank
    /**项目ID*/
    private String projectId;

    @NotNull
    @NotBlank
    /**商铺名称*/
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 商铺用途
     */
    private String purpose;

    @NotNull
    @NotBlank
    /**所属人姓名*/
    private String owner;

    //    @Mobile
    /**
     * 所属人姓名
     */
    private String mobile;

    /**
     * 所属人姓名
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 所属人姓名
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime endTime;

    @Length(max = 100)
    /**所属人姓名*/
    private String remark;

    @Length(max = 980)
    /**所属人姓名*/
    private String introduction;

    /**
     * 所属人姓名
     */
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private List<SpaceDTO> spaces;

    private List<String> spaceIds;

    private SpaceDTO spaceDTO;

    private List<Long> securityFileIds;

    private List<FileVO> securityFileVOS;

    private List<Long> trainingPhotoFileIds;

    private List<FileVO> trainingPhotoFileVOS;

    private List<Long> companyPhotoFileIds;

    private List<FileVO> companyPhotoFileVOS;

    private List<CompanyUserDTO> companyUserDTOS;

    private Boolean isOwner;

    private List<String> companyIds;

    private Boolean longTerm;
    private List<Long> businessLicenseFileList;
    private List<FileVO> businessLicenseFileVOS;
    /**
     * 档位id
     */
    private Long stallId;
    /**
     * 档位名称
     */
    private String stallName;
    private List<Long> companyFileIds;
    private List<FileVO> companyFileIdsFileVOS;
    /**
     * 安全协议文件ids
     */
    private String securityFileId;
    /**
     * 安全培训照片ids
     */
    private String trainingPhotoFileId;
    /**
     * 封面照片ids
     */
    private String companyPhotoFileId;

}
