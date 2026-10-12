package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class InspectStandardTypeReferVO {

    /**
     * 关联类别：asset资产 gear挡位 device设备
     */
    private String targetCategory;

    /**
     * 可能为资产、挡位、现场点的具体类型ID
     */
    private List<String> targetTypes;

}
