package com.guhecloud.cube.deliver.project.merchant.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangliang
 * @author 2022/01/11
 * @description 手机APP列表看的内容和PC上看协议的内容完全不一样，因此重新写一个DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WholesalerProtocolAppDTO extends ConvertionDTO<WholesalerProtocolAppDTO, WholesalerProtocol> {

    public WholesalerProtocolAppDTO() {}

    public WholesalerProtocolAppDTO(WholesalerProtocol protocol) {
        super();
        map(protocol);
    }

    public WholesalerProtocolAppDTO(List<WholesalerProtocol> list) {
        super();
        map(list);
    }

    private String id;

    private String projectId;

    private String code;

    private String name;

    private String templateId;

    private String templateName;

    private String templateType;

    private List<FileVO> templateFileVOS;

    private String status;

    private String remark;

    private Integer signed;

    private WholeSpaceDTO stallDto;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
