package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDevice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/04
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerDeviceDTO extends ConvertionDTO<WholesalerDeviceDTO, WholesalerDevice> {
    public WholesalerDeviceDTO() {}

    public WholesalerDeviceDTO(WholesalerDevice device) {
        super();
        map(device);
    }

    public WholesalerDeviceDTO(List<WholesalerDevice> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String typeId;

    private String typeName;

    private Integer count;

    private String stallId;

    private String stallName;

    private String companyId;

    private String companyName;

    private String description;

    private Integer danger;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<Long> fileIds;

    private List<FileVO> FileDTOS;

    // 满足前端组件在编辑时回显档位名称
    private List<SpaceDTO> spaceDTOS;

}
