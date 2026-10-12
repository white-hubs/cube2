package com.guhecloud.cube.srv.gen.personnel.management.client.param.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 修改员工工作状态
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeWorkingStatusCommand {
    private Long employeeId;
    private WorkingStatus workingStatus;

    // 是否有更新状态的前置条件
    private boolean condition;
    private boolean originalStatusIsNull;     // 原始状态必须等于空
    private List<String> originalInWorkingStatusList; // 原始状态必须在此状态列表中
}
