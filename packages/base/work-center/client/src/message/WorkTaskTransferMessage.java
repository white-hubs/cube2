package com.guhecloud.cube.biz.work.center.client.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/17 20:05
 * @Description:
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkTaskTransferMessage {

    public static final String TOPIC = "WORK_TASK_TRANSFER";

    /**
     * 租户
     */
    private Integer tenantId;

    /**
     * 交班人
     */
    private Long userId;

    /**
     * 接班人
     */
    private Long targetId;
    /**
     * 业务主键
     */
    private List<String> businessKeyList;

}