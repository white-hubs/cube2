package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 工程申请单列表
 * @date 2023/9/15 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectApplyListVO extends ProcessInstanceDTO {
    /**
     * 工单编号
     */
    private String applyNum;
    /**
     * 小散工程名称
     */
    private String projectName;
}
