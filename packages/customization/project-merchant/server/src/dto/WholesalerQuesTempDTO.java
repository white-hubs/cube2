package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuesTemp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/11
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerQuesTempDTO extends ConvertionDTO<WholesalerQuesTempDTO, WholesalerQuesTemp> {

    public WholesalerQuesTempDTO() {}

    public WholesalerQuesTempDTO(WholesalerQuesTemp temp) {
        super();
        map(temp);
    }

    public WholesalerQuesTempDTO(List<WholesalerQuesTemp> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    @Length(min = 1, max = 50, message = "模板名称不能超过50个字")
    private String name;

    private String type;
    private String typeName;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted = false;

    private List<String> questionIds;

    private List<WholesalerQuestionDTO> questionDtos;
}
