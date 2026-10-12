package com.guhecloud.cube.base.alarm.client.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 报警消息发送对象参数
 *
 * @author lanchun
 * @date 2022/6/6 10:25
 */
@Data
public class SatAlarmMsgTargetSendParam {

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空")
    @Min(value = 1, message = "用户主键不能小于1")
    private Long userId;

    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名称不能为空")
    @Size(max = 50, message = "用户名称不能大于50")
    private String userName;

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    @Size(max = 50, message = "用户所属部门名称不能大于50")
    private String departmentName;
}
