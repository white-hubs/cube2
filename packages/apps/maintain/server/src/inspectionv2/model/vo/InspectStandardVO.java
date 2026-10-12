package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectStandardVO {

    private Long id;

    /**
     * 标准名称
     */
    private String name;

    private String remark;

    /**
     * 标准编码
     */
    private String code;

    /**
     * 1启用 0禁用
     */
    private Boolean enable;
    /**
     * 默认巡检标准
     */
    private boolean defaultStandard;

    /**
     * 巡检项
     */
    List<InspectStandardItemVO> standardItems;

    /**
     * 巡检类型关系
     */
    List<InspectStandardTypeReferVO> standardTypeRefers;

    /**
     * 巡检项名称
     */
    private String standardItemNames;
}
