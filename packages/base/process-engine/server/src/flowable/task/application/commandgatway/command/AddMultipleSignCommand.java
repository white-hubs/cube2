package com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/9/6 14:08
 * @description
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddMultipleSignCommand {
    @NotBlank
    private String taskId;
    private String account;
    @NotEmpty
    private Set<String> addSignAccounts;
}
