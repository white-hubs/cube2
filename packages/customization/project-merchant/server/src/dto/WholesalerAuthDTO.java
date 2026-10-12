package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/08
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerAuthDTO extends ConvertionDTO<WholesalerAuthDTO, WholesalerAuth> {

    public WholesalerAuthDTO() {
    }

    public WholesalerAuthDTO(WholesalerAuth wholesalerAuth) {
        super();
        map(wholesalerAuth);
    }

    public WholesalerAuthDTO(List<WholesalerAuth> list) {
        super();
        map(list);
    }

    private Long id;

    private String projectName;

    private String projectId;

    private String userId;

    private String companyId;

    private String companyName;

    private String enterpriseName;

    private String stallName;

    private String realName;

    private String postCode;

    //    @IdCard
    private String identityCard;

    //    @Mobile
    private String mobile;

    private String address;

    private String registrationNumber;

    private Integer status;

    private String loginAccount;

    private String reviewer;

    private LocalDateTime reviewTime;

    private String rejectReason;

    private String stallIds;

    private String createBy;

    private LocalDateTime createTime;

    private Long personnelPicId;

    private FileVO personnelPicIdsDtos;

    private Long idCardPhotoId;

    private FileVO idCardPhotoDtos;

    private Long idCardNationId;

    private FileVO idCardNationDtos;

    private List<String> licenseIds;
    private String licenseId;
    private List<FileVO> licenseDtos;

    private String verifyCode;

    private boolean repAuth;

}
