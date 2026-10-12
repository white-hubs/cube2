package com.guhecloud.cube.deliver.project.merchant.dto;

/**
 * @author wangliang
 * @author 2022/01/14
 * @description
 */

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerContract;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerSecondPart;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerContractDTO extends ConvertionDTO<WholesalerContractDTO, WholesalerContract> {
    public WholesalerContractDTO() {}

    public WholesalerContractDTO(WholesalerContract contract) {
        super();
        map(contract);
    }

    public WholesalerContractDTO(List<WholesalerContract> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    private Integer signingWay;

    private String contractTypeId;

    private String contractTypeName;

    private WholesalerContractTypeDTO contractTypeDto;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    private String remark;

    private String signatoryName;

    private LocalDateTime signatoryTime;

    private WholesalerFirstPartDTO firstPartDto;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private WholesalerSecondPart secondPart;

    private List<String> stallIds;

    private List<WholeSpaceDTO> stallDtos;

    private String paperCode;

    private String oldId;

    private String oldCode;

    private List<Long> paperContractIds;

    private List<FileVO> paperContractDtos;

    private List<FileVO> seatDtos;
}
