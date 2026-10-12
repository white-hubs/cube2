package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/10
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerProtocolDTO extends ConvertionDTO<WholesalerProtocolDTO, WholesalerProtocol> {

    public WholesalerProtocolDTO() {}

    public WholesalerProtocolDTO(WholesalerProtocol protocol) {
        super();
        map(protocol);
    }

    public WholesalerProtocolDTO(List<WholesalerProtocol> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String name;

    private String code;

    private String templateId;

    private String templateName;

    private String templateType;
    private String templateTypeName;

    private String remark;

    /**
     * 开始时间是否是现在
     */
    private boolean beginTimeIsNow;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer processingStatus;

    private String status;

    private String xxlTaskIds;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<String> stallIds;

    private List<WholeSpaceDTO> stallDtos;

    private Integer count;

    private Integer signedCount;

    private Integer notSignedCount;

    private Integer signedStatus;

}
