package com.guhecloud.cube.gm.meattrade.plugin.couple.domain.valueobj;

import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.enumeration.CoupleStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoupleStatus {

    /**
     * 状态
     */
    private CoupleStatusEnum coupleStatus;
}
