package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionResult;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.NotifyEventBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author mioopp
 * @create 2020-06-22 14:20
 */

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushBizLog {

  /**
   * 通知的原始数据
   */
  private NotifyEventBO notifyEventBO;

  private List<SingleLinkConditionResult> eventMessageList;

  private String msg;

  private long notifyTs;

  /**
   * {@link NotifyStatus}
   */
  private String notifyStatus;

}
