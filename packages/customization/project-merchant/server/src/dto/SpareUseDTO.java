package com.guhecloud.cube.deliver.project.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SpareUse;
import com.guhecloud.cube.deliver.project.merchant.entity.SpareUseSpare;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 备品备件使用记录
 * </p>
 *
 * @author zwb
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SpareUseDTO extends ConvertionDTO<SpareUseDTO, SpareUse> {

    public SpareUseDTO() {
    }

    public SpareUseDTO(SpareUse spareUse) {
        super();
        map(spareUse);
    }

    public SpareUseDTO(List<SpareUse> spareUses) {
        super();
        map(spareUses);
    }

    private String id;

    private String projectId;

    private String miantFirm;

    private String deviceId;

    private String deviceName;

    private String deviceTypeId;

    private String deviceTypeCode;

    private String deviceTypeName;

    private String areaId1;

    private String areaId2;

    private String areaId3;

    private String installAddress;

    private String purpose;

    private String contactPerson;

    private String contactPhone;

    private String businessType;

    private String businessKey;

    private String remark;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String createByName;

    private List<SpareUseSpare> useSpares;

    private List<String> areaIds;

    private List<String> areaNames;

    private List<String> maintFrontFileIds;

    private List<FileVO> maintFrontFileVOS;

    private List<String> maintAfterFileIds;

    private List<FileVO> maintAfterFileVOS;

}
