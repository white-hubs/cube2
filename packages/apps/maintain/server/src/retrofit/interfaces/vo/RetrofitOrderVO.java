package com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 更新改造2.0 列表返回参数
 *
 * @author lanchun
 * @date 2024/1/8 17:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RetrofitOrderVO extends ProcessInstanceDTO {

    /**
     * 表单编号
     */
    private String code;

    /**
     * 改造对象id
     */
    private Long objId;

    /**
     * 改造对象名称
     */
    private String objName;

    /**
     * 申报理由
     */
    private String reason;

    /**
     * 期望解决日期
     */
    private LocalDateTime expectedResolutionDate;

    /**
     * 确认结果
     */
    private String identifyResult;

    /**
     * 确认描述
     */
    private String identifyRemarks;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理结果描述
     */
    private String handleResultName;

    /**
     * 处理描述
     */
    private String handleRemarks;
}
