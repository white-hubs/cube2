package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JudgmentConfig {

    /**
     * 研判时限（秒）
     */
    private Integer judgmentTimeoutSeconds;

}
