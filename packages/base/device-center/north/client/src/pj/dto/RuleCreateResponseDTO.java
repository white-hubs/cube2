package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fl
 * @date 2022-04-21 16:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleCreateResponseDTO {
    private String ruleId;
    private String ruleNum;
}
