package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 隐患记录明细
 * @date 2023/4/14 18:13
 */
@Data
public class HiddenDangerDetailVO {
    /**
     * 提交时间
     */
    private String submitTime;
    /**
     * 隐患单名称
     */
    private String name;
    /**
     * 隐患对象
     */
    private String hiddenDangerObj;
    /**
     * 隐患来源
     */
    private String source;
    /**
     * 隐患分类
     */
    private String classify;
    /**
     * 隐患程度
     */
    private String levelName;
    /**
     * 提交人
     */
    private String submitter;
    /**
     * 状态
     */
    private String state;
    /**
     * 处理结果
     */
    private String result;
}
