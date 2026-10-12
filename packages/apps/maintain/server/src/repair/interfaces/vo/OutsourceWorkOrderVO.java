package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 委外维修工单列表
 * @date 2024/1/8 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OutsourceWorkOrderVO extends ProcessInstanceDTO {
    /**
     * 委外单编号
     */
    private String code;
    /**
     * 委外对象名称
     */
    private String objName;
    /**
     * 处理结果
     */
    private String handleResult;
}
