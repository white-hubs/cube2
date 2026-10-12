package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

/**
 * @author lzy
 * @date 2022/5/6
 */
@Data
public class FaultCaseCommand {

    /**
     * 故障案例名称
     */
    @NotEmpty(message = "故障案例名称不能为空!")
    private String name;

    /**
     * 故障类型主键
     */
    @NotNull(message = "故障类型主键不能为空!")
    private Long faultTypeId;

    /**
     * 故障类型名称
     */
    @NotEmpty(message = "故障类型名称不能为空!")
    private String faultTypeName;

    /**
     * 设备类型主键
     */
    private Long deviceTypeId;

    @NotEmpty(message = "产品品类code不能为空!")
    private String productCategoryCode;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 故障现象
     */
    private String faultPhenomenon;

    /**
     * 故障原因
     */
    private String faultReason;

    /**
     * 处理措施
     */
    private String treatmentMeasures;

    /**
     * 文件上传主键集合
     */
    @NotNull(message = "文件不能为空!")
    private Collection<Long> fileIds;
}
