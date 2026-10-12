package com.guhecloud.cube.deliver.project.merchant.dto;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.BimInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class BimInfoDTO extends ConvertionDTO<BimInfoDTO, BimInfo> {

    public BimInfoDTO() {
    }

    public BimInfoDTO(BimInfo o) {
        super();
        map(o);
    }

    public BimInfoDTO(List<BimInfo> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;
    private String projectId;

    private String businessType;

    @NotNull
    @NotBlank
    private String businessKey;

    /**
     * 模型文件ID
     */
    private String modelFileId;
    
    private String modelLoadPath;

    private String modelLevelId;

    /**
     * 楼层名称
     */
    private String modelLevel;
    
    /**
     * 专业名称
     */
    private String specialty;
    
    private String specialtyName;
    
    /**
     * 构建编码
     */
    private String ecode;

    /**
     * 构建ID
     */
    private String objectId;
    
    /**
     * 坐标
     */
    private String x;
    private String y;
    private String z;
    
    /**
     * 模型属性
     */
    private String props;

    /**
     * 关联的设备详情
     */
    private DeviceDTO deviceDTO;

    /**
     * 关联的空间详情
     */
    private SpaceDTO spaceDTO;

    /**
     * 是否是批量导入（默认false）
     */
    private Boolean isImport=false;


}
