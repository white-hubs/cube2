package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionMeta;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.SingleLinkConditionResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author mioopp
 * @create 2020-06-18 9:08
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkageMetaEventLog {

  private String msg;
  private Long tenantId;

  private String linkExpress;

  private SingleLinkConditionMeta singleLinkConditionMeta;

  private List<SingleLinkConditionResult> matchedConditionResults;

  private String triggerLinkActionId;

}
