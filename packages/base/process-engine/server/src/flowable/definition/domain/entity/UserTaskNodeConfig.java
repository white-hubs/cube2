package com.guhecloud.cube.base.flow.flowable.definition.domain.entity;

import com.guhecloud.cube.base.flow.flowable.definition.domain.NewApprovalNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/9/7 14:02
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskNodeConfig {
    private Copy copy;
    private NewApprovalNode newApprovalNode;
}
