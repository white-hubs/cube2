package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AlarmHandlerRankCardVO {

    /**
     * 响应数
     */
    private Float respondedRingRatio;

    /**
     * 响应率
     */
    private Float respondedRatio;

    /**
     * 处理数
     */
    private Float handlerRingRatio;

    /**
     * 处理率
     */
    private Float handlerRatio;

    /**
     * 超时数
     */
    private Float timeOutRingRatio;

    /**
     * 超时率
     */
    private Float timeOutRatio;

    /**
     * 响应率排名
     */
    List<AlarmUserRatioVO> userResponedRatioRankList;

    /**
     * 处理率排名
     */
    List<AlarmUserRatioVO> userhandlerRatioRankList;

    /**
     * 超时率排名
     */
    List<AlarmUserRatioVO> usertimeOutRatioRankList;

    @Data
    public static class AlarmUserRatioVO {

        private String userName;

        private Float Ratio;
    }
}
