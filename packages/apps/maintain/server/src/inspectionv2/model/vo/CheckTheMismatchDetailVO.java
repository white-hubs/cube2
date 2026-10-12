package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 稽查不匹配项明细
 * @date 2023/4/14 17:31
 */
@Data
public class CheckTheMismatchDetailVO {
    /**
     * 巡检项名称
     */
    private String name;
    /**
     * 巡检对象
     */
    private String inspectionObj;
    /**
     * 巡检项内容
     */
    private String content;
    /**
     * 巡检结果
     */
    private String result;
    /**
     * 上报结果
     */
    private String reportResult;
    /**
     * 稽查结果
     */
    private String checkResult;
    /**
     * 任务负责人
     */
    private String principal;
    /**
     * 稽查人
     */
    private String checkPerson;
}
