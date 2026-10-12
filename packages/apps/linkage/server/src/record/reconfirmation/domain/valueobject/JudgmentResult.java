package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.valueobject;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JudgmentResult {

    /**
     * 事件id
     */
    private Long id;

    /**
     * 研判结果
     */
    private AiReconfirmResultEnum result;
}
