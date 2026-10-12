package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/18
 */
@Data
public class FaultCaseTypeVO {

    /**
     * 故障分类主键
     */
    private Long faultTypeId;

    /**
     * 故障分类名称
     */
    private String faultTypeName;

    /**
     * 故障案例列表
     */
    private List<FaultCaseVO> faultCases;

}
