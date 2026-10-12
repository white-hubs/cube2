package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocolTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/10
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerProtocolTemplateDTO extends ConvertionDTO<WholesalerProtocolTemplateDTO, WholesalerProtocolTemplate> {
    public WholesalerProtocolTemplateDTO() {}

    public WholesalerProtocolTemplateDTO(WholesalerProtocolTemplate template) {
        super();
        map(template);
    }

    public WholesalerProtocolTemplateDTO(List<WholesalerProtocolTemplate> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    @Length(min = 1, max = 50, message = "名称最多不能超过50字")
    private String name;

    private String type;
    private String typeName;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<Long> protocolFileIds;

    private List<FileVO> protocolFileDTOS;
}
