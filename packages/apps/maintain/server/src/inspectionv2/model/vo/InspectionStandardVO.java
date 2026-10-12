package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wql
 * @describe 巡检标准
 * @date 2023/4/21 11:50
 */
@Data
public class InspectionStandardVO {
    /**
     * 标准id
     */
    private Long id;
    /**
     * 标准名称
     */
    private String name;
    /**
     * 标准描述
     */
    private String describe;
    /**
     * 标准编码
     */
    private String code;
    /**
     * 1启用 0禁用
     */
    private Boolean enable;
    /**
     * 巡检项
     */
    private List<InspectionItemVO> standardItems;
}
