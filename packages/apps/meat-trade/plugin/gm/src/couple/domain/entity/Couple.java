package com.guhecloud.cube.gm.meattrade.plugin.couple.domain.entity;

import com.guhecloud.cube.gm.meattrade.plugin.couple.domain.valueobj.CoupleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Couple {

    /**
     * 挂钩主键
     */
    private Long id;

    /**
     * 挂钩编号
     */
    private String code;

    /**
     * 挂钩状态
     */
    private CoupleStatus status;

    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    public void changeReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    public void changeCoupleStatus(CoupleStatus status) {
        this.status = status;
    }
}
