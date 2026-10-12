package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.guhecloud.cube.base.flow.flowable.process.application.enumeration.ProcessLookUpKeyType;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/10/15 15:22
 * @description
 */
@Data
public class CloseBatchProcessParam {
    @NotEmpty
    private Set<String> keys;
    private ProcessLookUpKeyType processLookUpKeyType;
}
