package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.Wholesaler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerDTO extends ConvertionDTO<WholesalerDTO, Wholesaler> {

    public WholesalerDTO() {}

    public WholesalerDTO(Wholesaler wholesaler) {
        super();
        map(wholesaler);
    }

    public WholesalerDTO(List<Wholesaler> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    private String name;

    private String owner;

    private String mobile;

    private String introduction;

    private String remark;

    private List<SpaceDTO> spaces;

    private List<String> securityFileIds;

    private List<FileVO> securityFileVOs;

    private List<String> trainingPhotoFileIds;

    private List<FileVO> trainingPhotoFileVOs;

    private List<String> fireHydrantFileIds;

    private List<FileVO> fireHydrantFileVOs;

    private List<CompanyUserDTO> companyUserDtos;
}
