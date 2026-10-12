package com.guhecloud.cube.biz.work.center.client.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 工作任务删除消息
 */
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class WorkTaskDelMessage extends WorkTaskFlowMessage {

}
